package com.hdu.team.hiwanan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hdu.team.hiwanan.R;
import com.hdu.team.hiwanan.model.HiCalendar;

import java.util.List;

/**
 * Created by JerryYin on 9/27/16.
 * RecyclerView Adapter
 * Calendar item adapter
 */

public class CusRecyclerViewAdapter extends RecyclerView.Adapter<CusRecyclerViewAdapter.CusViewHolder> {

    private Context mContext;
    private List<HiCalendar> mCalendars;


    public CusRecyclerViewAdapter(Context mContext, List<HiCalendar> mCalendars) {
        this.mContext = mContext;
        this.mCalendars = mCalendars;
    }

    public class CusViewHolder extends RecyclerView.ViewHolder{

        private TextView month_odl;           //月份
        private TextView month_zodiac;    //农历月份
        private TextView year_old;        //农历年份
        private TextView year_zodiac;     //生肖
        private TextView day_zodiac;      //日


        private TextView year;            //新历年份
        private TextView month;            //新历月份
        private TextView weekend;            //新历星期
        private TextView day;             //日  (大字)
        private TextView day1;             //日

        private TextView do_something;    //宜做事
        private TextView words;           //名言
        private TextView author;          //作者

        public CusViewHolder(View itemView) {
            super(itemView);
            month_odl = (TextView) itemView.findViewById(R.id.text_month_old);
            month_zodiac = (TextView) itemView.findViewById(R.id.text_month_zodiac);
            year_old = (TextView) itemView.findViewById(R.id.text_year_old);
            year_zodiac = (TextView) itemView.findViewById(R.id.text_year_zodiac);
            day = (TextView) itemView.findViewById(R.id.text_day);
            day_zodiac = (TextView) itemView.findViewById(R.id.text_day_zodiac);
            year = (TextView) itemView.findViewById(R.id.text_year);
            month = (TextView) itemView.findViewById(R.id.text_month);
            day1 = (TextView) itemView.findViewById(R.id.text_day1);
            weekend = (TextView) itemView.findViewById(R.id.text_weekend);
            do_something = (TextView) itemView.findViewById(R.id.text_suit);
            words = (TextView) itemView.findViewById(R.id.text_words);
            author = (TextView) itemView.findViewById(R.id.text_author);
        }
    }


    @Override
    public CusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_calendar, parent, false);
        CusViewHolder holder = new CusViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CusViewHolder holder, int position) {
        HiCalendar calendar = mCalendars.get(position);
        if (calendar == null)
            return;
        holder.month_odl.setText(calendar.getMonth());
        holder.month_zodiac.setText(calendar.getMonth_zodiac());
        holder.year_old.setText(calendar.getYear_old());
        holder.year_zodiac.setText(calendar.getYear_zodiac());
        holder.day.setText(calendar.getWeekend());
        holder.day_zodiac.setText(calendar.getDay_zodiac());
        holder.year.setText(calendar.getYear());
        holder.month.setText(calendar.getMonth());
        holder.day1.setText(calendar.getDay());
        holder.weekend.setText(calendar.getWeekend());
        holder.do_something.setText(calendar.getSuit());
        holder.words.setText(calendar.getWords());
        holder.author.setText(calendar.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mCalendars.size();
    }


}
