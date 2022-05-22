package com.ts.obtaining_quotes.controller;

import com.ts.obtaining_quotes.domain.FinancialInstrumentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api.v1.quotes/")
public interface QuoteRestControllerAPI {

    @PostMapping("add-new-quote")
    ResponseEntity<?> addNewQuotesInService(@RequestBody FinancialInstrumentDto financialInstrumentDto);
}
