package com.market.stock.companydomain.domain;


import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
public class StockDetails {
    private double maxPrice;
    private double minPrice;
    private double averagePrice;
    private List<Stock> stocks;
}
