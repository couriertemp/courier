package uz.courier.models;

import uz.courier.api.response.models.DocumentType;
import com.google.gson.annotations.SerializedName;

public class Document {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private DocumentType type;

    @SerializedName("file")
    private File file;

    public int getId() {
        return id;
    }

    public DocumentType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
