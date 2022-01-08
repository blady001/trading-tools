package com.dbrz.trading.analysis.condition.heikinashi;

import com.dbrz.trading.analysis.Candlestick;
import com.dbrz.trading.analysis.CandlestickHelper;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HeikinAshiConverterTest {

    private HeikinAshiConverter converter;
    private CandlestickHelper candlestickHelper;

    @BeforeEach
    void init() {
        converter = new HeikinAshiConverter();
        candlestickHelper = new CandlestickHelper();
    }

    @Test
    void testSingleCandlestick() throws IOException, CsvException {
        // given
        var givenCandlesticks = Collections.singletonList(givenCandlesticks().get(4));
        var givenExpectedCandlesticks = Collections.singletonList(givenHaCandlesticks().get(4));

        // when
        var actualHaCandlesticks = converter.convertToHACandlesticks(givenCandlesticks);

        // then
        assertThat(actualHaCandlesticks).isEqualTo(givenExpectedCandlesticks);
        assertThat(actualHaCandlesticks).hasSize(1);
    }

    @Test
    void testMultipleCandlesticks() throws IOException, CsvException {
        // given
        var givenCandlesticks = givenCandlesticks();
        var givenExpectedCandlesticks = givenHaCandlesticks();

        // when
        var actualHaCandlesticks = converter.convertToHACandlesticks(givenCandlesticks);

        // then
        assertThat(actualHaCandlesticks).isEqualTo(givenExpectedCandlesticks);
    }

    private List<Candlestick> givenCandlesticks() throws IOException, CsvException {
        return candlestickHelper.loadFromCsv("csv/candlesticks_1d_27_09_21_-_01_10_21.csv");
    }

    private List<Candlestick> givenHaCandlesticks() throws IOException, CsvException {
        return candlestickHelper.loadFromCsv("csv/ha_candlesticks_1d_27_09_21_-_01_10_21.csv");
    }
}
