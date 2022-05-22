package com.ts.obtaining_quotes.jpa.reposytory;

import com.ts.obtaining_quotes.jpa.domain.FinancialInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FinancialInstrumentRepository extends JpaRepository<FinancialInstrument, Long> {

    @Override
    <S extends FinancialInstrument> S save(S entity);

    @Query(nativeQuery = true, value = "select count(id) from finance_instrument where figi = :figi")
    int getIntAtFIGI(@Param("figi") String figi);
}
