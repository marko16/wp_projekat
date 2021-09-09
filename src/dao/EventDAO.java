package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Event;
import model.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class EventDAO {
    private HashMap<Integer, Event> events;
    private LocationDAO locationDAO = new LocationDAO();

    public EventDAO() {
        events = new HashMap<>();
        this.loadAll();
        try {
            locationDAO.loadAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, Event> loadAll() {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<Integer,Event>>(){}.getType();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("files/events.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert br != null;
        this.events = gson.fromJson(br, token);
        return this.events;
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
        e1.setLocation(locationDAO.findOne(1));
        e1.setRegularPrice(300);
        e1.setName("Grad kulture");
        e1.setStartTime(new Date(121, Calendar.SEPTEMBER, 22));
        e1.setEventType("Cultural event");
        e1.setPoster("images/e1.jfif");
        e1.setSalesman("s5");

        Event e2 = new Event();
        e2.setId(2);
        e2.setActive(true);
        e2.setCapacity(210);
        e2.setAvailableTickets(10);
        e2.setLocation(locationDAO.findOne(2));
        e2.setRegularPrice(250);
        e2.setName("Koncert Rade Manojlovic");
        e2.setStartTime(new Date(121, Calendar.SEPTEMBER, 15));
        e2.setEventType("Concert");
        e2.setPoster("images/e2.jfif");
        e2.setSalesman("s5");

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

    public int nextId() {
        return Collections.max(this.events.keySet()) + 1;
    }

    public void add(Event event) {
        events.put(event.getId(), event);
        try {
            this.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLocationAvailable(Event event, Location location) {
        try {
            locationDAO.loadAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(Event e : this.events.values()) {
            Location l = e.getLocation();
            if(e.getId() != event.getId()) {
                if(e.getStartTime().equals(event.getStartTime()) && l.getLatitude() == location.getLatitude() && l.getLongitude() == location.getLongitude()) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Event> getAvailableEventsForSalesman(String salesman) {
        ArrayList<Event> availableEvents = new ArrayList<>();
        for(Event event : this.events.values()) {
            if(event.getSalesman().equals(salesman)) {
                availableEvents.add(event);
            }
        }
        return availableEvents;
    }
}
