package com.mebank.statement.utility;

import com.mebank.statement.domain.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author Bahar Javidan
 * @since 2019-11-20
 */
public class FileUtilityTest {

    @Test
    public void parseCsvFileTest() throws IOException {
        FileUtility utility = new FileUtility();
        try {
            List<Transaction> transactions = utility.parseCsvFile("data-test.csv");
            Assert.assertEquals(7, transactions.size());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
