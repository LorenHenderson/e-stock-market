package com.market.stock.stocksdomain.repository;

import com.market.stock.stocksdomain.models.command.StocksCommand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksCommandRepository  extends MongoRepository<StocksCommand, String> {
}
