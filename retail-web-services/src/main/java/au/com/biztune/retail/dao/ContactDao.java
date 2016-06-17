package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Contact;

import java.util.List;

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

    /**
     * update contact per id.
     * @param contact contact
     */
    void update(Contact contact);

    /**
     * remove contact.
     * @param contactId contactId
     */
    void delete(long contactId);

    /**
     * remove contact.
     * @param contactIds contactIds
     */
    void deleteContactWhereIdNotIn(List contactIds);
}
