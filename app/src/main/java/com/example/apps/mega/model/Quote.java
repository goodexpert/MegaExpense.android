package com.example.apps.mega.model;

import java.io.Serializable;

public class Quote implements Serializable  {

    private String currencyPair;

    private Double exchangeRate;

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
