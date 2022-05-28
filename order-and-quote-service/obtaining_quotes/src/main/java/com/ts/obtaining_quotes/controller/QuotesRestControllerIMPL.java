package com.ts.obtaining_quotes.controller;

import com.ts.obtaining_quotes.domain.FinancialInstrumentDto;
import com.ts.obtaining_quotes.facade.ObtainingQuotesFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuotesRestControllerIMPL implements QuoteRestControllerAPI {
    private final ObtainingQuotesFacade facade;

    @Autowired
    public QuotesRestControllerIMPL(ObtainingQuotesFacade facade){
        this.facade = facade;
    }

    @Override
    public ResponseEntity<?> addNewQuotesInService(FinancialInstrumentDto financialInstrumentDto) {
        try{
            var responseFacade = facade.startTrackQuote(financialInstrumentDto);
            return new ResponseEntity<>(responseFacade, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("There was an error. Please contact your administrator", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
