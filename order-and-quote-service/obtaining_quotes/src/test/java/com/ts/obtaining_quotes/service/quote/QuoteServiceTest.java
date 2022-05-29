package com.ts.obtaining_quotes.service.quote;

import com.ts.obtaining_quotes.MainClassTests;
import com.ts.obtaining_quotes.config.QuoteBrokerConfiguration;
import com.ts.obtaining_quotes.domain.FinancialInstrumentDto;
import com.ts.obtaining_quotes.enums.TimeFrame;
import com.ts.obtaining_quotes.jpa.reposytory.FinancialInstrumentRepository;
import com.ts.obtaining_quotes.jpa.reposytory.QuoteRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QuoteServiceTest extends MainClassTests {
    @Autowired
    QuoteService quoteService;
    @Autowired
    QuoteRepository quoteRepository;
    @Autowired
    FinancialInstrumentRepository financialInstrumentRepository;
    @Autowired
    QuoteBrokerConfiguration configuration;

    @Before
    public void createService() {
        FinancialInstrumentDto dto = new FinancialInstrumentDto()
                .setFIGI("BBG004730ZJ9")
                .setTool("VTBR")
                .setNominal(0.000001);
        quoteService = QuoteServiceImpl.creatQuoteService(dto, quoteRepository, financialInstrumentRepository, configuration);
    }

    @Test
    public void safeHistoryQuoteInDBTest() {
        quoteService.saveHistoriesQuoteInDB();
        var instrument = financialInstrumentRepository.getByTool("VTBR");
        var list5Minutes = quoteRepository.getAllByTimeFrame(TimeFrame._5_MINUTES);
        var listHours = quoteRepository.getAllByTimeFrame(TimeFrame._HOUR);
        var listDays = quoteRepository.getAllByTimeFrame(TimeFrame._DAY);
        assertNotNull(instrument);
        assertEquals(instrument.getFIGI(), "BBG004730ZJ9");
        assertEquals(instrument.getNominal(), 0.000001);
        assertNotNull(instrument.getCreateDate());
        assertNotEquals(list5Minutes.size(), 0);
        assertNotEquals(listHours.size(), 0);
        assertNotEquals(listDays.size(), 0);
    }

}
