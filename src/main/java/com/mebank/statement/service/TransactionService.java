package com.mebank.statement.service;

import com.mebank.statement.domain.Transaction;
import com.mebank.statement.domain.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides the methods to filter the transactions and contains the business to select transactions.
 *
 * @author Bahar Javidan
 * @since 2019-11-20
 */
public class TransactionService {

    /**
     * This method gets an account number and the valid transactions and returns the total amount transferred into the account
     * @param account account number
     * @param validTransactions valid transactions
     * @return total amount transferred into the account
     */
    public Double getSumOfCreditAmount(String account, List<Transaction> validTransactions) {
        return validTransactions.stream()
                .filter(i -> i.getToAccountId().equals(account))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * This method gets an account number and the valid transactions,then returns total amount transferred from the account
     * @param account account number
     * @param validTransactions valid transactions
     * @return returns total amount transferred from the account
     */

    public Double getSumOfDebitAmount(String account, List<Transaction> validTransactions) {
        return validTransactions.stream()
                .filter(i -> i.getFromAccountId().equals(account))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     *  This method excludes transactions with their reverse record and
     *  returns valid transactions of a specific account within a specified time period
     * @param account account number
     * @param fromDate start date
     * @param endDate end date
     * @param transactions banking transactions
     * @return a list of valid transactions
     */

    public List<Transaction> getValidTransactions(String account, LocalDateTime fromDate, LocalDateTime endDate, List<Transaction> transactions) {
        List<String> reversedTransactionIds = getReversedTransactions(account, transactions);
        return transactions.stream()
                .filter(i -> i.getFromAccountId().trim().equals(account) || i.getToAccountId().trim().equals(account))
                .filter(i -> i.getTransactionType().equals(TransactionType.PAYMENT))
                .filter(i -> !reversedTransactionIds.contains(i.getTransactionId()))
                .filter(i -> (i.getCreatedAt().isAfter(fromDate) && i.getCreatedAt().isBefore(endDate)) || i.getCreatedAt().isEqual(fromDate) || i.getCreatedAt().isEqual(endDate))
                .collect(Collectors.toList());
    }

    /**
     * This methods finds reversed transactions for a specific account.
     * @param account account number
     * @param transactions transactions
     * @return a list of reversed transactions
     */

    private List<String> getReversedTransactions(String account, List<Transaction> transactions) {
        return transactions.stream()
                .filter(i -> i.getFromAccountId().trim().equals(account) || i.getToAccountId().trim().equals(account))
                .filter(i -> i.getTransactionType().equals(TransactionType.REVERSAL))
                .map(Transaction::getRelatedTransaction)
                .collect(Collectors.toList());
    }

    /**
     * This method prints the result.
     * @param validTransactions a list of valid transactions
     * @param debited total amount which is transferred from the account.
     * @param credited total amount which is transferred to the account.
     */

    public void printResult(List<Transaction> validTransactions, Double debited, Double credited) {
        double totalAmount = credited - debited;
        boolean negativeNumber = totalAmount < 0;

        System.out.printf("\nRelative balance for this period is: %s$%.2f %n", negativeNumber ? "-" : "", negativeNumber ? -totalAmount : totalAmount);
        System.out.println("Number of transactions included is: " + validTransactions.size());
    }

}
