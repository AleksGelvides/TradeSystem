package com.ts.obtaining_quotes.mappers;

import com.ts.obtaining_quotes.domain.FinancialInstrumentDto;
import com.ts.obtaining_quotes.jpa.domain.FinancialInstrument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FinancialInstrumentMapper {
    FinancialInstrumentMapper INSTANCE = Mappers.getMapper(FinancialInstrumentMapper.class);

    FinancialInstrument toFinancialInstrument(FinancialInstrumentDto financialInstrumentDto); //Quote to Financial Instrument

}
