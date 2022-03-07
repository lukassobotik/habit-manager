package com.sforge.habitsprototype4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sforge.habitsprototype4.databinding.ActivityMain2Binding;
import com.sforge.habitsprototype4.ui.gallery.GalleryFragment;
import com.sforge.habitsprototype4.ui.settings.SettingActivity;
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
import java.util.Timer;
import java.util.TimerTask;

import maes.tech.intentanim.CustomIntent;

public class Main2Activity extends AppCompatActivity {
    private UserSettings settings;

    private SelectionEventDecorator selectionEventDecorator;
    private SelectionEventDecorator invalidateSelectionEventDecorator;
    private AppBarConfiguration mAppBarConfiguration;

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MenuItem menu_settings;
    ConstraintLayout habit_item;
    ConstraintLayout home_constraint;

    MyDatabaseHelper myDB;
    MyDatabaseHelper2 myDB2;
    ArrayList<String> db_id, db_name, db_tag, db_repeat;
    ArrayList<String> db_calendar_id, db_calendar_date, db_calendar_status;
    ArrayList<String> currentHabitIds, currentHabitNames, currentHabitTags, currentHabitRepeat;
    CustomAdapter customAdapter;

    String theme = "black";
    String fdof = "monday";
    CardView cardView;

    MaterialCalendarView mcv;
    final boolean[] weekMode = {false};
    final boolean[] enableMonthMode = {false};
    final boolean[] showAllHabits = {false};
    boolean disabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActivity();

        loadSharedPreferences();
        updateTheme();

        loadCalendarPreferences();
        addCalendarEvents();
        calendarDatabaseHelper();
        loadCalendarDays();

        findIDs();
        databaseHelper();
        getCurrentDay();
    }

    public void createActivityViews() {
        com.sforge.habitsprototype4.databinding.ActivityMain2Binding binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain2.toolbar);
        binding.appBarMain2.fab.setOnClickListener(view -> {
            Intent intent = new Intent(Main2Activity.this, AddActivity.class);
            startActivity(intent);
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_logout, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        onFragmentEnter(navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    protected void onFragmentEnter(NavController navController) {

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            Log.d("navDestination", String.valueOf(navDestination));
            if (!disabled) {
                if (String.valueOf(navDestination).equals("Destination(com.sforge.habitsprototype4:id/nav_home) label=Home class=com.sforge.habitsprototype4.ui.home.HomeFragment")) {
                    activityReload();
                }
                if (String.valueOf(navDestination).equals("Destination(com.sforge.habitsprototype4:id/nav_gallery) label=Calendar class=com.sforge.habitsprototype4.ui.gallery.GalleryFragment")) {
                    findViewById(R.id.fab).setVisibility(View.GONE);
                }
                else{
                    findViewById(R.id.fab).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void createActivity() {
        disabled = true;
        createActivityViews();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                disabled = false;
            }
        }, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mcv = findViewById(R.id.calendarView);
        mcv.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).setShowWeekDays(true).commit();
        mcv.setTopbarVisible(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            activityReload();
        }
    }

    public void databaseHelper() {
        myDB = new MyDatabaseHelper(Main2Activity.this);
        db_id = new ArrayList<>();
        db_name = new ArrayList<>();
        db_tag = new ArrayList<>();
        db_repeat = new ArrayList<>();

        storeHabitDataInArrays();

        customAdapter = new CustomAdapter(Main2Activity.this, Main2Activity.this, db_id, db_name, db_tag, db_repeat);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Main2Activity.this));
    }

    @SuppressWarnings("all")
    public void storeHabitDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                db_id.add(cursor.getString(0));
                db_name.add(cursor.getString(1));
                db_tag.add(cursor.getString(2));
                db_repeat.add(cursor.getString(3));
            }
        }
    }

    public void findIDs() {
        recyclerView = findViewById(R.id.recycler_view);
        add_button = findViewById(R.id.fab);
        menu_settings = findViewById(R.id.action_settings);
        habit_item = findViewById(R.id.habit_item);
        home_constraint = findViewById(R.id.home_constraint);
        cardView = findViewById(R.id.habitCardView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mcv = findViewById(R.id.calendarView);
        View view = findViewById(R.id.fab);
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(Main2Activity.this, SettingActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_reload) {
            activityReload();
        }
        if (id == R.id.action_show_month_view){
            if (enableMonthMode[0]) {
                enableMonthMode[0] = false;
                mcv.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).setShowWeekDays(true).commit();
                mcv.setTopbarVisible(false);
            } else {
                enableMonthMode[0] = true;
                mcv.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).setShowWeekDays(true).commit();
                mcv.setTopbarVisible(true);
                Snackbar.make(view, "Showing Month Calendar", Snackbar.LENGTH_LONG).setAction("Showing Month Calendar", null).show();
            }

        }
        if (id == R.id.action_show_all_habits){
            if (showAllHabits[0]) {
                mcv.setVisibility(View.VISIBLE);
                showAllHabits[0] = false;
                getCurrentDay();
                Snackbar.make(view, "Showing Today's Habits", Snackbar.LENGTH_LONG).setAction("Showing Today's Habits", null).show();
            } else {
                mcv.setVisibility(View.GONE);
                showAllHabits[0] = true;
                databaseHelper();
                mcv.clearSelection();
                Snackbar.make(view, "Showing All Habits", Snackbar.LENGTH_LONG).setAction("Showing All Habits", null).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void activityReload() {
        Intent intent = new Intent(Main2Activity.this, Main2Activity.class);
        startActivity(intent);
        CustomIntent.customType(Main2Activity.this, "fadein-to-fadeout");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activityReload();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void loadSharedPreferences() {
        settings = (UserSettings) getApplication();
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String pref = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);

        String[] itemsSettings = pref.split(",");

        List<String> items = new ArrayList<>(Arrays.asList(itemsSettings));

        theme = items.get(0);

        settings.setCustomTheme(theme);

        if (items.size() > 1)
            fdof = items.get(1);
        settings.setFDOF_Setting(fdof);

        Bundle bundle = new Bundle();
        bundle.putString("fdof", fdof);

        Fragment galleryFragment = new GalleryFragment();
        galleryFragment.setArguments(bundle);
    }

    @SuppressLint({"UseCompatLoadingForDrawables"})
    public void updateTheme() {
        mcv = findViewById(R.id.calendarView);

        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME)) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            mcv.setHeaderTextAppearance(R.color.white);
            mcv.setWeekDayTextAppearance(R.color.white);
            mcv.setArrowColor(R.color.white);
            mcv.setDateTextAppearance(R.color.white);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mcv.setHeaderTextAppearance(R.color.black);
            mcv.setWeekDayTextAppearance(R.color.black);
            mcv.setArrowColor(R.color.black);
            mcv.setDateTextAppearance(R.color.black);
        }
    }

    public void loadCalendarPreferences() {
        mcv = findViewById(R.id.calendarView);
        if (fdof.equals("monday"))
            mcv.state().edit().setFirstDayOfWeek(Calendar.MONDAY).commit();
        else if (fdof.equals("sunday"))
            mcv.state().edit().setFirstDayOfWeek(Calendar.SUNDAY).commit();
    }

    public void calendarDatabaseHelper() {
        myDB2 = new MyDatabaseHelper2(Main2Activity.this);
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

    public void showSelectedDaysHabit(int day){
        currentHabitIds = new ArrayList<>();
        currentHabitNames = new ArrayList<>();
        currentHabitTags = new ArrayList<>();
        currentHabitRepeat = new ArrayList<>();

        String weekDay = "";
        switch(day){
            case 0:
                weekDay = "SU";
                break;
            case 1:
                weekDay = "MO";
                break;
            case 2:
                weekDay = "TU";
                break;
            case 3:
                weekDay = "WE";
                break;
            case 4:
                weekDay = "TH";
                break;
            case 5:
                weekDay = "FR";
                break;
            case 6:
                weekDay = "SA";
                break;
        }

        for (int i = 0; i < db_repeat.size(); i++){
            String s = db_repeat.get(i);
            if (s.contains(weekDay)){
                currentHabitIds.add(db_id.get(i));
                currentHabitNames.add(db_name.get(i));
                currentHabitTags.add(db_tag.get(i));
                currentHabitRepeat.add(s);
            }
        }

        customAdapter = new CustomAdapter(Main2Activity.this, Main2Activity.this, currentHabitIds, currentHabitNames, currentHabitTags, currentHabitRepeat);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Main2Activity.this));
    }
    @SuppressWarnings("deprecation")
    public void getCurrentDay(){
        mcv = findViewById(R.id.calendarView);
        java.util.Date todayDate = new java.util.Date();
        todayDate.setHours(0);
        todayDate.setMinutes(0);
        todayDate.setSeconds(0);

        CalendarDay calendarDay = CalendarDay.from(todayDate);
        mcv.setSelectedDate(calendarDay);

        int weekDay = todayDate.getDay();
        showSelectedDaysHabit(weekDay);
    }

    @SuppressWarnings("all")
    private void addCalendarEvents() {
        mcv = findViewById(R.id.calendarView);
        final String NONE = "none";
        final String DONE = "done";
        final String FAIL = "fail";
        final String SKIP = "skip";

        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                java.util.Date selDate = date.getDate();
                int weekDay = selDate.getDay();
                showSelectedDaysHabit(weekDay);
            }
        });

        mcv.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weekMode[0] == true) {
                    weekMode[0] = false;
                    mcv.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).setShowWeekDays(true).commit();
                } else if (weekMode[0] == false) {
                    weekMode[0] = true;
                    mcv.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).setShowWeekDays(true).commit();
                }
            }
        });
    }
    @SuppressWarnings("deprecation")
    private void loadCalendarDays() {
        mcv = findViewById(R.id.calendarView);
        for (int i = 0; i < db_calendar_date.size(); i++) {
            String edit = db_calendar_date.get(i);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss zzzzzzzzz yyyy");
            try {
                Date loadDate = formatter.parse(edit);
                CalendarDay loadDates = CalendarDay.from(loadDate);
                Collection<CalendarDay> previousDates = Collections.singleton(loadDates);
                SelectionEventDecorator previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_done), previousDates);
                mcv.addDecorators(previousSelectionEventDecorator);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}