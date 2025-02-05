package Project;

public class RLocation {
    private String objectName;
    private double latitude;
    private double longitude;
    private double altitude;
    private String address;

    // Constructor
    public RLocation(String objectName, double latitude, double longitude, double altitude, String address) {
        if (!isValidName(objectName)) {
            throw new IllegalArgumentException("Invalid object name. Only letters, numbers, and underscores are allowed.");
        }
        this.objectName = objectName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.address = address;
    }

    // Getters and Setters
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        if (!isValidName(objectName)) {
            throw new IllegalArgumentException("Invalid object name.");
        }
        this.objectName = objectName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Method to validate object name
    private boolean isValidName(String name) {
        return name.matches("[A-Za-z0-9_]+");
    }

    // Method to remove the object (simulation of right-click menu functionality)
    public void remove() {
        System.out.println("Object " + objectName + " removed.");
        // Additional logic for removal can be added here
    }

    @Override
    public String toString() {
        return "RLocation{" +
                "objectName='" + objectName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", address='" + address + '\'' +
                '}';
    }
}
