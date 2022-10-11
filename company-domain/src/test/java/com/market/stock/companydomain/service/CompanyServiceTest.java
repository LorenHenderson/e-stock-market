package com.market.stock.companydomain.service;

import com.market.stock.companydomain.domain.CommandStock;
import com.market.stock.companydomain.domain.Company;
import com.market.stock.companydomain.domain.SaveStockEvent;
import com.market.stock.companydomain.domain.Stock;
import com.market.stock.companydomain.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

//    CompanyServiceTest(CompanyService companyService){
//        this.companyService = companyService;
//    }

    @Test
    public void fetchCompanyDetails_shouldReturn_Company() {
        when(companyRepository.existsCompanyByCompanyCode(any())).thenReturn(true);
        when(companyRepository.findByCompanyCode(any())).thenReturn(new Company("3",
                "122",
                "Millers",
                "me",
                10000000,
                "bye@here",
                "DAT",
                null));
        Company fetchedCompany = companyService.fetchCompanyDetails("123");
        assertEquals("122", fetchedCompany.getCompanyCode());
        verify(companyRepository, times(1)).findByCompanyCode("123");
    }

    @Test
    public void updateCompany_should_update_stock() {
        when(companyRepository.existsCompanyByCompanyCode(any())).thenReturn(true);
        when(companyRepository.findByCompanyCode(any())).thenReturn(
                new Company("3",
                        "122",
                        "Millers",
                        "me",
                        10000000,
                        "bye@here",
                        "DAT",
                        null));

        Company saveCompany =  new Company(
                "3",
                "122",
                "Millers",
                "me",
                10000000,
                "bye@here",
                "DAT",
                new ArrayList<>() {
                    {
                        add(new Stock("1", "122", 11.11, ""));
                    }
                }
        );
        when(companyRepository.save(any())).thenReturn(saveCompany);
        SaveStockEvent stockEvent = new SaveStockEvent(
                "12",
                "122",
                "SAVE_STOCK",
                new CommandStock(
                        "1",
                        "122",
                        11.11,
                        ""),
                LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)));
        companyService.updateCompany(stockEvent);

        verify(companyRepository, times(1)).save(saveCompany);
        verify(companyRepository, times(1)).existsCompanyByCompanyCode("122");
        verify(companyRepository, times(1)).findByCompanyCode("122");

    }
}
