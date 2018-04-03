package au.com.biztune.retail.domain;

/**
 * Created by arash on 26/03/2018.
 */
public class ProdDeptCat {
    private long id;
    private long orguId;
    private long prodId;
    private long deptId;
    private long catId;
    private long catValId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public long getCatId() {
        return catId;
    }

    public void setCatId(long catId) {
        this.catId = catId;
    }

    public long getCatValId() {
        return catValId;
    }

    public void setCatValId(long catValId) {
        this.catValId = catValId;
    }
}
