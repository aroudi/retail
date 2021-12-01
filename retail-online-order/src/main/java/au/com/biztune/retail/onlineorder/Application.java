package au.com.biztune.retail.onlineorder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot Application class.
 */

@SpringBootApplication
@MapperScan("au.com.biztune.retail.dao")
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
