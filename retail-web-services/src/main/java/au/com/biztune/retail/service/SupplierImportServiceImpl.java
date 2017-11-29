package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ConfigCategoryDao;
import au.com.biztune.retail.dao.SupplierDao;
import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.Contact;
import au.com.biztune.retail.domain.SuppOrguLink;
import au.com.biztune.retail.domain.Supplier;
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
import java.util.ListIterator;

/**
 * Created by arash on 28/11/2017.
 */

@Component
public class SupplierImportServiceImpl implements SupplierImportService {
    private final Logger logger = LoggerFactory.getLogger(SupplierImportServiceImpl.class);
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ConfigCategoryDao configCategoryDao;
    @Autowired
    private SessionState sessionState;
    @Autowired
    private SupplierDao supplierDao;

    /**
     * import location from csv file.
     * @param inputStream inputStream
     * @throws Exception Exception
     * @return CommonResponse
     */
    public CommonResponse importSupplierFromCsvInputStream(InputStream inputStream) throws Exception {
        CSVReader csvReader = null;
        final CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
        try {
            ListIterator<String[]> iterator = null;
            String[] csvRow = null;
            csvReader = new CSVReader(new InputStreamReader(inputStream));
            if (csvReader == null) {
                logger.error("not able to open supplier inputStream");
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("not able to open supplier inputStream");
                commonResponse.addMessage("not able to open csv file");
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
                final CommonResponse result = importSupplierWhole(csvRow[3], csvRow[1], csvRow[4], csvRow[5], csvRow[6], csvRow[7], csvRow[8], csvRow[12], csvRow[10]
                        , csvRow[9], csvRow[11], csvRow[13], csvRow[14], csvRow[15], StringUtil.strToInt(csvRow[28]), StringUtil.strToInt(csvRow[29])
                        , StringUtil.strToInt(csvRow[30]));

                if (result.getStatus() == IdBConstant.RESULT_FAILURE) {
                    commonResponse.addMessage("Not able to import supplier " + csvRow[1] + " Error: " + result.getMessage());
                } else {
                    commonResponse.addMessage("Supplier [" + csvRow[1] + "] Imported successfully");
                }
                commonResponse.addAll(result.getMessageList());
                commonResponse.setStatus(commonResponse.getStatus() * result.getStatus());
            }
            return commonResponse;
        } catch (Exception e) {
            logger.error("error in importing products : ", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage("error in importing suppliers. see the logs");
            commonResponse.addMessage("error in importing suppliers. see the logs");
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
            return commonResponse;
        }
    }


    /**
     * import whole supplier data.
     * @param supplierCode barcode
     * @param supplierName supplier name
     * @param mainContact main contact
     * @param mainPosition main position
     * @param mainAddress1 mainAddress1
     * @param mainAddress2 mainAddress2
     * @param mainAddress3 mainAddress3
     * @param mainCountry mainCountry
     * @param mainState mainState
     * @param mainSuburb mainsuburb
     * @param mainPostcode mainPostcode
     * @param mainPhone mainPhone
     * @param mainFax mainFax
     * @param mainEmail mainEmail
     * @param freightFree freightFree
     * @param inActive inActive
     * @param deliveryDelay deliveryDelay
     * @return CommonResponse
     */
    private CommonResponse importSupplierWhole(
            String supplierCode,
            String supplierName,
            String mainContact,
            String mainPosition,
            String mainAddress1,
            String mainAddress2,
            String mainAddress3,
            String mainCountry,
            String mainState,
            String mainSuburb,
            String mainPostcode,
            String mainPhone,
            String mainFax,
            String mainEmail,
            int freightFree,
            int inActive,
            int deliveryDelay
    )
    {
        final CommonResponse response = new CommonResponse();
        try {
            if (supplierCode == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.addMessage("supplier ABN is empty");
                return response;
            }
            if (supplierName == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.addMessage("supplier name is empty");
                return response;
            }
            Supplier supplier;
            supplier = supplierService.getSupplierByCode(supplierCode);
            if (supplier == null) {
                supplier = supplierService.getSupplierByName(supplierName);
            } else {
                response.addMessage("Supplier with ABN [" + supplierCode + "] exists. record was updated");
            }
            if (supplier == null) {
                supplier = new Supplier();
                supplier.setId(-1);
            } else {
                response.addMessage("Supplier with name [" + supplierName + "] exists. record was updated");
            }
            supplier.setSupplierName(supplierName);
            supplier.setSupplierCode(supplierCode);
            supplier.setContactKnownAs(mainPosition);
            supplier.setDeliveryFreightFree(freightFree != 0);
            ConfigCategory supplierStatus;
            if (inActive == 0) {
                supplierStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.CONFIG_SUPLIER_STATUS, IdBConstant.SUPPLIER_STATUS_ACTIVE);
            } else {
                supplierStatus = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.CONFIG_SUPLIER_STATUS, IdBConstant.SUPPLIER_STATUS_HELD);
            }
            supplier.setSupplierStatus(supplierStatus);
            supplier.setStockCover(deliveryDelay);

            final SuppOrguLink suppOrguLink = new SuppOrguLink();
            suppOrguLink.setOrguId(sessionState.getOrgUnit().getId());
            supplier.setSuppOrguLink(suppOrguLink);

            final Contact contact = new Contact();
            contact.setCountry(mainCountry);
            contact.setState(mainState);
            contact.setAddress1(mainAddress1);
            contact.setAddress2(mainAddress2 + " " + mainAddress3);
            contact.setEmail(mainEmail);
            contact.setFax(mainFax);
            contact.setPhone(mainPhone);
            contact.setSuburb(mainSuburb);
            contact.setPostCode(mainPostcode);
            //contact.set(mainPosition);

            try {
                final String[] contactName = mainContact.trim().split("\\s+");
                supplier.setContactFirstName(contactName[0]);
                if (contactName.length == 2) {
                    supplier.setContactSurName(contactName[1]);
                }
            } catch (Exception e) {
                logger.error("error in parsing supplier contact no");
            }
            supplier.setContact(contact);
            final CommonResponse result = supplierService.addSupplier(supplier);
            response.addMessage(result.getMessage());
            response.setStatus(result.getStatus());
            return response;
        } catch (Exception e) {
            logger.error("Exception in importing supplier", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
