package Project;

import java.util.Random;

public class RSingleLineSourceModel {
    private boolean displayOutput;
    private RLocation point1Location;
    private RLocation point2Location;
    private double theta;
    private double depth;
    private double latitudeGeometricalUncertainty;
    private double longitudeGeometricalUncertainty;
    private RLocation outputLocation;

    // Constructor
    public RSingleLineSourceModel(boolean displayOutput, RLocation point1Location, RLocation point2Location, double depth,
                                  double latitudeGeometricalUncertainty, double longitudeGeometricalUncertainty) {
        this.displayOutput = displayOutput;
        this.point1Location = point1Location;
        this.point2Location = point2Location;
        this.theta = 0.5; // Can be Random variable between 0 and 1
        this.depth = depth;
        this.latitudeGeometricalUncertainty = latitudeGeometricalUncertainty;
        this.longitudeGeometricalUncertainty = longitudeGeometricalUncertainty;
        generateOutputLocation();
    }

    // Method to generate the output location based on theta
    private void generateOutputLocation() {
        double latitude = point1Location.getLatitude() + theta * (point2Location.getLatitude() - point1Location.getLatitude());
        double longitude = point1Location.getLongitude() + theta * (point2Location.getLongitude() - point1Location.getLongitude());
        double altitude = point1Location.getAltitude() + theta * (point2Location.getAltitude() - point1Location.getAltitude());
        this.outputLocation = new RLocation("Response", latitude, longitude, altitude, "");
    }

    // Getters and Setters
    public boolean isDisplayOutput() {
        return displayOutput;
    }

    public void setDisplayOutput(boolean displayOutput) {
        this.displayOutput = displayOutput;
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

    public double getLatitudeGeometricalUncertainty() {
        return latitudeGeometricalUncertainty;
    }

    public void setLatitudeGeometricalUncertainty(double latitudeGeometricalUncertainty) {
        this.latitudeGeometricalUncertainty = latitudeGeometricalUncertainty;
    }

    public double getLongitudeGeometricalUncertainty() {
        return longitudeGeometricalUncertainty;
    }

    public void setLongitudeGeometricalUncertainty(double longitudeGeometricalUncertainty) {
        this.longitudeGeometricalUncertainty = longitudeGeometricalUncertainty;
    }

    public RLocation getOutputLocation() {
        return outputLocation;
    }

    @Override
    public String toString() {
        return "RSingleLineSourceModel{" +
                "displayOutput=" + displayOutput +
                ", point1Location=" + point1Location +
                ", point2Location=" + point2Location +
                ", theta=" + theta +
                ", depth=" + depth +
                ", latitudeGeometricalUncertainty=" + latitudeGeometricalUncertainty +
                ", longitudeGeometricalUncertainty=" + longitudeGeometricalUncertainty +
                ", outputLocation=" + outputLocation +
                '}';
    }
}
