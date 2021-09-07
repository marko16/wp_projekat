package model;

import java.util.ArrayList;

public class Customer extends User {
    private ArrayList<String> tickets;
    private int points;
    private String customerType;
    private boolean blocked;
    private boolean sus;

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isSus() {
        return sus;
    }

    public void setSus(boolean sus) {
        this.sus = sus;
    }

    public ArrayList<String> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<String> tickets) {
        this.tickets = tickets;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}
