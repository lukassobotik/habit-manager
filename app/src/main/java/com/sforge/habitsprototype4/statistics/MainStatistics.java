package com.sforge.habitsprototype4.statistics;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainStatistics extends AppCompatActivity {

    public int countFailedDays(ArrayList<String> calendarId, ArrayList<String> calendarDate, ArrayList<String> calendarStatus){
        int failedDays = 0;
        for (int i = 0; i < calendarDate.size(); i++) {
            if(calendarStatus.get(i).equals("fail")){
                failedDays++;
            }
        }
        return failedDays;
    }
    public int countCompletedDays(ArrayList<String> calendarId, ArrayList<String> calendarDate, ArrayList<String> calendarStatus){
        int completedDays = 0;
        for (int i = 0; i < calendarDate.size(); i++) {
            if(calendarStatus.get(i).equals("done")){
                completedDays++;
            }
        }
        return completedDays;
    }

}
