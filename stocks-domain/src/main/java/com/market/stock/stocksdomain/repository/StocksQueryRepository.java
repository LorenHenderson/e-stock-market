package com.market.stock.stocksdomain.repository;

import com.market.stock.stocksdomain.models.query.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksQueryRepository extends JpaRepository<Stock, String> {
//    @Modifying      // to mark delete or update query
//    @Query(value = "DELETE FROM STOCK s WHERE s.companyCode")
    int deleteByCompanyCode(String companyCode);
}
