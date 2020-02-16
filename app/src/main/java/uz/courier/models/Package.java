package uz.courier.models;

import com.google.gson.annotations.SerializedName;

public class Package {

    private static final int PAYMENT_TYPE_CASH = 1;
    private static final int PAYMENT_TYPE_CARD = 2;

    @SerializedName("id")
    private int id;

    @SerializedName("from")
    private String from;

    @SerializedName("to")
    private String to;

    @SerializedName("transport_type_id")
    private int transportTypeId;

    @SerializedName("start_date")
    private int startDate;

    @SerializedName("end_date")
    private int endDate;

    @SerializedName("weight")
    private int weight;

    @SerializedName("description")
    private String description;

    @SerializedName("distance")
    private int distance;

    @SerializedName("payment_type")
    private int paymentType;

    @SerializedName("start_lat")
    private float startLat;

    @SerializedName("start_lon")
    private float startLon;

    @SerializedName("end_lat")
    private float endLat;

    @SerializedName("end_lon")
    private float endLon;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("created_at")
    private int createdAt;

    @SerializedName("updated_at")
    private int updatedAt;

    @SerializedName("status")
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTransportTypeId() {
        return transportTypeId;
    }

    public void setTransportTypeId(int transportTypeId) {
        this.transportTypeId = transportTypeId;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public float getStartLat() {
        return startLat;
    }

    public void setStartLat(float startLat) {
        this.startLat = startLat;
    }

    public float getStartLon() {
        return startLon;
    }

    public void setStartLon(float startLon) {
        this.startLon = startLon;
    }

    public float getEndLat() {
        return endLat;
    }

    public void setEndLat(float endLat) {
        this.endLat = endLat;
    }

    public float getEndLon() {
        return endLon;
    }

    public void setEndLon(float endLon) {
        this.endLon = endLon;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public int getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(int updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
