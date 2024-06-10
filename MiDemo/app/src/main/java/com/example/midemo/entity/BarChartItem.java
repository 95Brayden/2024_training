package com.example.midemo.entity;

/* 用于描述绘制柱状图时，每一个柱子表示的对象*/
public class BarChartItem {
    private int year;
    private int month;
    private int day;
    private float summoney;

    public BarChartItem(int year, int month, int day, float summoney) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.summoney = summoney;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public float getSumMoney() {
        return summoney;
    }

}