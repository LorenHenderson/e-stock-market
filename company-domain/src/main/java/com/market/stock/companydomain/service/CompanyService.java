package com.market.stock.companydomain.service;

import com.market.stock.companydomain.client.StocksCommandClient;
import com.market.stock.companydomain.domain.*;
import com.market.stock.companydomain.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CompanyService {

    private CompanyRepository companyRepository;
    private StocksCommandClient stocksCommandClient;

    public CompanyService(CompanyRepository repo, StocksCommandClient stocksCommandClient){
        companyRepository = repo;
        this.stocksCommandClient= stocksCommandClient;
    }

    public Company registerCompany(RequestCompany requestCompany) throws IllegalArgumentException{
        log.info("Entering SAVE COMPANY...");
        Company fetchedCompany = companyRepository.findByCompanyCode(requestCompany.getCompanyCode());
        Company company = new Company();
        if(Objects.nonNull(fetchedCompany)){
            log.error("Duplicate Company Code {}. Try sending unique code.", requestCompany.getCompanyCode());
            throw new IllegalArgumentException("Duplicate Company Code");
        }
        BeanUtils.copyProperties(requestCompany, company);
        return companyRepository.save(company);
    }

    public Company fetchCompanyDetails(String companyCode){
        Company fetchedCompany = new Company();
        if(StringUtils.isBlank(companyCode)){
            log.error("Trying to fetch company with empty CompanyCode {}", companyCode);
            throw new IllegalArgumentException("Cannot fetch company using empty CompanyCode:"+ companyCode +". Try including companyCode");
        }

        if(companyRepository.existsCompanyByCompanyCode(companyCode)){
            log.info("Fetching data for CompanyCode: {}", companyCode);
            fetchedCompany = companyRepository.findByCompanyCode(companyCode);
        }

        return fetchedCompany;
    }

    public List<Company> fetchAll(){
        return companyRepository.findAll();
    }

    @KafkaListener(topics= "saveStocksCommand", groupId = "companyUpdate", containerFactory = "saveStockEventConcurrentKafkaListenerContainerFactory")
    public void updateCompany(@Payload SaveStockEvent addStocksCommand){
        Company fetchedCompany;
        List<Stock> stocksList;
        if(ObjectUtils.isEmpty(addStocksCommand)){
            log.error("Event failure. Failed to receive update event");
            return;
        }
        if(companyRepository.existsCompanyByCompanyCode(addStocksCommand.getCompanyCode())){
            log.info("Update Company Event {}",addStocksCommand );
            fetchedCompany = companyRepository.findByCompanyCode(addStocksCommand.getCompanyCode());
            stocksList = updateStockList(fetchedCompany.getStocks(), addStocksCommand.getStock());
            fetchedCompany.setStocks(stocksList);
            companyRepository.save(fetchedCompany);
            log.info("Company: {} updated", fetchedCompany);
        }
    }

    public Company deleteCompany(String companyCode){

        if(StringUtils.isBlank(companyCode)){
            IllegalArgumentException e =new IllegalArgumentException("Cannot delete company using empty CompanyCode:"+ companyCode);
            log.info("Failed to provide companyCode", e);
            throw  e;
        }
        log.info("Deleting company for companyCode: {}"+companyCode);

        Company deletedCompany = companyRepository.deleteByCompanyCode(companyCode);
        if(ObjectUtils.allNull(deletedCompany)){
            log.warn("No Company to delete for CompanyCode: {}", companyCode);
            return null;
        }

        String deleteResponse = stocksCommandClient.deleteStock(companyCode);

        log.info("Deleting stocks for companyCode by: "+ deleteResponse);
        return deletedCompany ;
    }

    private List<Stock> updateStockList(List<Stock> fetchedStock, CommandStock eventStock){
        List<Stock> stocksList;
        Stock stock = new Stock(
                eventStock.getId(),
                eventStock.getCompanyCode(),
                eventStock.getStockPrice(),
                eventStock.getTimestamp());

        log.info("Updating Stock: {}", stock);
        if(ObjectUtils.isEmpty(fetchedStock) ){
            stocksList = new ArrayList<>(){
                {
                    add(stock);
                }
            };
        }else{
            stocksList = new ArrayList<>();
            stocksList.addAll(fetchedStock);
            stocksList.add(stock);
        }

        return stocksList;
    }
}