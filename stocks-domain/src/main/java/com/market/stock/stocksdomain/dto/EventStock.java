package com.market.stock.stocksdomain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventStock {
    private String id;
    private String companyCode;
    private double stockPrice;
    private String timestamp;
}
