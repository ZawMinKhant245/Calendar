package com.example.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calendar.databinding.ActivityMainBinding;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import mmcalendar.CalendarType;
import mmcalendar.Config;
import mmcalendar.Language;
import mmcalendar.MyanmarDate;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private LocalDate today;
    private  int monthvalue;
    private  static final String[] MONTHS={"JAN","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        inUi();
        iniListner();

    }

    private void iniListner() {
        binding.btPrevious.setOnClickListener(v -> previousMonth());
        binding.btNext.setOnClickListener(v -> nextMonth());
    }

    @SuppressLint("NewApi")
    private void nextMonth() {
        today=today.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        updateCalenderHeader();
        updateCalenderBody();
    }

    @SuppressLint("NewApi")
    private void previousMonth() {
        today=today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        updateCalenderHeader();
        updateCalenderBody();
    }

    @SuppressLint("NewApi")
    private void inUi() {
        Config.initDefault(
                new Config.Builder()
                        .setCalendarType(CalendarType.ENGLISH)
                        .setLanguage(Language.MYANMAR)
                        .build());

        today=LocalDate.now();
        binding.tvMiddle.setText(today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        updateCalenderHeader();
        updateCalenderBody();
    }
    @SuppressLint("NewApi")
    private void updateCalenderBody() {
         LocalDate startDay=today.with(TemporalAdjusters.firstDayOfMonth());
         int startWeekDay=startDay.getDayOfWeek().getValue() ==7 ? 0 : startDay.getDayOfWeek().getValue();

         int day;
         for(day=0;day<startWeekDay;day++){
             binding.glCalender.getChildAt(day).setVisibility(View.INVISIBLE);
         }

         while (startDay.getMonthValue()==monthvalue){
             int index=day;
             if(index>=35){
                 index=day - 35;
             }
             TextView dayOfWeek=(TextView) binding.glCalender.getChildAt(index);
             if(dayOfWeek.getVisibility()==View.INVISIBLE) dayOfWeek.setVisibility(View.VISIBLE);
             dayOfWeek.setText(String.valueOf(startDay.getDayOfMonth()));

             if (startDay.isEqual(LocalDate.now())){
                dayOfWeek.setBackgroundResource(R.drawable.bg_date_change);
             }else if(startDay.isEqual(today)){
                 dayOfWeek.setBackgroundResource(R.drawable.bg_other_date);
             } else {
                 dayOfWeek.setBackgroundResource(R.drawable.bg_text_defult);
             }

             dayOfWeek.setTag(startDay);
             startDay = startDay.plusDays(1);
             day++;
         }
        while (day < 35){
            binding.glCalender.getChildAt(day).setVisibility(View.INVISIBLE);
            day++;
        }
    }

    @SuppressLint("NewApi")
    private void updateCalenderHeader() {
        monthvalue=today.getMonthValue();
        binding.tvFirst.setText(String.valueOf(today.getYear()));
        binding.tvSecond.setText(MONTHS[monthvalue-1]);
        binding.tvToday.setText(String.valueOf(today.getDayOfWeek()));
        binding.tvTodayDate.setText(String.valueOf(today.getDayOfMonth()));


        MyanmarDate myanmarDate=MyanmarDate.of(today);
        binding.tvDay.setText(myanmarDate.getMoonPhase()+" "+myanmarDate.getFortnightDay()+"ရက်");
        binding.tvUnderDay.setText(myanmarDate.getWeekDay()+"နေ့");
        binding.tvDescription.setText(myanmarDate.format("S (s) k, B (y) k, M p (f) r, En"));
    }


    @SuppressLint("NewApi")
    public void onDayClicked(View view) {
        TextView tv=(TextView) view;
        today=(LocalDate) tv.getTag();
        updateCalenderHeader();
        updateCalenderBody();

    }
}