package com.hdu.team.hiwanan.model;

/**
 * Created by JerryYin on 9/27/16.
 * 自定义日历内容model
 */

public class HiCalendar {

    private String month;           //月份
    private String year_old;        //农历年份
    private String year_zodiac;     //生肖
    private String month_zodiac;    //农历月份
    private String day_zodiac;      //日
    private String date;            //新历日期
    private String day;             //周几
    private String do_something;    //宜做事
    private String words;           //名言
    private String author;          //作者


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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDo_something() {
        return do_something;
    }

    public void setDo_something(String do_something) {
        this.do_something = do_something;
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
}
