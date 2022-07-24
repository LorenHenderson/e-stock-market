package com.market.stock.stocksdomain.models.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("stocks-command-events")
public class StocksCommand {
    @Id
    @Generated()
    private String id;
    private String companyCode;
    private String command;
    private CommandRequestStock stock;
    private LocalDateTime eventTimestamp;

}
