package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.PriceDao;
import au.com.biztune.retail.dao.SuppProdPriceDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.StringUtil;
import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by arash on 4/11/2016.
 */
@Component
public class ProductImportServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(ProductImportServiceImpl.class);

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SuppProdPriceDao suppProdPriceDao;

    @Autowired
    private PriceDao priceDao;

    @Autowired
    private SessionState sessionState;

    /**
     * import location from csv file.
     * @param inputStream inputStream
     * @throws Exception Exception
     * @return CommonResponse
     */
    public CommonResponse importProductsFromCsvInputStream(InputStream inputStream) throws Exception {
        CSVReader csvReader = null;
        final CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
        try {
            ListIterator<String[]> iterator = null;
            String[] csvRow = null;
            csvReader = new CSVReader(new InputStreamReader(inputStream));
            if (csvReader == null) {
                logger.error("not able to open product inputStream");
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.addMessage("not able to open product inputStream");
                return commonResponse;
            }
            iterator = csvReader.readAll().listIterator();
            //read header
            if (iterator.hasNext()) {
                iterator.next();
            }
            while (iterator.hasNext()) {
                csvRow = iterator.next();
                if (csvRow == null) {
                    continue;
                }
                importProductWhole(csvRow[1], csvRow[2], csvRow[3], csvRow[4], csvRow[5], csvRow[6], csvRow[7]
                        , csvRow[11], csvRow[14], csvRow[16], StringUtil.strToDouble(csvRow[19]), StringUtil.strToDouble(csvRow[21]),
                        StringUtil.strToDouble(csvRow[20]), true);
            }
            commonResponse.addMessage("Product list imported successfully");
            return commonResponse;
        } catch (Exception e) {
            logger.error("error in importing products : ", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.addMessage("error in importing products. see the logs");
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
            return commonResponse;
        }

    }

    /**
     * import product into the system.
     * @param brand brand
     * @param partNo partNo
     * @param catalogueNo catalogueNo
     * @param reference reference
     * @param description description
     * @param supplierCode supplierCode
     * @param supplierName supplierName
     * @param prodType prodType
     * @param unitOfMeasure unitOfMeasure
     * @param taxCode taxCode
     * @param cost cost
     * @param price price
     * @param bulkPrice bulkPrice
     * @param overWriteProduct overWriteProduct
     * @return ImportedProductResult
     */
    public ImportProductResult importProductWhole(String brand
            , String partNo
            , String catalogueNo
            , String reference
            , String description
            , String supplierCode
            , String supplierName
            , String prodType
            , String unitOfMeasure
            , String taxCode
            , double cost
            , double price
            , double bulkPrice
            , boolean overWriteProduct)
    {
        try {
            final ImportProductResult importProductResult = new ImportProductResult();
            final UnitOfMeasure unitOfMeasure1 = unitOfMeasureService.addUnitOfMeasure(unitOfMeasure, unitOfMeasure);
            final Supplier supplier = supplierService.addSupplier(supplierCode, supplierName, 0, 0, 0, 0.00, 0, "", 0.00, "");
            final Product product = productService.addProduct(catalogueNo, catalogueNo, description, reference
                    , taxCode, brand, prodType, prodType, supplier, catalogueNo, unitOfMeasure1, cost, price
            , bulkPrice, overWriteProduct);
            importProductResult.setImportedProduct(product);
            importProductResult.setImportedSupplier(supplier);
            importProductResult.setUnitOfMeasure(unitOfMeasure1);
            return importProductResult;
        } catch (Exception e) {
            logger.error("Exception in importing product: " + partNo, e);
            return null;
        }

    }
    /**
     * update product price from csv file.
     * @param inputStream csv file containing updated price
     * @throws Exception Exception
     * @return CommonResponse
     */
    public CommonResponse updateProductPriceFromCsvInputStream(InputStream inputStream) throws Exception {
        CSVReader csvReader = null;
        final CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
        try {
            ListIterator<String[]> iterator = null;
            String[] csvRow = null;
            csvReader = new CSVReader(new InputStreamReader(inputStream));
            if (csvReader == null) {
                logger.error("not able to open product price csv file");
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.addMessage("not able to open csv file");
                return commonResponse;
            }
            iterator = csvReader.readAll().listIterator();
            //read header
            if (iterator.hasNext()) {
                iterator.next();
            }
            //get o for sell-price
            //final PriceCode priceCode = priceDao.getProductPriceCodePerCode(IdBConstant.SELL_PRICE_CODE);
            while (iterator.hasNext()) {
                csvRow = iterator.next();
                if (csvRow == null) {
                    continue;
                }
                //check if the price has being changed.
                if (csvRow[3].equals(csvRow[4]) && csvRow[5].equals(csvRow[6]) && csvRow[7].equals(csvRow[8])) {
                    continue;
                }
                //update product costs.
                //check if product with catalogue no exists for that supplier. if not then display and error
                final List<SuppProdPrice> suppProdPrice = suppProdPriceDao.getSuppProdPricePerOrgUnitIdAndSupplierNameAndCatalogNo(
                        sessionState.getOrgUnit().getId(), csvRow[0], csvRow[1]);
                if (suppProdPrice != null && suppProdPrice.size() > 0 && suppProdPrice.get(0) != null) {
                    suppProdPriceDao.updateProductCostsPerSolIdAndProdId(suppProdPrice.get(0).getSolId(), suppProdPrice.get(0).getProdId()
                            , StringUtil.strToDouble(csvRow[4]), StringUtil.strToDouble(csvRow[6]), StringUtil.strToDouble(csvRow[8]));
                    //priceDao.updatePricePerProdIdAndPrccId(StringUtil.strToLong(csvRow[2]), priceCode.getId(), StringUtil.strToDouble(csvRow[8]));
                    productService.updateProductCostBaseDefaultSupplier(suppProdPrice.get(0).getProdId(), StringUtil.strToDouble(csvRow[6]));
                    if (suppProdPrice.size() > 1) {
                        commonResponse.addMessage("there were [" + suppProdPrice.size() + "] products with catalogue no [" + csvRow[1]
                                + "] from supplier [" + csvRow[0] + "] which one of them updated");
                    }
                } else {
                    commonResponse.addMessage("Catalogue No [" + csvRow[1] + "] for Supplier [" + csvRow[0] + "] does not exists. row ignored");
                }

            }
            commonResponse.addMessage("product price imported successfully");
            return commonResponse;
        } catch (Exception e) {
            logger.error("error in importing products : ", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.addMessage("error in updating product price from csv. see the logs");
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
            return commonResponse;
        }

    }

}
