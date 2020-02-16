package uz.courier.models;

import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("iso2")
    private String iso2;

    @SerializedName("iso3")
    private String iso3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }
}
