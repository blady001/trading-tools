package com.dbrz.trading.exchange;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.dbrz.trading.model.Candlestick;
import com.dbrz.trading.model.Exchange;
import com.dbrz.trading.model.Timeframe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class BinanceAdapter implements Exchange {

    private BinanceApiRestClient binanceApiRestClient;

    @Override
    public List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Integer limit) {
        var binanceCandlesticks = binanceApiRestClient.getCandlestickBars(
                symbol, getCandlestickInterval(timeframe), limit, null, null);
        return convertCandlesticks(binanceCandlesticks);
    }

    @Override
    public List<Candlestick> getCandlesticks(String symbol, Timeframe timeframe, Instant startTime, Instant endTime) {
        var binanceCandlesticks = binanceApiRestClient.getCandlestickBars(
                symbol, getCandlestickInterval(timeframe), null, startTime.toEpochMilli(), endTime.toEpochMilli());
        return convertCandlesticks(binanceCandlesticks);
    }

    private List<Candlestick> convertCandlesticks(List<com.binance.api.client.domain.market.Candlestick> binanceCandlesticks) {
        return binanceCandlesticks.stream().map(this::convertCandlestick).collect(Collectors.toList());
    }

    private CandlestickInterval getCandlestickInterval(Timeframe timeframe) {
        return CandlestickInterval.valueOf(timeframe.getSymbol());
    }

    private Candlestick convertCandlestick(com.binance.api.client.domain.market.Candlestick candlestick) {
        return new Candlestick(
                new BigDecimal(candlestick.getOpen()),
                new BigDecimal(candlestick.getHigh()),
                new BigDecimal(candlestick.getLow()),
                new BigDecimal(candlestick.getClose()),
                Instant.ofEpochMilli(candlestick.getOpenTime()),
                Instant.ofEpochMilli(candlestick.getCloseTime())
        );
    }
}
