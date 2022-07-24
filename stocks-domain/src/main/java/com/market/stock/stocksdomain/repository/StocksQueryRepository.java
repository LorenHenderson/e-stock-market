package com.market.stock.stocksdomain.repository;

import com.market.stock.stocksdomain.models.query.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksQueryRepository extends JpaRepository<Stock, String> {
    void deleteByCompanycode(String companyCode);
}
