package au.com.biztune.retail.service;

import au.com.biztune.retail.response.CommonResponse;

import java.io.InputStream;

/**
 * Created by arash on 29/11/2017.
 */
public interface SupplierImportService {
    /**
     * import location from csv file.
     * @param inputStream inputStream
     * @throws Exception Exception
     * @return CommonResponse
     */
    CommonResponse importSupplierFromCsvInputStream(InputStream inputStream)  throws Exception;
}
