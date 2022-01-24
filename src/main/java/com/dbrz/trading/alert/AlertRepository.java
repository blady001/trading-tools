package com.dbrz.trading.alert;

import com.dbrz.trading.exchange.Exchange;
import com.dbrz.trading.exchange.Timeframe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByIsActiveTrue();

    List<Alert> findByExchangeAndTimeframeAndIsActiveTrue(Exchange exchange, Timeframe timeframe);
}
