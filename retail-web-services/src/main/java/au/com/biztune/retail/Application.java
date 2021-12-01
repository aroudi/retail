package au.com.biztune.retail;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * Spring boot Application class.
 */

@ServletComponentScan
@SpringBootApplication
//import configuration beans from xml file
@ImportResource("retail-web-services-Context.xml")
public class Application {
    /**
     * Main method of Spring Boot Application class.
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
