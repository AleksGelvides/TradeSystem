package com.ts.obtaining_quotes.config;

import com.ts.obtaining_quotes.enums.TimeFrame;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;

@Data
@Primary
@Configuration
@ConfigurationProperties(prefix = "service.tinkoff")
public class QuoteBrokerConfiguration {
    private String token;
    private int days;
    private int steps;
    private int wait;
    private ArrayList<TimeFrame> timeframe;
}
