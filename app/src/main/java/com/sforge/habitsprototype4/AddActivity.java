package com.sforge.habitsprototype4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import com.sforge.habitsprototype4.ui.settings.UserSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {
    private UserSettings settings;

    EditText tag, name, repeat;
    Button add_button;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    String fdof, theme;

    ArrayList<String> repetition = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        tag = findViewById(R.id.name_input);
        name = findViewById(R.id.tag_input);
        repeat = findViewById(R.id.repeat_input);
        add_button = findViewById(R.id.add_button);
        // --Weekday Labels
        monday = findViewById(R.id.monday_button);
        tuesday = findViewById(R.id.tuesday_button);
        wednesday = findViewById(R.id.wednesday_button);
        thursday = findViewById(R.id.thursday_button);
        friday = findViewById(R.id.friday_button);
        saturday = findViewById(R.id.saturday_button);
        sunday = findViewById(R.id.sunday_button);
        //
        repetition.add(0,"");
        repetition.add(1,"");
        repetition.add(2,"");
        repetition.add(3,"");
        repetition.add(4,"");
        repetition.add(5,"");
        repetition.add(6,"");

        sunday.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                repetition.remove(0);
                repetition.add(0,"SU");
            }
            else{
                repetition.remove(0);
                repetition.add(0,"");
            }
            stringBuilder.setLength(0);
            for(String s : repetition){
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        });
        monday.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                repetition.remove(1);
                repetition.add(1, "MO");
            }
            else{
                repetition.remove(1);
                repetition.add(1,"");
            }
            stringBuilder.setLength(0);
            for(String s : repetition){
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        });
        tuesday.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                repetition.remove(2);
                repetition.add(2,"TU");
            }
            else{
                repetition.remove(2);
                repetition.add(2,"");
            }
            stringBuilder.setLength(0);
            for(String s : repetition){
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        });
        wednesday.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                repetition.remove(3);
                repetition.add(3,"WE");
            }
            else{
                repetition.remove(3);
                repetition.add(3,"");
            }
            stringBuilder.setLength(0);
            for(String s : repetition){
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        });
        thursday.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                repetition.remove(4);
                repetition.add(4,"TH");
            }
            else{
                repetition.remove(4);
                repetition.add(4,"");
            }
            stringBuilder.setLength(0);
            for(String s : repetition){
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        });
        friday.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                repetition.remove(5);
                repetition.add(5,"FR");
            }
            else{
                repetition.remove(5);
                repetition.add(5,"");
            }
            stringBuilder.setLength(0);
            for(String s : repetition){
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        });
        saturday.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                repetition.remove(6);
                repetition.add(6,"SA");
            }
            else{
                repetition.remove(6);
                repetition.add(6,"");
            }
            stringBuilder.setLength(0);
            for(String s : repetition){
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        });
        loadSharedPreferences();
        setThemePreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_action_done) {
            MyDatabaseHelper MyDB = new MyDatabaseHelper(AddActivity.this);
            MyDB.addHabit(tag.getText().toString().trim(),
                    name.getText().toString().trim(),
                    stringBuilder.toString());

            tag.setText(tag.getText().toString());
            Log.d("debug", tag.toString());
            name.setText(name.getText().toString());
            repeat.setText(repeat.getText().toString());
            Intent intent = new Intent(AddActivity.this, Main2Activity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

        if(items.size() > 1)
            fdof = items.get(1);
        Log.d("fdof", "Main2Activity: " + fdof);
        settings.setFDOF_Setting(fdof);
    }
    private void setThemePreferences(){
        if(settings.getCustomTheme().equals(UserSettings.DARK_THEME)){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.layout_bg_corners, null));
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.layout_bg_light, null));
        }
    }
}