package com.market.stock.stocksdomain.service;

import com.market.stock.stocksdomain.constants.ApplicationConstants;
import com.market.stock.stocksdomain.dto.DeleteStockEvent;
import com.market.stock.stocksdomain.dto.EventStock;
import com.market.stock.stocksdomain.dto.SaveStockEvent;
import com.market.stock.stocksdomain.dto.StockDetails;
import com.market.stock.stocksdomain.models.command.CommandRequestStock;
import com.market.stock.stocksdomain.models.command.StocksCommand;
import com.market.stock.stocksdomain.repository.StocksCommandRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class StocksCommandService {

    private final KafkaTemplate<String, SaveStockEvent> saveStockEventKafkaTemplate;
    private final KafkaTemplate<String, DeleteStockEvent> deleteStockEventKafkaTemplate;
    private final KafkaTemplate<String, StockDetails> stockDetailsEventKafkaTemplate;

    private final StocksCommandRepository stockCommandDB;

    public StocksCommandService(KafkaTemplate<String, SaveStockEvent> saveStockEventKafkaTemplate, KafkaTemplate<String, DeleteStockEvent> deleteStockEventKafkaTemplate, KafkaTemplate<String, StockDetails> stockDetailsEventKafkaTemplate, StocksCommandRepository stockCommandDB) {
        this.saveStockEventKafkaTemplate = saveStockEventKafkaTemplate;
        this.deleteStockEventKafkaTemplate = deleteStockEventKafkaTemplate;
        this.stockDetailsEventKafkaTemplate = stockDetailsEventKafkaTemplate;
        this.stockCommandDB = stockCommandDB;
    }

    public StocksCommand saveAddStockCommand(CommandRequestStock stock){
        final String id = UUID.randomUUID().toString();

        StocksCommand stocksCommand = new StocksCommand();
        stocksCommand.setCompanyCode(stock.getCompanyCode());
        stocksCommand.setCommand(ApplicationConstants.SAVE_STOCKS_COMMAND);
        stocksCommand.setStock(new CommandRequestStock(
                stock.getCompanyCode(),
                stock.getStockPrice()
                ));

        SaveStockEvent event = new SaveStockEvent(
                stocksCommand.getId(),
                stocksCommand.getCompanyCode(),
                stocksCommand.getCommand(),
                new EventStock(
                        stocksCommand.getId(),
                        stocksCommand.getStock().getCompanyCode(),
                        stocksCommand.getStock().getStockPrice(),
                        stocksCommand.getEventTimestamp()),
                        stocksCommand.getEventTimestamp());
        log.info("Saving Stocks Command {} ", stocksCommand);
        log.info("Saving Stock {} ", stocksCommand.getStock());

        if(ObjectUtils.allNull(stockCommandDB.save(stocksCommand))){
            log.error("Unable to save stockStock Command: {}", stocksCommand);
            return null;
        }
        log.info("Sending save stock stock: {}", stocksCommand);
        saveStockEventKafkaTemplate.send(ApplicationConstants.SAVE_STOCKS_TOPIC, ApplicationConstants.SAVE_STOCKS_COMMAND, event);

        return stocksCommand;
    }

    public void saveDeleteStockCommand(String companyCode){
        if(StringUtils.isBlank(companyCode)){
            log.error("Unable to delete stock for empty CompanyCode");
            throw new IllegalArgumentException("CompanyCode cannot be blank");
        }

        StocksCommand stocksCommand = stockCommandDB.save(new StocksCommand(
                companyCode,
                ApplicationConstants.DELETE_STOCKS_COMMAND,
                null));

        if(ObjectUtils.allNull(stocksCommand) ){
            log.error("Unable to save DeleteStocks Command: {} : Please check the Company Code", stocksCommand);
            throw new RuntimeException("Unable to save DeleteStocks Command");
        }

        log.info("Stocks Command Saved {} ", stocksCommand);
        DeleteStockEvent deleteStock = new DeleteStockEvent();
        deleteStock.setCompanyCode( new ObjectId().toString());
        deleteStock.setCompanyCode(companyCode);
        log.info("Sending delete stock for companyCode: {}", companyCode);
        deleteStockEventKafkaTemplate.send(ApplicationConstants.DELETE_STOCKS_TOPIC, ApplicationConstants.DELETE_STOCKS_COMMAND, deleteStock);
    }
}
