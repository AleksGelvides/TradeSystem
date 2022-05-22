package com.ts.obtaining_quotes.service.quote;

import com.ts.obtaining_quotes.enums.Status;
import com.ts.obtaining_quotes.jpa.domain.FinancialInstrument;
import org.springframework.stereotype.Service;

@Service
public interface QuoteService {

    void saveHistoriesQuoteInDB(FinancialInstrument instrument);

    Enum<Status> getQuotes();

}
