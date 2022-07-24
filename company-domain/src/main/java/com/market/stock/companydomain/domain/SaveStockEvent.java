package com.market.stock.companydomain.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveStockEvent {

    private String id;
    private String companyCode;
    private String command;
    private CommandStock stock;
    private LocalDateTime eventTimestamp;
}
