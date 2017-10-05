package au.com.biztune.retail.upload;


import au.com.biztune.retail.generated.invoice.Invoices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * Created by arash on 03/09/2017.
 */
@Component
public class InvoiceUploader {

    private Invoices invoices;
    private final Logger logger = LoggerFactory.getLogger(InvoiceUploader.class);

    /**
     * upload Invoice from input stream.
     * @param stream stream
     * @return Invoices Object
     */
    public Invoices uploadInvoiceFromInputStream(InputStream stream) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Invoices.class);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            invoices = (Invoices) unmarshaller.unmarshal(stream);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(invoices, System.out);
        } catch (Exception e) {
            logger.error("Exception in reading file:", e);
        }
        return invoices;
    }

    /**
     * upload invoice xml.
     * @param filePath filePath.
     * @return Invoices
     */
    public Invoices uploadInvoiceFromFile(String filePath) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(Invoices.class);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            final InputStream stream = getClass().getResourceAsStream(filePath);
            invoices = (Invoices) unmarshaller.unmarshal(stream);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(invoices, System.out);
        } catch (Exception e) {
            logger.error("Exception in reading file:", e);
        }
        return invoices;
    }
}
