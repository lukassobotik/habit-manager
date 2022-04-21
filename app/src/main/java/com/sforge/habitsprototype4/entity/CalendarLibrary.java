package com.sforge.habitsprototype4.entity;

import java.time.LocalDate;

public class CalendarLibrary implements Comparable<CalendarLibrary> {
    private int _id;
    private LocalDate date;
    private CalendarStatus status;

    public CalendarLibrary (final int _id, final LocalDate date, final CalendarStatus status) {
        this._id = _id;
        this.date = date;
        this.status = status;
    }

    public int get_id() {
        return _id;
    }

    public LocalDate getDate() {
        return date;
    }

    public CalendarStatus getStatus() {
        return status;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(CalendarStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(CalendarLibrary other) {
        return this.date.compareTo(other.date);
    }
}
