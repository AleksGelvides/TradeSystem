package com.ts.obtaining_quotes.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class QuoteDTO {
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal open;
    private final BigDecimal close;
    private final LocalDateTime openTime;

}
