package com.market.stock.stocksdomain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteStockEvent {
    private String id;
    private String companyCode;
}
