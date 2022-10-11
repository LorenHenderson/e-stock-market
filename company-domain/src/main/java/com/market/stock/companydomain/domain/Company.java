package com.market.stock.companydomain.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="company")
public class Company {

    @Id
    @Generated
    private String id;

    @NotNull(message="RequestCompany code cannot be empty")
    private String companyCode;

    @NotNull(message = "RequestCompany name cannot be empty")
    private String companyName;

    @NotNull(message = "RequestCompany CEO cannot be empty")
    private String companyCEO;

    @NotNull(message = "RequestCompany turnover cannot be empty")
    @Min(value = 10000000, message = "Minimum turnover is 10CR(10M)")
    private long companyTurnover;

    @NotNull(message = "RequestCompany website cannot be empty")
    private String companyWebsite;

    @NotNull(message = "RequestCompany stock exchange cannot be empty")
    private String stockExchange;

    private List<Stock> stocks;
}
