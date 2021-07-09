package com.example.stonks.RequestClasses.YahooFinance;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Trend {

//    private AnItem item;
//
//    public AnItem getItem() {
//        return item;
//    }
//
//    public void setItem(AnItem item) {
//        this.item = item;
//    }

    private String period;
    private Growth growth;

    public Growth getGrowth() {
        return growth;
    }

    public void setGrowth(Growth growth) {
        this.growth = growth;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
