package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dto.UserDTO;
import model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SalesmanDAO {
    private HashMap<String, Salesman> salesmen;

    public SalesmanDAO() {
        salesmen = new HashMap<String, Salesman>();

        try {
            loadAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAll() throws IOException {
        Salesman s1 = new Salesman();
        s1.setUsername("s1");
        s1.setFirstName("Ivana");
        s1.setLastName("Manojlovic");
        s1.setPassword("123");
        s1.setBlocked(false);
        s1.setGender(Gender.FEMALE);
        s1.setBirthday(new Date(1978, Calendar.JUNE, 22));

        Salesman s2 = new Salesman();
        s2.setUsername("s2");
        s2.setFirstName("Mira");
        s2.setLastName("Miric");
        s2.setPassword("123");
        s2.setBlocked(false);
        s2.setGender(Gender.FEMALE);
        s2.setBirthday(new Date(1976, Calendar.JUNE, 22));

        salesmen.put("s1", s1);
        salesmen.put("s2", s2);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        FileWriter fw = new FileWriter("files/salesmen.json");
        gson.toJson(this.salesmen, fw);
        fw.flush();
        fw.close();
    }

    public HashMap<String, Salesman> loadAll() throws FileNotFoundException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Type token = new TypeToken<HashMap<String, Salesman>>(){}.getType();
        BufferedReader br = new BufferedReader(new FileReader("files/salesmen.json"));
        this.salesmen = gson.fromJson(br, token);
        return this.salesmen;
    }

    public Salesman login(String username, String password) {
        Salesman salesman = salesmen.getOrDefault(username, null);
        if(salesman != null && salesman.getPassword().equals(password)) {
            return salesman;
        }
        return null;
    }

    public Salesman findOne(String username) {
        return this.salesmen.getOrDefault(username, null);
    }

    public void addSalesman(Salesman salesman) {
        salesman.setBlocked(false);

        salesmen.put(salesman.getUsername(), salesman);

        try {
            this.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEventToSalesman(Event event) {
        Salesman salesman = salesmen.get(event.getSalesman());

        if(salesman.getEvents() == null) salesman.setEvents(new ArrayList<>());
        salesman.getEvents().add(event.getId());
    }

    public boolean editProfile(String username, String firstName, String lastName, String birthday, String gender) {
        Salesman salesman = salesmen.get(username);
        salesman.setFirstName(firstName);
        salesman.setLastName(lastName);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        salesman.setBirthday(date);
        salesman.setGender(Gender.valueOf(gender.toUpperCase(Locale.ROOT)));
        return true;
    }

    public ArrayList<UserDTO> getUsersAdmin() {
        ArrayList<UserDTO> users = new ArrayList<>();

        for(Salesman salesman : this.salesmen.values()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(salesman.getUsername());
            userDTO.setFirstName(salesman.getFirstName());
            userDTO.setLastName(salesman.getLastName());
            userDTO.setRole(Role.SALESMAN);
            userDTO.setPoints(-1);
            userDTO.setUserType(null);
            userDTO.setBlocked(salesman.isBlocked());

            users.add(userDTO);
        }

        return users;
    }

    public boolean block(String username) {
        Salesman s = salesmen.get(username);
        if(s.isBlocked()) return false;
        s.setBlocked(true);
        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean unblock(String username) {
        Salesman s = salesmen.get(username);
        if(!s.isBlocked()) return false;
        s.setBlocked(false);
        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
