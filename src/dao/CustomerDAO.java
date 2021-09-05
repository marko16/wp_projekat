package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Admin;
import model.Customer;
import model.CustomerType;
import model.Gender;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
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
        
//        try {
//            writeAll();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
    }

    private void writeAll() throws IOException {
        Customer c1 = new Customer();
        c1.setUsername("c1");
        c1.setPassword("123");
        c1.setFirstName("Mile");
        c1.setLastName("Kitic");
        c1.setBlocked(false);
        c1.setPoints(20);
        c1.setSus(false);
        c1.setBirthday(new Date(1998, Calendar.JANUARY, 2));
        c1.setGender(Gender.MALE);

        Customer c2 = new Customer();
        c2.setFirstName("Toma");
        c2.setLastName("Zdravkovic");
        c2.setGender(Gender.FEMALE);
        c2.setUsername("c2");
        c2.setPassword("123");
        c2.setBlocked(false);
        c2.setPoints(20);
        c2.setSus(false);
        c2.setBirthday(new Date(1997, Calendar.JANUARY, 2));

        customers.put("c1", c1);
        customers.put("c2", c2);

        Gson gson = new Gson();
        FileWriter fw = new FileWriter("files/customers.json");
        gson.toJson(this.customers, fw);
        fw.flush();
        fw.close();
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
