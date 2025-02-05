package Project;

public class RLocation {
    private String objectName;
    private double latitude;
    private double longitude;
    private double altitude;

    // Constructor
    public RLocation(String objectName, double latitude, double longitude, double altitude) {
        this.objectName = objectName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    // Getters and Setters
    public String getObjectName() {
        return objectName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "RLocation{" +
                "objectName='" + objectName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                '}';
    }
}
