package com.ts.obtaining_quotes.service.quote;

import com.ts.obtaining_quotes.domain.FinancialInstrumentDto;
import com.ts.obtaining_quotes.enums.Status;
import com.ts.obtaining_quotes.enums.TimeFrame;
import com.ts.obtaining_quotes.jpa.domain.FinancialInstrument;
import com.ts.obtaining_quotes.jpa.domain.Quote;
import com.ts.obtaining_quotes.jpa.reposytory.FinancialInstrumentRepository;
import com.ts.obtaining_quotes.jpa.reposytory.QuoteRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Slf4j
@Scope("prototype")
@Service
public class QuoteServiceImpl implements QuoteService {

    private String token = "t.92Z55zO5fgs1P_dQn0-iYqdKPfg9sEZQ-Nz4CG3-ImSk6k1yA1UNFFiCuFZFVXJChPlwAV43FfnJ4F_WBWKrWA";
    private final InvestApi api;
    private final FinancialInstrumentDto financialInstrumentDto;
    private final QuoteRepository quoteRepository;
    private final FinancialInstrumentRepository finRepository;

    private QuoteServiceImpl(FinancialInstrumentDto financialInstrumentDto,
                             QuoteRepository repository,
                             FinancialInstrumentRepository finRepository) {
        this.api = InvestApi.create(token);
        this.quoteRepository = repository;
        this.finRepository = finRepository;
        this.financialInstrumentDto = financialInstrumentDto;
    }

    public static QuoteServiceImpl creatQuoteService(FinancialInstrumentDto financialInstrumentDto,
                                                     QuoteRepository quoteRepository,
                                                     FinancialInstrumentRepository finRepository) {
        return new QuoteServiceImpl(financialInstrumentDto, quoteRepository, finRepository);
    }

    @Override
    @SneakyThrows
    public void saveHistoriesQuoteInDB(FinancialInstrument instrument) {
        try {
            int days = 100; // Нужно исправить и убрать хардкод
            log.info("Loading financial instrument in database: " + instrument.getTool());
            finRepository.save(instrument);

            log.info("Loading quotes in database | Time frame: " + TimeFrame._DAY);
            quoteRepository.saveAll(loadHistory(days, instrument, TimeFrame._DAY));
            log.info("Quotes saved in DB | instrument: " + instrument.getTool());

            log.info("Loading quotes in database | Time frame: " + TimeFrame._HOUR);
            quoteRepository.saveAll(loadHistory(days, instrument, TimeFrame._HOUR));
            log.info("Quotes saved in DB | instrument: " + instrument.getTool());

            log.info("Loading quotes in database | Time frame: " + TimeFrame._5_MINUTES);
            quoteRepository.saveAll(loadHistory(days, instrument, TimeFrame._5_MINUTES));
            log.info("Quotes saved in DB | instrument: " + instrument.getTool());

        } catch (Exception e) {
            e.getStackTrace();
        }


    }

    @Override
    public Enum<Status> getQuotes() {
        return null;
    }

    @SneakyThrows
    private ArrayList<Quote> loadHistory(int days,
                                         FinancialInstrument instrument,
                                         TimeFrame timeFrame) {
        ArrayList<Quote> quoteList = new ArrayList<>();
        try {
            int step = 100; // Максимум запросов в минуту от тинька.

            log.info("Get quotes | Timeframe: " + timeFrame + " started");
            for (int x = 1; x <= days; x++) {

                api.getMarketDataService()
                        .getCandlesSync(financialInstrumentDto.getFIGI(),
                                Instant.now().minus(x, ChronoUnit.DAYS),
                                Instant.now().minus(x - 1, ChronoUnit.DAYS),
                                TimeFrame.getCandleInterval(timeFrame))
                        .stream().map(h -> new Quote(h, instrument, timeFrame)).forEach(quoteList::add);

                if (x == step) {
                    step += step;
                    log.info("Get quotes | Timeframe: " + timeFrame + " expects");
                    Thread.currentThread().join(1000 * 60);
                    log.info("Get quotes | Timeframe: " + timeFrame + " started");
                }
            }
            log.info("Received quotes | instrument: " + instrument.getTool() + " quotes count: " + quoteList.size());
            return quoteList;
        } catch (Exception e) {
            e.getStackTrace();
            throw new Exception("Get quotes error. Check stack trace");
        }
    }
}
