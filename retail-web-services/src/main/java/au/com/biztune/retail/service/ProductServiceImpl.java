package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.ProductForm;
import au.com.biztune.retail.form.ProductSearchForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.DataChangeIndicator;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.SearchClause;
import au.com.biztune.retail.util.SearchClauseBuilder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.SecurityContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 23/03/2016.
 */
@Component
@Aspect
public class ProductServiceImpl implements ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ConfigCategoryDao categoryDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private SessionState sessionState;

    @Autowired
    private OrgUnitDao orgUnitDao;

    @Autowired
    private TaxRuleDao taxRuleDao;

    @Autowired
    private SupplierDao supplierDao;

    @Autowired
    private SuppProdPriceDao suppProdPriceDao;

    @Autowired
    private UnitOfMeasureDao unitOfMeasureDao;

    @Autowired
    private LegalTenderDao legalTenderDao;

    @Autowired
    private PriceBandDao priceBandDao;

    @Autowired
    private PriceDao priceDao;

    @Autowired
    private ConfigCategoryDao configCategoryDao;

    @Autowired
    private ConfigCategoryService configCategoryService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private StockService stockService;
    @Autowired
    private UserService userService;
    /**
     * add product and its related objects.
     * @param productForm productForm
     * @return CommonResponse
     */
    public CommonResponse addProductWhole(ProductForm productForm) {
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);

            if (productForm == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("product object is null");
                return response;
            }
            final boolean isNew = productForm.getProdId() > 0 ? false : true;
            Product product = null;
            if (isNew) {
                product = createProductObject(productForm);
            } else {
                product = updateProductObject(productForm);
                //see if we have a price change here
                final Price oldPrice = priceDao.getProductCostPricePerProdId(productForm.getProdId());
                if (oldPrice.getPrcePrice() != productForm.getCostPrice()) {
                    stockService.createCostVarianceEvent(productForm.getProdId(), oldPrice.getPrcePrice(), productForm.getCostPrice(), 1, productForm.getUserId());
                }
                //delete product related objects
                deleteProductRelatedObjects(productForm);
            }
            final ProdOrguLink prodOrguLink = createProdOrguLink(productForm, product);
            productForm.setProdId(product.getId());
            product.setProdOrguLink(prodOrguLink);
            createProuTxrlLink(productForm, product);
            createSuppProdPrice(productForm, product);
            createProductPrice(productForm, product);
            createProductGroupLink(productForm);
            updateDataChangeIndicators();
            return response;
        } catch (Exception e) {
            logger.error("Exception in saving Product: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in saving Product");
            return response;
        }
    }

    /**
     * get all products.
     * @return List of products
     */
    public List<Product> getAllProducts() {
        try {
            return productDao.getAllProductsPerOrgUnitId(sessionState.getOrgUnit().getId());

        } catch (Exception e) {
            logger.error("Error in retrieving product list", e);
            return null;
        }
    }

    /**
     * get all products in paging format.
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @return paging.
     */
    public ProductSearchForm getAllProductsInPagingFormat(long pageNo, long pageSize) {
        try {
            final long rowNoFrom = (pageNo - 1) * pageSize + 1;
            final long rowNoTo = rowNoFrom + pageSize - 1;
            final ProductSearchForm productSearchForm = new ProductSearchForm();
            productSearchForm.setResult(productDao.getAllProductExtendedPerOrgUnitIdPaging(sessionState.getOrgUnit().getId(), rowNoFrom, rowNoTo));
            productSearchForm.setTotalRecords(productDao.searchProductsTotalRecords(sessionState.getOrgUnit().getId(), null));
            return productSearchForm;
        } catch (Exception e) {
            logger.error("Error in retrieving product list", e);
            return null;
        }
    }

    /**
     * get Product Detail.
     * @param prodId prodId
     * @return ProductForm
     */
    public ProductForm getProductDetail(long prodId) {
        try {
            final Product product = productDao.getProductPerOrgUnitIdAndProdId(sessionState.getOrgUnit().getId(), prodId);
            if (product == null) {
                logger.error("Not able to find product with id " + prodId);
                return null;
            }
            final ProductForm productForm = new ProductForm();
            productForm.setProdId(product.getId());
            productForm.setProdSku(product.getProdSku());
            productForm.setProdBarcode(product.getProdBarcode());
            productForm.setReference(product.getReference());
            productForm.setProdName(product.getProdName());
            productForm.setProdType(product.getProdType());
            productForm.setProdDesc(product.getProdDesc());
            productForm.setProdBrand(product.getProdBrand());
            productForm.setProdClass(product.getProdClass());
            productForm.setProuId(product.getProdOrguLink().getId());
            productForm.setStatus(product.getProdOrguLink().getStatus());
            productForm.setTaxRules(product.getProdOrguLink().getTaxRules());
            productForm.setSuppProdPrices(product.getSuppProdPriceList());
            productForm.setProdLocation(product.getProdLocation());
            productForm.setProductGroups(product.getProductGroups());

            //we only DISPLAY SELL_PRICE TO THE USER
            if (product.getPriceList() != null && product.getPriceList().size() > 0) {
                for (Price price : product.getPriceList()) {
                    if (price.getPriceCode().getPrccCode().equals(IdBConstant.SELL_PRICE_CODE)) {
                        productForm.setPrceUnitOfMeasure(price.getUnitOfMeasure());
                        productForm.setPrceTaxIncluded(price.isPrceTaxIncluded());
                        productForm.setPrceUnomQty(price.getUnomQty());
                        productForm.setPrceMargin(price.getMargin());
                        productForm.setPrcePrice(price.getPrcePrice());
                    }
                    if (price.getPriceCode().getPrccCode().equals(IdBConstant.COST_PRICE_CODE)) {
                        productForm.setCostPrice(price.getPrcePrice());
                    }
                }
            }
            return productForm;
        } catch (Exception e) {
            logger.error("Error in retrieving product detail");
            return null;
        }
    }

    private Product createProductObject(ProductForm productForm) {
        final Product product = new Product();
        product.setOrguIdOwning(sessionState.getOrgUnit().getId());

        product.setProdSku(productForm.getProdSku());
        product.setProdBarcode(productForm.getProdBarcode());
        product.setReference(productForm.getReference());
        product.setProdName(productForm.getProdName());
        product.setProdType(productForm.getProdType());
        product.setProdDesc(productForm.getProdDesc());
        product.setProdOwnBrand(false);
        product.setProdBrand(productForm.getProdBrand());
        product.setProdClass(productForm.getProdClass());
        product.setProdLocation(productForm.getProdLocation());
        productDao.insertProduct(product);
        logger.info("Product Object Inserted");
        return product;
    }

    private Product updateProductObject (ProductForm productForm) {
        final Product product = new Product();
        product.setOrguIdOwning(sessionState.getOrgUnit().getId());
        final Timestamp timestamp = new Timestamp(new Date().getTime());
        product.setProdModified(timestamp);
        product.setId(productForm.getProdId());
        product.setProdSku(productForm.getProdSku());
        product.setProdBarcode(productForm.getProdBarcode());
        product.setReference(productForm.getReference());
        product.setProdName(productForm.getProdName());
        product.setProdType(productForm.getProdType());
        product.setProdDesc(productForm.getProdDesc());
        product.setProdOwnBrand(false);
        product.setProdBrand(productForm.getProdBrand());
        product.setProdClass(productForm.getProdClass());
        product.setProdLocation(productForm.getProdLocation());
        productDao.updateProduct(product);
        logger.info("Product Object updated");
        return product;
    }
    private ProdOrguLink createProdOrguLink(ProductForm productForm, Product product) {
        //get orgunit from dao
        final ProdOrguLink prodOrguLink = new ProdOrguLink();
        //prodOrguLink.setId(product.getId());
        prodOrguLink.setOrguId(sessionState.getOrgUnit().getId());
        //get product status
        //ConfigCategory productStatus = configCategoryDao.getCategoryOfTypeAndCode("PRODUCT_STATUS", prodStatusCode);

        prodOrguLink.setStatus(productForm.getStatus());
        prodOrguLink.setProdId(product.getId());
        productDao.insertProdOrguLink(prodOrguLink);
        logger.info("product orgulink inserted");
        return prodOrguLink;
    }

    private void createProuTxrlLink(ProductForm productForm, Product product) {
        //insert product tax rule
        if (productForm.getTaxRules() != null) {
            for (TaxRule taxRule : productForm.getTaxRules()) {
                final ProuTxrlLink prouTxrlLink = new ProuTxrlLink();
                prouTxrlLink.setProuId(product.getProdOrguLink().getId());
                prouTxrlLink.setTxrlId(taxRule.getId());
                productDao.insertProdTaxLink(prouTxrlLink);
            }
        }
        logger.info("tax rule link inserted");
    }

    private void createSuppProdPrice(ProductForm productForm, Product product) {
        if (productForm.getSuppProdPrices() != null && productForm.getSuppProdPrices().size() > 0) {
            for (SuppProdPrice suppProdPrice : productForm.getSuppProdPrices()) {
                if (suppProdPrice.isDeleted()) {
                    continue;
                }
                if (suppProdPrice.getId() > 0) {
                    suppProdPriceDao.updateValues(suppProdPrice);
                } else {
                    final LegalTender legalTender = legalTenderDao.getLegalTenderByCode(IdBConstant.LEGAL_TENDER_AU);
                    suppProdPrice.setLegalTender(legalTender);
                    if (suppProdPrice.getSupplier() != null) {
                        suppProdPrice.setSolId(suppProdPrice.getSupplier().getSuppOrguLink().getId());
                    }
                    suppProdPrice.setSprcCreated(new Timestamp(new Date().getTime()));
                    suppProdPrice.setSprcModified(new Timestamp(new Date().getTime()));
                    suppProdPrice.setProdId(product.getId());
                    //suppProdPrice.setSprcPrefferBuy(true);
                    suppProdPriceDao.insert(suppProdPrice);
                    logger.info("supplier price inserted");
                }

            }
        }
    }

    private void createProductPrice(ProductForm productForm, Product product) {
        final PriceBand priceBand = priceBandDao.getPriceBandPerCode(IdBConstant.PRICE_BAND_CODE);
        PriceCode priceCode1 = priceDao.getProductPriceCodePerCode(IdBConstant.SELL_PRICE_CODE);
        final Price price1 = new Price();
        price1.setUnitOfMeasure(productForm.getPrceUnitOfMeasure());
        price1.setUnomQty(productForm.getPrceUnomQty());
        price1.setMargin(productForm.getPrceMargin());
        price1.setPrcePrice(productForm.getPrcePrice());
        price1.setPrceTaxIncluded(productForm.isPrceTaxIncluded());
        price1.setPriceBand(priceBand);
        price1.setPriceCode(priceCode1);
        price1.setProdId(product.getId());
        price1.setPrceCreated(new Timestamp(new Date().getTime()));
        price1.setPrceModified(new Timestamp(new Date().getTime()));
        price1.setPrceFromDate(new Timestamp(new Date().getTime()));
        price1.setPrceToDate(new Timestamp(new Date().getTime()));
        price1.setPrceSetCentral(false);
        priceDao.insert(price1);
        logger.info("price inserted");
        //insert cost
        priceCode1 = priceDao.getProductPriceCodePerCode(IdBConstant.COST_PRICE_CODE);
        price1.setPriceCode(priceCode1);
        price1.setPrcePrice(productForm.getCostPrice());
        priceDao.insert(price1);

    }

    private void createProductGroupLink(ProductForm productForm) {
        try {
            if (productForm.getProductGroups() != null && !productForm.getProductGroups().isEmpty()) {
                for (ProdDeptCat prodDeptCat : productForm.getProductGroups()) {
                    if (prodDeptCat.getDeptId() > 0 && prodDeptCat.getCatId() > 0 && prodDeptCat.getCatValId() > 0) {
                        prodDeptCat.setOrguId(sessionState.getOrgUnit().getId());
                        prodDeptCat.setProdId(productForm.getProdId());
                        productCategoryDao.insertProdDeptCategory(prodDeptCat);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception in saving product group link", e);
        }
    }
    private void deleteProductRelatedObjects(ProductForm productForm) {
        priceDao.deleteProdPricePerProdId(productForm.getProdId());
        //ProductPurchaseItem might get used in PurchaseOrder and we can not delete it.
        //suppProdPriceDao.deleteSuppProdPricePerProdIdAndOrguId(productForm.getProdId(), sessionState.getOrgUnit().getId());
        productDao.deleteProdTaxLink(productForm.getProuId());
        productDao.deleteProdOrguLink(productForm.getProuId());
        productCategoryDao.deleteProdDeptCatByOrguIdAndProdId(sessionState.getOrgUnit().getId(), productForm.getProdId());
    }

    /**
     * get product object per sku.
     * @param skuCode skuCode
     * @return Product
     */
    public Product getProductPerSku(String skuCode) {
        try {
            final List<Product> result = productDao.getProductPerSku(skuCode);
            if (result == null) {
                return null;
            } else {
                return result.get(0);
            }

        } catch (Exception e) {
            logger.error("Error in getting product per sku:", e);
            return null;
        }
    }

    /**
     * get product object per sku.
     * @param skuCode skuCode
     * @param reference reference
     * @return Product
     */
    public Product getProductPerSkuAndRef(String skuCode, String reference) {
        try {
            final List<Product> result = productDao.getProductPerSkuAndRef(skuCode, reference);
            if (result == null) {
                return null;
            } else {
                return result.get(0);
            }

        } catch (Exception e) {
            logger.error("Error in getting product per sku:", e);
            return null;
        }
    }

    /**
     * search products per sku or name or reference.
     * @param searchStr search string
     * @return List of product sale item.
     */
    public List<ProductSaleItem> getAllProductsAsSaleItem(String searchStr) {
        try {
            String searchCr = searchStr;
            if ("@ALL@".equals(searchStr)) {
                searchCr = "";
            }
            searchCr = "%" + searchCr + "%";
            return productDao.getAllProductSaleItemsPerOrgUnitId(searchCr);

        } catch (Exception e) {
            logger.error("Error in retrieving sale item list", e);
            return null;
        }
    }

    /**
     * get product sale item per sku.
     * @param sku sku
     * @return ProductSaleItem
     */
    public ProductSaleItem getProductSaleItemPerOrguIdAndSku(String sku) {
        try {
            final List<ProductSaleItem> productSaleItemList = productDao.getProductSaleItemPerOrgUnitIdAndSku(sessionState.getOrgUnit().getId(), sku);
            if (productSaleItemList != null && productSaleItemList.size() > 0) {
                return productSaleItemList.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.error("Error in retrieving sale item per sku");
            return null;
        }
    }

    /**
     * get product object per sku.
     * @param skuCode skuCode
     * @return Product
     */
    public ProductSaleItem getProductSaleItemPerSku(String skuCode) {
        try {
            final List<ProductSaleItem> productSaleItemList = productDao.getProductSaleItemPerSku(skuCode);
            if (productSaleItemList != null && productSaleItemList.size() > 0) {
                return productSaleItemList.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.error("Error in getting product sale item per sku:", e);
            return null;
        }
    }

    /**
     * get product object per sku.
     * @param prodId prodId
     * @return Product
     */
    public ProductSaleItem getProductSaleItemPerProdId(long prodId) {
        try {
            return productDao.getProductSaleItemPerProdId(prodId);

        } catch (Exception e) {
            logger.error("Error in getting product sale item per prod id:", e);
            return null;
        }
    }

    /**
     * get product object per sku.
     * @param reference reference
     * @return Product
     */
    public ProductSaleItem getProductSaleItemPerReference(String reference) {
        try {
            return productDao.getProductSaleItemPerReference(reference);

        } catch (Exception e) {
            logger.error("Error in getting product sale item per sku:", e);
            return null;
        }
    }

    /**
     * import product from doors3 csv.
     * @param prodSku prodSku
     * @param prodName prodName
     * @param prodDesc prodDesc
     * @param reference reference
     * @param taxName taxName
     * @param prodBrand prodBrand
     * @param prodClass prodClass
     * @param prodType prodType
     * @param supplier supplier
     * @param catalogueNo catalogueNo
     * @param unitOfMeasure unitOfMeasure
     * @param cost cost
     * @param price price
     * @param bulkPrice bulkPrice
     * @param overWriteProduct overWriteProduct
     * @param userId userId
     * @return Product
     */
    public Product addProduct(String prodSku
            , String prodName
            , String prodDesc
            , String reference
            , String taxName
            , String prodBrand
            , String prodClass
            , String prodType
            , Supplier supplier
            , String catalogueNo
            , UnitOfMeasure unitOfMeasure
            , double cost
            , double price
            , double bulkPrice
            , boolean overWriteProduct
            , long userId)
    {
        try {
            final Timestamp currentTime = new Timestamp(new Date().getTime());
            final ConfigCategory configProdType = configCategoryService.addConfigCategory(IdBConstant.TYPE_PRODUCT_TYPE
                    , prodType, prodType, prodType, 0, "blue");

            //check if product exists.
            Product product = null;
            if (checkIfProductExistsPerSkuAndReference(prodSku, reference)) {
                product = getProductPerSkuAndRef(prodSku, reference);

                //set the verification flag so in importing BOQ we know that product is already exists.
                product.setVerified(true);

                //check if we need to overWrite the current product. the overWrite flag is True for ImportProduct from Doors3
                // but it is false for import BOQ.
                if (overWriteProduct) {
                    //check if this product is already assigned to the supplier
                    SuppProdPrice suppProdPrice = suppProdPriceDao.checkIfSupplierProductExistsPerOrguIdAndProdIdAndSuppId(
                            sessionState.getOrgUnit().getId(), product.getId(), supplier.getId());
                    if (suppProdPrice == null) {
                        //get supplier legal tender
                        final LegalTender legalTender = legalTenderDao.getLegalTenderByCode(IdBConstant.LEGAL_TENDER_AU);
                        suppProdPrice = new SuppProdPrice();
                        suppProdPrice.setSolId(supplier.getSuppOrguLink().getId());
                        suppProdPrice.setCatalogueNo(catalogueNo);
                        suppProdPrice.setUnitOfMeasure(unitOfMeasure);
                        suppProdPrice.setUnitOfMeasureContent(unitOfMeasure);
                        suppProdPrice.setUnomQty(1);
                        suppProdPrice.setLegalTender(legalTender);
                        suppProdPrice.setSprcCreated(currentTime);
                        suppProdPrice.setSprcModified(currentTime);
                        suppProdPrice.setProdId(product.getId());
                        suppProdPrice.setSprcPrefferBuy(true);
                        suppProdPrice.setPrice(price);
                        //insert product tax rule
                        TaxLegVariance taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(taxName);
                        if (taxLegVariance == null) {
                            taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(IdBConstant.DEFAULT_PRODUCT_TAX_CODE);
                        }
                        suppProdPrice.setTaxLegVariance(taxLegVariance);
                        suppProdPrice.setCostBeforeTax(cost);
                        suppProdPriceDao.insert(suppProdPrice);
                    } else {
                        suppProdPrice.setCostBeforeTax(cost);
                        suppProdPrice.setPrice(price);
                        TaxLegVariance taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(taxName);
                        if (taxLegVariance == null) {
                            taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(IdBConstant.DEFAULT_PRODUCT_TAX_CODE);
                        }
                        suppProdPrice.setTaxLegVariance(taxLegVariance);
                        suppProdPriceDao.updateValues(suppProdPrice);
                    }
                    //update product cost to default supplier cost.
                    updateProductCostBaseDefaultSupplier(product.getId(), price, userId);
                    //update product description:
                    if (!product.getProdDesc().equals(prodDesc)) {
                        productDao.updateProductDescription(prodDesc, product.getId());
                    }
                }
            } else {
                //insert product
                product = new Product();
                //set the verification flag to false so in importing BOQ we know that product is a new product.
                product.setVerified(false);
                product.setOrguIdOwning(sessionState.getOrgUnit().getId());
                product.setProdSku(prodSku);
                product.setReference(reference);
                product.setProdName(prodName);
                product.setProdDesc(prodDesc);
                product.setProdBrand(prodBrand);
                product.setProdClass(prodClass);
                product.setProdOwnBrand(false);
                if (configProdType != null) {
                    product.setProdType(configProdType);
                }
                productDao.insertProduct(product);

                //insert prod orgunit
                final ProdOrguLink prodOrguLink = new ProdOrguLink();
                prodOrguLink.setId(product.getId());
                prodOrguLink.setOrguId(sessionState.getOrgUnit().getId());
                //get product status IMPORTED
                final ConfigCategory productStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.CONFIG_PRODUCT_STATUS, IdBConstant.PRODUCT_STATUS_IN_REVIEW);
                prodOrguLink.setStatus(productStatus);
                prodOrguLink.setProdId(product.getId());
                productDao.insertProdOrguLink(prodOrguLink);

                //insert product tax rule
                final TaxRule taxRule = taxRuleDao.getTaxRuleByCode(taxName);
                if (taxRule != null) {
                    final ProuTxrlLink prouTxrlLink = new ProuTxrlLink();
                    prouTxrlLink.setProuId(prodOrguLink.getId());
                    prouTxrlLink.setTxrlId(taxRule.getId());
                    productDao.insertProdTaxLink(prouTxrlLink);
                }
                //get supplier legal tender

                final LegalTender legalTender = legalTenderDao.getLegalTenderByCode(IdBConstant.LEGAL_TENDER_AU);
                final SuppProdPrice suppProdPrice = new SuppProdPrice();
                suppProdPrice.setSolId(supplier.getSuppOrguLink().getId());
                suppProdPrice.setCatalogueNo(catalogueNo);
                suppProdPrice.setUnitOfMeasure(unitOfMeasure);
                suppProdPrice.setUnitOfMeasureContent(unitOfMeasure);
                suppProdPrice.setUnomQty(1);
                suppProdPrice.setLegalTender(legalTender);
                suppProdPrice.setCostBeforeTax(cost);
                //suppProdPrice.setCostBeforeTax(cost);
                suppProdPrice.setSprcCreated(currentTime);
                suppProdPrice.setSprcModified(currentTime);
                suppProdPrice.setProdId(product.getId());
                suppProdPrice.setSprcPrefferBuy(true);
                suppProdPrice.setBulkPrice(bulkPrice);
                suppProdPrice.setPrice(price);
                //insert product tax rule
                TaxLegVariance taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(taxName);
                if (taxLegVariance == null) {
                    taxLegVariance = taxRuleDao.getTaxLegVarianceByCode(IdBConstant.DEFAULT_PRODUCT_TAX_CODE);
                }
                suppProdPrice.setTaxLegVariance(taxLegVariance);
                suppProdPriceDao.insert(suppProdPrice);

                //import product price
                //get price band
                final PriceBand priceBand = priceBandDao.getPriceBandPerCode(IdBConstant.PRICE_BAND_CODE);

                //
                final Price prodPrice = new Price();
                //get price code

                PriceCode priceCode = priceDao.getProductPriceCodePerCode(IdBConstant.SELL_PRICE_CODE);


                final Price price1 = new Price();
                //TODO: we need to set Quantity and price for each of imported BOQ in Price table
                price1.setUnomQty(1);
                //price1.setMargin(margin);
                price1.setPrcePrice(price);
                //price1.setPrceTaxIncluded(taxInclude);
                price1.setPriceBand(priceBand);
                price1.setPriceCode(priceCode);
                price1.setProdId(product.getId());
                price1.setUnitOfMeasure(unitOfMeasure);
                price1.setPrceCreated(currentTime);
                price1.setPrceModified(currentTime);
                price1.setPrceFromDate(currentTime);
                price1.setPrceToDate(currentTime);
                price1.setPrceSetCentral(false);
                priceDao.insert(price1);
                //SET COST
                price1.setPrcePrice(cost);
                priceCode = priceDao.getProductPriceCodePerCode(IdBConstant.COST_PRICE_CODE);
                price1.setPriceCode(priceCode);
                priceDao.insert(price1);
                updateDataChangeIndicators();
            }
            return product;
        } catch (Exception e) {
            logger.error("Exception in importing product:", e);
            return null;
        }
    }

    /**
     * get All product class.
     * @return product class
     */
    public List<String> getAllProductClass() {
        try {
            return productDao.getAllProductClass();
        } catch (Exception e) {
            logger.error("Exception in getting product class:", e);
            return null;
        }
    }

    /**
     * search products.
     * @param productSearchForm productSearchForm
     * @return product search form.
     */
    public ProductSearchForm searchProductPaging(ProductSearchForm productSearchForm) {
        try {
            final long rowNoFrom = (productSearchForm.getPageNo() - 1) * productSearchForm.getPageSize() + 1;
            final long rowNoTo = rowNoFrom + productSearchForm.getPageSize() - 1;
            //final ProductSearchForm productSearchForm = new ProductSearchForm();
            final List<SearchClause> searchClauseList = SearchClauseBuilder.buildProductSearchWhereCluase(productSearchForm);
            productSearchForm.setResult(productDao.searchProductsExtendedPaging(sessionState.getOrgUnit().getId(), rowNoFrom, rowNoTo, searchClauseList));
            productSearchForm.setTotalRecords(productDao.searchProductsTotalRecords(sessionState.getOrgUnit().getId(), searchClauseList));
            return productSearchForm;
        } catch (Exception e) {
            logger.error("Error in retrieving product list", e);
            return null;
        }
    }

    /**
     * whenever there is a product update, set the change indicator.
     */
    @After("execution(* au.com.biztune.retail.service.ProductServiceImpl.addProduct*(..))")
    public void updateDataChangeIndicators() {
        try {
            logger.info("Aspect for product change invoiked.");
            final List<AppUser> appUsers = userDao.getAllActiveUsers();
            if (appUsers == null || appUsers.isEmpty()) {
                return;
            }
            final DataChangeIndicator dataChangeIndicator = new DataChangeIndicator();
            dataChangeIndicator.setProductDataUpdated(true);
            for (AppUser appUser : appUsers) {
                sessionState.addDataChangeIndicator(appUser.getId(), dataChangeIndicator);
            }
        } catch (Exception e) {
            logger.error("Exception in updating product change indicator.", e);
        }
    }

    /**
     * update supplier prpoduct prices in bulk.
     * change price from application.
     * @param updatedPriceList : updated price list
     * @param securityContext securityContext
     * @return Common Response
     */
    public CommonResponse updateSupplierProductPricesInBulk(List<SuppProdPrice> updatedPriceList, SecurityContext securityContext) {
        final AppUser appUser = userService.getAppUserFromSecurityContext(securityContext);
        final CommonResponse response = new CommonResponse();
        try {
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (updatedPriceList == null || updatedPriceList.size() < 1) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("price list is empty");
                return response;
            }
            //get pricecode for sell-price
            final PriceCode priceCode = priceDao.getProductPriceCodePerCode(IdBConstant.SELL_PRICE_CODE);

            for (SuppProdPrice suppProdPrice : updatedPriceList) {
                //if price not changed then continue
                if (suppProdPrice == null || !suppProdPrice.isChanged()) {
                    continue;
                }
                //update product costs.
                suppProdPriceDao.updateProductCostsPerSolIdAndProdId(suppProdPrice.getSolId(), suppProdPrice.getProdId()
                        , suppProdPrice.getCostBeforeTax(), suppProdPrice.getRrp(), suppProdPrice.getBulkPrice());
                priceDao.updatePricePerProdIdAndPrccId(suppProdPrice.getProdId(), priceCode.getId(), suppProdPrice.getRrp());
                //update product cost base default supplier
                updateProductCostBaseDefaultSupplier(suppProdPrice.getProdId(), suppProdPrice.getRrp(), appUser.getId());
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in updating Product price in bulk: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in updating prices");
            return response;
        }
    }
    /**
     * get product reservation list.
     * @param prodOId prodOId
     * @return list of product reservation list.
     */
    public List<StockReserve> getProductReservationInfo(long prodOId) {
        try {
            return stockDao.getStockReservePerOrguIdAndProdId(sessionState.getOrgUnit().getId(), prodOId);
        } catch (Exception e) {
            logger.error("Exceptoion in retreiving product reservation list");
            return  null;
        }
    }

    /**
     * check if product already exists in db.
     * @param sku sku
     * @param ref ref
     * @return true if exists otherwise return false
     */
    public boolean checkIfProductExistsPerSkuAndReference(String sku, String ref) {
        try {
            final List<Product> matchedProducts = productDao.checkProductExistPerSkuAndRef(sku, ref);
            if (matchedProducts == null || matchedProducts.size() == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("Exceptoion in checking product existance");
            return  false;
        }
    }

    /**
     * update product cost base for default supplier.
     * if product cost is changed, we need to fire a cost variance event.
     * @param prodId prodId
     * @param rrp rrp
     * @param userId userId
     */
    public void updateProductCostBaseDefaultSupplier(long prodId, double rrp, long userId) {
        //update product cost to default supplier cost.
        final List<SuppProdPrice> defaultSpps = suppProdPriceDao.getDefaultSuppliersPerOrguIdAndProdId(sessionState.getOrgUnit().getId(), prodId);
        if (defaultSpps != null && defaultSpps.size() > 0) {
            final Price productCost = priceDao.getProductCostPricePerProdId(prodId);
            if (productCost != null) {
                //create product cost variance event.
                final double oldPrice = productCost.getPrcePrice();
                final double newPrice = defaultSpps.get(0).getCostBeforeTax();
                final Double qtyAffected = stockDao.getProductSaleablePristineStockQty(prodId, sessionState.getOrgUnit().getId());
                if (qtyAffected != null) {
                    stockService.createCostVarianceEvent(prodId, oldPrice, newPrice, qtyAffected, userId);
                }
                productCost.setPrcePrice(defaultSpps.get(0).getCostBeforeTax());
                priceDao.updatePricePerProdIdAndPrccId(prodId, productCost.getPriceCode().getId(), productCost.getPrcePrice());
            }
            final Price productRrp = priceDao.getProductSellPricePerProdId(prodId);
            if (productRrp != null) {
                productRrp.setPrcePrice(rrp);
                priceDao.updatePricePerProdIdAndPrccId(prodId, productRrp.getPriceCode().getId(), productRrp.getPrcePrice());
            }
        }
    }

    /**
     * change product status to finalise.
     * @param productList list of products Id
     * @return Common Response
     */
    public CommonResponse finaliseProductStatus(List<Long> productList) {
        final CommonResponse response = new CommonResponse();
        try {
            final ConfigCategory productStatusFinalised = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.CONFIG_PRODUCT_STATUS, IdBConstant.PRODUCT_STATUS_FINALISED);
            if (productStatusFinalised != null) {
                productDao.updateProductStatus(productStatusFinalised.getId(), sessionState.getOrgUnit().getId(), productList);
                response.setStatus(IdBConstant.RESULT_SUCCESS);
                response.setMessage("update succeeded");
                return response;
            } else {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("update failed");
                return response;
            }

        } catch (Exception e) {
            logger.error("Exception in finalising product status: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in finalising prduct status");
            return response;
        }

    }

    /**
     * delete a product logically. set the deleted flag to true and add 'DELETED + TIMESTAMP' to some fields.
     * @param productIdList product Id List.
     * @return commonResponse.
     */
    public CommonResponse logicalDeleteProduct(List<Long> productIdList) {
        final CommonResponse response = new CommonResponse();
        try {
            final Timestamp currentDate = new Timestamp(new Date().getTime());
            response.setStatus(IdBConstant.RESULT_SUCCESS);
            if (productIdList == null || productIdList.size() < 1) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("empty list");
                return response;
            }
            Product product = null;
            String newValue;
            for (Long productId : productIdList) {
                product = productDao.getProductPerProdId(productId);
                if (product == null) {
                    continue;
                }
                if (product.getProdBarcode() != null) {
                    newValue = product.getProdBarcode().trim() + "-DELETED-" + currentDate;
                    if (newValue.length() > 500) {
                        newValue = newValue.substring(0, 499);
                    }
                    product.setProdBarcode(newValue);
                }
                if (product.getProdName() != null) {
                    newValue = product.getProdName().trim() + "-DELETED-" + currentDate;
                    if (newValue.length() > 500) {
                        newValue = newValue.substring(0, 499);
                    }
                    product.setProdName(newValue);
                }
                if (product.getProdDesc() != null) {
                    newValue = product.getProdDesc().trim() + "-DELETED-" + currentDate;
                    if (newValue.length() > 2048) {
                        newValue = newValue.substring(0, 2047);
                    }
                    product.setProdDesc(newValue);
                }
                if (product.getProdSku() != null) {
                    newValue = product.getProdSku().trim() + "-DELETED-" + currentDate;
                    if (newValue.length() > 200) {
                        newValue = newValue.substring(0, 199);
                    }
                    product.setProdSku(newValue);
                }
                if (product.getReference() != null) {
                    newValue = product.getReference().trim() + "-DELETED-" + currentDate;
                    if (newValue.length() > 200) {
                        newValue = newValue.substring(0, 199);
                    }
                    product.setReference(newValue);
                }
                product.setDeleted(true);
                productDao.updateProduct(product);
                suppProdPriceDao.markProductAsDeletedPerProdId(product.getId());
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception in deleting products: ", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage("Exception in deleting products");
            return response;
        }
    }

}
