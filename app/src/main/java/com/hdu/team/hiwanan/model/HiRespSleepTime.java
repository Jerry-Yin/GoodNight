package com.hdu.team.hiwanan.model;

import java.util.List;

/**
 * Created by JerryYin on 5/4/17.
 */

public class HiRespSleepTime {

    /**
     * result : [{"date":"2017-05-01","actual_duration":1,"sleep_stamp":"","user_id":2,"start":"13:00","end":"14:00","wakeup_stamp":"","plan_duration":2},{"date":"2017-05-02","actual_duration":1,"sleep_stamp":"","user_id":2,"start":"13:00","end":"14:00","wakeup_stamp":"","plan_duration":2}]
     * status : 1
     */

    private int status;
    private List<SleepTimeLine> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SleepTimeLine> getResult() {
        return result;
    }

    public void setResult(List<SleepTimeLine> result) {
        this.result = result;
    }

    public static class SleepTimeLine {
        /**
         * date : 2017-05-01
         * actual_duration : 1
         * sleep_stamp :
         * user_id : 2
         * start : 13:00
         * end : 14:00
         * wakeup_stamp :
         * plan_duration : 2
         */

        private String date;
        private int actual_duration;
        private String sleep_stamp;
        private int user_id;
        private String start;
        private String end;
        private String wakeup_stamp;
        private int plan_duration;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getActual_duration() {
            return actual_duration;
        }

        public void setActual_duration(int actual_duration) {
            this.actual_duration = actual_duration;
        }

        public String getSleep_stamp() {
            return sleep_stamp;
        }

        public void setSleep_stamp(String sleep_stamp) {
            this.sleep_stamp = sleep_stamp;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getWakeup_stamp() {
            return wakeup_stamp;
        }

        public void setWakeup_stamp(String wakeup_stamp) {
            this.wakeup_stamp = wakeup_stamp;
        }

        public int getPlan_duration() {
            return plan_duration;
        }

        public void setPlan_duration(int plan_duration) {
            this.plan_duration = plan_duration;
        }
    }
}
