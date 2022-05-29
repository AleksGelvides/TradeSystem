package com.ts.obtaining_quotes.facade;


import com.ts.obtaining_quotes.config.QuoteBrokerConfiguration;
import com.ts.obtaining_quotes.message.FacadeResponse;
import com.ts.obtaining_quotes.domain.FinancialInstrumentDto;
import com.ts.obtaining_quotes.enums.Status;
import com.ts.obtaining_quotes.jpa.reposytory.FinancialInstrumentRepository;
import com.ts.obtaining_quotes.jpa.reposytory.QuoteRepository;
import com.ts.obtaining_quotes.service.quote.QuoteService;
import com.ts.obtaining_quotes.service.quote.QuoteServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
@EnableConfigurationProperties(QuoteBrokerConfiguration.class)
public class ObtainingQuotesFacade {
    @Autowired
    private QuoteBrokerConfiguration config;
    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private FinancialInstrumentRepository finRepository;
    private final ReentrantLock lock;


    public ObtainingQuotesFacade() {
        this.lock = new ReentrantLock();
    }

    @SneakyThrows
    public FacadeResponse startTrackQuote(FinancialInstrumentDto financialInstrumentDto) {
        log.info("Start tracking financial instrument: " + financialInstrumentDto.getTool());
        if (lock.isLocked()) {
            log.error("This method executing adding other data");
            return new FacadeResponse(Status.WARNING, "Please wait! Its working");

        } else if (finRepository.getIntAtFIGI(financialInstrumentDto.getFIGI()) != 0) {
            log.error("This financial instrument was added before");
            return new FacadeResponse(Status.ERROR, "This financial instrument was added before. FIGI: " + financialInstrumentDto.getFIGI());

        } else {
            QuoteService quoteService = QuoteServiceImpl.creatQuoteService(financialInstrumentDto, quoteRepository, finRepository, this.config);
            Runnable work = () -> {
                lock.lock();
                log.warn("Was created quote service: " + quoteService.hashCode() + "-> Time: " + LocalDateTime.now());
                quoteService.saveHistoriesQuoteInDB();
                lock.unlock();
            };

            new Thread(work).start();
            log.warn("Service: " + quoteService.hashCode() + " -> received all quotes!");
            return new FacadeResponse(Status.EXECUTED, "Getting data by: " + financialInstrumentDto.getTool());
        }
    }
}
