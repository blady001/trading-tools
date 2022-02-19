package com.dbrz.trading.strategy;

import com.dbrz.trading.exchange.Candlestick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ta4j.core.Bar;
import org.ta4j.core.num.DecimalNum;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandlestickMapperTest {

    @Mock
    private Clock clock;

    @InjectMocks
    private CandlestickMapper candlestickMapper;

    private final BigDecimal givenOpenPrice = new BigDecimal("1");
    private final BigDecimal givenHighPrice = new BigDecimal("2");
    private final BigDecimal givenLowPrice = new BigDecimal("0.50");
    private final BigDecimal givenClosePrice = new BigDecimal("1.50");

    private static final ZoneId TEST_ZONE_ID = ZoneOffset.UTC;
    private static final Instant TEST_INSTANT = Instant.from(ZonedDateTime.of(2022, 2, 19, 12, 0, 0, 0, TEST_ZONE_ID));

    @BeforeEach
    void init() {
        when(clock.instant()).thenReturn(TEST_INSTANT);
        when(clock.getZone()).thenReturn(TEST_ZONE_ID);
    }

    @Test
    void shouldMapCandlestickToBar() {
        // given
        var givenCandlestick = givenCandlestick();

        // when
        var actualBar = candlestickMapper.candlestickToBar(givenCandlestick);

        // then
        thenBarGotCreatedFromCandlestick(actualBar, givenCandlestick);
    }

    private Candlestick givenCandlestick() {
        return new Candlestick(
                givenOpenPrice,
                givenHighPrice,
                givenLowPrice,
                givenClosePrice,
                givenOpenInstant(),
                givenCloseInstant()
        );
    }

    private Instant givenOpenInstant() {
        return Instant.now(clock).minus(1, ChronoUnit.HOURS);
    }

    private Instant givenCloseInstant() {
        return Instant.now(clock);
    }

    private void thenBarGotCreatedFromCandlestick(Bar bar, Candlestick candlestick) {
        assertThat(bar.getOpenPrice()).isEqualTo(DecimalNum.valueOf(candlestick.open()));
        assertThat(bar.getHighPrice()).isEqualTo(DecimalNum.valueOf(candlestick.high()));
        assertThat(bar.getLowPrice()).isEqualTo(DecimalNum.valueOf(candlestick.low()));
        assertThat(bar.getClosePrice()).isEqualTo(DecimalNum.valueOf(candlestick.close()));
        assertThat(bar.getBeginTime()).isEqualTo(ZonedDateTime.ofInstant(candlestick.openTime(), TEST_ZONE_ID));
        assertThat(bar.getEndTime()).isEqualTo(ZonedDateTime.ofInstant(candlestick.closeTime(), TEST_ZONE_ID));
        assertThat(bar.getVolume()).isEqualTo(DecimalNum.valueOf(0));
    }
}
