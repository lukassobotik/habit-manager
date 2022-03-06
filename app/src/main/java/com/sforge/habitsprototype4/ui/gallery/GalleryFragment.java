package com.sforge.habitsprototype4.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sforge.habitsprototype4.CustomAdapter;
import com.sforge.habitsprototype4.EventDecorator;
import com.sforge.habitsprototype4.Main2Activity;
import com.sforge.habitsprototype4.MyDatabaseHelper2;
import com.sforge.habitsprototype4.R;
import com.sforge.habitsprototype4.SelectionEventDecorator;
import com.sforge.habitsprototype4.databinding.FragmentGalleryBinding;
import com.sforge.habitsprototype4.ui.settings.UserSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    MaterialCalendarView mcv;
    View root;
    final boolean[] weekMode = {false};
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

        ArrayList<CalendarDay> selectedDays = new ArrayList<>();
        ArrayList<CalendarDay> loadedDays = new ArrayList<>();
        ArrayList<CalendarDay> calDay_calendar_date = new ArrayList<>();
        ArrayList<CalendarDay> invalidateDays = new ArrayList<>();
        ArrayList<String> calendar_data;

        java.util.Date todayDate = new java.util.Date();
        CalendarDay calendarDayTodayDate = CalendarDay.from(todayDate);
        final Collection<CalendarDay>[] dateDays = new Collection[]{Collections.singleton(calendarDayTodayDate)};

        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String status;
                String selectedDay = String.valueOf(date);

                java.util.Date todayDate = new java.util.Date();
                todayDate.setHours(0);
                todayDate.setMinutes(0);
                todayDate.setSeconds(0);
                Log.d("decorator", "Today Date: " + String.valueOf(todayDate));
                Collection<EventDecorator> eventDecorators;
                Collection<CalendarDay> calendarDays = Collections.singleton(date);
                java.util.Date dateInfo = date.getDate();
                String stringDateInfo = String.valueOf(dateInfo);

                dateDays[0] = Collections.singleton(date);

                Log.d("decorator", "dateDays: " + String.valueOf(dateDays[0]));

                for (int i = 0; i < db_calendar_date.size(); i++) {
                    String edit = db_calendar_date.get(i);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzzzzzzz yyyy");
                    try {
                        Date selDate = formatter.parse(edit);
                        CalendarDay calSelDay = CalendarDay.from(selDate);
                        calDay_calendar_date.add(calSelDay);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                calendarDatabaseHelper();
                loadCalendarDays();

                selectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.color_primary), dateDays[0]);
                invalidateSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.transparent), dateDays[0]);

                MyDatabaseHelper2 MyDB2 = new MyDatabaseHelper2(getContext());

                CalendarDay calendarDayDate;

                Log.d("decorator", String.valueOf(db_calendar_date));

                if (!db_calendar_date.contains(stringDateInfo)) {
                    Log.d("decorator", "false");
                    mcv.addDecorators(selectionEventDecorator);
                    status = DONE;
                    Log.d("decorator", "Status: " + status.toString());
                    MyDB2.addHabit(String.valueOf(dateInfo).trim(), //date
                            status.toString().trim()); //status
                } else if (db_calendar_date.contains(stringDateInfo)) {
                    mcv.clearSelection();
                    Log.d("calendar_fixes", String.valueOf(db_calendar_date));

                    for (int i = 0; i < db_calendar_date.size(); i++) {
                        String edit = db_calendar_date.get(i);
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzzzzzzz yyyy");
                        try {
                            Date calendar_date = formatter.parse(edit);
                            calendarDayDate = CalendarDay.from(calendar_date);
                            if (String.valueOf(calendarDayDate).equals(String.valueOf(date))) {
                                Log.d("decorator", "remove: " + edit);
                                db_calendar_date.remove(date);
                                String delete_row_id = db_calendar_id.get(i);
                                myDB2.deleteOneRow(delete_row_id);
                                Log.d("decorator", "remove: " + String.valueOf(delete_row_id));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mcv.addDecorator(invalidateSelectionEventDecorator);
            }
        });

        mcv.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("decorator", String.valueOf(weekMode[0]));
                if (weekMode[0] == true) {
                    weekMode[0] = false;
                    mcv.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).setShowWeekDays(true).isCacheCalendarPositionEnabled(true).commit();
                } else if (weekMode[0] == false) {
                    weekMode[0] = true;
                    mcv.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).setShowWeekDays(true).isCacheCalendarPositionEnabled(true).commit();
                }
            }
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
                previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.color_primary), previousDates);
                mcv.addDecorators(previousSelectionEventDecorator);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}