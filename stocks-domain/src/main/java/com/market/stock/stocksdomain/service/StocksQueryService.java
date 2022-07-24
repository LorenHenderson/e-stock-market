package com.market.stock.stocksdomain.service;

import com.market.stock.stocksdomain.constants.ApplicationConstants;
import com.market.stock.stocksdomain.dto.DeleteStockEvent;
import com.market.stock.stocksdomain.dto.SaveStockEvent;
import com.market.stock.stocksdomain.models.query.Stock;
import com.market.stock.stocksdomain.repository.StocksQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Service
@Slf4j
public class StocksQueryService {
    private final StocksQueryRepository stocksQueryRepository;

    public StocksQueryService(StocksQueryRepository stocksQueryRepository) {
        this.stocksQueryRepository = stocksQueryRepository;
    }

    @KafkaListener(topics= ApplicationConstants.SAVE_STOCKS_TOPIC, groupId = "${spring.kafka.consumer.group-id}", containerFactory = "saveStockEventConcurrentKafkaListenerContainerFactory")
    public Stock saveStock(@Payload SaveStockEvent requestStock){
        Stock stock;
        if(ObjectUtils.allNull(requestStock.getStock())){
            log.error("unable to save missing stock {}", requestStock);
        return new Stock();
        }
            stock = new Stock(requestStock.getStock().getId(),
                requestStock.getStock().getCompanyCode(),
                requestStock.getStock().getStockPrice(),
                LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)));

        log.info("consuming {}", requestStock);

        return stocksQueryRepository.save(stock);
    }

    @KafkaListener(topics= ApplicationConstants.DELETE_STOCKS_TOPIC, groupId= "deleteGroup", containerFactory = "deleteStockEventConcurrentKafkaListenerContainerFactory")
    public void deleteStock(@Payload DeleteStockEvent deleteStock) {
        log.info("deleting Stock with companyCode: {}", deleteStock.getCompanyCode());
        try{
            stocksQueryRepository.deleteById(deleteStock.getCompanyCode());
        }catch(Exception e){
            log.error("No Stock exists with ID: {}...{}", deleteStock.getCompanyCode(), e.getMessage(), e);
        }
    }
}
