package uz.courier.models;

import com.google.gson.annotations.SerializedName;

public class File {

    @SerializedName("id")
    private int id;

    @SerializedName("ext")
    private String ext;

    @SerializedName("src")
    private String src;

    @SerializedName("thumbnails")
    private Image thumbnails;

    public Image getThumbnails() {
        return thumbnails;
    }
}
