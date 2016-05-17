package au.com.biztune.retail.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arash on 5/05/2016.
 */
public class PurchaseOrderHeader {
    private long id;
    private OrgUnit orgUnit;
    private String pohOrderNumber;
    private Supplier supplier;
    private String pohSuppRef;
    private String pohSuppContact;
    private Timestamp pohCreatedDate;
    private Timestamp pohLastModifiedDate;
    private long pohLastModifiedBy;
    private Timestamp pohLastDelvDate;
    private boolean pohCancelled;
    private boolean pohApproved;
    private String delvName;
    private String delvAddress;
    private String delvPhone;
    private String delvMobile;
    private Timestamp pohDelvDate;
    private Timestamp pohExpTransmit;
    private Timestamp pohActTransmit;
    private Timestamp pohConfirmDate;
    private double pohValueGross;
    private double pohValueNett;
    private double pohTotalQty;
    private int pohTotalLines;
    private ConfigCategory pohType;
    private ConfigCategory pohStatus;
    private ConfigCategory pohCreationType;
    private List<PurchaseLine> lines;


    /**
     * constructor.
     */
    public PurchaseOrderHeader() {
        super();
        this.pohValueGross = 0.00;
        this.pohValueNett = 0.00;
        this.pohTotalQty = 0;
        this.pohTotalLines = 0;
    }

    /**
     * add line to purchase Order.
     * @param purchaseLine purchaseLine
     */
    public void addLine(PurchaseLine purchaseLine) {
        this.setPohValueGross(this.getPohValueGross() + purchaseLine.getPolValueOrdered());
        this.setPohTotalQty(this.getPohTotalQty() + purchaseLine.getPolQtyOrdered());
        this.setPohTotalLines(this.getPohTotalLines() + 1);
        if (lines == null) {
            lines = new ArrayList<PurchaseLine>();
        }
        lines.add(purchaseLine);
    }
    public String getDelvName() {
        return delvName;
    }

    public void setDelvName(String delvName) {
        this.delvName = delvName;
    }

    public String getDelvAddress() {
        return delvAddress;
    }

    public void setDelvAddress(String delvAddress) {
        this.delvAddress = delvAddress;
    }

    public String getDelvPhone() {
        return delvPhone;
    }

    public void setDelvPhone(String delvPhone) {
        this.delvPhone = delvPhone;
    }

    public String getDelvMobile() {
        return delvMobile;
    }

    public void setDelvMobile(String delvMobile) {
        this.delvMobile = delvMobile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public String getPohOrderNumber() {
        return pohOrderNumber;
    }

    public void setPohOrderNumber(String pohOrderNumber) {
        this.pohOrderNumber = pohOrderNumber;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getPohSuppRef() {
        return pohSuppRef;
    }

    public void setPohSuppRef(String pohSuppRef) {
        this.pohSuppRef = pohSuppRef;
    }

    public String getPohSuppContact() {
        return pohSuppContact;
    }

    public void setPohSuppContact(String pohSuppContact) {
        this.pohSuppContact = pohSuppContact;
    }

    public Timestamp getPohCreatedDate() {
        return pohCreatedDate;
    }

    public void setPohCreatedDate(Timestamp pohCreatedDate) {
        this.pohCreatedDate = pohCreatedDate;
    }

    public Timestamp getPohLastModifiedDate() {
        return pohLastModifiedDate;
    }

    public void setPohLastModifiedDate(Timestamp pohLastModifiedDate) {
        this.pohLastModifiedDate = pohLastModifiedDate;
    }

    public long getPohLastModifiedBy() {
        return pohLastModifiedBy;
    }

    public void setPohLastModifiedBy(long pohLastModifiedBy) {
        this.pohLastModifiedBy = pohLastModifiedBy;
    }

    public Timestamp getPohLastDelvDate() {
        return pohLastDelvDate;
    }

    public void setPohLastDelvDate(Timestamp pohLastDelvDate) {
        this.pohLastDelvDate = pohLastDelvDate;
    }

    public boolean isPohCancelled() {
        return pohCancelled;
    }

    public void setPohCancelled(boolean pohCancelled) {
        this.pohCancelled = pohCancelled;
    }

    public boolean isPohApproved() {
        return pohApproved;
    }

    public void setPohApproved(boolean pohApproved) {
        this.pohApproved = pohApproved;
    }

    public Timestamp getPohDelvDate() {
        return pohDelvDate;
    }

    public void setPohDelvDate(Timestamp pohDelvDate) {
        this.pohDelvDate = pohDelvDate;
    }

    public Timestamp getPohExpTransmit() {
        return pohExpTransmit;
    }

    public void setPohExpTransmit(Timestamp pohExpTransmit) {
        this.pohExpTransmit = pohExpTransmit;
    }

    public Timestamp getPohActTransmit() {
        return pohActTransmit;
    }

    public void setPohActTransmit(Timestamp pohActTransmit) {
        this.pohActTransmit = pohActTransmit;
    }

    public Timestamp getPohConfirmDate() {
        return pohConfirmDate;
    }

    public void setPohConfirmDate(Timestamp pohConfirmDate) {
        this.pohConfirmDate = pohConfirmDate;
    }

    public double getPohValueGross() {
        return pohValueGross;
    }

    public void setPohValueGross(double pohValueGross) {
        this.pohValueGross = pohValueGross;
    }

    public double getPohValueNett() {
        return pohValueNett;
    }

    public void setPohValueNett(double pohValueNett) {
        this.pohValueNett = pohValueNett;
    }

    public double getPohTotalQty() {
        return pohTotalQty;
    }

    public void setPohTotalQty(double pohTotalQty) {
        this.pohTotalQty = pohTotalQty;
    }

    public int getPohTotalLines() {
        return pohTotalLines;
    }

    public void setPohTotalLines(int pohTotalLines) {
        this.pohTotalLines = pohTotalLines;
    }

    public ConfigCategory getPohType() {
        return pohType;
    }

    public void setPohType(ConfigCategory pohType) {
        this.pohType = pohType;
    }

    public ConfigCategory getPohStatus() {
        return pohStatus;
    }

    public void setPohStatus(ConfigCategory pohStatus) {
        this.pohStatus = pohStatus;
    }

    public ConfigCategory getPohCreationType() {
        return pohCreationType;
    }

    public void setPohCreationType(ConfigCategory pohCreationType) {
        this.pohCreationType = pohCreationType;
    }

    public List<PurchaseLine> getLines() {
        return lines;
    }

    public void setLines(List<PurchaseLine> lines) {
        this.lines = lines;
    }
}
