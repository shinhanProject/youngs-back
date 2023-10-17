package com.youngs.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "stockCode")
public class StockCode {
    @Id
    private String corp_code;

    @Column(name = "stock_code")
    private String stock_code;

    @Column(name = "corp_name")
    private String corp_name;
}
