package au.com.biztune.retail.domain;

/**
 * Created by akhoshraft on 29/02/2016.
 */
public class ConfigCategory {
        private long id;
        private int configTypeId;
        private String categoryCode;
        private String displayName;
        private String description;
        private int categoryOrder;
        private String color;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getConfigTypeId() {
        return configTypeId;
    }

    public void setConfigTypeId(int configTypeId) {
        this.configTypeId = configTypeId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryOrder() {
        return categoryOrder;
    }

    public void setCategoryOrder(int categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
