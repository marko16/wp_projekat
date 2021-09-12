package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Event;
import model.Location;
import model.Ticket;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class EventDAO {
    private HashMap<Integer, Event> events;
    private LocationDAO locationDAO = new LocationDAO();

    public EventDAO() {
        events = new HashMap<>();
        loadAll();
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
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
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
        events = new HashMap<>();


        Event e1 = new Event();
        e1.setId(1);
        e1.setActive(true);
        e1.setCapacity(230);
        e1.setAvailableTickets(0);
        e1.setLocation(locationDAO.findOne(1));
        e1.setRegularPrice(300);
        e1.setName("Grad kulture");
        e1.setStartTime(new Date(121, Calendar.SEPTEMBER, 22));
        e1.setEventType("Cultural event");
        e1.setPoster("images/e1.jfif");
        e1.setSalesman("s5");
        e1.setDeleted(false);

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
        e2.setDeleted(false);

        Event e3 = new Event();
        e3.setId(3);
        e3.setActive(true);
        e3.setCapacity(100);
        e3.setAvailableTickets(50);
        e3.setLocation(locationDAO.findOne(3));
        e3.setRegularPrice(400);
        e3.setName("Manifestacija u Novom Sadu");
        e3.setStartTime(new Date(121, Calendar.SEPTEMBER, 19));
        e3.setEventType("Concert");
        e3.setPoster("images/e3.png");
        e3.setSalesman("s5");
        e3.setDeleted(false);

        events.put(1, e1);
        events.put(2, e2);
        events.put(3, e3);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
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
            if(event.getSalesman().equals(salesman) && event.isActive()) {
                availableEvents.add(event);
            }
        }
        return availableEvents;
    }

    public boolean delete(int id) {
        Event event = events.get(id);
        if(event.isDeleted()) return false;

        event.setDeleted(true);
        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<Event> search(String search) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String finalSearch = search.toLowerCase();

        return events.values().stream().filter(x ->
            x.getName().toLowerCase().contains(finalSearch) || sdf.format(x.getStartTime()).contains(finalSearch) ||
                    String.valueOf(x.getRegularPrice()).contains(finalSearch) ||
                    x.getLocation().getCity().toLowerCase().concat(x.getLocation().getStreet().toLowerCase())
                        .concat(x.getLocation().getNumber()).concat(String.valueOf(x.getLocation().getZipcode())).contains(finalSearch))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Event> getAdminEvents() {
        return new ArrayList<>(events.values());
    }

    public boolean activate(int id) {
        Event event = events.get(id);
        if(event.isActive()) return false;

        event.setActive(true);
        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
