package com.example.stonks.RequestClasses.YahooFinance;

public class FinancialData {
    private TargetLowPrice targetLowPrice;
    private TargetMedianPrice targetMedianPrice;
    private TargetHighPrice targetHighPrice;
    private ProfitMargins profitMargins;
    private GrossMargins grossMargins;
    private ReturnOnAssets returnOnAssets;

    public TargetLowPrice getTargetLowPrice() {
        return targetLowPrice;
    }

    public void setTargetLowPrice(TargetLowPrice targetLowPrice) {
        this.targetLowPrice = targetLowPrice;
    }

    public TargetMedianPrice getTargetMedianPrice() {
        return targetMedianPrice;
    }

    public void setTargetMedianPrice(TargetMedianPrice targetMedianPrice) {
        this.targetMedianPrice = targetMedianPrice;
    }

    public TargetHighPrice getTargetHighPrice() {
        return targetHighPrice;
    }

    public void setTargetHighPrice(TargetHighPrice targetHighPrice) {
        this.targetHighPrice = targetHighPrice;
    }

    public ProfitMargins getProfitMargins() {
        return profitMargins;
    }

    public void setProfitMargins(ProfitMargins profitMargins) {
        this.profitMargins = profitMargins;
    }

    public GrossMargins getGrossMargins() {
        return grossMargins;
    }

    public void setGrossMargins(GrossMargins grossMargins) {
        this.grossMargins = grossMargins;
    }

    public ReturnOnAssets getReturnOnAssets() {
        return returnOnAssets;
    }

    public void setReturnOnAssets(ReturnOnAssets returnOnAssets) {
        this.returnOnAssets = returnOnAssets;
    }
}
