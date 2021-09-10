package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Admin;
import model.Gender;

import java.io.*;
import java.lang.reflect.Type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminDAO {
    private HashMap<String, Admin> admins;

    public AdminDAO() {
        admins = new HashMap<String, Admin>();

        try {
            loadAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Admin> loadAll() throws FileNotFoundException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Type token = new TypeToken<HashMap<String, Admin>>(){}.getType();
        BufferedReader br = new BufferedReader(new FileReader("files/admins.json"));
        this.admins = gson.fromJson(br, token);
        return this.admins;
    }

    public Admin login(String username, String password) {
        for (Map.Entry<String, Admin> entry : admins.entrySet()) {
            if(entry.getValue().getUsername().equals(username) && entry.getValue().getPassword().equals(password)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Admin findOne(String username) {
        return admins.getOrDefault(username, null);
    }

    public void writeAll() {
        Gson gson = new Gson();
        try {
            FileWriter fw = new FileWriter("files/admins.json");
            gson.toJson(this.admins, fw);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean editProfile(String username, String firstName, String lastName, String birthday, String gender) {
        Admin admin = admins.get(username);
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        admin.setBirthday(date);
        admin.setGender(Gender.valueOf(gender.toUpperCase(Locale.ROOT)));
        return true;
    }
}
