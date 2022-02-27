package com.sforge.habitsprototype4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sforge.habitsprototype4.ui.settings.UserSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private UserSettings settings;

    EditText tag, name, repeat;
    Button add_button;

    String fdof, theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        tag = findViewById(R.id.name_input);
        name = findViewById(R.id.tag_input);
        repeat = findViewById(R.id.repeat_input);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper MyDB = new MyDatabaseHelper(AddActivity.this);
                MyDB.addHabit(tag.getText().toString().trim(),
                              name.getText().toString().trim(),
                              repeat.getText().toString().trim());

                tag.setText(tag.getText().toString());
                Log.d("debug", tag.toString());
                name.setText(name.getText().toString());
                repeat.setText(repeat.getText().toString());
                Intent intent = new Intent(AddActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        loadSharedPreferences();
        setThemePreferences();
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
    private void setThemePreferences(){
        if(settings.getCustomTheme().equals(UserSettings.DARK_THEME)){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}