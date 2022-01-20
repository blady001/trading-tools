package com.dbrz.trading.exchange.binance;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.exception.BinanceApiException;
import com.dbrz.trading.exchange.Candlestick;
import com.dbrz.trading.exchange.Timeframe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BinanceAdapterTest {

    @Mock
    private BinanceApiRestClient binanceApiRestClient;

    @InjectMocks
    private BinanceAdapter binanceAdapter;

    @Test
    void shouldGetCandlesticksWithinSpecificTime() {
        // given
        var givenBinanceCandlestick = givenBinanceCandlestick();
        when(binanceApiRestClient.getCandlestickBars(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(givenBinanceCandlestick));

        var givenSymbol = "BTCUSDT";
        var givenTimeframe = Timeframe.DAY;
        var givenStartTime = Instant.now();
        var givenEndTime = Instant.now().plus(1, ChronoUnit.DAYS);

        // when
        var result = binanceAdapter.getCandlesticks(givenSymbol, givenTimeframe, givenStartTime, givenEndTime);

        // then
        verify(binanceApiRestClient, times(1)).getCandlestickBars(
                givenSymbol, CandlestickInterval.DAILY, null, givenStartTime.toEpochMilli(), givenEndTime.toEpochMilli());
        assertThat(result).hasSize(1);
        assertCandlesticksEqual(givenBinanceCandlestick, result.get(0));
    }

    @Test
    void shouldGetCandlesticksWithLimit() {
        // given
        var givenBinanceCandlestick = givenBinanceCandlestick();
        when(binanceApiRestClient.getCandlestickBars(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(givenBinanceCandlestick));

        var givenSymbol = "BTCUSDT";
        var givenTimeframe = Timeframe.WEEK;
        var givenLimit = 1;

        // when
        var result = binanceAdapter.getCandlesticks(givenSymbol, givenTimeframe, givenLimit);

        // then
        verify(binanceApiRestClient, times(1)).getCandlestickBars(
                givenSymbol, CandlestickInterval.WEEKLY, givenLimit, null, null);
        assertThat(result).hasSize(1);
        assertCandlesticksEqual(givenBinanceCandlestick, result.get(0));
    }

    @Test
    void shouldBeOpened() {
        // given
        doNothing().when(binanceApiRestClient).ping();

        // when
        var actual = binanceAdapter.isExchangeOpened();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldNotBeOpened() {
        // given
        doThrow(new BinanceApiException("test")).when(binanceApiRestClient).ping();

        // when
        var actual = binanceAdapter.isExchangeOpened();

        // then
        assertThat(actual).isFalse();
    }

    private void assertCandlesticksEqual(com.binance.api.client.domain.market.Candlestick expected, Candlestick actual) {
        assertThat(actual.open()).isEqualTo(new BigDecimal(expected.getOpen()));
        assertThat(actual.close()).isEqualTo(new BigDecimal(expected.getClose()));
        assertThat(actual.high()).isEqualTo(new BigDecimal(expected.getHigh()));
        assertThat(actual.low()).isEqualTo(new BigDecimal(expected.getLow()));
        assertThat(actual.openTime()).isEqualTo(Instant.ofEpochMilli(expected.getOpenTime()));
        assertThat(actual.closeTime()).isEqualTo(Instant.ofEpochMilli(expected.getCloseTime()));
    }

    private com.binance.api.client.domain.market.Candlestick givenBinanceCandlestick() {
        var candlestick = new com.binance.api.client.domain.market.Candlestick();
        candlestick.setOpen("1");
        candlestick.setHigh("3");
        candlestick.setLow("0.5");
        candlestick.setClose("2");
        candlestick.setOpenTime(Instant.now().toEpochMilli());
        candlestick.setCloseTime(Instant.now().toEpochMilli());
        return candlestick;
    }
}
