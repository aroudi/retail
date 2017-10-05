package au.com.biztune.retail.service;

import au.com.biztune.retail.response.CommonResponse;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;

/**
 * Created by arash on 3/10/2017.
 */
public interface InvoiceImportService {
    /**
     * upload Invoice.
     * @param uploadedInputStream uploadedInputStream
     * @param securityContext securityContext
     * @return CommonResponse
     */
    CommonResponse importInvoice (InputStream uploadedInputStream, SecurityContext securityContext);
}
