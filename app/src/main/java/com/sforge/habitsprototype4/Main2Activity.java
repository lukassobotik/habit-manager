package com.sforge.habitsprototype4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;
import com.prolificinteractive.materialcalendarview.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sforge.habitsprototype4.databinding.ActivityMain2Binding;
import com.sforge.habitsprototype4.ui.settings.SettingActivity;
import com.sforge.habitsprototype4.ui.settings.UserSettings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity {
    private UserSettings settings;
    private EventDecorator eventDecorator, previousEventDecorator;
    private SelectionEventDecorator selectionEventDecorator, invalidateSelectionEventDecorator, previousSelectionEventDecorator;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MenuItem menu_settings;
    ConstraintLayout habit_item;
    ConstraintLayout home_constraint;

    MyDatabaseHelper myDB;
    MyDatabaseHelper2 myDB2;
    ArrayList<String> db_id, db_name, db_tag, db_repeat;
    ArrayList<String> db_calendar_id, db_calendar_date, db_calendar_status;
    CustomAdapter customAdapter;

    String theme = "black";
    String fdof;

    final boolean[] weekMode = {false};
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

    }

    public void createActivityViews(){
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain2.toolbar);
        binding.appBarMain2.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, AddActivity.class);
                startActivity(intent);
            }
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
    protected void onFragmentEnter(NavController navController){

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(!disabled) {
                    if (String.valueOf(navDestination).equals("Destination(com.sforge.habitsprototype4:id/nav_home) label=Home class=com.sforge.habitsprototype4.ui.home.HomeFragment")) {
                        activityReload();
                    }
                }
            }
        });
    }
    private void createActivity(){
        disabled = true;
        createActivityViews();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                disabled = false;
            }
        }, 1000);
    }

    public void databaseHelper(){
        myDB = new MyDatabaseHelper(Main2Activity.this);
        db_id = new ArrayList<>();
        db_name = new ArrayList<>();
        db_tag = new ArrayList<>();
        db_repeat = new ArrayList<>();

        storeHabitDataInArrays();

        customAdapter = new CustomAdapter(Main2Activity.this,Main2Activity.this, db_id, db_name, db_tag, db_repeat);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Main2Activity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            activityReload();
        }
    }
    @SuppressWarnings("all")
    public void storeHabitDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){

        }
        else {
            while (cursor.moveToNext()){
                db_id.add(cursor.getString(0));
                db_name.add(cursor.getString(1));
                db_tag.add(cursor.getString(2));
                db_repeat.add(cursor.getString(3));
            }
        }
    }

    public void findIDs(){
        recyclerView = findViewById(R.id.recycler_view);
        add_button = findViewById(R.id.fab);
        menu_settings = findViewById(R.id.action_settings);
        habit_item = findViewById(R.id.habit_item);
        home_constraint = findViewById(R.id.home_constraint);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Log.d("options_menu", "settings");
            Intent intent = new Intent(Main2Activity.this, SettingActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_reload){
            Log.d("options_menu", "reload page");
            activityReload();
        }

        return super.onOptionsItemSelected(item);
    }

    public void activityReload(){
        Intent intent = new Intent(Main2Activity.this, Main2Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("bug_fixing", "backPressed");
        activityReload();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void loadSharedPreferences()
    {
        settings = (UserSettings) getApplication();
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String pref = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);

        String[] itemsSettings = pref.split(",");

        List<String> items = new ArrayList<>(Arrays.asList(itemsSettings));

        theme = items.get(0);

        Log.d("theme_preferences", "Main2Activity: " + theme);
        settings.setCustomTheme(theme);

        fdof = items.get(1);
        Log.d("fdof", "Main2Activity: " + fdof);
        settings.setFDOF_Setting(fdof);
    }
    public void updateTheme(){
        MaterialCalendarView mcv = findViewById(R.id.calendarView);
        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);

        if(settings.getCustomTheme().equals(UserSettings.DARK_THEME)){
            Log.d("theme_preferences", "dark");
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            mcv.setHeaderTextAppearance(R.color.white);
            mcv.setWeekDayTextAppearance(R.color.white);
            mcv.setArrowColor(R.color.white);
            mcv.setDateTextAppearance(R.color.white);
        }
        else{
            Log.d("theme_preferences", "light");
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            mcv.setHeaderTextAppearance(R.color.black);
            mcv.setWeekDayTextAppearance(R.color.black);
            mcv.setArrowColor(R.color.black);
            mcv.setDateTextAppearance(R.color.black);
        }
    }

    public void loadCalendarPreferences(){
        MaterialCalendarView mcv = findViewById(R.id.calendarView);
        if(fdof.equals("monday"))
            mcv.state().edit().setFirstDayOfWeek(Calendar.MONDAY).commit();
        else if (fdof.equals("sunday"))
            mcv.state().edit().setFirstDayOfWeek(Calendar.SUNDAY).commit();
    }
    public void calendarDatabaseHelper(){
        myDB2 = new MyDatabaseHelper2(Main2Activity.this);
        db_calendar_id = new ArrayList<>();
        db_calendar_date = new ArrayList<>();
        db_calendar_status = new ArrayList<>();

        storeCalendarDataInArrays();
    }
    public void storeCalendarDataInArrays(){
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
    private void addCalendarEvents(){
        MaterialCalendarView mcv = findViewById(R.id.calendarView);
        final String NONE = "none";
        final String DONE = "done";
        final String FAIL = "fail";
        final String SKIP = "skip";

        ArrayList<CalendarDay> selectedDays = new ArrayList<>();
        ArrayList<CalendarDay> loadedDays = new ArrayList<>();
        //mcv.setSelectedDate(selectedDay);

        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                DayViewDecorator dayViewDecorator = eventDecorator;
                String status;
                String selectedDay = String.valueOf(date);
                Toast.makeText(Main2Activity.this, "Day: " + selectedDay, Toast.LENGTH_SHORT).show();

                java.util.Date todayDate = new java.util.Date();
                todayDate.setHours(0);
                todayDate.setMinutes(0);
                todayDate.setSeconds(0);
                Log.d("decorator", "Today Date: " + String.valueOf(todayDate));
                Collection<EventDecorator> eventDecorators;
                Collection<CalendarDay> calendarDays = Collections.singleton(date);
                java.util.Date dateInfo = date.getDate();
                String stringDateInfo = String.valueOf(dateInfo);
                Collection<CalendarDay> dateDays = Collections.singleton(date);

                Log.d("decorator", "dateDays: " + String.valueOf(dateDays));

                MyDatabaseHelper2 MyDB2 = new MyDatabaseHelper2(Main2Activity.this);
                selectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.dark_blue), dateDays);
                invalidateSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.transparent), dateDays);

                loadCalendarDays();
                storeCalendarDataInArrays();

                Log.d("decorator", String.valueOf(db_calendar_date));

                if(!db_calendar_date.contains(stringDateInfo)){
                    Log.d("decorator", "false");
                    //selectedDays.add(date);
                    //mcv.addDecorators(eventDecorator);
                    mcv.addDecorators(selectionEventDecorator);
                    status = DONE;
                    Log.d("decorator", "Status: " + status.toString());
                    MyDB2.addHabit(String.valueOf(dateInfo).trim(), //date
                                   status.toString().trim()); //status

                }
                else if(db_calendar_date.contains(stringDateInfo)){
                    db_calendar_date.remove(stringDateInfo);
                    for(int i = 0; i < db_calendar_date.size(); i++){
                        String edit = db_calendar_date.get(i);
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzzzzzzz yyyy");
                        try {
                            Date calendar_date = formatter.parse(edit);
                            CalendarDay calendarDayDate = CalendarDay.from(calendar_date);
                            if(String.valueOf(calendarDayDate).equals(String.valueOf(date))){
                                Log.d("decorator", "remove: " + edit);
                                String delete_row_id = db_calendar_id.get(i);
                                myDB2.deleteOneRow(delete_row_id);
                                Log.d("decorator", "remove: " + String.valueOf(delete_row_id));
                            }
                            else{
                                Log.d("decorator", "remove info: " + "calendarDayDate: " + String.valueOf(calendarDayDate));
                                Log.d("decorator", "remove info: " + "date: " + String.valueOf(date.toString()));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    mcv.addDecorators(invalidateSelectionEventDecorator);
                    activityReload();
                }
                Log.d("decorator", selectedDays.toString());
            }
        });

        mcv.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("decorator", String.valueOf(weekMode[0]));
                if(weekMode[0] == true){
                    weekMode[0] = false;
                    mcv.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                }
                else if(weekMode[0] == false){
                    weekMode[0] = true;
                    mcv.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                }
            }
        });
    }
    private void loadCalendarDays(){
        MaterialCalendarView mcv = findViewById(R.id.calendarView);
        for(int i = 0; i < db_calendar_date.size(); i++){
            String edit = db_calendar_date.get(i);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss zzzzzzzzz yyyy");
            try {
                Date loadDate = formatter.parse(edit);
                CalendarDay loadDates = CalendarDay.from(loadDate);
                Collection<CalendarDay> previousDates = Collections.singleton(loadDates);
                previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.dark_blue), previousDates);
                mcv.addDecorators(previousSelectionEventDecorator);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}