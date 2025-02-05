package Project;

import java.util.Random;

public class RSingleLineSourceModel {
    private RLocation point1Location;
    private RLocation point2Location;
    private double theta;
    private double depth;
    private RLocation outputLocation;

    // Constructor
    public RSingleLineSourceModel(RLocation point1Location, RLocation point2Location, double depth) {
        this.point1Location = point1Location;
        this.point2Location = point2Location;
        this.theta = 0.5; // Can be Random variable between 0 and 1
        this.depth = depth;
        generateOutputLocation();
    }

    // Method to generate the output location based on theta
    private void generateOutputLocation() {
        double latitude = point1Location.getLatitude() + theta * (point2Location.getLatitude() - point1Location.getLatitude());
        double longitude = point1Location.getLongitude() + theta * (point2Location.getLongitude() - point1Location.getLongitude());
        double altitude = depth;
        this.outputLocation = new RLocation("Response", latitude, longitude, altitude);
    }

    public RLocation getPoint1Location() {
        return point1Location;
    }

    public void setPoint1Location(RLocation point1Location) {
        this.point1Location = point1Location;
        generateOutputLocation();
    }

    public RLocation getPoint2Location() {
        return point2Location;
    }

    public void setPoint2Location(RLocation point2Location) {
        this.point2Location = point2Location;
        generateOutputLocation();
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        if (theta < 0 || theta > 1) {
            throw new IllegalArgumentException("Theta must be between 0 and 1");
        }
        this.theta = theta;
        generateOutputLocation();
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public RLocation getOutputLocation() {
        return outputLocation;
    }

    @Override
    public String toString() {
        return "RSingleLineSourceModel{" +
                ", point1Location=" + point1Location +
                ", point2Location=" + point2Location +
                ", theta=" + theta +
                ", depth=" + depth +
                ", outputLocation=" + outputLocation +
                '}';
    }
}
