package com.market.stock.stocksdomain.models.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandRequestStock {
    private String companyCode;
    private double stockPrice;
}
