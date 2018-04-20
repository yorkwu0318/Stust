package yorkdev.sample.stustday2;


import com.google.gson.annotations.SerializedName;

public class AirQuality {
    public String SiteName;
    public String County;
    public String AQI;
    public String Pollutant;
    public String Status;
    public String SO2;
    public String CO;
    public String CO_8hr;
    public String O3;
    public String O3_8hr;
    public String PM10;
    @SerializedName("PM2.5")
    public String PM25; // FIXME check this code
    public String NO2;
    public String NOx;
    public String NO;
    public String WindSpeed;
    public String WindDirec;
    public String PublishTime;
    @SerializedName("PM2.5_AVG")
    public String PM25_AVG; // FIXME check this code
    public String PM10_AVG;
    public String Latitude;
    public String Longitude;
}
