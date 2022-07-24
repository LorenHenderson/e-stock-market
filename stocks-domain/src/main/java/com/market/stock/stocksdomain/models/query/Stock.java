package com.market.stock.stocksdomain.models.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name= "stocks")
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    private String id;
    private String companycode;
    private double stockprice;
    private String timestamp;
}