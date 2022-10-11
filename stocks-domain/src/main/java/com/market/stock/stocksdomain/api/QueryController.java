package com.market.stock.stocksdomain.api;

import com.market.stock.stocksdomain.dto.StockDetails;
import com.market.stock.stocksdomain.service.StocksQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="api/v1.0/market")
@Slf4j
public class QueryController {
    private final StocksQueryService stockQueryService;

    public QueryController(StocksQueryService stockQueryService) {
        this.stockQueryService = stockQueryService;
    }

    @GetMapping("/stock/get/{companyCode}/{startDate}/{endDate}")
    public ResponseEntity<StockDetails> getStocksByDateRange(@PathVariable String companyCode, @PathVariable String startDate, @PathVariable String endDate){
        StockDetails stockDetails;
        try{
            stockDetails = stockQueryService.getStockDetails(companyCode, startDate, endDate);
            log.info("fetching stock by company code: {}, start date: {}, end date: {}", companyCode, startDate, endDate);
            log.info("fetched stock: {}", stockDetails);
        }catch(Exception e){
            log.error("Failed to retrieve stock details for Company code {}  {}", companyCode, e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }

        return  ResponseEntity.status(HttpStatus.OK).body(stockDetails);
    }
}
