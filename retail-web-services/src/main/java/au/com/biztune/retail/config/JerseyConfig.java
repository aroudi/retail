package au.com.biztune.retail.config;

import au.com.biztune.retail.rest.*;
import au.com.biztune.retail.security.AuthenticationFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * coniguration class for jersey.
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

    /**
     * JerseyConfig.
     */
    public JerseyConfig() {

        register(AccountingRest.class);
        register(BillOfQuantityRest.class);
        register(CashSessionRest.class);
        register(ConfigCategoryRest.class);
        register(CustomerRest.class);
        register(DeliveryNoteRest.class);
        register(PaymentMediaRest.class);
        register(ProductGroupRest.class);
        register(ProductRest.class);
        register(PurchaseOrderRest.class);
        register(ReportingRest.class);
        register(SaleOrderRest.class);
        register(SaleSummaryReportRest.class);
        register(SupplierRest.class);
        register(TaxRuleRest.class);
        register(TransactionRest.class);
        register(UnitOfMeasureRest.class);
        register(UserRest.class);
        register(org.glassfish.jersey.media.multipart.MultiPartFeature.class);
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
        register(CorsFilter.class);
        register(AuthenticationFilter.class);

    }
}