package com.market.stock.companydomain.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stocks-domain")
public interface StocksCommandClient {
    @DeleteMapping("api/v1.0/market/delete/{companyCode}")
    String deleteStock(@PathVariable String companyCode);
}
