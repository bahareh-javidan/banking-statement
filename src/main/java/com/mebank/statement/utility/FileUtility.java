package com.mebank.statement.utility;

import com.mebank.statement.domain.Transaction;
import com.mebank.statement.domain.TransactionType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mebank.statement.CodeChallengeApp.DATE_FORMAT;

/**
 * @author Bahar Javidan
 * @since 2019-11-20
 */
public class FileUtility {
    private static final String COMMA = ",";
    private static final String FILE_PREFIX = "src/main/resources/";

    /**
     * This method gets the fileName of the input file and convert them to List of Transactions.
     * The file should be in the resources directory.
     *
     * @param fileName the name to input file
     * @return list of transaction items in the file in ArrayList data type
     * @throws IOException when the file could not be opened to read
     */
    public List<Transaction> parseCsvFile(String fileName) throws IOException {
        Function<String, Transaction> mapToItem = line -> {
            String[] recordArray = line.split(COMMA);
            Transaction item = new Transaction();

            item.setTransactionId(recordArray[0].trim());
            item.setFromAccountId(recordArray[1].trim());
            item.setToAccountId(recordArray[2].trim());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            item.setCreatedAt(LocalDateTime.parse(recordArray[3].trim(), formatter));
            item.setAmount(Double.parseDouble(recordArray[4].trim()));
            item.setTransactionType(TransactionType.valueOf(recordArray[5].trim()));
            if (recordArray.length > 6) {
                String reverseId = recordArray[6].trim();
                item.setRelatedTransaction(reverseId);
            }
            return item;
        };

        Stream<String> lines;
        Path path = Paths.get(FILE_PREFIX + fileName);
        lines = Files.lines(path);

        // skip the header of the csv then ignore empty lines then map them to item
        return lines.skip(1).filter(line -> !line.isEmpty()).map(mapToItem).collect(Collectors.toList());
    }
}
