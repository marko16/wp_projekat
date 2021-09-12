package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class LocationDAO {
    HashMap<Integer, Location> locations;

    public LocationDAO() {
        locations = new HashMap<>();

        try {
          loadAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        try {
//            writeAll();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void writeAll() throws IOException {
//        locations = new HashMap<>();
//        Location l1 = new Location();
//        l1.setCity("Novi Sad");
//        l1.setLatitude(45.2361365);
//        l1.setLongitude(19.838948636610578);
//        l1.setNumber("7");
//        l1.setZipcode(21000);
//        l1.setStreet("Bulevar despota Stefana");
//
//        Location l2 = new Location();
//        l2.setCity("Novi Sad");
//        l2.setLatitude(45.2596599);
//        l2.setLongitude(19.8331895);
//        l2.setNumber("43");
//        l2.setZipcode(21000);
//        l2.setStreet("Bulevar oslobodjenja");
//
//        Location l3 = new Location();
//        l3.setCity("Novi Sad");
//        l3.setLatitude(45.2549067);
//        l3.setLongitude(19.8345434);
//        l3.setNumber("5");
//        l3.setZipcode(21000);
//        l3.setStreet("Novosadskog sajma");
//
//        locations.put(1, l1);
//        locations.put(2, l2);
//        locations.put(3, l3);

        Gson gson = new Gson();
        FileWriter fw = new FileWriter("files/locations.json");
        gson.toJson(this.locations, fw);
        fw.flush();
        fw.close();
    }

    public void loadAll() throws FileNotFoundException {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<Integer,Location>>(){}.getType();
        BufferedReader br = new BufferedReader(new FileReader("files/locations.json"));
        this.locations = gson.fromJson(br, token);
    }

    public int nextId() {
        return Collections.max(this.locations.keySet()) + 1;
    }

    public void add(Location location) {
        locations.put(location.getId(), location);
        try {
            this.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location findOne(int location) {
        return locations.getOrDefault(location, null);
    }
}
