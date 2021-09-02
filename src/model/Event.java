package model;

import java.time.LocalDateTime;

public class Event {
    private String name;
    private String eventType;
    private int capacity;
    private LocalDateTime startTime;
    private double regularPrice;
    private boolean isActive;
    private Location location;
    private String poster;
}
