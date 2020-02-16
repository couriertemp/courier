package uz.courier.api.response;

import com.google.gson.annotations.SerializedName;

public class DataResponse<T> {

    public static int STATUS_SUCCESS = 1;
    public static int STATUS_ERROR = -1;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private T data;

    public boolean isSuccess() {
        return code > 0;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
