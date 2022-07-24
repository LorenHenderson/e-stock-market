package com.market.stock.stocksdomain.api;

import com.market.stock.stocksdomain.constants.ApplicationConstants;
import com.market.stock.stocksdomain.dto.SaveStockEvent;
import com.market.stock.stocksdomain.models.query.Stock;
import com.market.stock.stocksdomain.service.StocksQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="api/v1.0/market")
public class QueryController {
    private StocksQueryService stockQueryService;

    public QueryController(StocksQueryService stockQueryService) {
        this.stockQueryService = stockQueryService;
    }

//    @KafkaListener(topics= ApplicationConstants.SAVE_STOCKS_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
//    @PostMapping("/add/{companyCode}")
//    public ResponseEntity<Stock> addStock(@Payload @RequestBody SaveStockEvent requestStock){
////        companyCode = Jsoup.clean(companyCode, Safelist.none());
////        StocksCommand stocksCommand = new StocksCommand();
////        stocksCommand.s
//        return ResponseEntity.ok(stockQueryService.saveStock(requestStock, requestStock.getStock().getCompanyCode()));
//    }
}
