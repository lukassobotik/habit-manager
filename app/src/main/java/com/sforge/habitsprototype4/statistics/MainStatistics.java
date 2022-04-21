package com.sforge.habitsprototype4.statistics;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.sforge.habitsprototype4.entity.CalendarLibrary;
import com.sforge.habitsprototype4.entity.CalendarStatus;
import com.sforge.habitsprototype4.entity.ConnectedDays;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MainStatistics extends AppCompatActivity {

    /**
     *  Function that will return the amount of failed days
     *
     * @param calendarEntities Object of ids, dates and statuses from DB
     */
    public int countFailedDays(List<CalendarLibrary> calendarEntities) {
        int failedDays = 0;
        for (int i = 0; i < calendarEntities.size(); i++) {
            if (CalendarStatus.fail.equals(calendarEntities.get(i).getStatus())) {
                failedDays++;
            }
        }
        return failedDays;
    }

    /**
     * Function that will return the amount of completed days
     *
     * @param calendarEntities Object of ids, dates and statuses from DB
     */
    public int countCompletedDays(List<CalendarLibrary> calendarEntities) {
        int completedDays = 0;
        for (int i = 0; i < calendarEntities.size(); i++) {
            if (CalendarStatus.done.equals(calendarEntities.get(i).getStatus())) {
                completedDays++;
            }
        }
        return completedDays;
    }

    /**
     * Function that will return all connected finished days.
     *
     * @param calendarEntities Object of ids, dates and statuses from DB
     */
    public ConnectedDays connectCompletedDays(List<CalendarLibrary> calendarEntities) {
        List<Integer> streaks = new ArrayList<>();
        List<Integer> id = new ArrayList<>();
        Collections.sort(calendarEntities);
        final List<CalendarLibrary> doneEntities = calendarEntities
                .stream()
                .filter(e -> CalendarStatus.done.equals(e.getStatus()))
                .collect(Collectors.toList());

        final LinkedList linkedList = new LinkedList(doneEntities);

        List<LocalDate> returnDates = new ArrayList<>();
        CalendarLibrary previous = null;
        CalendarLibrary current = null;
        CalendarLibrary next = null;

        int streak = 0;
        int identifier = 0;

        final Iterator it = linkedList.iterator();

        if (it.hasNext()) {
            previous = (CalendarLibrary) it.next();
        }
        int limit = 0;
        while (it.hasNext() || limit < calendarEntities.size()) {
            limit++;
            if (current == null) {
                current = (CalendarLibrary) it.next();
            }

            if (it.hasNext()) {
                next = (CalendarLibrary) it.next();
            }

            if (previous.getDate().plusDays(1).equals(current.getDate())) {
                returnDates.add(previous.getDate());

                streak++;

                id.add(identifier);
            }
            if (!next.getDate().minusDays(1).equals(current.getDate())
                    && current.getDate().minusDays(1).equals(previous.getDate())) {
                returnDates.add(current.getDate());
                streak++;
                streaks.add(streak);
                streak = 0;

                id.add(identifier);
                identifier++;
            }

            previous = current;
            current = next;
        }
        ConnectedDays connectedDates = new ConnectedDays(returnDates, streaks, id, null);
        return connectedDates;
    }
}
