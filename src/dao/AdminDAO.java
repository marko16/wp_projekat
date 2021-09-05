package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Admin;

import java.io.*;
import java.lang.reflect.Type;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public void loadAll() throws FileNotFoundException {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<String, Admin>>(){}.getType();
        BufferedReader br = new BufferedReader(new FileReader("files/admins.json"));
        this.admins = gson.fromJson(br, token);
    }

    public Admin findOne(String username, String password) {
        for (Map.Entry<String, Admin> entry : admins.entrySet()) {
            if(entry.getValue().getUsername().equals(username) && entry.getValue().getPassword().equals(password)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
