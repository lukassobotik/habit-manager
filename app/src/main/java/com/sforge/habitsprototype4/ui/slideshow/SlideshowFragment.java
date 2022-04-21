package com.sforge.habitsprototype4.ui.slideshow;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sforge.habitsprototype4.Main2Activity;
import com.sforge.habitsprototype4.MyDatabaseHelper2;
import com.sforge.habitsprototype4.R;
import com.sforge.habitsprototype4.databinding.FragmentSlideshowBinding;
import com.sforge.habitsprototype4.entity.CalendarLibrary;
import com.sforge.habitsprototype4.entity.CalendarStatus;
import com.sforge.habitsprototype4.entity.ConnectedDays;
import com.sforge.habitsprototype4.statistics.MainStatistics;
import com.taosif7.android.ringchartlib.RingChart;
import com.taosif7.android.ringchartlib.models.RingChartData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import im.dacer.androidcharts.LineView;
import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    View root;

    ConnectedDays connectedDays;
    TextView completedDays, failedDays, longestStreak;
    int failDays, doneDays, longStreak;
    int failDaysPercentage, doneDaysPercentage;
    RingChart ringChart;

    List<CalendarLibrary> calendarEntities = new ArrayList<>();
    ArrayList<String> db_calendar_id, db_calendar_date, db_calendar_status;
    MyDatabaseHelper2 myDB2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        completedDays = binding.statsDoneDays;
        failedDays = binding.statsFailedDays;
        longestStreak = binding.longestStreakDays;

        ringChart = binding.ringChart;

        calendarDatabaseHelper();
        sendDataForStatistics();
        setPieView();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void calendarDatabaseHelper() {
        myDB2 = new MyDatabaseHelper2(getContext());
        db_calendar_id = new ArrayList<>();
        db_calendar_date = new ArrayList<>();
        db_calendar_status = new ArrayList<>();

        storeCalendarDataInArrays();
    }

    public void storeCalendarDataInArrays() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss O yyyy");

        Cursor cursor = myDB2.readAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                calendarEntities.add(new CalendarLibrary(
                        cursor.getInt(0),
                        LocalDate.parse(cursor.getString(1), formatter),
                        CalendarStatus.valueOf(cursor.getString(2))));

                db_calendar_id.add(cursor.getString(0));
                db_calendar_date.add(cursor.getString(1));
                db_calendar_status.add(cursor.getString(2));
            }
        }
    }

    public void sendDataForStatistics(){
        MainStatistics statistics = new MainStatistics();
        failDays = statistics.countFailedDays(calendarEntities);
        doneDays = statistics.countCompletedDays(calendarEntities);
        connectedDays = statistics.connectCompletedDays(calendarEntities);
        longStreak = countLongestStreak(connectedDays);

        StringBuilder sb = new StringBuilder();
        sb.append(failDays);
        sb.append(" Days");
        failedDays.setText(sb);
        sb.setLength(0);

        sb.append(doneDays);
        sb.append(" Days");
        completedDays.setText(sb);
        sb.setLength(0);

        sb.append(longStreak);
        sb.append(" Days");
        longestStreak.setText(sb);
        sb.setLength(0);
    }
    public int countLongestStreak(ConnectedDays connectedDays){
        ArrayList<Integer> streaks = (ArrayList<Integer>) connectedDays.getStreak();
        return Collections.max(streaks);
    }

    public void setPieView(){
        if(doneDays != 0 || failDays != 0){
            int total = failDays + doneDays;
            doneDaysPercentage = (doneDays * 100) / total;
            failDaysPercentage = 100 - doneDaysPercentage;
            float donePercentageFloat = doneDaysPercentage;
            float failedPercentageFloat = failDaysPercentage;
            donePercentageFloat = donePercentageFloat / 100;
            failedPercentageFloat = failedPercentageFloat / 100;

            ringChart.setLayoutMode(RingChart.renderMode.MODE_CONCENTRIC);
            ringChart.showLabels(true);
            RingChartData doneData = new RingChartData(donePercentageFloat, ContextCompat.getColor(requireActivity(), R.color.calendar_done), doneDaysPercentage + "%");
            RingChartData failedData = new RingChartData(failedPercentageFloat, ContextCompat.getColor(requireActivity(), R.color.calendar_failed), failDaysPercentage + "%");
            ArrayList<RingChartData> data_list = new ArrayList<RingChartData>() {{
                add(doneData);
                add(failedData);
            }};
            ringChart.setData(data_list);
        }
    }
}