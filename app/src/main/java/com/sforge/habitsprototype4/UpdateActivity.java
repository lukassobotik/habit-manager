package com.sforge.habitsprototype4;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
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

public class UpdateActivity extends AppCompatActivity {
    private UserSettings settings;

    EditText name_input, tag_input, repeat_input;
    Button update_button, delete_button;

    String id, name, tag, repeat;

    String fdof, theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        name_input = findViewById(R.id.name_input2);
        tag_input = findViewById(R.id.tag_input2);
        repeat_input = findViewById(R.id.repeat_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.remove_button);

        getIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Edit " + name);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                name = name_input.getText().toString().trim();
                tag = tag_input.getText().toString().trim();
                repeat = repeat_input.getText().toString().trim();
                myDB.updateData(id, name, tag, repeat);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
        loadSharedPreferences();
        setThemePreferences();
    }
    void getIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("tag") && getIntent().hasExtra("repeat")){
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            tag = getIntent().getStringExtra("tag");
            repeat = getIntent().getStringExtra("repeat");

            //set intent data
            name_input.setText(name);
            tag_input.setText(tag);
            repeat_input.setText(repeat);

        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + "?");
        builder.setMessage("Are you sure you want to delete " + name + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
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