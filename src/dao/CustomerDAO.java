package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Admin;
import model.Customer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CustomerDAO {
    private HashMap<String, Customer> customers;

    public CustomerDAO() {
        customers = new HashMap<String, Customer>();

        try {
            loadAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAll() throws FileNotFoundException {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<String, Customer>>(){}.getType();
        BufferedReader br = new BufferedReader(new FileReader("files/customers.json"));
        this.customers = gson.fromJson(br, token);
    }

    public Customer findOne(String username, String password) {
//        for (Map.Entry<String, Customer> entry : customers.entrySet()) {
//            if(entry.getValue().getUsername().equals(username) && entry.getValue().getPassword().equals(password)) {
//                return entry.getValue();
//            }
//        }
        return null;
    }

}
