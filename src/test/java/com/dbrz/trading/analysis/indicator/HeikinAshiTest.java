package com.dbrz.trading.analysis.indicator;

import com.dbrz.trading.analysis.Candlestick;
import com.dbrz.trading.analysis.CandlestickHelper;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HeikinAshiTest {

    private CandlestickHelper candlestickHelper;

    @BeforeEach
    void init() {
        candlestickHelper = new CandlestickHelper();
    }

    @Test
    void testSingleCandlestick() throws IOException, CsvException {
        // given
        var givenCandlesticks = Collections.singletonList(givenCandlesticks(CandlestickHelper.CSV_5_CANDLESTICKS).get(4));
        var givenExpectedCandlesticks = Collections.singletonList(givenHaCandlesticks().get(4));

        // when
        var actualHaCandlesticks = HeikinAshi.fromSeries(givenCandlesticks);

        // then
        assertThat(actualHaCandlesticks).isEqualTo(givenExpectedCandlesticks);
        assertThat(actualHaCandlesticks).hasSize(1);
    }

    @Test
    void testMultipleCandlesticks() throws IOException, CsvException {
        // given
        var givenCandlesticks = givenCandlesticks(CandlestickHelper.CSV_5_CANDLESTICKS);
        var givenExpectedCandlesticks = givenHaCandlesticks();

        // when
        var actualHaCandlesticks = HeikinAshi.fromSeries(givenCandlesticks);

        // then
        assertThat(actualHaCandlesticks).isEqualTo(givenExpectedCandlesticks);
    }

    @Test
    void testBullishCandlesticksOverMonthTimeSpan() throws IOException, CsvException {
        // given
        var givenCandlesticks = givenCandlesticks(CandlestickHelper.CSV_100_CANDLESTICKS);

        // when
        var actualHaCandles = HeikinAshi.fromSeries(givenCandlesticks);

        // then
        assertThat(actualHaCandles).hasSize(100);
        assertThat(actualHaCandles.get(0).isBullish()).isTrue();
        assertThat(actualHaCandles.get(1).isBullish()).isTrue();
        assertThat(actualHaCandles.get(2).isBullish()).isFalse();
        assertThat(actualHaCandles.get(3).isBullish()).isFalse();
        assertThat(actualHaCandles.get(4).isBullish()).isFalse();
        assertThat(actualHaCandles.get(5).isBullish()).isFalse();
        assertThat(actualHaCandles.get(6).isBullish()).isFalse();
        assertThat(actualHaCandles.get(7).isBullish()).isFalse();
        assertThat(actualHaCandles.get(8).isBullish()).isFalse();
        assertThat(actualHaCandles.get(9).isBullish()).isFalse();
        assertThat(actualHaCandles.get(10).isBullish()).isFalse();
        assertThat(actualHaCandles.get(11).isBullish()).isFalse();
        assertThat(actualHaCandles.get(12).isBullish()).isFalse();
        assertThat(actualHaCandles.get(13).isBullish()).isFalse();
        assertThat(actualHaCandles.get(14).isBullish()).isFalse();
        assertThat(actualHaCandles.get(15).isBullish()).isFalse();
        assertThat(actualHaCandles.get(16).isBullish()).isTrue();
        assertThat(actualHaCandles.get(17).isBullish()).isTrue();
        assertThat(actualHaCandles.get(18).isBullish()).isFalse();
        assertThat(actualHaCandles.get(20).isBullish()).isFalse();
        assertThat(actualHaCandles.get(21).isBullish()).isFalse();
        assertThat(actualHaCandles.get(22).isBullish()).isFalse();
        assertThat(actualHaCandles.get(23).isBullish()).isFalse();
        assertThat(actualHaCandles.get(24).isBullish()).isFalse();
        assertThat(actualHaCandles.get(25).isBullish()).isFalse();
        assertThat(actualHaCandles.get(26).isBullish()).isTrue();
        assertThat(actualHaCandles.get(27).isBullish()).isFalse();
        assertThat(actualHaCandles.get(28).isBullish()).isTrue();
        assertThat(actualHaCandles.get(29).isBullish()).isFalse();
        assertThat(actualHaCandles.get(30).isBullish()).isTrue();
        assertThat(actualHaCandles.get(31).isBullish()).isFalse();
    }

    @Test
    void testBearishCandlesticksOverMonthTimeSpan() throws IOException, CsvException {
        // given
        var givenCandlesticks = givenCandlesticks(CandlestickHelper.CSV_100_CANDLESTICKS);

        // when
        var actualHaCandles = HeikinAshi.fromSeries(givenCandlesticks);

        // then
        assertThat(actualHaCandles).hasSize(100);
        assertThat(actualHaCandles.get(0).isBearish()).isFalse();
        assertThat(actualHaCandles.get(1).isBearish()).isFalse();
        assertThat(actualHaCandles.get(2).isBearish()).isFalse();
        assertThat(actualHaCandles.get(3).isBearish()).isFalse();
        assertThat(actualHaCandles.get(4).isBearish()).isTrue();
        assertThat(actualHaCandles.get(5).isBearish()).isFalse();
        assertThat(actualHaCandles.get(6).isBearish()).isFalse();
        assertThat(actualHaCandles.get(7).isBearish()).isTrue();
        assertThat(actualHaCandles.get(8).isBearish()).isFalse();
        assertThat(actualHaCandles.get(9).isBearish()).isFalse();
        assertThat(actualHaCandles.get(10).isBearish()).isTrue();
        assertThat(actualHaCandles.get(11).isBearish()).isTrue();
        assertThat(actualHaCandles.get(12).isBearish()).isTrue();
        assertThat(actualHaCandles.get(13).isBearish()).isFalse();
        assertThat(actualHaCandles.get(14).isBearish()).isFalse();
        assertThat(actualHaCandles.get(15).isBearish()).isFalse();
        assertThat(actualHaCandles.get(16).isBearish()).isFalse();
        assertThat(actualHaCandles.get(17).isBearish()).isFalse();
        assertThat(actualHaCandles.get(18).isBearish()).isFalse();
        assertThat(actualHaCandles.get(20).isBearish()).isFalse();
        assertThat(actualHaCandles.get(21).isBearish()).isTrue();
        assertThat(actualHaCandles.get(22).isBearish()).isTrue();
        assertThat(actualHaCandles.get(23).isBearish()).isTrue();
        assertThat(actualHaCandles.get(24).isBearish()).isTrue();
        assertThat(actualHaCandles.get(25).isBearish()).isFalse();
        assertThat(actualHaCandles.get(26).isBearish()).isFalse();
        assertThat(actualHaCandles.get(27).isBearish()).isFalse();
        assertThat(actualHaCandles.get(28).isBearish()).isFalse();
        assertThat(actualHaCandles.get(29).isBearish()).isFalse();
        assertThat(actualHaCandles.get(30).isBearish()).isFalse();
        assertThat(actualHaCandles.get(31).isBearish()).isFalse();
    }

    private List<Candlestick> givenCandlesticks(String filename) throws IOException, CsvException {
        return candlestickHelper.loadFromCsv(filename);
    }

    private List<Candlestick> givenHaCandlesticks() throws IOException, CsvException {
        return candlestickHelper.loadFromCsv("csv/ha_candlesticks_1d_27_09_21_-_01_10_21.csv");
    }
}
