package au.gov.nsw.railcorp.config;

import au.gov.nsw.railcorp.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
/**
 * ServletInitializer.
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * Configure SpringApplicationBuilder.
     *
     * @param application SpringApplicationBuilder
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
