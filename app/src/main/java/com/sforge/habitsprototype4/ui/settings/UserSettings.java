package com.sforge.habitsprototype4.ui.settings;

import android.app.Application;

public class UserSettings extends Application
{
    public static final String PREFERENCES = "preferences";

    public static final String CUSTOM_THEME = "customTheme";
    public static final String LIGHT_THEME = "lightTheme";
    public static final String DARK_THEME = "darkTheme";

    public static final String FDOF_MONDAY = "monday";
    public static final String FDOF_SUNDAY = "sunday";

    public static final String ONE_MONTH_LOAD_PER_SWIPE = "one";
    public static final String THREE_MONTHS_LOAD_PER_SWIPE = "three";

    public static final String HALF_SECOND = "half a second";
    public static final String THREE_FOURTHS_OF_A_SECOND = "three fourths of a second";
    public static final String ONE_SECOND = "one second";
    public static final String TWO_SECONDS = "two seconds";

    private String customTheme;
    private String FDOF_Setting;
    private String monthLoadsPerSwipe;
    private String secondsToLoad;

    public String getCustomTheme()
    {
        return customTheme;
    }
    public String getFDOF_Setting()
    {
        return FDOF_Setting;
    }
    public String getMonthLoadsPerSwipe(){
        return monthLoadsPerSwipe;
    }
    public String getSecondsToLoad(){
        return secondsToLoad;
    }


    public void setCustomTheme(String customTheme)
    {
        this.customTheme = customTheme;
    }
    public void setFDOF_Setting(String FDOF_Setting)
    {
        this.FDOF_Setting = FDOF_Setting;
    }
    public void setMonthLoadsPerSwipe(String monthLoadsPerSwipe){
        this.monthLoadsPerSwipe = monthLoadsPerSwipe;
    }
    public void setSecondsToLoad(String secondsToLoad){
        this.secondsToLoad = secondsToLoad;
    }
}