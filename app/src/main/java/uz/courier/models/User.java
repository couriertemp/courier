package uz.courier.models;

import android.util.Patterns;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("password")
    private String password;

    @SerializedName("token")
    private String token;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("country")
    private Country country;

    @SerializedName("region")
    private Region region;

    @SerializedName("image")
    private Image image;

    @SerializedName("created_at")
    private int createdAt;

    @SerializedName("updated_at")
    private int updatedAt;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }

    public String getPassword() {
        return password;
    }

    public boolean isPasswordLengthValid() {
        return getPassword().length() > 5;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getPhone() {
        return phone;
    }

    public Image getImage() {
        return image;
    }

}
