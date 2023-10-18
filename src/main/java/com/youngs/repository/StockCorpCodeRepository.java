package com.youngs.repository;

import com.youngs.entity.StockCorpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockCorpCodeRepository extends JpaRepository<StockCorpCode, String> {
    @Query("SELECT s.corpCode FROM StockCorpCode s WHERE s.corpName = :name")
    String findByName(@Param("name") String name);
}