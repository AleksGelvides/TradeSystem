package com.ts.obtaining_quotes.jpa.reposytory;

import com.ts.obtaining_quotes.jpa.domain.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Override
    <S extends Quote> List<S> saveAll(Iterable<S> entities);

    @Query(nativeQuery = true, value = "select count(id) from quote_histories")
    long getCountElements();

}
