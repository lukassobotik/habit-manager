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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sforge.habitsprototype4.MyDatabaseHelper2;
import com.sforge.habitsprototype4.R;
import com.sforge.habitsprototype4.databinding.FragmentSlideshowBinding;
import com.sforge.habitsprototype4.statistics.MainStatistics;

import java.util.ArrayList;

import im.dacer.androidcharts.LineView;
import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    View root;

    TextView completedDays, failedDays;
    int failDays, doneDays;
    int failDaysPercentage, doneDaysPercentage;
    PieView pieView;

    ArrayList<String> db_calendar_id, db_calendar_date, db_calendar_status;
    MyDatabaseHelper2 myDB2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        completedDays = binding.statsDoneDays;
        failedDays = binding.statsFailedDays;

        pieView = binding.pieView;

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
        Cursor cursor = myDB2.readAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                db_calendar_id.add(cursor.getString(0));
                db_calendar_date.add(cursor.getString(1));
                db_calendar_status.add(cursor.getString(2));
            }
        }
    }

    public void sendDataForStatistics(){
        MainStatistics statistics = new MainStatistics();
        failDays = statistics.countFailedDays(db_calendar_id, db_calendar_date, db_calendar_status);
        doneDays = statistics.countCompletedDays(db_calendar_id, db_calendar_date, db_calendar_status);

        failedDays.setText(String.valueOf(failDays));
        completedDays.setText(String.valueOf(doneDays));
    }

    public void setPieView(){
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();
        int total = failDays + doneDays;

        if(doneDays == 0 || failDays == 0)
            pieHelperArrayList.add(new PieHelper(0, Color.alpha(1)));
        else{
            doneDaysPercentage = (doneDays * 100) / total;
            failDaysPercentage = 100 - doneDaysPercentage;
            pieHelperArrayList.add(new PieHelper(doneDaysPercentage, Color.parseColor("#0FC547")));
            pieHelperArrayList.add(new PieHelper(failDaysPercentage, Color.parseColor("#FF4519")));
        }
        Log.d("percentage", String.valueOf(doneDaysPercentage));
        Log.d("percentage", String.valueOf(failDaysPercentage));

        pieView.setDate(pieHelperArrayList);
        pieView.selectedPie(8);
        pieView.showPercentLabel(true);
    }
}