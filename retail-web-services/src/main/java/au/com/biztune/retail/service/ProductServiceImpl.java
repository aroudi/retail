package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.ProductForm;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 23/03/2016.
 */
@Component
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
                //delete product related objects
                product = updateProductObject(productForm);
                deleteProductRelatedObjects(productForm);
            }
            final ProdOrguLink prodOrguLink = createProdOrguLink(productForm, product);
            product.setProdOrguLink(prodOrguLink);
            createProuTxrlLink(productForm, product);
            createSuppProdPrice(productForm, product);
            createProductPrice(productForm, product);
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
            logger.error("Error in retrieving product list");
            return null;
        }
    }

    /**
     * get all products in paging format.
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @return paging.
     */
    public List<Product> getAllProductsInPagingFormat(long pageNo, long pageSize) {
        try {
            final long rowNoFrom = (pageNo - 1) * pageSize + 1;
            final long rowNoTo = rowNoFrom + pageSize - 1;
            return productDao.getAllProductsPerOrgUnitIdPaging(sessionState.getOrgUnit().getId(), rowNoFrom, rowNoTo);

        } catch (Exception e) {
            logger.error("Error in retrieving product list");
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
                    suppProdPriceDao.insert(suppProdPrice);
                    logger.info("supplier price inserted");
                }

            }
        }
    }

    private void createProductPrice(ProductForm productForm, Product product) {
        final PriceBand priceBand = priceBandDao.getPriceBandPerCode(IdBConstant.PRICE_BAND_CODE);
        final PriceCode priceCode1 = priceDao.getProductPriceCodePerCode(IdBConstant.SELL_PRICE_CODE);
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
    }

    private void deleteProductRelatedObjects(ProductForm productForm) {
        priceDao.deleteProdPricePerProdId(productForm.getProdId());
        //ProductPurchaseItem might get used in PurchaseOrder and we can not delete it.
        //suppProdPriceDao.deleteSuppProdPricePerProdIdAndOrguId(productForm.getProdId(), sessionState.getOrgUnit().getId());
        productDao.deleteProdTaxLink(productForm.getProuId());
        productDao.deleteProdOrguLink(productForm.getProuId());
    }

    /**
     * get product object per sku.
     * @param skuCode skuCode
     * @return Product
     */
    public Product getProductPerSku(String skuCode) {
        try {
            return productDao.getProductPerSku(skuCode);

        } catch (Exception e) {
            logger.error("Error in getting product per sku:", e);
            return null;
        }
    }
    /**
     * get all products as Sale Items.
     * @return List of ProductSaleItem
     */
    public List<ProductSaleItem> getAllProductsAsSaleItem() {
        try {
            return productDao.getAllProductSaleItemsPerOrgUnitId(sessionState.getOrgUnit().getId());

        } catch (Exception e) {
            logger.error("Error in retrieving sale item list");
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
            return productDao.getProductSaleItemPerOrgUnitIdAndSku(sessionState.getOrgUnit().getId(), sku);

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
            return productDao.getProductSaleItemPerSku(skuCode);

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
     * import product.
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
            , double bulkPrice)
    {
        try {
            final Timestamp currentTime = new Timestamp(new Date().getTime());
            final ConfigCategory configProdType = configCategoryService.addConfigCategory(IdBConstant.TYPE_PRODUCT_TYPE
                    , prodType, prodType, prodType, 0, "blue");
            //insert product
            Product product = productDao.getProductPerSku(prodSku);
            if (product == null) {
                product = new Product();
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
                final ConfigCategory productStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.CONFIG_PRODUCT_STATUS, IdBConstant.PRODUCT_STATUS_IMPORTED);
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
                suppProdPrice.setPrice(cost);
                suppProdPrice.setSprcCreated(currentTime);
                suppProdPrice.setSprcModified(currentTime);
                suppProdPrice.setProdId(product.getId());
                suppProdPrice.setBulkPrice(bulkPrice);
                suppProdPriceDao.insert(suppProdPrice);

                //import product price
                //get price band
                final PriceBand priceBand = priceBandDao.getPriceBandPerCode(IdBConstant.PRICE_BAND_CODE);

                //
                final Price prodPrice = new Price();
                //get price code

                final PriceCode priceCode = priceDao.getProductPriceCodePerCode(IdBConstant.SELL_PRICE_CODE);


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
            }
            return product;
        } catch (Exception e) {
            logger.error("Exception in importing product:", e);
            return null;
        }
    }
}
