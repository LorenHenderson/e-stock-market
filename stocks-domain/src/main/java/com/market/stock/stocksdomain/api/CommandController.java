package com.market.stock.stocksdomain.api;

import com.market.stock.stocksdomain.models.command.CommandRequestStock;
import com.market.stock.stocksdomain.models.command.StocksCommand;
import com.market.stock.stocksdomain.service.StocksCommandService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1.0/market")
public class CommandController {

    private final StocksCommandService stocksCommandService;

    public CommandController(StocksCommandService scs) {
        stocksCommandService = scs;
    }

    @PostMapping("/stock/add")
    public ResponseEntity<StocksCommand> saveStock(@RequestBody CommandRequestStock requestStock){
        return ResponseEntity.ok(stocksCommandService.saveAddStockCommand( requestStock));
    }

    @DeleteMapping("/delete/{companyCode}")
    public String deleteStock(@PathVariable String companyCode){
        companyCode = Jsoup.clean(companyCode, Safelist.none());
        stocksCommandService.saveDeleteStockCommand(companyCode);
        return "Sending stock to delete: "+ companyCode;
    }


}
