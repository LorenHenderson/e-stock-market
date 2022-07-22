package com.market.stock.stocksdomain.service;

import com.market.stock.stocksdomain.constants.ApplicationConstants;
import com.market.stock.stocksdomain.dto.SaveStockEvent;
import com.market.stock.stocksdomain.dto.CommandStock;
import com.market.stock.stocksdomain.models.command.CommandRequestStock;
import com.market.stock.stocksdomain.models.command.StocksCommand;
import com.market.stock.stocksdomain.repository.StocksCommandRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class StocksCommandService {

    private KafkaTemplate<String, SaveStockEvent> kafkaTemplate;

    private StocksCommandRepository stockCommandDB;

    public StocksCommandService(KafkaTemplate<String, SaveStockEvent> kafkaTemplate, StocksCommandRepository stockCommandDB) {
        this.kafkaTemplate = kafkaTemplate;
        this.stockCommandDB = stockCommandDB;
    }

    public StocksCommand saveCommand( CommandRequestStock stock){
        final String id = UUID.randomUUID().toString();

        StocksCommand stocksCommand = new StocksCommand();
        stocksCommand.setId(id);
        stocksCommand.setCompanyCode(stock.getCompanyCode());
        stocksCommand.setCommand(ApplicationConstants.SAVE_STOCKS_COMMAND);
        stocksCommand.setEvent(new CommandRequestStock(stock.getCompanyCode(), stock.getCompanyCode(), stock.getStockPrice(), stock.getTimestamp()));
        stocksCommand.setEventTimestamp(LocalDateTime.now());

        SaveStockEvent event = new SaveStockEvent(
                stocksCommand.getId(),
                stocksCommand.getCompanyCode(),
                stocksCommand.getCommand(),
                new CommandStock(stocksCommand.getEvent().getId(),
                        stocksCommand.getEvent().getCompanyCode(),
                        stocksCommand.getEvent().getStockPrice(),
                        stocksCommand.getEvent().getTimestamp()),
                stocksCommand.getEventTimestamp());
        log.info("Saving Stocks Command {} ", stocksCommand);

        if(StringUtils.isBlank(stockCommandDB.save(stocksCommand).getId())){
            return new StocksCommand();
        }
        kafkaTemplate.send(ApplicationConstants.SAVE_STOCKS_TOPIC, ApplicationConstants.SAVE_STOCKS_COMMAND, event);
        log.info("Sending stock {}", stocksCommand);

        return stocksCommand;
    }
}
