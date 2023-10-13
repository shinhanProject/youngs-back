package com.youngs.repository;

import com.youngs.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, String> {
    Word findByName(String name);
}