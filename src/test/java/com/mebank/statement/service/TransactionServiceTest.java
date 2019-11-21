package com.mebank.statement.service;

import com.mebank.statement.domain.Transaction;
import com.mebank.statement.domain.TransactionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mebank.statement.CodeChallengeApp.DATE_FORMAT;

/**
 * @author Bahar Javidan
 * @since 2019-11-20
 */
public class TransactionServiceTest {
    private List<Transaction> transactions = new ArrayList<>();
    private TransactionService ts = new TransactionService();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);


    @Before
    public void setup() {

        Transaction t1 = new Transaction();
        t1.setTransactionId("1");
        t1.setFromAccountId("A1");
        t1.setToAccountId("A2");
        t1.setCreatedAt(LocalDateTime.parse("20/10/2018 12:00:00", formatter));
        t1.setAmount(10.0);
        t1.setTransactionType(TransactionType.PAYMENT);
        transactions.add(t1);

        Transaction t2 = new Transaction();
        t2.setTransactionId("2");
        t2.setFromAccountId("A1");
        t2.setToAccountId("A3");
        t2.setCreatedAt(LocalDateTime.parse("20/10/2018 14:00:00", formatter));
        t2.setAmount(14.0);
        t2.setTransactionType(TransactionType.PAYMENT);
        transactions.add(t2);

        Transaction t3 = new Transaction();
        t3.setTransactionId("3");
        t3.setFromAccountId("A2");
        t3.setToAccountId("A3");
        t3.setCreatedAt(LocalDateTime.parse("20/10/2018 15:00:00", formatter));
        t3.setAmount(5.99);
        t3.setTransactionType(TransactionType.PAYMENT);
        transactions.add(t3);

        Transaction t4 = new Transaction();
        t4.setTransactionId("4");
        t4.setFromAccountId("A3");
        t4.setToAccountId("A1");
        t4.setCreatedAt(LocalDateTime.parse("20/10/2018 16:00:00", formatter));
        t4.setAmount(14.0);
        t4.setTransactionType(TransactionType.REVERSAL);
        t4.setRelatedTransaction("2");
        transactions.add(t4);

    }

    @Test
    public void getSumOfCreditAmountTest() {
        Double sum = ts.getSumOfCreditAmount("A2", transactions);
        Assert.assertEquals(10, sum, 0.0);
    }

    @Test
    public void getSumOfCreditAmountWithInvalidAccountTest() {
        Double sum = ts.getSumOfCreditAmount("B2", transactions);
        Assert.assertEquals(0, sum, 0.0);
    }

    @Test
    public void getSumOfDebitAmountTest() {
        Double sum = ts.getSumOfDebitAmount("A1", transactions);
        Assert.assertEquals(24.0, sum, 0.0);
    }

    @Test
    public void getSumOfDebitAmountWithInvalidAccountTest() {
        Double sum = ts.getSumOfDebitAmount("B1", transactions);
        Assert.assertEquals(0, sum, 0.0);
    }

    @Test
    public void getValidTransactionsTest() {
        List<Transaction> result = ts.getValidTransactions("A1", LocalDateTime.parse("20/10/2018 11:00:00", formatter), LocalDateTime.parse("20/10/2018 17:00:00", formatter), transactions);
        Assert.assertEquals(1, result.size());
    }
}
