package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.TxnHeader;
import au.com.biztune.retail.response.PoFromSoResponse;
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
     * @return Response object containing List of created purchase orders
     */
    @Transactional
    PoFromSoResponse createPurchaseOrderFromSaleOrder(List<Long> txhdIdList, SecurityContext securityContext);

    /**
     * get all invoices of sale order.
     * @param txhdId sale order id
     * @return list of invoices
     */
    List<TxnHeader> getAllInvoiceOfSaleOrder(long txhdId);
    }
