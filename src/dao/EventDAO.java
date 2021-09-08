package dao;

import com.google.gson.Gson;
import model.Event;
import model.Location;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EventDAO {
    private HashMap<Integer, Event> events;
    private LocationDAO locationDAO = new LocationDAO();

    public EventDAO() {
        events = new HashMap<>();

        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAll() throws IOException {
        try {
            locationDAO.loadAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Event e1 = new Event();
        e1.setId(1);
        e1.setActive(true);
        e1.setCapacity(230);
        e1.setAvailableTickets(33);
        e1.setLocation(locationDAO.locations.get(1));
        e1.setRegularPrice(300);
        e1.setName("Grad kulture");
        e1.setStartTime(new Date(121, Calendar.SEPTEMBER, 22));
        e1.setEventType("Cultural event");

        Event e2 = new Event();
        e2.setId(2);
        e2.setActive(true);
        e2.setCapacity(210);
        e2.setAvailableTickets(10);
        e2.setLocation(locationDAO.locations.get(2));
        e2.setRegularPrice(250);
        e2.setName("Koncert Rade Manojlovic");
        e2.setStartTime(new Date(121, Calendar.SEPTEMBER, 15));
        e2.setEventType("Concert");

        events.put(1, e1);
        events.put(2, e2);

        Gson gson = new Gson();
        FileWriter fw = new FileWriter("files/events.json");
        gson.toJson(this.events, fw);
        fw.flush();
        fw.close();
    }

    public ArrayList<Event> getAvailableEvents() {
        ArrayList<Event> availableEvents = new ArrayList<>();
        for(Event event : this.events.values()) {
            if(event.isActive()) {
                availableEvents.add(event);
            }
        }
        return availableEvents;
    }

    public Event findOne(Integer id) {
        return events.getOrDefault(id, null);
    }

    public void adjustCapacity(int amount, int eventId) {
        Event event = this.findOne(eventId);
        event.setAvailableTickets(event.getAvailableTickets() - amount);
        try {
            writeAll();
            // PROEMNI IME FJE
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
