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
import java.util.stream.Collectors;

public class CustomerDAO {
    private HashMap<String, Customer> customers;
    private HashMap<String, CustomerType> customerTypes;

    public CustomerDAO() {
        customers = new HashMap<String, Customer>();
        customerTypes = new HashMap<>();

        try {
            writeAll();
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            loadAll();
            loadTypes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }

    private void loadTypes() throws FileNotFoundException {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<String, CustomerType>>(){}.getType();
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
        c1.setCustomerType("REGULAR");

        Customer c2 = new Customer();
        c2.setCustomerType("REGULAR");
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

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        FileWriter fw = new FileWriter("files/customers.json");
        gson.toJson(this.customers, fw);
        fw.flush();
        fw.close();
    }

    public HashMap<String, Customer> loadAll() throws FileNotFoundException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Type token = new TypeToken<HashMap<String, Customer>>(){}.getType();
        BufferedReader br = new BufferedReader(new FileReader("files/customers.json"));
        this.customers = gson.fromJson(br, token);
        return this.customers;
    }

    public Customer login(String username, String password) {
        for (Map.Entry<String, Customer> entry : customers.entrySet()) {
            if(entry.getValue().getUsername().equals(username) && entry.getValue().getPassword().equals(password)) {
                return entry.getValue();
            }
        }
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
            System.out.println("ajde" + points);

        }

        Customer c = customers.get(username);
//        c.getTickets().addAll(tickets);

        c.setPoints(c.getPoints() + points);
        if(c.getCustomerType().equals("REGULAR") && c.getPoints() >= this.customerTypes.get("BRONZE").getPointThreshold()) {
            c.setCustomerType("BRONZE");
        } else if(c.getCustomerType().equals("BRONZE") && c.getPoints() >= this.customerTypes.get("SILVER").getPointThreshold()) {
            c.setCustomerType("SILVER");
        } else if(c.getCustomerType().equals("SILVER") && c.getPoints() >= this.customerTypes.get("GOLD").getPointThreshold()) {
            c.setCustomerType("GOLD");
        }

        try {
            this.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(Customer customer) {
        customer.setPoints(0);
        customer.setBlocked(false);
        customer.setSus(false);
        customer.setTickets(new ArrayList<>());
        customer.setCustomerType("REGULAR");
        customers.put(customer.getUsername(), customer);


        try {
            this.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeProfile(String username) {
        customers.get(username);
    }

    public boolean editProfile(String username, String firstName, String lastName, String birthday, String gender) {
        Customer customer = customers.get(username);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        customer.setBirthday(date);
        customer.setGender(Gender.valueOf(gender.toUpperCase(Locale.ROOT)));
        return true;
    }

    public ArrayList<UserDTO> getUsersAdmin() {
        ArrayList<UserDTO> users = new ArrayList<>();

        for(Customer customer : this.customers.values()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(customer.getUsername());
            userDTO.setFirstName(customer.getFirstName());
            userDTO.setLastName(customer.getLastName());
            userDTO.setRole(Role.CUSTOMER);
            userDTO.setPoints(customer.getPoints());
            userDTO.setUserType(customer.getCustomerType());
            userDTO.setBlocked(customer.isBlocked());

            users.add(userDTO);
        }

        return users;
    }

    public boolean block(String username) {
        Customer c = customers.get(username);
        if(c.isBlocked()) return false;
        c.setBlocked(true);
        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean unblock(String username) {
        Customer c = customers.get(username);
        if(!c.isBlocked()) return false;
        c.setBlocked(false);
        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Collection<UserDTO> search(String search) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String finalSearch = search.toLowerCase();
        ArrayList<UserDTO> userDTOS = new ArrayList<>();

        ArrayList<Customer> toConvert =  customers.values().stream().filter(x ->
                x.getFirstName().toLowerCase().contains(finalSearch) || x.getLastName().toLowerCase().contains(finalSearch) ||
                        x.getUsername().toLowerCase().contains(finalSearch))
                .collect(Collectors.toCollection(ArrayList::new));

        for(Customer c : toConvert) {
            UserDTO userDTO = new UserDTO();

            userDTO.setBlocked(c.isBlocked());
            userDTO.setUserType(c.getCustomerType());
            userDTO.setUsername(c.getUsername());
            userDTO.setLastName(c.getLastName());
            userDTO.setFirstName(c.getFirstName());
            userDTO.setRole(Role.CUSTOMER);
            userDTO.setPoints(c.getPoints());

            userDTOS.add(userDTO);
        }

        return userDTOS;
    }
}
