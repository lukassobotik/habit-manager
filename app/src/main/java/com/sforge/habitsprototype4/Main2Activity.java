package com.sforge.habitsprototype4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.sforge.habitsprototype4.databinding.ActivityMain2Binding;
import com.sforge.habitsprototype4.statistics.MainStatistics;
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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import maes.tech.intentanim.CustomIntent;

public class Main2Activity extends AppCompatActivity {
    private UserSettings settings;
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

    public String theme;
    public String fdof = "monday";
    public String monthLoadsPerSwipe = "three";
    public String secondsToLoad = "one second";
    public String currentFragment = "home";
    CardView cardView;
    ImageView emptyImageView;
    TextView noHabitsText;

    MaterialCalendarView mcv;
    final boolean[] weekMode = {false};
    final boolean[] enableMonthMode = {false};
    final boolean[] showAllHabits = {false};
    boolean disabled = false;

    Date[] arrayOfDates;
    String[] dates;
    Date[] datesArray;
    String[] oldDates;
    List<String> oldDatesList;
    Date formatDate;
    ArrayList<CalendarDay> decoratedDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActivity();

        loadSharedPreferences();
        updateTheme();

        loadCalendarPreferences();
        addCalendarEvents();
        calendarDatabaseHelper();
        declareArrayLists(new Date());
        loadCalendarDays(new Date());

        findIDs();
        databaseHelper();
        getCurrentDay();

        sendDataForStatistics();
    }

    public void createActivityViews() {
        com.sforge.habitsprototype4.databinding.ActivityMain2Binding binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain2.toolbar);
        binding.appBarMain2.fab.setOnClickListener(view -> {
            Intent intent = new Intent(Main2Activity.this, AddActivity.class);
            startActivity(intent);
            CustomIntent.customType(Main2Activity.this, "fadein-to-fadeout");
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_logout, R.id.nav_settings, R.id.nav_quotes, R.id.nav_counter)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main2);
        onFragmentEnter(navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    protected void onFragmentEnter(NavController navController) {
        mcv = findViewById(R.id.calendarView);
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (!disabled) {
                if (String.valueOf(navDestination.getLabel()).equals("Home")) {
                    findViewById(R.id.action_show_all_habits).setVisibility(View.VISIBLE);
                    findViewById(R.id.action_show_month_view).setVisibility(View.VISIBLE);
                    recreate();
                }
                if (String.valueOf(navDestination.getLabel()).equals("Calendar")) {
                    findViewById(R.id.fab).setVisibility(View.GONE);
                }
                else{
                    findViewById(R.id.fab).setVisibility(View.VISIBLE);
                }
            }
            if(String.valueOf(navDestination.getLabel()).equals("Home"))
                currentFragment = "Home";
            if(String.valueOf(navDestination.getLabel()).equals("Calendar")){
                currentFragment = "Calendar";
                findViewById(R.id.action_show_all_habits).setVisibility(View.GONE);
                findViewById(R.id.action_show_month_view).setVisibility(View.GONE);
            }
            if(String.valueOf(navDestination.getLabel()).equals("Statistics")){
                currentFragment = "Statistics";
                findViewById(R.id.action_show_all_habits).setVisibility(View.GONE);
                findViewById(R.id.action_show_month_view).setVisibility(View.GONE);
            }
            if(String.valueOf(navDestination.getLabel()).equals("Quotes")){
                currentFragment = "Quotes";
                findViewById(R.id.action_show_all_habits).setVisibility(View.GONE);
                findViewById(R.id.action_show_month_view).setVisibility(View.GONE);
            }
            if(String.valueOf(navDestination.getLabel()).equals("Counter")){
                currentFragment = "Counter";
                findViewById(R.id.action_show_all_habits).setVisibility(View.GONE);
                findViewById(R.id.action_show_month_view).setVisibility(View.GONE);
            }
            if(String.valueOf(navDestination.getLabel()).equals("Logout")){
                currentFragment = "Logout";
                findViewById(R.id.action_show_all_habits).setVisibility(View.GONE);
                findViewById(R.id.action_show_month_view).setVisibility(View.GONE);
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
        mcv.setAlpha(0);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            mcv.setAlpha(1);
            mcv.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left_to_right));
        }, 100);
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
        emptyImageView = findViewById(R.id.empty_image_view);
        noHabitsText = findViewById(R.id.no_habits_text);
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
            CustomIntent.customType(Main2Activity.this, "fadein-to-fadeout");
        }
        if (id == R.id.action_reload) {
            activityReload();
        }
        if (id == R.id.action_show_month_view){
            if(currentFragment.equals("Home")){
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
        }
        if (id == R.id.action_show_all_habits){
            if(currentFragment.equals("Home")){
                if (showAllHabits[0]) {
                    setCurrentDayTitle();
                    mcv.setVisibility(View.VISIBLE);
                    showAllHabits[0] = false;
                    getCurrentDay();
                    Snackbar.make(view, "Showing Today's Habits", Snackbar.LENGTH_LONG).setAction("Showing Today's Habits", null).show();
                } else {
                    Objects.requireNonNull(getSupportActionBar()).setTitle("All Habits");
                    mcv.setVisibility(View.GONE);
                    showAllHabits[0] = true;
                    Cursor cursor = myDB2.readAllData();
                    if(cursor.getCount() == 0){
                        emptyImageView.setVisibility(View.VISIBLE);
                        noHabitsText.setVisibility(View.VISIBLE);}
                    else{
                        emptyImageView.setVisibility(View.GONE);
                        noHabitsText.setVisibility(View.GONE);
                    }
                    databaseHelper();
                    mcv.clearSelection();
                    Snackbar.make(view, "Showing All Habits", Snackbar.LENGTH_LONG).setAction("Showing All Habits", null).show();
                }
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

        if (items.size() > 0)
            theme = items.get(0);
        settings.setCustomTheme(theme);

        if (items.size() > 1)
            fdof = items.get(1);
        settings.setFDOF_Setting(fdof);

        if(items.size() > 2)
            monthLoadsPerSwipe = items.get(2);
        settings.setMonthLoadsPerSwipe(monthLoadsPerSwipe);

        if(items.size() > 3)
            secondsToLoad = items.get(3);
        settings.setSecondsToLoad(secondsToLoad);
    }

    @SuppressLint({"UseCompatLoadingForDrawables"})
    public void updateTheme() {
        mcv = findViewById(R.id.calendarView);
        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            mcv.setHeaderTextAppearance(R.color.white);
            mcv.setWeekDayTextAppearance(R.color.white);
            mcv.setArrowColor(R.color.white);
            mcv.setDateTextAppearance(R.color.white);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

        if(!currentHabitNames.isEmpty()){
            emptyImageView.setVisibility(View.GONE);
            noHabitsText.setVisibility(View.GONE);}
        else {
            emptyImageView.setVisibility(View.VISIBLE);
            noHabitsText.setVisibility(View.VISIBLE);}

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
        setCurrentDayTitle();

        int weekDay = todayDate.getDay();
        showSelectedDaysHabit(weekDay);
    }
    public void setCurrentDayTitle(){
        Date myDate = new Date();
        @SuppressLint("SimpleDateFormat") String selDate = new SimpleDateFormat("E MMM dd").format(myDate);
        Objects.requireNonNull(getSupportActionBar()).setTitle(selDate);
    }
    public void setSelectedDayTitle(Date date){
        @SuppressLint("SimpleDateFormat") String selDate = new SimpleDateFormat("E MMM dd").format(date);
        Objects.requireNonNull(getSupportActionBar()).setTitle(selDate);
    }

    @SuppressWarnings("all")
    private void addCalendarEvents() {
        mcv = findViewById(R.id.calendarView);
        final String DONE = "done";
        final String FAIL = "fail";
        final String SKIP = "skip";

        java.util.Date todayDate = new java.util.Date();
        todayDate.setHours(0);
        todayDate.setMinutes(0);
        todayDate.setSeconds(0);
        CalendarDay calendarDayTodayDate = CalendarDay.from(todayDate);
        final Collection<CalendarDay>[] dateDays = new Collection[]{Collections.singleton(calendarDayTodayDate)};

        mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        ArrayList<String> selectedDay = new ArrayList<>();
        ArrayList<CalendarDay> selectedDays = new ArrayList<>();

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                java.util.Date selDate = date.getDate();
                int weekDay = selDate.getDay();
                showSelectedDaysHabit(weekDay);
                setSelectedDayTitle(selDate);
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

        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                Date date = calendarDay.getDate();
                loadCalendarDays(date);
            }
        });
    }

    private void declareArrayLists(Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss zzzzzzzzz yyyy");
        arrayOfDates = new Date[db_calendar_date.size()];
        for(int index = 0; index < db_calendar_date.size(); index++){
            try {
                arrayOfDates[index] = sdf.parse(db_calendar_date.get(index));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Arrays.sort(arrayOfDates);
        dates = new String[arrayOfDates.length];
        for(int index = 0; index < db_calendar_date.size(); index++){
            dates[index] = sdf.format(arrayOfDates[index]);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("E MMM dd yyyy");
        for(int index = 0; index < arrayOfDates.length; index++){
            dates[index] = sdf2.format(arrayOfDates[index]);
        }

        datesArray = new Date[db_calendar_date.size()];
        for(int index = 0; index < db_calendar_date.size(); index++){
            try {
                datesArray[index] = sdf.parse(db_calendar_date.get(index));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        oldDates = new String[arrayOfDates.length];
        for(int index = 0; index < arrayOfDates.length; index++){
            assert datesArray[index] != null;
            oldDates[index] = sdf2.format(datesArray[index]);
        }

        oldDatesList = Arrays.asList(oldDates);
        formatDateSwipeAction(date);
    }

    private void formatDateSwipeAction(Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("E MMM dd yyyy");
        formatDate = new Date();
        String formattedDate = sdf2.format(date);
        try {
            formatDate = sdf2.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadCalendarDays(Date date) {
        mcv = findViewById(R.id.calendarView);
        int maxLoad;
        int delay;
        if(monthLoadsPerSwipe.equals("one"))
            maxLoad = 31;
        else
            maxLoad = 93;

        switch(secondsToLoad){
            case UserSettings.INSTANT:
                delay = 0;
                break;
            case UserSettings.HALF_SECOND:
                delay = 500;
                break;
            case UserSettings.THREE_FOURTHS_OF_A_SECOND:
                delay = 750;
                break;
            case UserSettings.TWO_SECONDS:
                delay = 2000;
                break;
            default:
                delay = 1000;
                break;
        }

        formatDateSwipeAction(date);

        //previous dates
        int match = -1;
        for(int index = 0; index < db_calendar_date.size(); index++){
            if(arrayOfDates[index].equals(formatDate)){
                match = index;
                final int[] matchingIndex = {match + 1};
                for (int i = 0; i < maxLoad; i++){
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        matchingIndex[0] = runPreviousDatesLoading(matchingIndex[0]);
                    }, delay);
                }
                break;
            }
        }

        //future dates
        int match1 = -1;
        for(int index = 0; index < db_calendar_date.size(); index++){
            if(arrayOfDates[index].equals(formatDate)){
                match1 = index;
                final int[] matchingIndex = {match1 + 1};
                for (int i = 0; i < maxLoad; i++){
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        matchingIndex[0] = runFutureDatesLoading(matchingIndex[0]);
                    }, delay);
                }
                break;
            }
        }
    }

    public int runPreviousDatesLoading(int matchingIndex){
        int index1 = oldDatesList.indexOf(dates[matchingIndex]);
        CalendarDay calendarDay = CalendarDay.from(arrayOfDates[matchingIndex]);
        Collection<CalendarDay> previousDates = Collections.singleton(calendarDay);
        SelectionEventDecorator previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.transparent), previousDates, this);
        if(db_calendar_status.get(index1).equals("done"))
            previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_done), previousDates, this);
        else if(db_calendar_status.get(index1).equals("fail"))
            previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_failed), previousDates, this);
        if(!decoratedDates.contains(calendarDay))
            mcv.addDecorators(previousSelectionEventDecorator);
        decoratedDates.add(calendarDay);
        if(matchingIndex > 0)
            matchingIndex--;
        return matchingIndex;
    }
    public int runFutureDatesLoading(int matchingIndex){
        int index1 = oldDatesList.indexOf(dates[matchingIndex]);
        CalendarDay calendarDay = CalendarDay.from(arrayOfDates[matchingIndex]);
        Collection<CalendarDay> previousDates = Collections.singleton(calendarDay);
        SelectionEventDecorator previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.transparent), previousDates, this);
        if(db_calendar_status.get(index1).equals("done"))
            previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_done), previousDates, this);
        else if(db_calendar_status.get(index1).equals("fail"))
            previousSelectionEventDecorator = new SelectionEventDecorator(getResources().getColor(R.color.calendar_failed), previousDates, this);
        if(!decoratedDates.contains(calendarDay))
            mcv.addDecorators(previousSelectionEventDecorator);
        decoratedDates.add(calendarDay);
        if(matchingIndex < db_calendar_date.size() - 1)
            matchingIndex++;
        return matchingIndex;
    }

    public void sendDataForStatistics(){
        MainStatistics statistics = new MainStatistics();
        statistics.countFailedDays(db_calendar_id, db_calendar_date, db_calendar_status);
        statistics.countCompletedDays(db_calendar_id, db_calendar_date, db_calendar_status);
    }
}