package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by akhoshraft on 16/03/2016.
 */
public class UnitOfMeasure {
    private long id;
    private String unomCode;
    private String unomDesc;
    private long unomFactor;
    private Timestamp unomCreated;
    private Timestamp unomModified;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUnomCode() {
        return unomCode;
    }

    public void setUnomCode(String unomCode) {
        this.unomCode = unomCode;
    }

    public String getUnomDesc() {
        return unomDesc;
    }

    public void setUnomDesc(String unomDesc) {
        this.unomDesc = unomDesc;
    }

    public long getUnomFactor() {
        return unomFactor;
    }

    public void setUnomFactor(long unomFactor) {
        this.unomFactor = unomFactor;
    }

    public Timestamp getUnomCreated() {
        return unomCreated;
    }

    public void setUnomCreated(Timestamp unomCreated) {
        this.unomCreated = unomCreated;
    }

    public Timestamp getUnomModified() {
        return unomModified;
    }

    public void setUnomModified(Timestamp unomModified) {
        this.unomModified = unomModified;
    }
}
