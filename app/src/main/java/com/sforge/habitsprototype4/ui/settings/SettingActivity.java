package com.sforge.habitsprototype4.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.sforge.habitsprototype4.Main2Activity;
import com.sforge.habitsprototype4.R;
import com.sforge.habitsprototype4.SecondsToLoadDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

public class SettingActivity extends AppCompatActivity implements SecondsToLoadDialog.SecondsToLoadDialogListener {
    private SwitchMaterial themeSwitch;
    private TextView themeTV;
    private SwitchMaterial fdofSwitch;
    private TextView fdofTV;
    private SwitchMaterial monthLoadSwitch;
    private TextView monthLoadTV;
    private Button openSecondsDialog;
    private TextView secondsOption;

    private UserSettings settings;

    List<String>SettingList = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settings = (UserSettings) getApplication();

        initWidgets();
        loadSharedPreferences();
        themeSwitchListener();
        fdofSwitchListener();
        monthLoadSwitchListener();

        openSecondsDialog.setOnClickListener(view -> {
            openSecondsDialog();
        });

    }

    public void openSecondsDialog(){
        SecondsToLoadDialog dialog = new SecondsToLoadDialog();
        dialog.show(getSupportFragmentManager(), "SecondsToLoadDialog");
    }

    @Override
    public void getData(int option) {
        StringBuilder optionString = new StringBuilder();
        switch(option){
            case 500:
                stringBuilder.append(option);
                stringBuilder.append(" MILLISECONDS");
                settings.setSecondsToLoad(UserSettings.HALF_SECOND);
                break;
            case 750:
                stringBuilder.append(option);
                stringBuilder.append(" MILLISECONDS");
                settings.setSecondsToLoad(UserSettings.THREE_FOURTHS_OF_A_SECOND);
                break;
            case 1000:
                stringBuilder.append(option / 1000);
                stringBuilder.append(" SECOND");
                settings.setSecondsToLoad(UserSettings.ONE_SECOND);
                break;
            case 2000:
                stringBuilder.append(option / 1000);
                stringBuilder.append(" SECONDS");
                settings.setSecondsToLoad(UserSettings.TWO_SECONDS);
                break;
        }
        secondsOption.setText(stringBuilder);
        stringBuilder.setLength(0);
        applySettings();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, Main2Activity.class);
        startActivity(intent);
    }

    private void initWidgets(){
        themeTV = findViewById(R.id.themeTV);
        themeSwitch = findViewById(R.id.themeSwitch);
        fdofTV = findViewById(R.id.FirstDayOfWeekTV);
        fdofSwitch = findViewById(R.id.FirstDayOfWeekSwitch);
        monthLoadTV = findViewById(R.id.monthLoadTV);
        monthLoadSwitch = findViewById(R.id.monthLoadSwitch);
        secondsOption = findViewById(R.id.secondsOption);
        openSecondsDialog = findViewById(R.id.dialog_second_open);
    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String pref = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);

        String[] itemsSettings = pref.split(",");

        List<String> items = new ArrayList<>(Arrays.asList(itemsSettings));

        String theme = items.get(0);
        settings.setCustomTheme(theme);

        String fdof = "monday";
        if(items.size() > 1)
            fdof = items.get(1);
        settings.setFDOF_Setting(fdof);

        String monthLoadsPerSwipe = "1";
        if(items.size() > 2)
            monthLoadsPerSwipe = items.get(2);
        settings.setMonthLoadsPerSwipe(monthLoadsPerSwipe);

        String secondsToLoad = "one second";
        if(items.size() > 3)
            secondsToLoad = items.get(3);
        settings.setSecondsToLoad(secondsToLoad);

        updateView();
    }
    private void themeSwitchListener() {
        themeSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked)
                settings.setCustomTheme(UserSettings.DARK_THEME);
            else
                settings.setCustomTheme(UserSettings.LIGHT_THEME);
            Intent intent1 = new Intent(SettingActivity.this, SettingActivity.class);
            startActivity(intent1);
            CustomIntent.customType(SettingActivity.this, "fadein-to-fadeout");
            finish();
            applySettings();
        });
    }

    private void fdofSwitchListener() {
        fdofSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked)
                settings.setFDOF_Setting(UserSettings.FDOF_MONDAY);
            else
                settings.setFDOF_Setting(UserSettings.FDOF_SUNDAY);
            applySettings();
        });
    }

    private void monthLoadSwitchListener() {
        monthLoadSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked)
                settings.setMonthLoadsPerSwipe(UserSettings.ONE_MONTH_LOAD_PER_SWIPE);
            else
                settings.setMonthLoadsPerSwipe(UserSettings.THREE_MONTHS_LOAD_PER_SWIPE);
            applySettings();
        });
    }
    @SuppressLint("SetTextI18n")
    private void updateView(){
        if(settings.getCustomTheme().equals(UserSettings.DARK_THEME)){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            themeTV.setText("Dark");
            themeSwitch.setChecked(true);
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.layout_bg_corners, null));
        } else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            themeTV.setText("Light");
            themeSwitch.setChecked(false);
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.layout_bg_light, null));
        }

        if(settings.getFDOF_Setting().equals(UserSettings.FDOF_MONDAY)){
            fdofTV.setText("Monday");
            fdofSwitch.setChecked(true);
        } else{
            fdofTV.setText("Sunday");
            fdofSwitch.setChecked(false);
        }

        if(settings.getMonthLoadsPerSwipe().equals(UserSettings.THREE_MONTHS_LOAD_PER_SWIPE)){
            monthLoadTV.setText("Three");
            monthLoadSwitch.setChecked(false);
        } else{
            monthLoadTV.setText("One");
            monthLoadSwitch.setChecked(true);
        }

        if(settings.getSecondsToLoad() != null){
            switch (settings.getSecondsToLoad()) {
                case UserSettings.HALF_SECOND:
                    secondsOption.setText("500 MILLISECONDS");
                    break;
                case UserSettings.THREE_FOURTHS_OF_A_SECOND:
                    secondsOption.setText("750 MILLISECONDS");
                    break;
                case UserSettings.ONE_SECOND:
                    secondsOption.setText("1 SECOND");
                    break;
                case UserSettings.TWO_SECONDS:
                    secondsOption.setText("2 SECONDS");
                    break;
            }
        }

    }

    private void applySettings(){
        SettingList.add(settings.getCustomTheme());
        SettingList.add(settings.getFDOF_Setting());
        SettingList.add(settings.getMonthLoadsPerSwipe());
        SettingList.add(settings.getSecondsToLoad());
        for(String s : SettingList){
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        Log.d("theme_preferences", stringBuilder.toString());

        SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE).edit();
        editor.putString(UserSettings.CUSTOM_THEME, stringBuilder.toString());
        editor.apply();
        updateView();

        stringBuilder.setLength(0);
        SettingList.clear();
    }
}