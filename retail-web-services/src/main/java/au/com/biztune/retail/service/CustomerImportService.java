package au.com.biztune.retail.service;

import au.com.biztune.retail.response.CommonResponse;

import java.io.InputStream;

/**
 * Created by arash on 4/12/2017.
 */
public interface CustomerImportService {
    /**
     * import location from csv file.
     * @param inputStream inputStream
     * @throws Exception Exception
     * @return CommonResponse
     */
    CommonResponse importCustomerFromCsvInputStream(InputStream inputStream) throws Exception;
}
