package com.dbrz.trading.analysis;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class CandlestickHelper {

    public static final String CSV_5_CANDLESTICKS = "csv/candlesticks_1d_27_09_21_-_01_10_21.csv";
    public static final String CSV_100_CANDLESTICKS = "csv/candlesticks_1d_25_06_21_to_02_10_21.csv";
    public static final String CSV_1K_CANDLESTICKS = "csv/candlesticks_1d_07_01_2019_-_02_10_21.csv";

    private static final String CSV_PATH_TEMPLATE = "src/test/resources/%s";

    public List<Candlestick> loadFromCsv(String filename) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(String.format(CSV_PATH_TEMPLATE, filename)))) {
            return reader.readAll().stream().map(this::convertCsvRowToCandlestick).toList();
        }
    }

    private Candlestick convertCsvRowToCandlestick(String[] rowData) {
        return new Candlestick(new BigDecimal(rowData[0]), new BigDecimal(rowData[1]), new BigDecimal(rowData[2]), new BigDecimal(rowData[3]), Instant.ofEpochMilli(Long.parseLong(rowData[4])), Instant.ofEpochMilli(Long.parseLong(rowData[5])));
    }
}
