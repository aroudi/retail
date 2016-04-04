package au.com.biztune.retail.upload;


import au.com.biztune.retail.generated.BillOfQuantity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * Created by arash on 4/04/2016.
 */
@Component
public class BillOfQuantityUploader {

    private BillOfQuantity billOfQuantity;
    private final Logger logger = LoggerFactory.getLogger(BillOfQuantityUploader.class);

    /**
     * upload BillOfQuantity from input stream.
     * @param stream stream
     * @return BillOfQuantity Object
     */
    public BillOfQuantity uploadBillOfQuantityFromInputStream(InputStream stream) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(BillOfQuantity.class);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            billOfQuantity = (BillOfQuantity) unmarshaller.unmarshal(stream);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(billOfQuantity, System.out);
        } catch (Exception e) {
            logger.error("Exception in reading file:", e);
        }
        return billOfQuantity;
    }

    /**
     * upload bill of quantity xml.
     * @param filePath filePath.
     * @return BillOfQuantity
     */
    public BillOfQuantity uploadBillOfQuantityFromFile(String filePath) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(BillOfQuantity.class);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            final InputStream stream = getClass().getResourceAsStream(filePath);
            billOfQuantity = (BillOfQuantity) unmarshaller.unmarshal(stream);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(billOfQuantity, System.out);
        } catch (Exception e) {
            logger.error("Exception in reading file:", e);
        }
        return billOfQuantity;
    }
}
