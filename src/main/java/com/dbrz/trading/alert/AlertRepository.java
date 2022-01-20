package com.dbrz.trading.alert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByIsActiveTrue();
}
