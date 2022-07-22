package com.market.stock.companydomain.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private String id;
    private String companyCode;
    private double stockPrice;
    private String timestamp;
}
