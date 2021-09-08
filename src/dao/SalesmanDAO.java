package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Customer;
import model.Gender;
import model.Salesman;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
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

//        try {
//            writeAll();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

        Gson gson = new Gson();
        FileWriter fw = new FileWriter("files/salesmen.json");
        gson.toJson(this.salesmen, fw);
        fw.flush();
        fw.close();
    }

    public void loadAll() throws FileNotFoundException {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<String, Customer>>(){}.getType();
        BufferedReader br = new BufferedReader(new FileReader("files/salesmen.json"));
        this.salesmen = gson.fromJson(br, token);
    }

    public Salesman login(String username, String password) {
//        for (Map.Entry<String, Salesman> entry : salesmen.entrySet()) {
//            if(entry.getValue().getUsername().equals(username) && entry.getValue().getPassword().equals(password)) {
//                return entry.getValue();
//            }
//        }
        return null;
    }

    public Salesman findOne(String username) {
        return this.salesmen.getOrDefault(username, null);
    }

}
