package com.ts.obtaining_quotes.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Component
public class FinancialInstrumentDto {
    private String FIGI;
    private String tool;
    private double nominal;
}
