package com.market.stock.companydomain.controller;

import com.market.stock.companydomain.domain.Company;
import com.market.stock.companydomain.domain.RequestCompany;
import com.market.stock.companydomain.service.CompanyService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value="api/v1.0/market")
@Validated
@CrossOrigin("http://localhost:3000")
public class CompanyController {
    private static final Logger LOG = LogManager.getLogger(CompanyService.class.getName());

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/company/register")
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody RequestCompany requestCompany){
       Company savedComp;
       LOG.info("Entering REGISTER COMPANY...");
       try{
           savedComp = companyService.registerCompany(requestCompany);
       }catch(IllegalArgumentException e){
           LOG.error(e.getMessage(), e);
           return ResponseEntity.status(HttpStatus.CONFLICT).build();
       }
        LOG.info("Returning From REGISTER COMPANY...");
        return  ResponseEntity.status(HttpStatus.CREATED).body(savedComp);
    }

    @GetMapping("/company/info/{companyCode}")
    public ResponseEntity<Company> fetchCompanyDetails(@PathVariable String companyCode){
        Company company;
        companyCode = Jsoup.clean(companyCode, Safelist.none());
        try{
            company = companyService.fetchCompanyDetails(companyCode);
        }catch(Exception e ){
            LOG.error("{}",e.getMessage(), e);
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
            LOG.error("Failed to delete Company with code: {}...{}", companyCode, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }

        return  ResponseEntity.status(HttpStatus.OK).body(deletedCompany);
    }


}
