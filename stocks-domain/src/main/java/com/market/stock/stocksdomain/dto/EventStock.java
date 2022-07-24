package com.market.stock.stocksdomain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventStock {
    private String id;
    private String companyCode;
    private double stockPrice;
    private String timestamp;
}
