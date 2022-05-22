package com.ts.obtaining_quotes.jpa.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "finance_instrument")
public class FinancialInstrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "figi")
    private String FIGI;
    @Column(name = "tool")
    private String tool;
    @Column(name = "nominal")
    private double nominal;
    @CreationTimestamp
    private LocalDateTime createDate;
}
