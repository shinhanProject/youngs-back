package com.youngs.repository;

import com.youngs.entity.BasicSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicSummaryRepository extends JpaRepository<BasicSummary, Long> {
}
