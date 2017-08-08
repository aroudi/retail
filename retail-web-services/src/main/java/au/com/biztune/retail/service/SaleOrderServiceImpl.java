package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.session.SessionState;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by arash on 6/07/2017.
 */
@Component
public class SaleOrderServiceImpl implements SaleOrderService {
    @Autowired
    private SessionState sessionState;
    private final Logger logger = LoggerFactory.getLogger(ConfigCategoryServiceImpl.class);
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private ConfigCategoryDao configCategoryDao;
    @Autowired
    private ProductDao productDao;
    private SecurityContext securityContext;
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private TxnDao txnDao;

    @Autowired
    private PurchaseOrderDao purchaseOrderDao;

    @Autowired
    private PoSoLinkDao poSoLinkDao;

    @Autowired
    private InvoiceDao invoiceDao;
    /**
     * create purcahse order from sale order.
     * @param txhdIdList txhd id list.
     * @param securityContext securityContext
     * @return List of created purchase orders
     */
    @Transactional
    public List<PurchaseOrderHeader> createPurchaseOrderFromSaleOrder(List<Long> txhdIdList, SecurityContext securityContext) {
        try {

            this.securityContext = securityContext;
            AppUser appUser;
            //set user
            final Principal principal = securityContext.getUserPrincipal();
            if (principal instanceof AppUser) {
                appUser = (AppUser) principal;
            } else {
                appUser = null;
            }
            final List<PurchaseOrderHeader> result = new ArrayList<PurchaseOrderHeader>();

            //fetch all Txn details for those txhdIds from db and filter only those with OUTSTANDING status.
            final List<TxnDetail> txnDetailList = txnDao.getTxnDetailsOfMultipleTxnId(txhdIdList)
                    .stream()
                    .filter(c -> (c.getStatus() == null) || (c.getStatus().getCategoryCode().equals(IdBConstant.SO_STATUS_OUTSTANDING))).collect(Collectors.toList());
            //group all txn details per supplier id.
            final Map<Long, List<TxnDetail>> supplierTxnDetailMap = txnDetailList
                    .stream()
                    .collect(Collectors
                            .groupingBy(TxnDetail::getSupplierId));

            //create purchase order for each supplier
            supplierTxnDetailMap.forEach((supplierKey, supplierSoItems) -> {
            /*
            if (supplierSoItems == null || supplierSoItems.size() < 1) {
                continue;
            }
            */
                //get the first item from the list and create the Purchase Order Header.
                final TxnDetail txnDetailItem = supplierSoItems.get(0);
                final PurchaseOrderHeader purchaseOrderHeader = purchaseOrderService.createPoFromSaleOrder(txnDetailItem, appUser);
                supplierSoItems.forEach(item -> {
                    //now we have item. let's create the header
                    if (purchaseOrderService.addLineToPoFromTxnDetail(purchaseOrderHeader, item)) {
                        //update billOfQuantity Item : purchased and balance values
                        //item.set(item.getQtyBalance());
                        //item.setQtyBalance(0.00);
                        final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_ON_ORDER);
                        if (status != null) {
                            item.setStatus(status);
                            //
                            item.setTxdeQtyOrdered(item.getTxdeQtyBackOrder());
                        }
                        txnDao.updateTxnDetailBackOrderAndStatus(item);
                    } else {
                        logger.debug("not able to create purchase order line for sale order item : " + purchaseOrderHeader.getPohOrderNumber() + " item: " + item.getId());
                    }
                });
                //update the total fields in purchase order header.
                purchaseOrderDao.updatePurchaseOrderHeader(purchaseOrderHeader);

                //fetch all project codes assigned to PO and insert as comma separated list to field PO_PRJ_CODE just for search.

                final List<PoSoLink> linkedProjects = poSoLinkDao.getAllPoSoProjectCodesPerPohId(purchaseOrderHeader.getId());
                if (linkedProjects != null && linkedProjects.size() > 0) {
                    final String commaSeparatedProjectCodes = linkedProjects.stream().map(PoSoLink::getProjectCode).collect(Collectors.joining(","));
                    purchaseOrderDao.updatePohProjectCode(purchaseOrderHeader.getId(), commaSeparatedProjectCodes);
                }
                result.add(purchaseOrderHeader);
            });
            //now change the status of Sale Order merged
            final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_SO_STATUS, IdBConstant.SO_STATUS_ON_ORDER);
            txhdIdList.forEach(txhdId -> {
                txnDao.updateTxnHeaderStatusPerTxhdId(txhdId, status.getId());
            });
            return result;
        } catch (Exception e) {
            logger.error("Exception in converting sale order to purchase order:", e);
            return null;
        }
    }

    /**
     * get all invoices of sale order.
     * @param txhdId sale order id
     * @return list of invoices
     */
    public List<TxnHeader> getAllInvoiceOfSaleOrder(long txhdId) {
        try {
            return invoiceDao.getAllInvoiceOfSaleOrder(sessionState.getOrgUnit().getId(), txhdId);

        } catch (Exception e) {
            logger.error("exception in returning invoice list of sale order", e);
            return null;
        }
    }

    public SessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(SessionState sessionState) {
        this.sessionState = sessionState;
    }

    public SupplierDao getSupplierDao() {
        return supplierDao;
    }

    public void setSupplierDao(SupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

    public ConfigCategoryDao getConfigCategoryDao() {
        return configCategoryDao;
    }

    public void setConfigCategoryDao(ConfigCategoryDao configCategoryDao) {
        this.configCategoryDao = configCategoryDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    public PurchaseOrderService getPurchaseOrderService() {
        return purchaseOrderService;
    }

    public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    public TxnDao getTxnDao() {
        return txnDao;
    }

    public void setTxnDao(TxnDao txnDao) {
        this.txnDao = txnDao;
    }

    public PurchaseOrderDao getPurchaseOrderDao() {
        return purchaseOrderDao;
    }

    public void setPurchaseOrderDao(PurchaseOrderDao purchaseOrderDao) {
        this.purchaseOrderDao = purchaseOrderDao;
    }
}
