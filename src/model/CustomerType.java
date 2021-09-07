package model;

public class CustomerType {
    private String typeName;
    private double discount;
    private int pointThreshold;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getPointThreshold() {
        return pointThreshold;
    }

    public void setPointThreshold(int pointThreshold) {
        this.pointThreshold = pointThreshold;
    }
}
