// RailCorp 2014
package au.com.biztune.retail.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arash on 30/05/14.
 */
public class DateUtil {

    private   static final Logger LOGGER_1 = LoggerFactory.getLogger(DateUtil.class);

    /**
     * dateToString.
     * @param date
     *          timeStamp
     * @return
     *      String
     */
    public static String dateToString(Timestamp date) {
        String result = null;
        if (date == null) {
            return result;
        }
        try {
            result = (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(date);
            return result;
        } catch (Exception e) {
            LOGGER_1.error ("Error in formatting date:" + e);
            return null;
        }
    }

    /**
     * dateToString.
     * @param date
     *          timeStamp
     * @param format
     *          format
     * @return
     *      String
     */
    public static String dateToString(Timestamp date, String format) {
        String result = null;
        if (date == null) {
            return result;
        }
        try {
            result = (new SimpleDateFormat(format)).format(date);
            return result;
        } catch (Exception e) {
            LOGGER_1.error ("Error in formatting date:" + e);
            return null;
        }
    }
    /**
     * string to date.
     * @param str
     *          string in special format
     * @return
     *      timestamp
     */
    public static Timestamp stringToDate(String str) {
        Timestamp result = null;
        if (str == null || str.isEmpty()) {
            return result;
        }
        try {
            final Date date = (new SimpleDateFormat("dd-MM-yyyy hh:mma")).parse(str);
            result = new Timestamp(date.getTime());
            return result;
        } catch (Exception e) {
            LOGGER_1.error ("Error in formatting date:" + e);
            return null;
        }
    }
    /**
     * string to date.
     * @param str
     *          string in special format
     * @param format
     *          string in special format
     * @return
     *      timestamp
     */
    public static Timestamp stringToDate(String str, String format) {
        Timestamp result = null;
        if (str == null || str.isEmpty()) {
            return result;
        }
        try {
            final Date date = (new SimpleDateFormat(format)).parse(str);
            result = new Timestamp(date.getTime());
            return result;
        } catch (Exception e) {
            LOGGER_1.error ("Error in formatting date:" + e);
            return null;
        }
    }
}
