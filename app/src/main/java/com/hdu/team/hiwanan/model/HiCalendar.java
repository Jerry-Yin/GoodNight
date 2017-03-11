package com.hdu.team.hiwanan.model;

/**
 * Created by JerryYin on 9/27/16.
 * 自定义日历内容model
 */

public class HiCalendar {

    private String month_old;           //月份
    private String year_old;        //农历年份
    private String year_zodiac;     //生肖
    private String month_zodiac;    //农历月份
    private String day_zodiac;      //日

    private String year;
    private String month;           //月份
    private String day;            //新历日期
    private String weekend;         //周几

    private String suit;    //宜做事
    private String avoio;   //不适合做事
    private String words;           //名言
    private String author;          //作者


//    public HiCalendar(String month_old, String year_old, String year_zodiac, String month_zodiac,
//                      String day_zodiac, String year, String month, String date, String weekend,
//                      String suit, String words, String author) {
//        this.month_old = month_old;
//        this.year_old = year_old;
//        this.year_zodiac = year_zodiac;
//        this.month_zodiac = month_zodiac;
//        this.day_zodiac = day_zodiac;
//        this.year = year;
//        this.month = month;
//        this.day = day;
//        this.weekend = weekend;
//        this.suit = suit;
//        this.words = words;
//        this.author = author;
//    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear_old() {
        return year_old;
    }

    public void setYear_old(String year_old) {
        this.year_old = year_old;
    }

    public String getYear_zodiac() {
        return year_zodiac;
    }

    public void setYear_zodiac(String year_zodiac) {
        this.year_zodiac = year_zodiac;
    }

    public String getMonth_zodiac() {
        return month_zodiac;
    }

    public void setMonth_zodiac(String month_zodiac) {
        this.month_zodiac = month_zodiac;
    }

    public String getDay_zodiac() {
        return day_zodiac;
    }

    public void setDay_zodiac(String day_zodiac) {
        this.day_zodiac = day_zodiac;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeekend() {
        return weekend;
    }

    public void setWeekend(String weekend) {
        this.weekend = weekend;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMonth_old() {
        return month_old;
    }

    public void setMonth_old(String month_old) {
        this.month_old = month_old;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAvoio() {
        return avoio;
    }

    public void setAvoio(String avoio) {
        this.avoio = avoio;
    }


}
