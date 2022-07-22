package com.market.stock.companydomain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.stock.companydomain.domain.Company;
import com.market.stock.companydomain.domain.RequestCompany;
import com.market.stock.companydomain.repository.CompanyRepository;
import com.market.stock.companydomain.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CompanyController.class)
@AutoConfigureMockMvc
public class CompanyControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private CompanyRepository companyRepository;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    private final String baseURL = "http://localhost:8082/api/v1.0/market";

    @Test
    public void registerCompany_should_return_Company() throws Exception {
        RequestCompany request = new RequestCompany( "3",
                "122",
                 "Millers",
                 "me",
                 10000000,
                 "bye@here",
                "DAT");

        Company response = new Company("3",
                "122",
                "Millers",
                "me",
                10000000,
                "bye@here",
                "DAT",
                null);

        String req = objectMapper.writeValueAsString(request);

        when(companyService.registerCompany(request)).thenReturn(response);

       mockMvc.perform(
                post(baseURL + "/company/register")
                        .content(req)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.companyName").value("Millers")).andReturn();

       verify(companyService).registerCompany(request);
    }

    @Test
    public void getAll_returns_all_companies() throws Exception {
        List<Company> companyList = new ArrayList<>(){
            {
               add( new Company("3",
                    "111",
                    "Millers",
                    "me",
                    10000000,
                    "bye@here.com",
                    "DAT",
                    null));
               add(new Company("3",
                    "125",
                    "Jonseys",
                    "them",
                    10000000,
                    "hi@there.com",
                    "JNS",
                    null));
            }
        };

        when(companyService.fetchAll()).thenReturn(companyList);

        mockMvc.perform(
                get(baseURL+"/company/getall")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].companyCode").value(111));

        verify(companyService).fetchAll();

    }


}