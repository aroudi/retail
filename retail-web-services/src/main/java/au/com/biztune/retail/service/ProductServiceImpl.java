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
        suppProdPriceDao.deleteSuppProdPricePerProdIdAndOrguId(productForm.getProdId(), sessionState.getOrgUnit().getId());
        productDao.deleteProdTaxLink(productForm.getProuId());
        productDao.deleteProdOrguLink(productForm.getProuId());
    }

}
