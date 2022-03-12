package com.sforge.habitsprototype4.ui.gallery;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.sforge.habitsprototype4.MyDatabaseHelper2;
import com.sforge.habitsprototype4.R;
import com.sforge.habitsprototype4.SelectionEventDecorator;
import com.sforge.habitsprototype4.databinding.FragmentGalleryBinding;
import com.sforge.habitsprototype4.statistics.MainStatistics;
import com.sforge.habitsprototype4.ui.settings.UserSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GalleryFragment extends Fragment {
    Context mContext;
    private FragmentGalleryBinding binding;
    MaterialCalendarView mcv;
    View root;
    MyDatabaseHelper2 myDB2;
    ArrayList<String> db_calendar_id, db_calendar_date, db_calendar_status;
    private SelectionEventDecorator selectionEventDecorator;
    private SelectionEventDecorator invalidateSelectionEventDecorator;
    private String fdof = "monday";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        mcv = (MaterialCalendarView) binding.monthCalendarView;
        mcv.state().edit().setShowWeekDays(true).commit();

        UserSettings settings = (UserSettings) this.requireActivity().getApplication();
        SharedPreferences sharedPreferences = this.requireActivity().getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String pref = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);

        String[] itemsSettings = pref.split(",");

        List<String> items = new ArrayList<>(Arrays.asList(itemsSettings));

        if (items.size() > 1)
            fdof = items.get(1);

        calendarDatabaseHelper();
        addCalendarEvents();
        loadCalendarDays();
        loadCalendarPreferences();

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
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
    @SuppressWarnings("all")
    private void addCalendarEvents() {
        final String DONE = "done";
        final String FAIL = "fail";
        final String SKIP = "skip";

        java.util.Date todayDate = new java.util.Date();
        todayDate.setHours(0);
        todayDate.setMinutes(0);
        todayDate.setSeconds(0);
        CalendarDay calendarDayTodayDate = CalendarDay.from(todayDate);
        final Collection<CalendarDay>[] dateDays = new Collection[]{Collections.singleton(calendarDayTodayDate)};

        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        mcv.setOnDateChangedListener((widget, date, selected) -> {
            mcv.setSelectionColor(Color.parseColor("#059C00"));
            String status;

            Date dateInfo = date.getDate();
            String stringDateInfo = String.valueOf(dateInfo);

            dateDays[0] = Collections.singleton(date);

            calendarDatabaseHelper();
            //loadCalendarDays();

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
                mcv.addDecorators(invalidateSelectionEventDecorator);
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
            sendDataForStatistics();
        });

        mcv.setOnDateLongClickListener((materialCalendarView, date) -> {
            mcv.setSelectionColor(Color.parseColor("#E81300"));
            mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
            String status;

            Date dateInfo = date.getDate();
            String stringDateInfo = String.valueOf(dateInfo);

            dateDays[0] = Collections.singleton(date);

            calendarDatabaseHelper();
            //loadCalendarDays();

            invalidateSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.transparent), dateDays[0]);
            selectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_failed), dateDays[0]);

            MyDatabaseHelper2 MyDB2 = new MyDatabaseHelper2(getContext());

            CalendarDay calendarDayDate;

            if (!db_calendar_date.contains(stringDateInfo)) {
                mcv.addDecorators(selectionEventDecorator);
                mcv.setSelectedDate(date);
                status = FAIL;
                MyDB2.addHabit(stringDateInfo.trim(), //date
                               status.trim()); //status

            } else if (db_calendar_date.contains(stringDateInfo)) {
                mcv.clearSelection();
                mcv.addDecorators(invalidateSelectionEventDecorator);
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
            sendDataForStatistics();
        });
    }

    @SuppressWarnings("deprecation")
    private void loadCalendarDays() {
        for (int i = 0; i < db_calendar_date.size(); i++) {
            String edit = db_calendar_date.get(i);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss zzzzzzzzz yyyy");
            try {
                Date loadDate = formatter.parse(edit);
                CalendarDay loadDates = CalendarDay.from(loadDate);
                Collection<CalendarDay> previousDates = Collections.singleton(loadDates);
                SelectionEventDecorator previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_done), previousDates);
                if(db_calendar_status.get(i).equals("done"))
                    previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_done), previousDates);
                else if(db_calendar_status.get(i).equals("fail"))
                    previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_failed), previousDates);
                mcv.addDecorators(previousSelectionEventDecorator);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendDataForStatistics(){
        MainStatistics statistics = new MainStatistics();
        int failedDays = statistics.countFailedDays(db_calendar_id, db_calendar_date, db_calendar_status);
        Log.d("statistics", "Failed: " + failedDays);
        int completedDays = statistics.countCompletedDays(db_calendar_id, db_calendar_date, db_calendar_status);
        Log.d("statistics", "Done: " + completedDays);
    }
}