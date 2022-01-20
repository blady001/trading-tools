package com.dbrz.trading.exchange.binance;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.dbrz.trading.exchange.Candlestick;
import com.dbrz.trading.exchange.ExchangeAdapter;
import com.dbrz.trading.exchange.Timeframe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("binanceAdapter")
@RequiredArgsConstructor
@Slf4j
class BinanceAdapter implements ExchangeAdapter {

    private final BinanceApiRestClient binanceApiRestClient;

    private static final EnumMap<Timeframe, CandlestickInterval> TIMEFRAME_TO_CANDLESTICK;

    static {
        TIMEFRAME_TO_CANDLESTICK = new EnumMap<>(Timeframe.class);
        TIMEFRAME_TO_CANDLESTICK.put(Timeframe.ONE_MINUTE, CandlestickInterval.ONE_MINUTE);
        TIMEFRAME_TO_CANDLESTICK.put(Timeframe.FIVE_MINUTES, CandlestickInterval.FIVE_MINUTES);
        TIMEFRAME_TO_CANDLESTICK.put(Timeframe.HALF_HOUR, CandlestickInterval.HALF_HOURLY);
        TIMEFRAME_TO_CANDLESTICK.put(Timeframe.HOUR, CandlestickInterval.HOURLY);
        TIMEFRAME_TO_CANDLESTICK.put(Timeframe.FOUR_HOURS, CandlestickInterval.FOUR_HOURLY);
        TIMEFRAME_TO_CANDLESTICK.put(Timeframe.DAY, CandlestickInterval.DAILY);
        TIMEFRAME_TO_CANDLESTICK.put(Timeframe.WEEK, CandlestickInterval.WEEKLY);
    }

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

    @Override
    public boolean isExchangeOpened() {
        try {
            binanceApiRestClient.ping();
            return true;
        } catch (Throwable t) {
            log.error("BinanceApiRestClient - error while pinging: ", t);
            return false;
        }
    }

    private List<Candlestick> convertCandlesticks(List<com.binance.api.client.domain.market.Candlestick> binanceCandlesticks) {
        return binanceCandlesticks.stream().map(this::convertCandlestick).collect(Collectors.toList());
    }

    private CandlestickInterval getCandlestickInterval(Timeframe timeframe) {
        return TIMEFRAME_TO_CANDLESTICK.get(timeframe);
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
