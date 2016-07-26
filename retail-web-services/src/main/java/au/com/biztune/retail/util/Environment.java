/*
 * Environment.java
 *
 * Created on July 18, 2002, 12:45 PM
 */

package au.com.biztune.retail.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

/**
 *  Provides static access to system wide environment variables.
 *  Variables should be defined as key value pairs in
 *  <code>application.properties</code>.  This
 *  file must reside in the root of the classpath.  For web
 *  applications this can be the <APP_NAME>/WEB-INF/classes
 *  directory.
 *
 *  @author asilverthorne doog2112@yahoo.com
 *  @version 1.0
 *  @since JDK 1.0
 */
public class Environment extends Object {

    /** Class constants **/
    public static final String PROPERTIES_FILE =
            "/retail.properties";

    /** Collection of application properties as read from PROPERTIES_FILE */
    private static Properties m_properties = null;

    private static String webAppContextDir;

    /** Static constructor
     *
     */
    static {
        refresh();
    }

    /** Constructor is hidden from view so as to implement a singleton
     *  model.  There is no need for this class to be instantiated
     *  directly.
     */
    private void Environment(){}

    /** Static initializer.  Gets a handler to PROPERTIES_FILE and loads
     *  data into the properties collection.
     *
     */
    public static void refresh(){
        m_properties = new Properties();
        try {
            String configDir = System.getenv("RETAIL_CONFIG_DIR");
            /** Get a class reference for the sole purpose of opening an
             *  input stream.  In JDK 1.4 the first two lines below should
             *  be removed and replaced with the following...
             *  InputStream is = Environment.class.getResource(
             *	 		PROPERTIES_FILE).openStream();
             */
            DummyObject obj = new DummyObject();
            InputStream is = obj.getClass().getResourceAsStream(
                    "/" +configDir + PROPERTIES_FILE);
            m_properties.load(is);
            is.close();
        }
        catch (FileNotFoundException fnfe){
            System.err.println("Environment Error - FileNotFoundException " +
                    fnfe.getMessage());
            fnfe.printStackTrace();
        }
        catch (IOException ioe){
            System.err.println("Environment Error - IOException " +
                    ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    /** Get the value of a property.  <code>null</code> is not returned.
     *  If a property name is not found, method returns an empty string.
     *
     * @param property - The property name to retrieve
     * @return String - The value as fetched from PROPERTIES_FLIE
     *
     */
    public static String get(String property){
        String value = (String) m_properties.getProperty(property);
        if (value == null){
            value = "";
        }
        value = value.replace("WEB_APP_CONTEXT/", webAppContextDir);
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome == null) {
            javaHome = get("JAVA_HOME");
        }
        value = value.replace("JAVA_HOME", javaHome);
        return value;
    }

    /** Set the value of a property.  The value will be set for the
     *  the life of the process, but will NOT be written to the
     *  properties file.  Edit the properties file manually to add
     *  permanent values.  If a null value is passed, the key will be
     *  removed.
     *
     * @param key - The key to set
     * @param value - The value to set
     *
     */
    public static void set(String key, String value){
        if (value == null){
            m_properties.remove(key);
        }
        else {
            m_properties.setProperty(key, value);
        }
    }

    /** List the output of the properties file to a specified
     *  PrintWriter
     *
     *  @param out - the PrintWriter to write the output to
     *  @since JDK 1.1
     */
    public static void list(PrintWriter out){
        m_properties.list(out);
    }

    public static void setWebAppContextDir (String contextDir) {
        webAppContextDir = contextDir;
    }

}