package com.market.stock.stocksdomain.service;

import com.market.stock.stocksdomain.constants.ApplicationConstants;
import com.market.stock.stocksdomain.dto.DeleteStockEvent;
import com.market.stock.stocksdomain.dto.SaveStockEvent;
import com.market.stock.stocksdomain.dto.StockDetails;
import com.market.stock.stocksdomain.models.query.Stock;
import com.market.stock.stocksdomain.repository.StocksQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.Filter.filter;

@Service
@Slf4j
public class StocksQueryService {

    @Autowired
    private final MongoTemplate mongoTemplate;

    private final StocksQueryRepository stocksQueryRepository;

    public StocksQueryService(MongoTemplate mongoTemplate, StocksQueryRepository stocksQueryRepository) {
        this.mongoTemplate = mongoTemplate;
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
    @Transactional
    public void deleteStock(@Payload DeleteStockEvent deleteStock) {
        log.info("deleting Stock with companyCode: {}", deleteStock.getCompanyCode());
        try{
            stocksQueryRepository.deleteByCompanyCode(deleteStock.getCompanyCode());
        }catch(Exception e){
            log.error("No Stock exists with ID: {}...{}", deleteStock.getCompanyCode(), e.getMessage(), e);
        }
    }

    public StockDetails getStockDetails(String companyCode, String startDate, String endDate){
//        StockDetails stockDetails = new StockDetails();

//                MatchOperation matchStocksBetweenDates = match(new Criteria("companyCode").is(companyCode));
//        MatchOperation getStocksBetweenDates = match(new Criteria("stock.timestamp").in("stocks").gte(startDate).lte(endDate));
        GroupOperation getAverageStockPriceBetweenDates = group("company.stocks.stockPrice").avg("stockPrice").as("averagePrice");
        GroupOperation groupByCompanyCode = group("company.stocks.stockPrice").first("stocks.stockPrice").as("minStock")
                .last("stocks.stockPrice").as("maxPrice");
        ProjectionOperation filterStocks = project().and(filter("stocks").as("stock")
                .by(ComparisonOperators.Gt.valueOf("stock.timestamp").greaterThan(startDate))).as("stocks")
                .and(filter("stocks").as("stock")
                        .by(ComparisonOperators.Lt.valueOf("stock.timestamp").lessThan(endDate))).as("stocks");

        //        GroupOperation getStocksBetweenDates = group("company.stocks").avg("stockPrice").as("averagePrice");


        Aggregation aggregation = newAggregation(filterStocks);
//        Aggregation aggregation = newAggregation(matchStocksBetweenDates, getAverageStockPriceBetweenDates,groupByCompanyCode, filterStocks);
        AggregationResults<StockDetails> result = mongoTemplate.aggregate(aggregation, "Company",StockDetails.class);

        return result.getUniqueMappedResult();
    }
}
