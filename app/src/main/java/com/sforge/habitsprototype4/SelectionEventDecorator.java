package com.sforge.habitsprototype4;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class SelectionEventDecorator implements DayViewDecorator{

    ShapeDrawable shapeDrawable = new ShapeDrawable();
    private final int color;
    private final HashSet<CalendarDay> dates;

    public SelectionEventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        shapeDrawable.setShape(new OvalShape());
        shapeDrawable.getPaint().setColor(color);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(shapeDrawable);
    }
}

