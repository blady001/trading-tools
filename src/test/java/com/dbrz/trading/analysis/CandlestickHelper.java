package com.dbrz.trading.analysis;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class CandlestickHelper {

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
