package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Customer;
import model.Salesman;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class SalesmanDAO {
    private HashMap<String, Salesman> salesmen;

    public SalesmanDAO() {
        salesmen = new HashMap<String, Salesman>();

        try {
            loadAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAll() throws FileNotFoundException {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<String, Customer>>(){}.getType();
        BufferedReader br = new BufferedReader(new FileReader("files/salesmen.json"));
        this.salesmen = gson.fromJson(br, token);
    }

    public Salesman findOne(String username, String password) {
//        for (Map.Entry<String, Salesman> entry : salesmen.entrySet()) {
//            if(entry.getValue().getUsername().equals(username) && entry.getValue().getPassword().equals(password)) {
//                return entry.getValue();
//            }
//        }
        return null;
    }

}
