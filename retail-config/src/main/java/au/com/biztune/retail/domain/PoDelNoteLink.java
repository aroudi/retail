package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 1/06/2016.
 */
public class PoDelNoteLink {
    private long id;
    private long pohId;
    private String pohOrderNumber;
    private long delnId;
    private String delnNoteNumber;
    private String delnGrn;
    private Timestamp delnDeliveryDate;

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

    public long getDelnId() {
        return delnId;
    }

    public void setDelnId(long delnId) {
        this.delnId = delnId;
    }

    public String getDelnNoteNumber() {
        return delnNoteNumber;
    }

    public void setDelnNoteNumber(String delnNoteNumber) {
        this.delnNoteNumber = delnNoteNumber;
    }

    public String getDelnGrn() {
        return delnGrn;
    }

    public void setDelnGrn(String delnGrn) {
        this.delnGrn = delnGrn;
    }

    public Timestamp getDelnDeliveryDate() {
        return delnDeliveryDate;
    }

    public void setDelnDeliveryDate(Timestamp delnDeliveryDate) {
        this.delnDeliveryDate = delnDeliveryDate;
    }
}
