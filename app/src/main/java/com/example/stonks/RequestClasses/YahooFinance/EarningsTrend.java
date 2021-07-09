package com.example.stonks.RequestClasses.YahooFinance;

import java.util.List;

public class EarningsTrend {
    private List<Trend> trend;

    public List<Trend> getTrend() {
        return trend;
    }

    public void setTrend(List<Trend> trend) {
        this.trend = trend;
    }
}
