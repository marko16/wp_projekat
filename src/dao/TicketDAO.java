package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.TicketDTO;
import model.Event;
import model.Salesman;
import model.Ticket;
import model.TicketType;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TicketDAO {
    private HashMap<String, Ticket> tickets;

    public TicketDAO() {
        this.tickets = new HashMap<>();
        loadAll();
    }

    public void loadAll() {
        Gson gson = new Gson();
        Type token = new TypeToken<HashMap<String, Ticket>>(){}.getType();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("files/tickets.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert br != null;
        this.tickets = gson.fromJson(br, token);
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

    public ArrayList<TicketDTO> getTicketsOfSalesmanEvents(String salesmanUsername, HashMap<Integer, Event> events) {
        ArrayList<TicketDTO> retVal = new ArrayList<>();
        for(Ticket ticket : tickets.values()) {
            if(events.get(ticket.getEvent()).getSalesman().equals(salesmanUsername)) {
                System.out.println("|ua");
                createTicketDTO(events, retVal, ticket);
            }
        }
        return retVal;
    }

    public ArrayList<TicketDTO> getTicketsOfCustomer(String customerUsername, HashMap<Integer, Event> events) {
        ArrayList<TicketDTO> retVal = new ArrayList<>();
        for(Ticket ticket : tickets.values()) {
            if(ticket.getCustomer().equals(customerUsername)) {
                createTicketDTO(events, retVal, ticket);
            }
        }
        return retVal;
    }

    private void createTicketDTO(HashMap<Integer, Event> events, ArrayList<TicketDTO> retVal, Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        Event event = events.get(ticket.getEvent());
        dto.setId(ticket.getId());
        dto.setEventName(event.getName());
        dto.setEventDate(event.getStartTime());
        dto.setCustomerUsername(ticket.getCustomer());
        dto.setPrice(ticket.getPrice());
        dto.setType(ticket.getType());
        dto.setReserved(ticket.isReserved());

        retVal.add(dto);
    }

    public Ticket findOne(String id) {
        return tickets.getOrDefault(id, null);
    }

    public void cancelReservation(String id) {
        Ticket t = tickets.get(id);
        t.setReserved(false);
        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TicketDTO> getTickets(HashMap<Integer, Event> events) {
        ArrayList<TicketDTO> retVal = new ArrayList<>();
        for(Ticket ticket : tickets.values()) {
                createTicketDTO(events, retVal, ticket);
        }
        return retVal;
    }

    public boolean delete(String ticketId) {
        Ticket t = tickets.get(ticketId);
        if(!t.isReserved()) return false;

        t.setReserved(false);
        try {
            writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
