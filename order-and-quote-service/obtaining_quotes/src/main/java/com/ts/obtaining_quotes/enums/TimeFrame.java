package com.ts.obtaining_quotes.enums;


import ru.tinkoff.piapi.contract.v1.CandleInterval;

public enum TimeFrame {
    NULL,
    _DONT_IDENTIFIER,
    _5_MINUTES,
    _HOUR,
    _DAY;

    public static TimeFrame getTimeFrame(CandleInterval interval) {
        switch (interval){
            case CANDLE_INTERVAL_5_MIN:
                return _5_MINUTES;
            case CANDLE_INTERVAL_HOUR:
                return  _HOUR;
            case CANDLE_INTERVAL_DAY:
                return _DAY;
            default:
                return _DONT_IDENTIFIER;
        }
    }

    public static CandleInterval getCandleInterval(TimeFrame timeFrame){
        switch (timeFrame){
            case _5_MINUTES:
                return CandleInterval.CANDLE_INTERVAL_5_MIN;
            case _HOUR:
                return  CandleInterval.CANDLE_INTERVAL_HOUR;
            case _DAY:
                return CandleInterval.CANDLE_INTERVAL_DAY;
            default:
                return CandleInterval.CANDLE_INTERVAL_UNSPECIFIED;
        }
    }
}
