package uz.courier.models;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("icon")
    private String icon;

    @SerializedName("small")
    private String small;

    @SerializedName("low")
    private String low;

    @SerializedName("normal")
    private String normal;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }
}
