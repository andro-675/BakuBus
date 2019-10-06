package tutorial.root.pojo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("BUS")
    private List<Bus> busList;

    public List<Bus> getBusList() {
        return busList;
    }

}
