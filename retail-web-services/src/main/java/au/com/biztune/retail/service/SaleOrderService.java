package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.PurchaseOrderHeader;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by akhoshraft on 7/07/2017.
 */
public interface SaleOrderService {

    /**
     * create purcahse order from sale order.
     * @param txhdIdList txhd id list.
     * @param securityContext securityContext
     * @return List of created purchase orders
     */
    @Transactional
    List<PurchaseOrderHeader> createPurchaseOrderFromSaleOrder(List<Long> txhdIdList, SecurityContext securityContext);
}
