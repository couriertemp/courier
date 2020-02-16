package uz.courier.models;

import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;

public class Place {
    private String placeId;
    private String placeText;
    private LatLng latLng;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceText() {
        return placeText;
    }

    public void setPlaceText(String placeText) {
        this.placeText = placeText;
    }
    public String toString(){
        return placeText;
    }

}