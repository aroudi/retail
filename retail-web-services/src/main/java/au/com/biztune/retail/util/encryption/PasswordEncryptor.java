/*
 * Package: au.com.biztune.retail.util.encryption
 * Class: PasswordEncryptor
 * Copyright: (c) 2019 Sydney Trains
 */
package au.com.biztune.retail.util.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

/**
 * PasswordEncryptor.
 *
 * @author arash
 */
public class PasswordEncryptor {

    private static final Logger log = LoggerFactory.getLogger(PasswordEncryptor.class);

    /**
     * Returns a hexadecimal encoded SHA-256 hash for the input String.
     * @param data data
     * C1C224B03CD9BC7B6A86D77F5DACE40191766C485CD55DC48CAF9AC873335D6F
     * @return encrypted password
     */
    public static String encrypt(String data) {
        final String result = null;
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        } catch (Exception ex) {
            log.debug("Exception in hashing code", ex);
        }
        return result;
    }

    /**
     * Use javax.xml.bind.DatatypeConverter class in JDK to convert byte array
     * to a hexadecimal string. Note that this generates hexadecimal in upper case.
     * @param hash hash
     * @return hashed code
     */
    private static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
}
