package com.market.stock.companydomain.controller;

import com.market.stock.companydomain.domain.Company;
import com.market.stock.companydomain.domain.RequestCompany;
//import jakarta.validation.Valid;
import com.market.stock.companydomain.service.CompanyService;
//import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="api/v1.0/market")
@Validated
@CrossOrigin("http://localhost:3000")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/company/register")
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody RequestCompany requestCompany){
        Company savedComp;
        log.info("Entering REGISTER COMPANY...");
        try{
            savedComp = companyService.registerCompany(requestCompany);
        }catch(IllegalArgumentException e){
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        log.info("Returning From REGISTER COMPANY...");
        return  ResponseEntity.status(HttpStatus.CREATED).body(savedComp);
    }

    @GetMapping("/company/info")
    public ResponseEntity<Company> fetchCompnayDetails(@QueryParam("companyCode") String companyCode){
        Company company;
        companyCode = Jsoup.clean(companyCode, Safelist.none());
        try{
            company = companyService.fetchCompanyDetails(companyCode);
        }catch(Exception e ){
            log.error("{}",e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }

        if( ObjectUtils.allNull(company))
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(company);

        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @GetMapping("/company/getall")
    public ResponseEntity<List<Company>> fetchAllCompanies(){
        return ResponseEntity.status(HttpStatus.OK).body(companyService.fetchAll());
    }

    @DeleteMapping("/company/delete/{companyCode}")
    public ResponseEntity<Company> deleteCompany(@PathVariable String companyCode){
        Company deletedCompany;
        try{
            deletedCompany = companyService.deleteCompany(companyCode);
        }catch(Exception e){
            log.error("Failed to delete Company with code: {}...{}", companyCode, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }

        return  ResponseEntity.status(HttpStatus.OK).body(deletedCompany);
    }
}