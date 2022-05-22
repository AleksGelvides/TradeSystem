package com.ts.obtaining_quotes.jpa.domain;


import com.ts.obtaining_quotes.enums.TimeFrame;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "quote_histories")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "high", nullable = false)
    private BigDecimal high;
    @Column(name = "low", nullable = false)
    private BigDecimal low;
    @Column(name = "open", nullable = false)
    private BigDecimal open;
    @Column(name = "close", nullable = false)
    private BigDecimal close;
    @Column(name = "open_date", nullable = false)
    private LocalDateTime openTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fin_instr_id")
    private FinancialInstrument instrument;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "time_frame_id")
    private TimeFrame timeFrame;

    public Quote(HistoricCandle historicCandle,
                 FinancialInstrument instrument,
                 TimeFrame timeFrame) {
        this.high = createPrice(historicCandle.getHigh().getNano(), instrument.getNominal());
        this.low = createPrice(historicCandle.getLow().getNano(), instrument.getNominal());
        this.open = createPrice(historicCandle.getOpen().getNano(), instrument.getNominal());
        this.close = createPrice(historicCandle.getClose().getNano(), instrument.getNominal());
        this.timeFrame = timeFrame;
        this.openTime = LocalDateTime.ofEpochSecond(historicCandle.getTimeOrBuilder().getSeconds(), 0, ZoneOffset.ofHours(7));
        this.instrument = instrument;
    }

    private BigDecimal createPrice(long nano, double nominal){
        return new BigDecimal(nano / 100 * nominal).setScale(6, RoundingMode.DOWN);
    }
}
