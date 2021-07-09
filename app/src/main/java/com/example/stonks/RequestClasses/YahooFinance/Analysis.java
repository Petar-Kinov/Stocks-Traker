package com.example.stonks.RequestClasses.YahooFinance;

public class Analysis {

    private EarningsTrend earningsTrend;
    private FinancialData financialData;

    public EarningsTrend getEarningsTrend() {
        return earningsTrend;
    }

    public void setEarningsTrend(EarningsTrend earningsTrend) {
        this.earningsTrend = earningsTrend;
    }

    public FinancialData getFinancialData() {
        return financialData;
    }

    public void setFinancialData(FinancialData financialData) {
        this.financialData = financialData;
    }
}
