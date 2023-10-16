package com.youngs.repository;

import com.youngs.entity.NewsSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsSummaryRepository extends JpaRepository<NewsSummary, Long> {
}
