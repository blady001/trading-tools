package com.dbrz.trading.alert;

import com.dbrz.trading.analysis.Timeframe;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String exchange;

    @Column(nullable = false)
    private Timeframe timeframe;

    @Column(nullable = false)
    private String trigger;

    @Column(nullable = false)
    private Integer notificationMethod;

    @Column(nullable = false)
    private Boolean isActive;
}