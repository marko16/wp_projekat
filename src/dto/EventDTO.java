package dto;

import model.Event;
import model.Location;

public class EventDTO {
    private Event event;
    private Location location;

    public EventDTO() {

    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
