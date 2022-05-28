package com.ts.obtaining_quotes.service.quote;

import com.ts.obtaining_quotes.config.QuoteBrokerConfiguration;
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

    private final InvestApi api;
    private final FinancialInstrumentDto financialInstrumentDto;
    private final QuoteRepository quoteRepository;
    private final FinancialInstrumentRepository finRepository;
    private QuoteBrokerConfiguration config;

    private QuoteServiceImpl(FinancialInstrumentDto financialInstrumentDto,
                             QuoteRepository repository,
                             FinancialInstrumentRepository finRepository,
                             QuoteBrokerConfiguration config) {
        this.config = config;
        this.api = InvestApi.create(this.config.getToken());
        this.quoteRepository = repository;
        this.finRepository = finRepository;
        this.financialInstrumentDto = financialInstrumentDto;
    }

    public static QuoteServiceImpl creatQuoteService(FinancialInstrumentDto financialInstrumentDto,
                                                     QuoteRepository quoteRepository,
                                                     FinancialInstrumentRepository finRepository,
                                                     QuoteBrokerConfiguration config) {
        return new QuoteServiceImpl(financialInstrumentDto, quoteRepository, finRepository, config);
    }

    @Override
    @SneakyThrows
    public void saveHistoriesQuoteInDB(FinancialInstrument instrument) {
        try {

            log.info("Loading financial instrument in database: " + instrument.getTool());
            finRepository.save(instrument);

            for(int x = 0; x <= config.getTimeframe().size() - 1; x++){
                log.info("Loading quotes in database | Time frame: " + config.getTimeframe().get(x));
                quoteRepository.saveAll(loadHistory(config.getDays(), instrument, config.getTimeframe().get(x)));
                log.info("Quotes saved in DB | instrument: " + instrument.getTool());

            }

        } catch (Exception e) {
            e.printStackTrace();
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
            int step = config.getSteps();
            log.info("Get quotes | Timeframe: " + timeFrame + " started");
            for (int x = 0; x <= days; x++) {

                api.getMarketDataService()
                        .getCandlesSync(financialInstrumentDto.getFIGI(),
                                Instant.now().minus(x, ChronoUnit.DAYS),
                                Instant.now().minus(x - 1, ChronoUnit.DAYS),
                                TimeFrame.getCandleInterval(timeFrame))
                        .stream().map(h -> new Quote(h, instrument, timeFrame)).forEach(quoteList::add);

                if (x == step) {
                    step += step;
                    log.info("Get quotes | Timeframe: " + timeFrame + " expects");
                    Thread.currentThread().join(config.getWait());
                    log.info("Get quotes | Timeframe: " + timeFrame + " started");
                }
            }
            log.info("Received quotes | instrument: " + instrument.getTool() + " quotes count: " + quoteList.size());
            return quoteList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Get quotes error. Check stack trace");
        }
    }
}
