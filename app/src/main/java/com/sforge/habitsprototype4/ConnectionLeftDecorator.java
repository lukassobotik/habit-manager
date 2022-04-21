package com.sforge.habitsprototype4;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class ConnectionLeftDecorator implements DayViewDecorator {
    private final HashSet<CalendarDay> dates;
    private final Drawable drawable;

    public ConnectionLeftDecorator(int color, Collection<CalendarDay> dates, Activity context) {
        this.dates = new HashSet<>(dates);
        drawable = context.getResources().getDrawable(R.drawable.connection_left);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
        view.setSelectionDrawable(drawable);
    }
}
