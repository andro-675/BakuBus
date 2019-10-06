package tutorial.root.pojo.response;

import com.google.gson.annotations.SerializedName;

public class Bus {

    @SerializedName("@attributes")
    private Attributes attributes;

    public Attributes getAttributes() {
        return attributes;
    }

}
