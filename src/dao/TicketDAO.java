package dao;

import com.google.gson.Gson;
import model.Ticket;
import model.TicketType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TicketDAO {
    private HashMap<String, Ticket> tickets;

    public TicketDAO() {
        this.tickets = new HashMap<>();
    }


    public ArrayList<String> createOrder(String username, String eventId, String amount, String ticketType, double regularPrice) {
        ArrayList<String> ticketIds = new ArrayList<>();

        for(int i = 0; i < Integer.parseInt(amount); i++) {
            Ticket ticket = new Ticket();
            ticket.setId(this.createId());
            ticket.setReserved(true);
            ticket.setCustomer(username);
            ticket.setEvent(Integer.parseInt(eventId));
            
            if(ticketType.equals("REGULAR")) {
                ticket.setType(TicketType.REGULAR);
                ticket.setPrice(regularPrice);
            } else if(ticketType.equals("FANPIT")) {
                ticket.setType(TicketType.FANPIT);
                ticket.setPrice(2*regularPrice);
            } else {
                ticket.setType(TicketType.VIP);
                ticket.setPrice(4*regularPrice);
            }
            
            tickets.put(ticket.getId(), ticket);
            ticketIds.add(ticket.getId());
        }
        try {
            this.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ticketIds;
    }

    private void writeAll() throws IOException {
        Gson gson = new Gson();
        FileWriter fw = new FileWriter("files/tickets.json");
        gson.toJson(this.tickets, fw);
        fw.flush();
        fw.close();
    }

    private String createId() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
