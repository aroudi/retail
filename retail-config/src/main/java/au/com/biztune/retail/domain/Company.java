package au.com.biztune.retail.domain;

import java.util.List;

/**
 * Company.
 *
 * @author arash
 */
public class Company {
    private long id;
    private String compName;
    private String compDesc;
    private String compCode;
    private byte[] compLogo;
    private byte[] compPicture;
    private boolean compIsTrading;
    private int compUserLicence;
    //company address
    private Contact contactInfo;
    //company contact list including company owner
    private List<Contact> contactList;

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

    public String getCompDesc() {
        return compDesc;
    }

    public void setCompDesc(String compDesc) {
        this.compDesc = compDesc;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public byte[] getCompLogo() {
        return compLogo;
    }

    public void setCompLogo(byte[] compLogo) {
        this.compLogo = compLogo;
    }

    public byte[] getCompPicture() {
        return compPicture;
    }

    public void setCompPicture(byte[] compPicture) {
        this.compPicture = compPicture;
    }

    public Contact getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(Contact contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
