package au.com.biztune.retail.domain;

/**
 * Created by arash on 26/03/2018.
 */
public class CategoryValue {
    private long id;
    private long cathId;
    private boolean active;
    private String catValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCathId() {
        return cathId;
    }

    public void setCathId(long cathId) {
        this.cathId = cathId;
    }

    public String getCatValue() {
        return catValue;
    }

    public void setCatValue(String catValue) {
        this.catValue = catValue;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
