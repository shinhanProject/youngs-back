package com.youngs.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "stock_corp_code")
public class StockCorpCode {
    @Id
    @Column(name = "corp_code")
    private String corpCode;

    @Column(name = "stock_code")
    private String stockCode;

    @Column(name = "corp_name")
    private String corpName;
}
