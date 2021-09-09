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
        Location l1 = new Location();
        l1.setCity("Novi Sad");
        l1.setLatitude(45.7979994);
        l1.setLongitude(15.9530886);
        l1.setNumber("11");
        l1.setZipcode(21000);
        l1.setStreet("Kineska cetvrt");

        Location l2 = new Location();
        l2.setCity("Novi Sad");
        l2.setLatitude(45.2596599);
        l2.setLongitude(19.8331895);
        l2.setNumber("43");
        l2.setZipcode(21000);
        l2.setStreet("Bulevar oslobodjenja");

        locations.put(1, l1);
        locations.put(2, l2);

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
