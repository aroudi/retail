package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.Product;
import au.com.biztune.retail.domain.Supplier;
import au.com.biztune.retail.domain.UnitOfMeasure;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.util.IdBConstant;
import au.com.biztune.retail.util.StringUtil;
import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
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
                commonResponse.setMessage("not able to open product inputStream");
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
                        , csvRow[11], csvRow[14], csvRow[16], StringUtil.strToDouble(csvRow[19]), StringUtil.strToDouble(csvRow[20]));
            }
            return commonResponse;
        } catch (Exception e) {
            logger.error("error in importing products : ", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage("error in importing products. see the logs");
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
     * @param bulkPrice bulkPrice
     */
    public void importProductWhole(String brand
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
            , double bulkPrice)
    {
        try {
            final UnitOfMeasure unitOfMeasure1 = unitOfMeasureService.addUnitOfMeasure(unitOfMeasure, unitOfMeasure);
            final Supplier supplier = supplierService.addSupplier(supplierCode, supplierName, 0, 0, 0, 0.00, 0, "", 0.00, "");
            final Product product = productService.addProduct(partNo, description, description, reference
                    , taxCode, brand, prodType, prodType, supplier, catalogueNo, unitOfMeasure1, cost, cost
            , bulkPrice);
        } catch (Exception e) {
            logger.error("Exception in importing product: " + partNo, e);
        }

    }
}