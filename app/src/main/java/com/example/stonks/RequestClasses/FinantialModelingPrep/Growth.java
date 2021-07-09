package com.example.stonks.RequestClasses.FinantialModelingPrep;


public class Growth {

//    private price price;
//
//    public com.example.stockanalyzer.price getPrice() {
//        return price;
//    }
//
//    public void setPrice(com.example.stockanalyzer.price price) {
//        this.price = price;
//    }

//    private defaultKeyStatistics defaultKeyStatistics;
//
//    public com.example.stockanalyzer.ModelClasses.defaultKeyStatistics getDefaultKeyStatistics() {
//        return defaultKeyStatistics;
//    }
//
//    public void setDefaultKeyStatistics(com.example.stockanalyzer.ModelClasses.defaultKeyStatistics defaultKeyStatistics) {
//        this.defaultKeyStatistics = defaultKeyStatistics;
//    }

    private double revenueGrowth;
    private double freeCashFlowGrowth;
    private double netIncomeGrowth;

    public double getFreeCashFlowGrowth() {
        return freeCashFlowGrowth;
    }

    public double getRevenueGrowth() {
        return revenueGrowth;
    }

    public void setRevenueGrowth(double revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }

    public void setFreeCashFlowGrowth(double freeCashFlowGrowth) {
        this.freeCashFlowGrowth = freeCashFlowGrowth;
    }

    public double getNetIncomeGrowth() {
        return netIncomeGrowth;
    }

    public void setNetIncomeGrowth(double netIncomeGrowth) {
        this.netIncomeGrowth = netIncomeGrowth;
    }
}
