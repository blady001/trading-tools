package com.dbrz.trading.analysis.condition;

import com.dbrz.trading.analysis.Candlestick;
import com.dbrz.trading.analysis.CandlestickHelper;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class HeikinAshiEntryConditionTest {

    private TradingCondition tradingCondition;
    private CandlestickHelper candlestickHelper;

    @BeforeEach
    void init() {
        tradingCondition = new HeikinAshiEntryCondition();
        candlestickHelper = new CandlestickHelper();
    }

    @Test
    void shouldSatisfyConditionOnCertainDates() throws IOException, CsvException {
        // given
        var candlesticksData = new ArrayList<>(givenCandlesticks());

        // when
        var signals = produceSignals(candlesticksData);

        // then
        verifySignals(signals);
    }

    private void verifySignals(List<Instant> signals) {
        var expectedDates = Set.of(
                Instant.ofEpochMilli(1624838400000L),
                Instant.ofEpochMilli(1626912000000L),
                Instant.ofEpochMilli(1628208000000L),
                Instant.ofEpochMilli(1629417600000L),
                Instant.ofEpochMilli(1631664000000L),
                Instant.ofEpochMilli(1633046400000L)
        );

        var datetimeThreshold = OffsetDateTime.of(2021, 6, 28, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
        var signalsToCheck = signals.stream().filter(x -> x.isAfter(datetimeThreshold)).toList();

        signalsToCheck.forEach(signal -> assertThat(signal).isIn(expectedDates));
    }

    private List<Instant> produceSignals(List<Candlestick> series) {
        var data = getSortedByOpenTimeAsc(series);
        return IntStream.range(6, data.size())
                .mapToObj(i -> getDataSlice(data, i))
                .map(this::getDateIfConditionSatisfied)
                .flatMap(Optional::stream)
                .toList();

    }

    private Optional<Instant> getDateIfConditionSatisfied(List<Candlestick> data) {
        Collections.reverse(data);
        if (tradingCondition.isSatisfied(data))
            return Optional.of(data.get(0).getOpenTime());
        else
            return Optional.empty();
    }

    private List<Candlestick> getDataSlice(List<Candlestick> data, Integer size) {
        return new ArrayList<>(data.subList(0, size + 1));
    }

    private List<Candlestick> getSortedByOpenTimeAsc(List<Candlestick> series) {
        var data = new ArrayList<>(series);
        data.sort(Comparator.comparing(Candlestick::getOpenTime));
        return data;
    }

    private List<Candlestick> givenCandlesticks() throws IOException, CsvException {
        return candlestickHelper.loadFromCsv(CandlestickHelper.CSV_1K_CANDLESTICKS);
    }
}
