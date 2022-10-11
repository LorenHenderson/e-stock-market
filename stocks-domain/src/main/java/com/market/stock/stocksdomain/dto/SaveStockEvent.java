package com.market.stock.stocksdomain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveStockEvent {

    private String id;
    private String companyCode;
    private String command;
    private EventStock stock;
    private String eventTimestamp;
}
