package au.com.biztune.retail.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by arash on 11/03/2015.
 */
@Component
public class Loader implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(Loader.class);

    /**
     * set default version to active version on startup.
     * @param event event
     */
    public void onApplicationEvent(ContextRefreshedEvent event) {
    }
}
