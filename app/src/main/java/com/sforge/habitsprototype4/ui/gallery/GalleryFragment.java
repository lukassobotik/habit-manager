package com.sforge.habitsprototype4.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sforge.habitsprototype4.EventDecorator;
import com.sforge.habitsprototype4.MyDatabaseHelper2;
import com.sforge.habitsprototype4.R;
import com.sforge.habitsprototype4.SelectionEventDecorator;
import com.sforge.habitsprototype4.databinding.FragmentGalleryBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    MaterialCalendarView mcv;
    Button done, fail, skip;
    View root;
    MyDatabaseHelper2 myDB2;
    ArrayList<String> db_calendar_id, db_calendar_date, db_calendar_status;
    private SelectionEventDecorator selectionEventDecorator, invalidateSelectionEventDecorator, previousSelectionEventDecorator;
    private String theme = "black";
    private String fdof = "monday";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        mcv = (MaterialCalendarView) binding.monthCalendarView;
        mcv.state().edit().setShowWeekDays(true).commit();

        if(getArguments() != null){
            fdof = getArguments().getString("fdof");
        }

        calendarDatabaseHelper();
        addCalendarEvents();
        loadCalendarDays();
        loadCalendarPreferences();

       // final TextView textView = binding.textGallery;
       // galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void loadCalendarPreferences() {
        if (fdof.equals("monday"))
            mcv.state().edit().setFirstDayOfWeek(Calendar.MONDAY).commit();
        else if (fdof.equals("sunday"))
            mcv.state().edit().setFirstDayOfWeek(Calendar.SUNDAY).commit();
    }

    public MaterialCalendarView returnCalendarInfo(){
        return mcv;
    }

    public View returnRoot(){
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("bug_fixing", "Gallery: " +  String.valueOf(context));
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

    private void addCalendarEvents() {

        final String NONE = "none";
        final String DONE = "done";
        final String FAIL = "fail";
        final String SKIP = "skip";

        java.util.Date todayDate = new java.util.Date();
        CalendarDay calendarDayTodayDate = CalendarDay.from(todayDate);
        final Collection<CalendarDay>[] dateDays = new Collection[]{Collections.singleton(calendarDayTodayDate)};

        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        mcv.setOnDateChangedListener((widget, date, selected) -> {
            String status;

            Date dateInfo = date.getDate();
            String stringDateInfo = String.valueOf(dateInfo);

            dateDays[0] = Collections.singleton(date);

            calendarDatabaseHelper();
            loadCalendarDays();

            invalidateSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.transparent), dateDays[0]);
            selectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_done), dateDays[0]);

            MyDatabaseHelper2 MyDB2 = new MyDatabaseHelper2(getContext());

            CalendarDay calendarDayDate;

            if (!db_calendar_date.contains(stringDateInfo)) {
                mcv.addDecorators(selectionEventDecorator);
                status = DONE;
                MyDB2.addHabit(String.valueOf(dateInfo).trim(), //date
                        status.trim()); //status
            } else if (db_calendar_date.contains(stringDateInfo)) {
                mcv.clearSelection();
                for (int i = 0; i < db_calendar_date.size(); i++) {
                    String edit = db_calendar_date.get(i);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzzzzzzz yyyy");
                    try {
                        Date calendar_date = formatter.parse(edit);
                        calendarDayDate = CalendarDay.from(calendar_date);
                        if (String.valueOf(calendarDayDate).equals(String.valueOf(date))) {
                            db_calendar_date.remove(date);
                            String delete_row_id = db_calendar_id.get(i);
                            myDB2.deleteOneRow(delete_row_id);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            mcv.addDecorator(invalidateSelectionEventDecorator);
        });
    }

    private void loadCalendarDays() {
        for (int i = 0; i < db_calendar_date.size(); i++) {
            String edit = db_calendar_date.get(i);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss zzzzzzzzz yyyy");
            try {
                Date loadDate = formatter.parse(edit);
                CalendarDay loadDates = CalendarDay.from(loadDate);
                Collection<CalendarDay> previousDates = Collections.singleton(loadDates);
                previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_done), previousDates);
                mcv.addDecorators(previousSelectionEventDecorator);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}