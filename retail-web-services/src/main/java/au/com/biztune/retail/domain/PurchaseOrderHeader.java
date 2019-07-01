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
    private double pohValueTax;
    private double pohTotalQty;
    private int pohTotalLines;
    private ConfigCategory pohType;
    private ConfigCategory pohStatus;
    private ConfigCategory pohCreationType;
    private List<PurchaseLine> lines;
    private List<PoDelNoteLink> linkedDelNotes;
    private Timestamp pohExpDelivery;
    private String pohExpDeliveryStr;
    private AppUser user;
    private String pohComment;
    private String pohPrefix;
    private String pohDlvAddress;
    private String pohContactPerson;
    private String pohContactPhone;
    private long pohRevision;
    private String pohCreatedDateStr;
    private boolean pohPrinted;
    private String pohPrjName;
    private String pohPrjCode;
    private String pohEmailTo;
    private boolean costsIncludeTax;

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
        //this.setPohValueGross(this.getPohValueGross() + purchaseLine.getPolValueGross());
        this.setPohValueNett(this.getPohValueNett() + purchaseLine.getPolValueOrdered());
        //this.setPohValueTax(this.getPohValueTax() + purchaseLine.getPolValueTax());
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

    public List<PoDelNoteLink> getLinkedDelNotes() {
        return linkedDelNotes;
    }

    public void setLinkedDelNotes(List<PoDelNoteLink> linkedDelNotes) {
        this.linkedDelNotes = linkedDelNotes;
    }

    public Timestamp getPohExpDelivery() {
        return pohExpDelivery;
    }

    public void setPohExpDelivery(Timestamp pohExpDelivery) {
        this.pohExpDelivery = pohExpDelivery;
    }

    public String getPohExpDeliveryStr() {
        return pohExpDeliveryStr;
    }

    public void setPohExpDeliveryStr(String pohExpDeliveryStr) {
        this.pohExpDeliveryStr = pohExpDeliveryStr;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getPohComment() {
        return pohComment;
    }

    public void setPohComment(String pohComment) {
        this.pohComment = pohComment;
    }

    public String getPohPrefix() {
        return pohPrefix;
    }

    public void setPohPrefix(String pohPrefix) {
        this.pohPrefix = pohPrefix;
    }

    public String getPohDlvAddress() {
        return pohDlvAddress;
    }

    public void setPohDlvAddress(String pohDlvAddress) {
        this.pohDlvAddress = pohDlvAddress;
    }

    public String getPohContactPerson() {
        return pohContactPerson;
    }

    public void setPohContactPerson(String pohContactPerson) {
        this.pohContactPerson = pohContactPerson;
    }

    public String getPohContactPhone() {
        return pohContactPhone;
    }

    public void setPohContactPhone(String pohContactPhone) {
        this.pohContactPhone = pohContactPhone;
    }

    public long getPohRevision() {
        return pohRevision;
    }

    public void setPohRevision(long pohRevision) {
        this.pohRevision = pohRevision;
    }

    public String getPohCreatedDateStr() {
        return pohCreatedDateStr;
    }

    public void setPohCreatedDateStr(String pohCreatedDateStr) {
        this.pohCreatedDateStr = pohCreatedDateStr;
    }

    public boolean isPohPrinted() {
        return pohPrinted;
    }

    public void setPohPrinted(boolean pohPrinted) {
        this.pohPrinted = pohPrinted;
    }

    public String getPohPrjName() {
        return pohPrjName;
    }

    public void setPohPrjName(String pohPrjName) {
        this.pohPrjName = pohPrjName;
    }

    public String getPohPrjCode() {
        return pohPrjCode;
    }

    public void setPohPrjCode(String pohPrjCode) {
        this.pohPrjCode = pohPrjCode;
    }

    public String getPohEmailTo() {
        return pohEmailTo;
    }

    public void setPohEmailTo(String pohEmailTo) {
        this.pohEmailTo = pohEmailTo;
    }

    public double getPohValueTax() {
        return pohValueTax;
    }

    public void setPohValueTax(double pohValueTax) {
        this.pohValueTax = pohValueTax;
    }

    public boolean isCostsIncludeTax() {
        return costsIncludeTax;
    }

    public void setCostsIncludeTax(boolean costsIncludeTax) {
        this.costsIncludeTax = costsIncludeTax;
    }
}
