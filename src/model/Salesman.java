package model;

import java.util.ArrayList;

public class Salesman extends User {
    private boolean blocked;
    private ArrayList<Integer> events;

    public Salesman() {
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public ArrayList<Integer> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Integer> events) {
        this.events = events;
    }
}
