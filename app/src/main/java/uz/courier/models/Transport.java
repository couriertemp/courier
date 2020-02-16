package uz.courier.models;

import com.google.gson.annotations.SerializedName;

public class Transport {

    @SerializedName("id")
    private int id;

    @SerializedName("transport_type")
    private TransportType type;

    @SerializedName("transport_mark")
    private String mark;

    @SerializedName("name")
    private String name;

    @SerializedName("number")
    private String number;

    @SerializedName("weight_min")
    private int weightMin;

    @SerializedName("weight_max")
    private int weightMax;

    @SerializedName("image")
    private Image image;

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

    public TransportType getType() {
        return type;
    }

    public void setType(TransportType type) {
        this.type = type;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getWeightMin() {
        return weightMin;
    }

    public void setWeightMin(int weightMin) {
        this.weightMin = weightMin;
    }

    public int getWeightMax() {
        return weightMax;
    }

    public void setWeightMax(int weightMax) {
        this.weightMax = weightMax;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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
