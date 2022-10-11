package com.market.stock.stocksdomain.dto;

import com.market.stock.stocksdomain.models.query.Stock;
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
