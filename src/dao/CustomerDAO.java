package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Admin;
import model.Customer;
import model.CustomerType;
import model.Gender;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class CustomerDAO {
    private HashMap<String, Customer> customers;
    private HashMap<String, CustomerType> customerTypes;

    public CustomerDAO() {
        customers = new HashMap<String, Customer>();
        customerTypes = new HashMap<>();

        try {
            loadAll();
            loadTypes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
//        try {
//            writeAll();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
    }

    private void loadTypes() throws FileNotFoundException {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<String, Customer>>(){}.getType();
        BufferedReader br = new BufferedReader(new FileReader("files/customer_types.json"));
        this.customerTypes = gson.fromJson(br, token);
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

    public Customer login(String username, String password) {
//        for (Map.Entry<String, Customer> entry : customers.entrySet()) {
//            if(entry.getValue().getUsername().equals(username) && entry.getValue().getPassword().equals(password)) {
//                return entry.getValue();
//            }
//        }
        return null;
    }

    public Customer findOne(String username) {
        return customers.getOrDefault(username, null);
    }

    public void addPoints(ArrayList<String> tickets, String ticketType, String amount, double regularPrice, String username) {
        int points = 0;
        if(ticketType.equals("REGULAR")) {
            points = (int) (((Integer.parseInt(amount) * regularPrice) / 1000) * 133 * 4);
        } else if(ticketType.equals("FANPIT")) {
            points = (int) (((Integer.parseInt(amount) * regularPrice) / 1000) * 133 * 4 * 2);
        } else {
            points = (int) (((Integer.parseInt(amount) * regularPrice) / 1000) * 133 * 4 * 4);
        }

        Customer c = this.findOne(username);
        c.getTickets().addAll(tickets);

        c.setPoints(c.getPoints() + points);

        if(c.getCustomerType().equals("REGULAR") && c.getPoints() >= customerTypes.get("BRONZE").getPointThreshold()) {
            c.setCustomerType("BRONZE");
        } else if(c.getCustomerType().equals("BRONZE") && c.getPoints() >= customerTypes.get("SILVER").getPointThreshold()) {
            c.setCustomerType("SILVER");
        } else if(c.getCustomerType().equals("SILVER") && c.getPoints() >= customerTypes.get("SILVER").getPointThreshold()) {
            c.setCustomerType("GOLD");
        }

        try {
            this.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
