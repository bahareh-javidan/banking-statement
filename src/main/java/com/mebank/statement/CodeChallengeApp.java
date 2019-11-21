package com.mebank.statement;

import com.mebank.statement.domain.Transaction;
import com.mebank.statement.exception.ValidationException;
import com.mebank.statement.service.TransactionService;
import com.mebank.statement.utility.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * @author Bahar Javidan
 * @since 2019-11-20
 */
public class CodeChallengeApp {
    public static final String DATE_FORMAT = "d/M/yyyy H:m:s";
    private static final String FILE_NAME = "data.csv";
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeChallengeApp.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("accountId: ");
        String account = scanner.nextLine();

        System.out.println("enter the date in the following format: 20/10/2018 12:00:00");
        System.out.print("from: ");
        String from = scanner.nextLine();

        System.out.print("to: ");
        String to = scanner.nextLine();

        TransactionService transactionService = new TransactionService();
        FileUtility fileUtility = new FileUtility();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDateTime fromDate = LocalDateTime.parse(from, formatter);
            LocalDateTime endDate = LocalDateTime.parse(to, formatter);

            validateDate(fromDate, endDate);

            List<Transaction> transactions = fileUtility.parseCsvFile(FILE_NAME);
            List<Transaction> validTransactions = transactionService.getValidTransactions(account, fromDate, endDate, transactions);

            Double debited = transactionService.getSumOfDebitAmount(account, validTransactions);
            Double credited = transactionService.getSumOfCreditAmount(account, validTransactions);
            transactionService.printResult(validTransactions, debited, credited);

        } catch (DateTimeParseException e) {
            throw new RuntimeException("Date format is not valid, the valid format is dd/MM/yyyy H:m:s");
        } catch (IOException e) {
            LOGGER.error("Could not read from file.", e);
            throw new RuntimeException();
        }
    }

    /**
     * Validates the input dates for the sequence of dates.
     *
     * @param fromDate the start date of the transaction retrieval
     * @param endDate  the end date of the transaction retrieval
     */
    private static void validateDate(LocalDateTime fromDate, LocalDateTime endDate) {
        if (fromDate.isAfter(endDate)) {
            throw new ValidationException("From date should be before the end date.");
        }
    }
}
