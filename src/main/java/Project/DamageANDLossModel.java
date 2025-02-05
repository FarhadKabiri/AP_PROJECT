package Project;

import java.sql.*;
import java.util.*;

public class DamageANDLossModel {
    private String jdbcURL = "jdbc:mysql://localhost:3306/ap_proj?useUnicode=true&characterEncoding=UTF-8";
    private String dbUser = "root";
    private String dbPassword = "FarhadKabiri";
    private StringBuilder output;

    public DamageANDLossModel() {
        this.output = new StringBuilder();
    }

    public void executeModel() {
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            RLocation point1 = new RLocation("Point1", 36.0, 50.72, 10.0, "Point1 Address");
            RLocation point2 = new RLocation("Point2", 35.82, 51.75, 15.0, "Point2 Address");
            RSingleLineSourceModel sourceModel = new RSingleLineSourceModel(true, point1, point2, 10.0, 0.5, 0.5);
            RLocation hypocenterLocation = sourceModel.getOutputLocation();

            double magnitude = 7;

            Map<String, Map<String, Object>> neighborhoodData = getNeighborhoodData(connection, hypocenterLocation, magnitude);
            saveAndPrintNeighborhoodData(neighborhoodData);
            saveAndPrintTotalData(neighborhoodData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Map<String, Object>> getNeighborhoodData(Connection connection, RLocation hypocenterLocation, double magnitude) throws SQLException {
        Map<String, Map<String, Object>> neighborhoodData = new HashMap<>();
        String sql = "SELECT building_code, latitude, longitude, pga_complete_damage, neighborhood, repair_cost FROM buildings";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        List<RLocation> buildingLocations = new ArrayList<>();
        List<RLocation> hypocenterLocations = new ArrayList<>(Collections.singletonList(hypocenterLocation));
        List<Double> magnitudes = new ArrayList<>(Collections.singletonList(magnitude));

        while (resultSet.next()) {
            String buildingCode = resultSet.getString("building_code");
            double latitude = resultSet.getDouble("latitude");
            double longitude = resultSet.getDouble("longitude");
            double pga = resultSet.getDouble("pga_complete_damage");
            String neighborhood = resultSet.getString("neighborhood");
            double repairCost = resultSet.getDouble("repair_cost");

            buildingLocations.add(new RLocation(buildingCode, latitude, longitude, 0, "Building"));

            RGenericAttenuationModel attenuationModel = new RGenericAttenuationModel(buildingLocations, magnitudes, hypocenterLocations);
            double[] intensities = attenuationModel.calculateIntensities();

            if (intensities[0] > pga) {
                neighborhoodData.putIfAbsent(neighborhood, new HashMap<>(Map.of("damagedBuildings", 0, "totalRepairCost", 0.0)));
                neighborhoodData.get(neighborhood).put("damagedBuildings", (int) neighborhoodData.get(neighborhood).get("damagedBuildings") + 1);
                neighborhoodData.get(neighborhood).put("totalRepairCost", (double) neighborhoodData.get(neighborhood).get("totalRepairCost") + repairCost);
            }
        }
        return neighborhoodData;
    }

    private void saveAndPrintNeighborhoodData(Map<String, Map<String, Object>> neighborhoodData) {
        List<Map.Entry<String, Map<String, Object>>> sortedNeighborhoods = new ArrayList<>(neighborhoodData.entrySet());
        sortedNeighborhoods.sort((e1, e2) -> Double.compare((double) e2.getValue().get("totalRepairCost"), (double) e1.getValue().get("totalRepairCost")));

        output.append("Total number of damaged buildings and repair costs by neighborhood:\n");
        for (Map.Entry<String, Map<String, Object>> entry : sortedNeighborhoods) {
            output.append("Neighborhood: ").append(entry.getKey()).append("\n");
            output.append("  Total damaged buildings: ").append(entry.getValue().get("damagedBuildings")).append("\n");
            output.append("  Total repair cost: ").append(entry.getValue().get("totalRepairCost")).append(" IRR\n");
        }
        output.append("\n");
    }

    private void saveAndPrintTotalData(Map<String, Map<String, Object>> neighborhoodData) {
        int totalDamagedBuildings = neighborhoodData.values().stream().mapToInt(data -> (int) data.get("damagedBuildings")).sum();
        double totalRepairCost = neighborhoodData.values().stream().mapToDouble(data -> (double) data.get("totalRepairCost")).sum();

        output.append("Total damaged buildings: ").append(totalDamagedBuildings).append("\n");
        output.append("Total repair cost: ").append(totalRepairCost).append(" IRR\n");
    }

    public String getOutput() {
        return output.toString();
    }

    public static void main(String[] args) {
        DamageANDLossModel model = new DamageANDLossModel();
        model.executeModel();
        System.out.println(model.getOutput());
    }
}
