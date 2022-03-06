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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import com.sforge.habitsprototype4.ui.settings.UserSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity {
    private UserSettings settings;

    EditText name_input, tag_input, repeat_input;
    Button update_button, delete_button;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    ArrayList<String> repetition = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();

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
        // --Weekday Labels
        monday = findViewById(R.id.monday_button_update);
        tuesday = findViewById(R.id.tuesday_button_update);
        wednesday = findViewById(R.id.wednesday_button_update);
        thursday = findViewById(R.id.thursday_button_update);
        friday = findViewById(R.id.friday_button_update);
        saturday = findViewById(R.id.saturday_button_update);
        sunday = findViewById(R.id.sunday_button_update);
        //
        getIntentData();
        List<String> items = Arrays.asList(repeat.split("\\s*,\\s*"));
        items.remove("   ");
        items.remove("   ");
        items.remove("   ");
        items.remove("   ");
        items.remove("   ");
        items.remove("   ");
        items.remove("   ");

        Log.d("repetition", repeat);
        Log.d("repetition", String.valueOf(items));

        if(items.contains("SU")){
            repetition.add(0,"SU");
            sunday.setChecked(true);}else{repetition.add(0,"");}
        if(items.contains("MO")){
            repetition.add(1,"MO");
            monday.setChecked(true);}else{repetition.add(1,"");}
        if(items.contains("TU")){
            repetition.add(2,"TU");
            tuesday.setChecked(true);}else{repetition.add(2,"");}
        if(items.contains("WE")){
            repetition.add(3,"WE");
            wednesday.setChecked(true);}else{repetition.add(3,"");}
        if(items.contains("TH")){
            repetition.add(4,"TH");
            thursday.setChecked(true);}else{repetition.add(4,"");}
        if(items.contains("FR")){
            repetition.add(5,"FR");
            friday.setChecked(true);}else{repetition.add(5,"");}
        if(items.contains("SA")){
            repetition.add(6,"SA");
            saturday.setChecked(true);}else{repetition.add(6,"");}

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

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Edit " + name);
        }
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
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
            myDB.deleteOneRow(id);
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        builder.create().show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int clickId = item.getItemId();

        if (clickId == R.id.update_action_done) {
            stringBuilder.setLength(0);
            for(String s : repetition){
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
            name = name_input.getText().toString().trim();
            tag = tag_input.getText().toString().trim();
            repeat = stringBuilder.toString();
            myDB.updateData(id, name, tag, repeat);
            Intent intent = new Intent(UpdateActivity.this, Main2Activity.class);
            startActivity(intent);
        }
        if (clickId == R.id.update_action_remove){
            confirmDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadSharedPreferences(){
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