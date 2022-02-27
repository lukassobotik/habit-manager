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

    private String customTheme;
    private String FDOF_Setting;

    public String getCustomTheme()
    {
        return customTheme;
    }
    public String getFDOF_Setting()
    {
        return FDOF_Setting;
    }

    public void setCustomTheme(String customTheme)
    {
        this.customTheme = customTheme;
    }
    public void setFDOF_Setting(String FDOF_Setting)
    {
        this.FDOF_Setting = FDOF_Setting;
    }
}