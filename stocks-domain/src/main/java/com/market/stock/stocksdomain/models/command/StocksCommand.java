package com.market.stock.stocksdomain.models.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("stocks-command-events")
public class StocksCommand {
    @Id
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final String id = UUID.randomUUID().toString();
    private String companyCode;
    private String command;
    private CommandRequestStock stock;
    private final String eventTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT));

}
