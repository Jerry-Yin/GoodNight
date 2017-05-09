package com.hdu.team.hiwanan.model;

/**
 * Created by JerryYin on 5/8/17.
 */

public class HiReqSleepTime {


    /**
     * action : query
     * params : {"userId":2,"duration":10}
     */

    private String action;
    private ParamsBean params;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * userId : 2
         * duration : 10
         */

        private int userId;
        private int duration;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}
