package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Contact;

/**
 * Created by akhoshraft on 29/02/2016.
 */
public interface ContactDao {
    /**
     * get specific contact by id.
     * @param contactId contactId
     * @return contact
     */
    Contact getContactById(long contactId);
    /**
     * Add new contact.
     * @param contact contact
     */
    void insert(Contact contact);
}
