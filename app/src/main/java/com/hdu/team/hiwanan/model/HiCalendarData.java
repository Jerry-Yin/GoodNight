package com.hdu.team.hiwanan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JerryYin on 10/14/16.
 */

public class HiCalendarData {
    /**
     * reason : Success
     * result : {"data":{"avoid":"开市.交易.作灶.纳财.上梁.安床.造屋.造船.","animalsYear":"鸡","weekday":"星期五","suit":"嫁娶.开光.祭祀.祈福.求嗣.出行.出火.入宅.移徙.解除.栽种.伐木.破土.谢土.安葬.","lunarYear":"丁酉年","lunar":"二月十三","year-month":"2017-3","date":"2017-3-10"}}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * data : {"avoid":"开市.交易.作灶.纳财.上梁.安床.造屋.造船.","animalsYear":"鸡","weekday":"星期五","suit":"嫁娶.开光.祭祀.祈福.求嗣.出行.出火.入宅.移徙.解除.栽种.伐木.破土.谢土.安葬.","lunarYear":"丁酉年","lunar":"二月十三","year-month":"2017-3","date":"2017-3-10"}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * avoid : 开市.交易.作灶.纳财.上梁.安床.造屋.造船.
             * animalsYear : 鸡
             * weekday : 星期五
             * suit : 嫁娶.开光.祭祀.祈福.求嗣.出行.出火.入宅.移徙.解除.栽种.伐木.破土.谢土.安葬.
             * lunarYear : 丁酉年
             * lunar : 二月十三
             * year-month : 2017-3
             * date : 2017-3-10
             */

            private String avoid;
            private String animalsYear;
            private String weekday;
            private String suit;
            private String lunarYear;
            private String lunar;
            @SerializedName("year-month")
            private String yearmonth;
            private String date;

            public String getAvoid() {
                return avoid;
            }

            public void setAvoid(String avoid) {
                this.avoid = avoid;
            }

            public String getAnimalsYear() {
                return animalsYear;
            }

            public void setAnimalsYear(String animalsYear) {
                this.animalsYear = animalsYear;
            }

            public String getWeekday() {
                return weekday;
            }

            public void setWeekday(String weekday) {
                this.weekday = weekday;
            }

            public String getSuit() {
                return suit;
            }

            public void setSuit(String suit) {
                this.suit = suit;
            }

            public String getLunarYear() {
                return lunarYear;
            }

            public void setLunarYear(String lunarYear) {
                this.lunarYear = lunarYear;
            }

            public String getLunar() {
                return lunar;
            }

            public void setLunar(String lunar) {
                this.lunar = lunar;
            }

            public String getYearmonth() {
                return yearmonth;
            }

            public void setYearmonth(String yearmonth) {
                this.yearmonth = yearmonth;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }






}
