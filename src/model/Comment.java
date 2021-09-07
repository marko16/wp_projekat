package model;

public class Comment {
    private int id;
    private String customer;
    private int event;
    private String text;
    private short rating;
    private int approved;
    private boolean deleted;
    private String buyer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }
}
