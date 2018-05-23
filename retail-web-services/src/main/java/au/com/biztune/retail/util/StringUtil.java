package au.com.biztune.retail.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by arash on 7/11/14.
 */
public class StringUtil {
    private static final Logger COMMON_LOGGER = LoggerFactory.getLogger(StringUtil.class);

    /**
     * Extract platform number from platform name.
     * @param nodeName nodeName
     * @return int platform number
     */
    public static int extractPlatformNoFromNodeName(String nodeName) {
        try {
            if (nodeName == null || nodeName.isEmpty()) {
                return -1;
            }
            final String includedNumber = nodeName.replaceAll("[^\\x30-\\x39]", "");
            return Integer.valueOf(includedNumber).intValue();

        } catch (Exception e) {
            COMMON_LOGGER.error("Exception in extracting number from platform name: ", e);
            return -1;
        }
    }

    /**
     * convert string to int.
     * @param str str
     * @return int
     */
    public static int strToInt(String str) {
        try {
            return Integer.valueOf(str.trim());
        } catch (Exception e) {
            //COMMON_LOGGER.error("Can't convert string to int :", e);
            return 0;
        }
    }

    /**
     * convert string to int.
     * @param str str
     * @return int
     */
    public static long strToLong(String str) {
        try {
            return Long.valueOf(str.trim());
        } catch (Exception e) {
            //COMMON_LOGGER.error("Can't convert string to int :", e);
            return 0;
        }
    }

    /**
     * convert int to str.
     * @param input input
     * @return int
     */
    public static String intToStr(int input) {
        try {
            return String.valueOf(input);
        } catch (Exception e) {
            //COMMON_LOGGER.error("Can't convert int to str :", e);
            return "";
        }
    }

    /**
     * convert String to double.
     * @param str str
     * @return double
     */
    public static double strToDouble(String str) {
        try {
            return Double.valueOf(str.trim());
        } catch (Exception e) {
            //COMMON_LOGGER.error("Can't convert string to int :", e);
            return 0;
        }
    }

    /**
     * Split string based on splitter.
     * @param sourceStr sourceStr
     * @param splitter splitter
     * @return String[]
     */
    public static String[] splitString(String sourceStr, String splitter) {
        try {
            if (sourceStr == null || sourceStr.isEmpty()) {
                return null;
            }
            if (splitter == null || splitter.isEmpty()) {
                return null;
            }
            final String[] result = sourceStr.split(splitter);
            return result;
        } catch (Exception e) {
            //COMMON_LOGGER.error("Error in splittring String :", e);
            return null;
        }
    }

    /**
     * Convert String to XMLGregorianCalendar.
     * @param s s
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar stringToXmlGregorianCalendar(String s) {
        try {
            XMLGregorianCalendar result = null;
            Date date;
            SimpleDateFormat simpleDateFormat;
            GregorianCalendar gregorianCalendar;

            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat.parse(s);
            gregorianCalendar =
                    (GregorianCalendar) GregorianCalendar.getInstance();
            gregorianCalendar.setTime(date);
            result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            return result;
        } catch (Exception e) {
            COMMON_LOGGER.error("Error on converting String to XMLGregorianCalendar :", e);
            return null;
        }
    }

    /**
     * convert Long to str.
     * @param input input
     * @return String
     */
    public static String longToStr(long input) {
        try {
            return String.valueOf(input);
        } catch (Exception e) {
            //COMMON_LOGGER.error("Can't convert int to str :", e);
            return "";
        }
    }
}
