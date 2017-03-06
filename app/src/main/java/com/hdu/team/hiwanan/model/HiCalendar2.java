package com.hdu.team.hiwanan.model;

/**
 * Created by JerryYin on 10/14/16.
 */

public class HiCalendar2 {
    /**
     * error_code : 0
     * reason : Success
     * result : {"data":
     *
     {
        "error_code": 0,
        "reason": "Success",
        "result": {
            "data": {
                "holiday": "元旦",//假日
                "avoid": "破土.安葬.行丧.开生坟.",//忌
                "animalsYear": "马",//属相
                "desc": "1月1日至3日放假调休，共3天。1月4日（星期日）上班。",//假日描述
                "weekday": "星期四",//周几
                "suit": "订盟.纳采.造车器.祭祀.祈福.出行.安香.修造.动土.上梁.开市.交易.立券.移徙.入宅.会亲友.安机械.栽种.纳畜.造屋.起基.安床.造畜椆栖.",//宜
                "lunarYear": "甲午年",//纪年
                "lunar": "十一月十一",//农历
                "year-month": "2015-1",//年份和月份
                "date": "2015-1-1"//具体日期
            }
        }
     }
     * */

    private int error_code;
    private String reason;
    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

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

    private class ResultBean {

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        private class DataBean {
            private String holiday;
            private String avoid;
            private String animalsYear;
            private String desc;
            private String weekday;
            private String suit;
            private String lunarYear;
            private String lunar;


            public String getHoliday() {
                return holiday;
            }

            public void setHoliday(String holiday) {
                this.holiday = holiday;
            }

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

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
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
        }
    }


}
