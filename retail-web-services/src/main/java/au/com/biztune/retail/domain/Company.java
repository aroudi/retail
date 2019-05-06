/*
 * Package: au.com.biztune.retail.domain
 * Class: Company
 * Copyright: (c) 2019 Sydney Trains
 */
package au.com.biztune.retail.domain;

/**
 * Company.
 *
 * @author arash
 */
public class Company {
    private long id;
    private String compName;
    private boolean compIsTrading;
    private int compUserLicence;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public boolean isCompIsTrading() {
        return compIsTrading;
    }

    public void setCompIsTrading(boolean compIsTrading) {
        this.compIsTrading = compIsTrading;
    }

    public int getCompUserLicence() {
        return compUserLicence;
    }

    public void setCompUserLicence(int compUserLicence) {
        this.compUserLicence = compUserLicence;
    }
}
