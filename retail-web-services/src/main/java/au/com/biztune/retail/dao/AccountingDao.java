package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Account;
import au.com.biztune.retail.domain.JournalEntry;
import au.com.biztune.retail.domain.JournalRule;
import au.com.biztune.retail.domain.JournalSupport;

import java.util.List;

/**
 * Created by arash on 22/11/2016.
 */
public interface AccountingDao {

    /**
     * insert account.
     * @param account account
     */
    void insertAccount(Account account);

    /**
     * insert journal entry.
     * @param journalEntry journalEntry
     */
    void insertJournalEntry(JournalEntry journalEntry);

    /**
     * insert journal support.
     * @param journalSupport journalSupport
     */
    void insertJournalSupport(JournalSupport journalSupport);

    /**
     * insert journal rule.
     * @param journalRule journalRule
     */
    void insertJournalRule(JournalRule journalRule);

    /**
     * get account by id.
     * @param accId accId
     * @return Account.
     */
    Account getAccountById(long accId);

    /**
     * get Journal Rule by OrguId and TxnType and Action.
     * @param orguId orguId
     * @param txnType txnType
     * @param action action
     * @return List of JournalRule
     */
    List<JournalRule> getJournalRuleByOrguIdAndTxnTypeAndAction(long orguId, String txnType, String action);

    /**
     * accounting export report.
     * @param clauseList clauseList
     * @return List of Journal Entry
     */
    List<JournalEntry> accountingExportRpt(List clauseList);

    /**
     * accounting summary export report for Accounting system.
     * @param clauseList clauseList
     * @return List of Journal Entry
     */
    List<JournalEntry> accountingSummaryExportRpt (List clauseList);
}
