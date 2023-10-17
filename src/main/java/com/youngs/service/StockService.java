package com.youngs.service;

import com.youngs.repository.StockCorpCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockService {
    private final StockCorpCodeRepository stockCodeRepository;

    @Transactional
    public List<String> getStocks() {
        // 종목 리스트 전체 반환
        List<String> nameList = stockCodeRepository.findAllCorpName();
        return nameList;
    }
}
