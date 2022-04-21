package com.sforge.habitsprototype4.entity;

import java.time.LocalDate;
import java.util.List;

public class ConnectedDays {
    private List<LocalDate> date;
    private List<Integer> id;
    private List<ConnectedTag> tag;
    private List<Integer> streak;

    public ConnectedDays(List<LocalDate> date, List<Integer> streak, List<Integer> id, List<ConnectedTag> tag) {
        this.date = date;
        this.streak = streak;
        this.id = id;
        this.tag = tag;
    }

    public void setDate(List<LocalDate> date) {
        this.date = date;
    }

    public void setStreak(List<Integer> streak) {
        this.streak = streak;
    }

    public List<Integer> getStreak() {
        return streak;
    }

    public List<LocalDate> getDate() {
        return date;
    }

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public void setTag(List<ConnectedTag> tag) {
        this.tag = tag;
    }

    public List<ConnectedTag> getTag() {
        return tag;
    }
}
