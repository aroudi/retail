package au.com.biztune.retail.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arash on 5/05/2016.
 */
public class PurchaseLine {
    private long id;
    private long pohId;
    private String pohOrderNumber;
    private int polLineNumber;
    private ProductPurchaseItem purchaseItem;
    private double polUnitCost;
    private double polPrice;
    private boolean polSpecialBuy;
    private TaxLegVariance taxLegVariance;
    private UnitOfMeasure unitOfMeasure;
    private double polQtyOrdered;
    private double polQtyCancelled;
    private double polQtyReceived;
    private double polQtyInvoiced;
    private double polValueOrdered;
    private double polValueReceived;
    private double polValueInvoiced;
    private String polFreeText;
    private Timestamp polModified;
    private Timestamp polDateReceived;
    private boolean polPriceChecked;
    private long polIdParentLine;
    private double polQtyCounted;
    private double polContents;
    private ConfigCategory polCreationType;
    private ConfigCategory polStatus;
    private UnitOfMeasure unomContents;
    private List<PoBoqLink> poBoqLinks;
    private boolean deleted;
    private double polQtyReserved;

    /**
     * add poBoqLink to list.
     * @param poBoqLink poBoqLink
     */
    public void addPoBoqLink(PoBoqLink poBoqLink) {
        if (poBoqLinks == null) {
            poBoqLinks = new ArrayList<PoBoqLink>();
        }
        poBoqLinks.add(poBoqLink);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPohId() {
        return pohId;
    }

    public void setPohId(long pohId) {
        this.pohId = pohId;
    }

    public String getPohOrderNumber() {
        return pohOrderNumber;
    }

    public void setPohOrderNumber(String pohOrderNumber) {
        this.pohOrderNumber = pohOrderNumber;
    }

    public int getPolLineNumber() {
        return polLineNumber;
    }

    public void setPolLineNumber(int polLineNumber) {
        this.polLineNumber = polLineNumber;
    }

    public ProductPurchaseItem getPurchaseItem() {
        return purchaseItem;
    }

    public void setPurchaseItem(ProductPurchaseItem purchaseItem) {
        this.purchaseItem = purchaseItem;
    }

    public double getPolUnitCost() {
        return polUnitCost;
    }

    public void setPolUnitCost(double polUnitCost) {
        this.polUnitCost = polUnitCost;
    }

    public double getPolPrice() {
        return polPrice;
    }

    public void setPolPrice(double polPrice) {
        this.polPrice = polPrice;
    }

    public boolean isPolSpecialBuy() {
        return polSpecialBuy;
    }

    public void setPolSpecialBuy(boolean polSpecialBuy) {
        this.polSpecialBuy = polSpecialBuy;
    }

    public TaxLegVariance getTaxLegVariance() {
        return taxLegVariance;
    }

    public void setTaxLegVariance(TaxLegVariance taxLegVariance) {
        this.taxLegVariance = taxLegVariance;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public double getPolQtyOrdered() {
        return polQtyOrdered;
    }

    public void setPolQtyOrdered(double polQtyOrdered) {
        this.polQtyOrdered = polQtyOrdered;
    }

    public double getPolQtyCancelled() {
        return polQtyCancelled;
    }

    public void setPolQtyCancelled(double polQtyCancelled) {
        this.polQtyCancelled = polQtyCancelled;
    }

    public double getPolQtyReceived() {
        return polQtyReceived;
    }

    public void setPolQtyReceived(double polQtyReceived) {
        this.polQtyReceived = polQtyReceived;
    }

    public double getPolQtyInvoiced() {
        return polQtyInvoiced;
    }

    public void setPolQtyInvoiced(double polQtyInvoiced) {
        this.polQtyInvoiced = polQtyInvoiced;
    }

    public double getPolValueOrdered() {
        return polValueOrdered;
    }

    public void setPolValueOrdered(double polValueOrdered) {
        this.polValueOrdered = polValueOrdered;
    }

    public double getPolValueReceived() {
        return polValueReceived;
    }

    public void setPolValueReceived(double polValueReceived) {
        this.polValueReceived = polValueReceived;
    }

    public double getPolValueInvoiced() {
        return polValueInvoiced;
    }

    public void setPolValueInvoiced(double polValueInvoiced) {
        this.polValueInvoiced = polValueInvoiced;
    }

    public String getPolFreeText() {
        return polFreeText;
    }

    public void setPolFreeText(String polFreeText) {
        this.polFreeText = polFreeText;
    }

    public Timestamp getPolModified() {
        return polModified;
    }

    public void setPolModified(Timestamp polModified) {
        this.polModified = polModified;
    }

    public Timestamp getPolDateReceived() {
        return polDateReceived;
    }

    public void setPolDateReceived(Timestamp polDateReceived) {
        this.polDateReceived = polDateReceived;
    }

    public boolean isPolPriceChecked() {
        return polPriceChecked;
    }

    public void setPolPriceChecked(boolean polPriceChecked) {
        this.polPriceChecked = polPriceChecked;
    }

    public long getPolIdParentLine() {
        return polIdParentLine;
    }

    public void setPolIdParentLine(long polIdParentLine) {
        this.polIdParentLine = polIdParentLine;
    }

    public double getPolQtyCounted() {
        return polQtyCounted;
    }

    public void setPolQtyCounted(double polQtyCounted) {
        this.polQtyCounted = polQtyCounted;
    }

    public double getPolContents() {
        return polContents;
    }

    public void setPolContents(double polContents) {
        this.polContents = polContents;
    }

    public UnitOfMeasure getUnomContents() {
        return unomContents;
    }

    public void setUnomContents(UnitOfMeasure unomContents) {
        this.unomContents = unomContents;
    }

    public List<PoBoqLink> getPoBoqLinks() {
        return poBoqLinks;
    }

    public void setPoBoqLinks(List<PoBoqLink> poBoqLinks) {
        this.poBoqLinks = poBoqLinks;
    }

    public ConfigCategory getPolCreationType() {
        return polCreationType;
    }

    public void setPolCreationType(ConfigCategory polCreationType) {
        this.polCreationType = polCreationType;
    }

    public ConfigCategory getPolStatus() {
        return polStatus;
    }

    public void setPolStatus(ConfigCategory polStatus) {
        this.polStatus = polStatus;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getPolQtyReserved() {
        return polQtyReserved;
    }

    public void setPolQtyReserved(double polQtyReserved) {
        this.polQtyReserved = polQtyReserved;
    }
}
