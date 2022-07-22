package com.market.stock.stocksdomain.api;

import com.market.stock.stocksdomain.models.command.CommandRequestStock;
import com.market.stock.stocksdomain.models.command.StocksCommand;
import com.market.stock.stocksdomain.repository.StocksCommandRepository;
import com.market.stock.stocksdomain.service.StocksCommandService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@RestController
@RequestMapping("api/v1.0/market")
public class CommandController {

    private final StocksCommandRepository stockCommandDB;
    private final StocksCommandService stocksCommandService;

    public CommandController(StocksCommandRepository db, StocksCommandService scs) {
        stockCommandDB = db;
        stocksCommandService = scs;
    }

    @PostMapping("/stock/add")
    public ResponseEntity<StocksCommand> saveStock(@RequestBody CommandRequestStock requestStock){
//        companyCode = Jsoup.clean(companyCode, Safelist.none());
        requestStock.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)));
//    CommandRequestStock stock = ;
        return ResponseEntity.ok(stocksCommandService.saveCommand( requestStock));
    }

}
