package com.ts.obtaining_quotes.facade;

import com.ts.obtaining_quotes.domain.FacadeResponse;
import com.ts.obtaining_quotes.domain.FinancialInstrumentDto;
import com.ts.obtaining_quotes.enums.Status;
import com.ts.obtaining_quotes.jpa.reposytory.FinancialInstrumentRepository;
import com.ts.obtaining_quotes.jpa.reposytory.QuoteRepository;
import com.ts.obtaining_quotes.mappers.FinancialInstrumentMapper;
import com.ts.obtaining_quotes.service.quote.QuoteService;
import com.ts.obtaining_quotes.service.quote.QuoteServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class ObtainingQuotesFacade {
    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private FinancialInstrumentRepository finRepository;
    private final ReentrantLock lock;
    private final ScheduledExecutorService _5MinutesScheduler;
    private final ScheduledExecutorService _1HourScheduler;
    private final ScheduledExecutorService _DayScheduler;

    public ObtainingQuotesFacade() {
        _5MinutesScheduler = Executors.newScheduledThreadPool(1);
        _1HourScheduler = Executors.newScheduledThreadPool(1);
        _DayScheduler = Executors.newScheduledThreadPool(1);
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
            QuoteService quoteService = QuoteServiceImpl.creatQuoteService(financialInstrumentDto, quoteRepository, finRepository);
            Runnable work = () -> {
                lock.lock();
                log.warn("Was created quote service: " + quoteService.hashCode() + "-> Time: " + LocalDateTime.now());
                quoteService.saveHistoriesQuoteInDB(FinancialInstrumentMapper.INSTANCE.toFinancialInstrument(financialInstrumentDto));
                lock.unlock();
            };
            new Thread(work).start();
            log.warn("Service: " + quoteService.hashCode() + " -> received all quotes!");
        }
        return new FacadeResponse(Status.EXECUTED, "Getting data by: " + financialInstrumentDto.getTool() + ". ");
    }
}
