package com.market.stock.companydomain.repository;

import com.market.stock.companydomain.domain.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
    Company findByCompanyCode(String companyCode);
    boolean existsCompanyByCompanyCode(String companyCode);
    Company deleteByCompanyCode(String companyCode);
}