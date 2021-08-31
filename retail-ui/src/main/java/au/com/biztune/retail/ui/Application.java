package au.com.biztune.retail.ui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot Application class.
 */

@SpringBootApplication
//import configuration beans from xml file
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
