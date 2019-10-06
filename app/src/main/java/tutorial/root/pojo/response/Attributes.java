package tutorial.root.pojo.response;

import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("BUS_ID")
    private String busID;

    @SerializedName("PLATE")
    private String plate;

    @SerializedName("DRIVER_NAME")
    private String driverName;

    @SerializedName("CURRENT_STOP")
    private String currentStop;

    @SerializedName("PREV_STOP")
    private String previousStop;

    @SerializedName("SPEED")
    private String speed;

    @SerializedName("BUS_MODEL")
    private String busModel;

    @SerializedName("LATITUDE")
    private String latitude;

    @SerializedName("LONGITUDE")
    private String longitude;

    @SerializedName("ROUTE_NAME")
    private String routeName;

    @SerializedName("LAST_UPDATE_TIME")
    private String lastUpdateTime;

    @SerializedName("DISPLAY_ROUTE_CODE")
    private String displayRouteCode;

    @SerializedName("SVCOUNT")
    private String svCount;

    public String getBusID() {
        return busID;
    }

    public String getPlate() {
        return plate;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getCurrentStop() {
        return currentStop;
    }

    public String getPreviousStop() {
        return previousStop;
    }

    public String getSpeed() {
        return speed;
    }

    public String getBusModel() {
        return busModel;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getDisplayRouteCode() {
        return displayRouteCode;
    }

    public String getSvCount() {
        return svCount;
    }
}
