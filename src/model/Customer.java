package model;

import java.util.ArrayList;

public class Customer extends User {
    private ArrayList<Ticket> tickets;
    private int points;
    private CustomerType customerType;
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

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
}
