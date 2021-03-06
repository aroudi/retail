package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.*;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.form.TxnDetailForm;
import au.com.biztune.retail.form.TxnHeaderForm;
import au.com.biztune.retail.form.TxnMediaForm;
import au.com.biztune.retail.generated.invoice.Invoices;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.upload.InvoiceUploader;
import au.com.biztune.retail.util.DateUtil;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by arash on 3/10/2017.
 */
@Component
public class InvoiceImportServiceImpl implements InvoiceImportService {

    private final Logger logger = LoggerFactory.getLogger(InvoiceImportServiceImpl.class);
    @Autowired
    private InvoiceUploader invoiceUploader;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UnitOfMeasureDao unitOfMeasureDao;
    @Autowired
    private TaxRuleDao taxRuleDao;
    @Autowired
    private PaymentMediaDao paymentMediaDao;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private ConfigCategoryDao configCategoryDao;
    @Autowired
    private BillOfQuantityDao billOfQuantityDao;
    @Autowired
    private BoqDetailDao boqDetailDao;

    private SecurityContext securityContext;
    /**
     * upload Invoice.
     * @param uploadedInputStream uploadedInputStream
     * @param securityContext securityContext
     * @return CommonResponse
     */
    public CommonResponse importInvoice (InputStream uploadedInputStream, SecurityContext securityContext) {
        this.securityContext = securityContext;
        final CommonResponse response = new CommonResponse();
        try {
            final Invoices invoices = invoiceUploader.uploadInvoiceFromInputStream(uploadedInputStream);
            if (invoices == null) {
                response.setStatus(IdBConstant.RESULT_FAILURE);
                response.setMessage("Not able to process input stream. please refer to the log");
                return response;
            }
            //process the invoices and create invoice
            return saveInvoice(invoices);

        } catch (Exception e) {
            logger.error("Exception in importing BOQ:", e);
            response.setStatus(IdBConstant.RESULT_FAILURE);
            response.setMessage(response.getMessage());
            return response;
        }
    }

    private CommonResponse saveInvoice(Invoices invoiceList) {
        final CommonResponse response = new CommonResponse();
        response.setMessage("");

        if (invoiceList.getInvoice() == null || invoiceList.getInvoice().size() < 1) {
            response.addMessage("Invalid invoice: <invoice> tag is empty");
            response.setStatus(IdBConstant.RESULT_FAILURE);
            return response;
        }

        TxnHeaderForm txnHeaderForm = null;
        TxnDetailForm txnDetailForm = null;
        for (Invoices.Invoice invoice : invoiceList.getInvoice()) {
            //check if invoice is already exists.
            if (invoiceDao.checkInvoiceExistsPerNo(invoice.getHeader().getInvoiceNo()) > 0) {
                response.addMessage("<invoiceNo> " + invoice.getHeader().getInvoiceNo() + ":  is already exists ---> skipped");
                continue;
            }
            //create txnHeader
            txnHeaderForm = new TxnHeaderForm();
            txnHeaderForm.setImported(true);
            txnHeaderForm.setId(-1);

            final ConfigCategory txnType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_TYPE, IdBConstant.TXN_TYPE_INVOICE);
            txnHeaderForm.setTxhdTxnType(txnType);

            //set header information
            if (invoice.getHeader() != null) {
                txnHeaderForm.setTxhdTxnNr(invoice.getHeader().getInvoiceNo());
                txnHeaderForm.setTxhdTradingDate(DateUtil.stringToDate(invoice.getHeader().getInvoiceDate(), "yyyy-MM-dd HH:mm:ss"));
                txnHeaderForm.setTxhdValueGross(invoice.getHeader().getTotalAmountGross());
                txnHeaderForm.setTxhdValueNett(invoice.getHeader().getTotalAmountNet());
                txnHeaderForm.setTxhdValueTax(invoice.getHeader().getTotalAmountTax());
                txnHeaderForm.setTxhdValueChange(invoice.getHeader().getValueChange());
            }
            if (invoice.getProject() != null) {
                txnHeaderForm.setTxhdPrjCode(invoice.getProject().getProjectNo());
            }
            if (invoice.getClient() != null) {
                txnHeaderForm.setTxhdContactPerson(invoice.getClient().getContactName());
                txnHeaderForm.setTxhdContactPhone(invoice.getClient().getContactNo());
                txnHeaderForm.setTxhdDlvAddress(invoice.getClient().getDeliveryAddress());
                final Customer customer = customerDao.getCustomerByCompanyName(invoice.getClient().getClientName());
                txnHeaderForm.setCustomer(customer);
            }

            //build the txn details
            if (invoice.getProducts() != null && invoice.getProducts().getProduct() != null) {
                for (Invoices.Invoice.Products.Product product: invoice.getProducts().getProduct()) {
                    //find the product;
                    final List<ProductSaleItem> productSaleItemMatched = productDao.getProductSaleItemPerSku(product.getSku());
                    if (productSaleItemMatched == null || productSaleItemMatched.size() < 1) {
                        response.addMessage("<sku> " + product.getSku() + " not found.");
                        response.setStatus(IdBConstant.RESULT_FAILURE);
                        //dont process this product
                        continue;
                    }
                    UnitOfMeasure prodUnitOfMeasure = unitOfMeasureDao.getUnomByCode(product.getUnit());
                    if (prodUnitOfMeasure == null) {
                        response.addMessage("<unit> " + product.getUnit() + " not found, created by system.");
                        prodUnitOfMeasure = new UnitOfMeasure();
                        prodUnitOfMeasure.setUnomCode(product.getUnit());
                        prodUnitOfMeasure.setUnomDesc(product.getUnit());
                        unitOfMeasureDao.insert(prodUnitOfMeasure);
                    }
                    TaxRule taxRule = taxRuleDao.getTaxRuleByCode(product.getTaxName());
                    if (taxRule == null) {
                        response.addMessage("<taxName> " + product.getTaxName() + " not found, used default one.");
                        taxRule = taxRuleDao.getTaxRuleByCode(IdBConstant.DEFAULT_PRODUCT_TAX_CODE);
                    }
                    txnDetailForm = new TxnDetailForm();
                    txnDetailForm.setId(-1);
                    txnDetailForm.setProduct(productSaleItemMatched.get(0));
                    txnDetailForm.setUnitOfMeasure(prodUnitOfMeasure);
                    txnDetailForm.setTxdeQuantitySold(product.getQty());
                    txnDetailForm.setTxdeQtyInvoiced(product.getQty());
                    txnDetailForm.setTxdeProdName(product.getProductName());
                    txnDetailForm.setInvoiced(true);
                    txnDetailForm.setTxdeValueLine(product.getSellPrice());
                    txnDetailForm.setTxdeValueGross(product.getSellPrice() * product.getQty());
                    txnDetailForm.setTxdeTax(taxRule.getTaxLegVariance().getTxlvRate());
                    txnDetailForm.setTxdeValueNet(txnDetailForm.getTxdeValueGross() * (1 + txnDetailForm.getTxdeTax()));
                    txnDetailForm.setTxdePriceSold(product.getNetAmount());

                    //todo:check the following values
                    txnDetailForm.setTxdeQtyBackOrder(0);
                    txnDetailForm.setTxdeQtyOrdered(0);
                    txnDetailForm.setTxdeQtyReceived(0);
                    txnDetailForm.setTxdeQtyBalance(0);
                    //---------------------------

                    txnHeaderForm.addTxnDetailForm(txnDetailForm);
                }
            }

            //build txn media list
            final ConfigCategory mediaType = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_TXN_MEDIA_TYPE, IdBConstant.TXN_MEDIA_TYPE_SALE);
            if (invoice.getPayments() != null && invoice.getPayments().getPayment() != null) {
                for (Invoices.Invoice.Payments.Payment payment : invoice.getPayments().getPayment()) {
                    final TxnMediaForm txnMediaForm = new TxnMediaForm();
                    txnMediaForm.setId(-1);
                    PaymentMedia paymentMedia = paymentMediaDao.getPaymentMediaPerName(payment.getPaymentMethod().trim());
                    if (paymentMedia == null) {
                        response.addMessage("<paymentMethod> " + payment.getPaymentMethod() + " not found, used CASH.");
                        paymentMedia = paymentMediaDao.getPaymentMediaPerCode(IdBConstant.PAY_MEDIA_CODE_CASH);
                    }
                    txnMediaForm.setPaymentMedia(paymentMedia);
                    txnMediaForm.setTxmdAmountLocal(payment.getAmount());
                    txnHeaderForm.addTxnMediaForm(txnMediaForm);
                    txnMediaForm.setTxmdType(mediaType);
                }
            }
        }
        //if the data are invalid just return the response.
        if (response.getStatus() == IdBConstant.RESULT_FAILURE) {
            logger.debug("InvoiceImporter: not saved invoice. result =" + response.getMessage());
            return response;
        }
        // save invoice
        final CommonResponse addInvoiceResponse = transactionService.createInvoice(txnHeaderForm, securityContext);
        //update related boq status and qty released.
        updateReleatedBoq(txnHeaderForm, invoiceList);
        response.addMessage(addInvoiceResponse.getMessage());
        response.setStatus(addInvoiceResponse.getStatus());
        if (addInvoiceResponse.getStatus() == IdBConstant.RESULT_SUCCESS) {
            response.addMessage("Invoice/s imported successfully.");
        }
        logger.debug("InvoiceImporter: result =" + response.toString());
        return response;
    }

    /**
     * update related relatedBoq.
     * @param txnHeaderForm txnHeaderForm
     * @param invoiceList invoiceList
     */
    public void updateReleatedBoq(TxnHeaderForm txnHeaderForm, Invoices invoiceList) {
        try {
            logger.debug("updating related boq for invoice.");
            for (Invoices.Invoice invoice : invoiceList.getInvoice()) {
                //fetch related boq.
                //todo: boq name should be unique and be mandatory for xsl
                BillOfQuantity billOfQuantity = billOfQuantityDao.getBillOfQuantityByNameAndOrderNo(invoice.getBoq().getName(), invoice.getBoq().getOrderNumber());
                if (billOfQuantity == null) {
                    logger.debug("boq with name " + invoice.getBoq().getName() + " and " + invoice.getBoq().getOrderNumber() + " Not found.");
                    return;
                }
                billOfQuantity.getLines().forEach(boqLine -> {
                    //get all quantity released;
                    final double quantityInvoiced = txnHeaderForm.getTxnDetailFormList().stream()
                            .filter(txnDetailForm -> txnDetailForm.getProduct().getId() == boqLine.getProduct().getId())
                            .map(txnDetailForm -> txnDetailForm.getTxdeQtyInvoiced()).reduce(0.00, Double::sum);
                    logger.debug("quantityInvoiced = " + quantityInvoiced);
                    if (quantityInvoiced > 0 && quantityInvoiced >= boqLine.getQuantity()) {
                        final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_FINAL);
                        boqDetailDao.updateBoqQtyReleasedPerBoqdId(quantityInvoiced, txnHeaderForm.getTxhdTxnNr(), status.getId(), boqLine.getId());
                    }
                    if (quantityInvoiced > 0 && quantityInvoiced < boqLine.getQuantity()) {
                        final ConfigCategory status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_PAR_RELEASED);
                        boqDetailDao.updateBoqQtyReleasedPerBoqdId(quantityInvoiced, txnHeaderForm.getTxhdTxnNr(), status.getId(), boqLine.getId());
                    }
                });
                //check if all products has been released.
                billOfQuantity = billOfQuantityDao.getBillOfQuantityByNameAndOrderNo(invoice.getBoq().getName(), invoice.getBoq().getOrderNumber());
                final long boqLineCount = billOfQuantity.getLines().size();
                final long noOfLineTotallyReleased = billOfQuantity.getLines().stream().filter(boqDetail -> boqDetail.getQtyReleased() >= boqDetail.getQuantity()).count();
                final long noOfLinePartiallyReleased = billOfQuantity.getLines().stream().filter(boqDetail -> (boqDetail.getQtyReleased() < boqDetail.getQuantity())
                        && (boqDetail.getQtyReleased() > 0)).count();
                ConfigCategory status = null;
                logger.debug("noOfLineTotallyReleased = " + noOfLineTotallyReleased);
                logger.debug("noOfLinePartiallyReleased = " + noOfLinePartiallyReleased);

                if (noOfLineTotallyReleased >= boqLineCount) {
                    status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_FINAL);
                    billOfQuantityDao.updateBoqStatusPerId(status.getId(), billOfQuantity.getId());
                } else if (noOfLineTotallyReleased < boqLineCount && (noOfLinePartiallyReleased > 0 || noOfLineTotallyReleased > 0)) {
                    status = configCategoryDao.getCategoryOfTypeAndCode(IdBConstant.TYPE_BOQ_STATUS, IdBConstant.BOQ_STATUS_PAR_RELEASED);
                    billOfQuantityDao.updateBoqStatusPerId(status.getId(), billOfQuantity.getId());
                }
                final Timestamp currentDate = new Timestamp(new Date().getTime());
                billOfQuantityDao.updateBoqDateReleasedPerId(currentDate, billOfQuantity.getId());
            }
        } catch (Exception e) {
            logger.debug("InvoiceImporter: Exception in updating related BOQ", e);
        }
    }
}
