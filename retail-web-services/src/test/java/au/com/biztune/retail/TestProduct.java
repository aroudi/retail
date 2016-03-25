package au.com.biztune.retail;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 23/03/2016.
 */
public class TestProduct {
    //private static final Logger logger = LoggerFactory.getLogger(TestProduct.class);
    private static ProductDao productDao = null;
    private static ConfigCategoryDao configCategoryDao = null;
    private static OrgUnitDao orgUnitDao = null;
    private static TaxRuleDao taxRuleDao = null;
    private static SupplierDao supplierDao = null;
    private static SuppProdPriceDao suppProdPriceDao = null;
    private static UnitOfMeasureDao unitOfMeasureDao = null;
    private static LegalTenderDao legalTenderDao = null;
    private static PriceBandDao priceBandDao = null;
    private static PriceDao priceDao = null;
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("retail-web-services-Context.xml");
        productDao = context.getBean(ProductDao.class);
        configCategoryDao = context.getBean(ConfigCategoryDao.class);
        orgUnitDao = context.getBean(OrgUnitDao.class);
        taxRuleDao = context.getBean(TaxRuleDao.class);
        supplierDao = context.getBean(SupplierDao.class);
        suppProdPriceDao = context.getBean(SuppProdPriceDao.class);
        unitOfMeasureDao = context.getBean(UnitOfMeasureDao.class);
        legalTenderDao = context.getBean(LegalTenderDao.class);
        priceBandDao = context.getBean(PriceBandDao.class);
        priceDao = context.getBean(PriceDao.class);

        //insertProduct("sku001","refrence001","prodName-lock","Lock","prodDesc-Lock description",false,"JOMON-BRAND", "prodClass", "LIVE","GST", "supp01","CatalogueNo009","parNo001","each",1,"AUD",100.00, 10.00, 1000,"A","SELL_PRICE",0.30,130.00,false,"each",1);
        fetchProductAndRelatedObjects();

    }

    public static void insertProduct(String prodSku,
                                     String reference,
                                     String prodName,
                                     String prodTypeCode,
                                     String prodDesc,
                                     boolean prodOwnBrand,
                                     String prodBrand,
                                     String prodClass,
                                     String prodStatusCode,
                                     String taxRuleCode,
                                     String supplierCode,
                                     String catalogueNo,
                                     String partNo,
                                     String suppUnomCode,
                                     double suppUnomQty,
                                     String suppLegtCode,
                                     double suppPrice,
                                     double suppBulkPrice,
                                     double suppBulkQty,
                                     String pribCode,
                                     String priceCode,
                                     double margin,
                                     double price,
                                     boolean taxInclude,
                                     String unomCode,
                                     double unomQty ) {

        try {
            System.out.println("in insertProduct");
            //get product type
            ConfigCategory productType = configCategoryDao.getCategoryOfTypeAndCode("PRODUCT_TYPE", prodTypeCode);
            System.out.println("product type fetched");

            OrgUnit orgUnit = orgUnitDao.getOrgUnitByTypeAndCode("COMPANY", "JOMON");
            System.out.println("orgunit fetched");

            Product product = new Product();
            product.setOrguIdOwning(orgUnit.getId());
            product.setProdSku(prodSku);
            product.setReference(reference);
            product.setProdName(prodName);
            product.setProdType(productType);
            product.setProdDesc(prodDesc);
            product.setProdOwnBrand(prodOwnBrand);
            product.setProdBrand(prodBrand);
            product.setProdClass(prodClass);

            productDao.insertProduct(product);
            System.out.println("product inserted");

            //get orgunit from dao
            ProdOrguLink prodOrguLink = new ProdOrguLink();
            prodOrguLink.setId(product.getId());
            prodOrguLink.setOrguId(orgUnit.getId());
            //get product status
            ConfigCategory productStatus = configCategoryDao.getCategoryOfTypeAndCode("PRODUCT_STATUS", prodStatusCode);

            System.out.println("prodStatus.id =" + productStatus.getId());
            prodOrguLink.setStatus(productStatus);
            prodOrguLink.setProdId(product.getId());
            productDao.insertProdOrguLink(prodOrguLink);
            System.out.println("product orgulink inserted");

            //insert product tax rule
            TaxRule taxRule = taxRuleDao.getTaxRuleByCode(taxRuleCode);
            System.out.println("tax rule feteched");
            ProuTxrlLink prouTxrlLink = new ProuTxrlLink();
            prouTxrlLink.setProuId(prodOrguLink.getId());
            prouTxrlLink.setTxrlId(taxRule.getId());

            productDao.insertProdTaxLink(prouTxrlLink);
            System.out.println("tax rule link inserted");

            //get supplier
            Supplier supplier = supplierDao.getSupplierByOrgUnitIdAndSuppCode(orgUnit.getId(), supplierCode);
            System.out.println("supplier fetched");

            //get supplier unit of measure
            UnitOfMeasure suppUnom = unitOfMeasureDao.getUnomByCode(suppUnomCode);


            //get supplier legal tender
            LegalTender legalTender = legalTenderDao.getLegalTenderByCode(suppLegtCode);
            System.out.println("legal tender feteched");
            SuppProdPrice suppProdPrice = new SuppProdPrice();
            suppProdPrice.setSolId(supplier.getSuppOrguLink().getId());
            suppProdPrice.setCatalogueNo(catalogueNo);
            suppProdPrice.setUnitOfMeasure(suppUnom);
            suppProdPrice.setPartNo(partNo);
            suppProdPrice.setUnomQty(suppUnomQty);
            suppProdPrice.setLegalTender(legalTender);
            suppProdPrice.setPrice(suppPrice);
            suppProdPrice.setBulkQty(suppBulkQty);
            suppProdPrice.setBulkPrice(suppBulkPrice);
            suppProdPrice.setSprcCreated(new Timestamp(new Date().getTime()));
            suppProdPrice.setSprcModified(new Timestamp(new Date().getTime()));
            suppProdPrice.setProdId(product.getId());
            suppProdPriceDao.insert(suppProdPrice);
            System.out.println("supplier price inserted");

            //get price band
            PriceBand priceBand = priceBandDao.getPriceBandPerCode(pribCode);
            System.out.println("price band feteched id = " +priceBand.getId());

            //
            Price prodPrice = new Price();
            //get price code

            PriceCode priceCode1 = priceDao.getProductPriceCodePerCode(priceCode);
            System.out.println("price code feteched");

            UnitOfMeasure unitOfMeasure = unitOfMeasureDao.getUnomByCode(unomCode);

            Price price1 = new Price();
            price1.setUnomQty(unomQty);
            price1.setMargin(margin);
            price1.setPrcePrice(price);
            price1.setPrceTaxIncluded(taxInclude);
            price1.setPriceBand(priceBand);
            price1.setPriceCode(priceCode1);
            price1.setProdId(product.getId());
            price1.setUnitOfMeasure(unitOfMeasure);
            price1.setPrceCreated(new Timestamp(new Date().getTime()));
            price1.setPrceModified(new Timestamp(new Date().getTime()));
            price1.setPrceFromDate(new Timestamp(new Date().getTime()));
            price1.setPrceToDate(new Timestamp(new Date().getTime()));
            price1.setPrceSetCentral(false);
            priceDao.insert(price1);
            System.out.println("price inserted");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void fetchProductAndRelatedObjects(){
        try{
            //fetch
            OrgUnit orgUnit = orgUnitDao.getOrgUnitByTypeAndCode("COMPANY", "JOMON");
            List<Product> products = productDao.getAllProductsPerOrgUnitId(orgUnit.getId());
            System.out.println("Product : " + products.get(0).getProdName() + " orguId =" + products.get(0).getProdOrguLink().getOrguId());
            Product product = productDao.getProductPerOrgUnitIdAndProdId(orgUnit.getId(), products.get(0).getId());
            //priceDao.getAllProductPrice()
            System.out.println("taxrule: " + product.getProdOrguLink().getTaxRules().get(0).getTaxLegVariance().getTxlvCode());
            System.out.println("price code: " + product.getPriceList().get(0).getPriceCode().getPrccCode());
            System.out.println("supplier price : " + product.getSuppProdPriceList().get(0).getPartNo());

        } catch (Exception e) {
            e.printStackTrace();
    }
    }

}
