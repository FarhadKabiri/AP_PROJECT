package Project;

import java.util.List;

public class RGenericAttenuationModel {
    private List<RLocation> buildingLocations;
    private List<Double> magnitudes;
    private List<RLocation> hypocentreLocations;

    private final double theta1 = 1.02;
    private final double theta2 = 0.249;
    private final double theta3 = 1.0;
    private final double theta4 = 0.00255;

    // Constructor
    public RGenericAttenuationModel(List<RLocation> buildingLocations, List<Double> magnitudes, List<RLocation> hypocentreLocations) {
        this.buildingLocations = buildingLocations;
        this.magnitudes = magnitudes;
        this.hypocentreLocations = hypocentreLocations;
    }

    // Method to calculate the distance between a building and a hypocentre
    private double calculateDistance(RLocation building, RLocation hypocentre) {
        double latDiff = building.getLatitude() - hypocentre.getLatitude();
        double lonDiff = building.getLongitude() - hypocentre.getLongitude();
        double altDiff = building.getAltitude() - hypocentre.getAltitude();
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff + altDiff * altDiff);
    }

    // Method to calculate PGA intensities at each building
    public double[] calculateIntensities() {
        double[] intensities = new double[buildingLocations.size()];
        for (int i = 0; i < buildingLocations.size(); i++) {
            RLocation building = buildingLocations.get(i);
            double intensitySum = 0.0;

            for (int j = 0; j < hypocentreLocations.size(); j++) {
                RLocation hypocentre = hypocentreLocations.get(j);
                double magnitude = magnitudes.get(j);
                double R = calculateDistance(building, hypocentre);
                intensitySum += Math.pow(10, (-theta1 + theta2 * magnitude - theta3 * Math.log10(R) + theta4 * R));
            }
            intensities[i] = intensitySum;
        }
        return intensities;
    }
}
