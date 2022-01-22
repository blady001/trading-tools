package com.dbrz.trading.exchange;

import java.util.Comparator;

class TimeframeComparator implements Comparator<Timeframe> {

    @Override
    public int compare(Timeframe o1, Timeframe o2) {
        return o1.getDuration().compareTo(o2.getDuration());
    }
}
