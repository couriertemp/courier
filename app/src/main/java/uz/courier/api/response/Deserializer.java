package uz.courier.api.response;

import uz.courier.api.response.models.Error;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class Deserializer implements JsonDeserializationContext {
    @Override
    public <T> T deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
        JsonObject responseObject = json.getAsJsonObject();

//        if (responseObject.get("messages").equals("error")) {
            Error error = new Error();
            error.setParamName(responseObject.get("data").getAsJsonObject().get("item").getAsString());
            return (T) error;
//        }
    }
}
