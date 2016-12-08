package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.*;

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

    /**
     * insert accounting export log.
     * @param accountingExport accountingExport
     */
    void insertAccountingExport(AccountingExport accountingExport);

    /**
     * insert accountExport Supplier Link.
     * @param accExportSuppLink accExportSuppLink
     */
    void insertAccExportSuppLink(AccExportSuppLink accExportSuppLink);

    /**
     * insert Accounting Export Delivery Note Header Link.
     * @param accExportDelnLink accExportDelnLink
     */
    void insertAccExportDelnLink(AccExportDelnLink accExportDelnLink);

    /**
     * insert Accounting Export Session Link.
     * @param accExportSessionLink accExportSessionLink
     */
    void insertAccExportSessionLink(AccExportSessionLink accExportSessionLink);

    /**
     * get journal entry per session list.
     * @param sessionList sessionList
     * @return journal entry list.
     */
    List<JournalEntry> getJournalEntryPerSessions(List sessionList);

    /**
     * get account by name.
     * @param accName accName
     * @return Account
     */
    Account getAccountByName(String accName);

    /**
     * get all accounts of org unit.
     * @param orguId orguId
     * @return List of Accounts
     */
    List<Account> getAllAccountsByOrguId(long orguId);

    /**
     * update account.
     * @param account account
     */
    void updateAccountCode(Account account);

    /**
     * get all acconting export per orguId.
     * @param orguId orguId
     * @return List of exported accounts.
     */
    List<AccountingExport> getAllAccountingExportPerOrguId(long orguId);

    /**
     * get AccountingExport by id.
     * @param id id
     * @return AccountingExport
     */
    AccountingExport getAccountingExportById(long id);
}
