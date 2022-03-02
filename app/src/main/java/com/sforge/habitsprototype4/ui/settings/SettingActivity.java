package com.sforge.habitsprototype4.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.sforge.habitsprototype4.Main2Activity;
import com.sforge.habitsprototype4.R;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private View parentView;
    private SwitchMaterial themeSwitch;
    private TextView themeTV, titleTV, themeDesc;
    private SwitchMaterial fdofSwitch;
    private TextView fdofTV, fdofTitleTV, fdofDesc;

    Main2Activity main2Activity;
    private UserSettings settings;

    public Intent i = new Intent(this, Main2Activity.class);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, Main2Activity.class);
        startActivity(intent);
    }

    private void initWidgets(){
        themeTV = findViewById(R.id.themeTV);
        themeDesc = findViewById(R.id.themeDesc);
        titleTV = findViewById(R.id.themeText);
        themeSwitch = findViewById(R.id.themeSwitch);
        parentView = findViewById(R.id.parentView);
        fdofTV = findViewById(R.id.FirstDayOfWeekTV);
        fdofDesc = findViewById(R.id.FirstDayOfWeekDesc);
        fdofTitleTV = findViewById(R.id.FirstDayOfWeekText);
        fdofSwitch = findViewById(R.id.FirstDayOfWeekSwitch);
    }

    private void loadSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String pref = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);

        String[] itemsSettings = pref.split(",");

        List<String> items = new ArrayList<>(Arrays.asList(itemsSettings));

        String theme = items.get(0);
        Log.d("theme_preferences", "theme: " + theme);
        settings.setCustomTheme(theme);

        String fdof = items.get(1);
        settings.setFDOF_Setting(fdof);

        updateView();
    }

    private void themeSwitchListener() {
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    settings.setCustomTheme(UserSettings.DARK_THEME);
                    Log.d("SharedPreferences", settings.getCustomTheme());
                }
                else {
                    settings.setCustomTheme(UserSettings.LIGHT_THEME);
                    Log.d("SharedPreferences", settings.getCustomTheme());
                }
                applySettings();
            }
        });
    }
    private void fdofSwitchListener() {
        fdofSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    settings.setFDOF_Setting(UserSettings.FDOF_MONDAY);
                    Log.d("fdof", settings.getFDOF_Setting());
                }
                else {
                    settings.setFDOF_Setting(UserSettings.FDOF_SUNDAY);
                    Log.d("fdof", settings.getFDOF_Setting());
                }
                applySettings();
            }
        });
    }

    private void updateView(){
        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);

        if(settings.getCustomTheme().equals(UserSettings.DARK_THEME)){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //titleTV.setTextColor(white);
            //themeDesc.setTextColor(white);
            //themeTV.setTextColor(white);
            themeTV.setText("Dark");
           // parentView.setBackgroundColor(black);
            themeSwitch.setChecked(true);
            //fdofTV.setTextColor(white);
            //fdofDesc.setTextColor(white);
            //fdofTitleTV.setTextColor(white);
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //titleTV.setTextColor(black);
            //themeDesc.setTextColor(black);
            //themeTV.setTextColor(black);
            themeTV.setText("Light");
            //parentView.setBackgroundColor(white);
            themeSwitch.setChecked(false);
            //fdofTV.setTextColor(black);
            //fdofDesc.setTextColor(black);
            //fdofTitleTV.setTextColor(black);
        }

        if(settings.getFDOF_Setting().equals(UserSettings.FDOF_MONDAY)){
            fdofTV.setText("Monday");
            fdofSwitch.setChecked(true);
        }
        else{
            fdofTV.setText("Sunday");
            fdofSwitch.setChecked(false);
        }

    }
    private void applySettings(){
        SettingList.add(settings.getCustomTheme());
        SettingList.add(settings.getFDOF_Setting());
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
        //SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        //SharedPreferences.Editor editor = preferences.edit();
        //editor.putString("SETTING_LIST", stringBuilder.toString());
        //editor.apply();

    }/*
    public boolean removeAll(Collection list) {
        boolean isModi = false;
        Iterator ite= new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        while (ite.hasNext()) {
            if (list.contains(ite.next())) {
                ite.remove();
                isModi = true;
            }
        }
        return isModi;
    }
    */
}