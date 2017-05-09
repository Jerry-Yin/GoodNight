package com.hdu.team.hiwanan.util.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hdu.team.hiwanan.constant.HiRequestCodes;
import com.hdu.team.hiwanan.util.HiLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by JerryYin on 11/24/15.
 * 时间
 */
public class HiTimesUtil {


    /**
     * 获取当前日期＋时间
     *
     * @return
     */
    public static String getCurDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    public static String getCurDateTimeNoSpace() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 自动补0
     *
     * @return
     */
    public static String getCurDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 月份日期前面不带0
     *
     * @return
     */
    public static String getCurDateNoZero() {
        SimpleDateFormat formatter = new SimpleDateFormat("y-M-d");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    public static String getCurTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取今天之前一周的日期（7天）
     * @return
     */
    public static List<String> getLastWeekDate(){
        List<String> lastWeek = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM.dd");
        Calendar c = Calendar.getInstance();
        //过去七天
        for (int i=-7; i<0; i++){
            c.setTime(new Date(System.currentTimeMillis()));
            c.add(Calendar.DATE, i);
            Date d = c.getTime();
            String day = format.format(d);
            lastWeek.add(day);
            c.clear();
        }
        return lastWeek;
    }

    /**
     * 获取今天之前一个月的日期（30天）
     * @return
     */
    public static List<String> getLastMonthDate(){
        List<String> lastWeek = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM.dd");
        Calendar c = Calendar.getInstance();
        //过去七天
        for (int i=30; i>0; i--){
            c.setTime(new Date(System.currentTimeMillis()));
            System.out.println("i = "+i);
            c.add(Calendar.DATE, -i);
            Date d = c.getTime();
            String day = format.format(d);
            lastWeek.add(day);
            System.out.println("day = "+day);
            c.clear();
        }
        return lastWeek;
    }

    /**
     * 。
     * 计算时差
     * 最大允许 24 小时
     *
     * @return
     */
    public static Bundle getTimeDif(int h, int m) {
        Bundle bundle = new Bundle();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = format.parse(getCurDateTime());
            Date endDate = format.parse(getCurDate() + " " + h + ":" + m + ":00");
            long diff = endDate.getTime() - curDate.getTime();      //得到的是 微妙 级别的差值

            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
//            long s=(diff/1000-days*24*60*60-hours*60*60-minutes*60);
//            long mi = minutes + 1;  //少了一分钟
            /**
             * 时间差为负值的情况
             * 最大为24小时
             */
            if (hours < 0 || minutes < 0) {
                minutes = 59 + minutes;
                hours = 24 + hours - 1;
            }
            bundle.putLong("days", days);
            bundle.putLong("hours", hours);
            bundle.putLong("minutes", minutes);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    /**
     * 倒计时工具
     *
     * @param time
     * @param handler
     */
    public static void startCountDown(final float time, final Handler handler, final int position) {
        if (time > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i <= time + 1; i++) {
                            final Message message = new Message();
                            message.what = HiRequestCodes.COUNT_DOWN;
                            message.arg1 = position;
                            message.obj = time - i;
                            handler.sendMessage(message);
                            Thread.sleep(1000);

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }).start();

        }
    }

    /**
     * 判断是不是刚刚 ，间隔10m 以内算是刚刚
     *
     * @param now  [hh, mm]
     * @param time [hh, mm, ss]
     * @return
     */
    public static boolean isjustNow(String[] now, String[] time) {
        if (!now[0].equals(time[0])) {
            return false;
        } else {
            int n = Integer.parseInt(now[1]);
            int t = Integer.parseInt(time[1]);
            if ((n > t && n - t <= 10) || (n < t && t - n <= 10))
                return true;
            else {
                return false;
            }
        }
    }

    /**
     * number --> week
     *
     * @param day(1--7)
     * @return
     */
    public static String getCurWeek(int day) {
        String week = "";
        if (day < 1 || day > 7)
            return "0";
        switch (day) {
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
            case 7:
                week = "星期天";
                break;

        }
        return week;
    }
}
