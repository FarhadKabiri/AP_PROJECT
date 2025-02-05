package Project;

import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/ap_proj?useUnicode=true&characterEncoding=UTF-8";
        String dbUser = "root";
        String dbPassword = "FarhadKabiri";

        StringBuilder output = new StringBuilder(); // ایجاد StringBuilder برای ذخیره خروجی

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            // ایجاد گسل خطی و کانون زلزله
            RLocation point1 = new RLocation("Point1", 36.0, 50.72, 10.0, "Point1 Address");
            RLocation point2 = new RLocation("Point2", 35.82, 51.75, 15.0, "Point2 Address");
            RSingleLineSourceModel sourceModel = new RSingleLineSourceModel(true, point1, point2, 10.0, 0.5, 0.5);
            RLocation hypocenterLocation = sourceModel.getOutputLocation();

            // فرض می‌کنیم magnitude مقدار مشخصی است
            double magnitude = 7; // مثال از قدرت زلزله

            // دریافت و پردازش اطلاعات ساختمان‌ها
            Map<String, Map<String, Object>> neighborhoodData = getNeighborhoodData(connection, hypocenterLocation, magnitude, output);

            // ذخیره و چاپ گزارش محله‌ها
            saveAndPrintNeighborhoodData(neighborhoodData, output);

            // ذخیره و چاپ مجموع ساختمان‌ها و هزینه‌ها
            saveAndPrintTotalData(neighborhoodData, output);

            // ارسال خروجی به کلاس AI_Assistant برای پردازش
            AI_Assistant assistant = new AI_Assistant();
            assistant.processOutput(output); // ارسال خروجی برای پردازش

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // دریافت اطلاعات محله‌ها و ساختمان‌های آسیب‌دیده
    private static Map<String, Map<String, Object>> getNeighborhoodData(Connection connection, RLocation hypocenterLocation, double magnitude, StringBuilder output) throws SQLException {
        Map<String, Map<String, Object>> neighborhoodData = new HashMap<>();
        String sql = "SELECT building_code, latitude, longitude, pga_complete_damage, neighborhood, repair_cost FROM buildings";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        // لیست ساختمان‌ها و مقادیر PGA آسیب‌پذیری
        List<RLocation> buildingLocations = new ArrayList<>();
        List<RLocation> hypocenterLocations = new ArrayList<>();
        List<Double> magnitudes = new ArrayList<>();

        hypocenterLocations.add(hypocenterLocation);
        magnitudes.add(magnitude);

        while (resultSet.next()) {
            String buildingCode = resultSet.getString("building_code");
            double latitude = resultSet.getDouble("latitude");
            double longitude = resultSet.getDouble("longitude");
            double pga = resultSet.getDouble("pga_complete_damage");
            String neighborhood = resultSet.getString("neighborhood");
            double repairCost = resultSet.getDouble("repair_cost");

            // اضافه کردن مکان ساختمان به لیست
            buildingLocations.add(new RLocation(buildingCode, latitude, longitude, 0, "Building"));

            // محاسبه شدت زلزله
            RGenericAttenuationModel attenuationModel = new RGenericAttenuationModel(buildingLocations, magnitudes, hypocenterLocations);
            double[] intensities = attenuationModel.calculateIntensities();

            // مقایسه شدت با PGA آسیب‌پذیری
            if (intensities[0] > pga) {
                // اگر شدت بیشتر از PGA باشد، ساختمان آسیب‌دیده است
                if (!neighborhoodData.containsKey(neighborhood)) {
                    neighborhoodData.put(neighborhood, new HashMap<>());
                    neighborhoodData.get(neighborhood).put("damagedBuildings", 0);
                    neighborhoodData.get(neighborhood).put("totalRepairCost", 0.0);
                }
                // بروزرسانی تعداد ساختمان‌های آسیب‌دیده و مجموع هزینه تعمیرات در محله
                neighborhoodData.get(neighborhood).put("damagedBuildings", (int) neighborhoodData.get(neighborhood).get("damagedBuildings") + 1);
                neighborhoodData.get(neighborhood).put("totalRepairCost", (double) neighborhoodData.get(neighborhood).get("totalRepairCost") + repairCost);
            }
        }
        return neighborhoodData;
    }

    // ذخیره و چاپ گزارش محله‌ها با مرتب‌سازی بر اساس خسارت
    private static void saveAndPrintNeighborhoodData(Map<String, Map<String, Object>> neighborhoodData, StringBuilder output) {
        List<Map.Entry<String, Map<String, Object>>> sortedNeighborhoods = new ArrayList<>(neighborhoodData.entrySet());

        // مرتب‌سازی محله‌ها بر اساس مجموع خسارت به ترتیب نزولی
        sortedNeighborhoods.sort((entry1, entry2) -> {
            double totalRepairCost1 = (double) entry1.getValue().get("totalRepairCost");
            double totalRepairCost2 = (double) entry2.getValue().get("totalRepairCost");
            return Double.compare(totalRepairCost2, totalRepairCost1); // ترتیب نزولی
        });

        output.append("Total number of damaged buildings and repair costs by neighborhood:\n");
        for (Map.Entry<String, Map<String, Object>> entry : sortedNeighborhoods) {
            String neighborhood = entry.getKey();
            Map<String, Object> data = entry.getValue();
            int damagedBuildings = (int) data.get("damagedBuildings");
            double totalRepairCost = (double) data.get("totalRepairCost");

            // ذخیره در خروجی
            output.append("Neighborhood: ").append(neighborhood).append("\n");
            output.append("  Total damaged buildings: ").append(damagedBuildings).append("\n");
            output.append("  Total repair cost: ").append(totalRepairCost).append(" IRR\n");
        }
        output.append("\n"); // خط فاصله
    }

    // ذخیره و چاپ کل ساختمان‌ها و هزینه‌ها
    private static void saveAndPrintTotalData(Map<String, Map<String, Object>> neighborhoodData, StringBuilder output) {
        int totalDamagedBuildings = 0;
        double totalRepairCost = 0.0;

        // محاسبه مجموع کل ساختمان‌های آسیب‌دیده و مجموع هزینه‌ها
        for (Map<String, Object> data : neighborhoodData.values()) {
            totalDamagedBuildings += (int) data.get("damagedBuildings");
            totalRepairCost += (double) data.get("totalRepairCost");
        }

        // ذخیره در خروجی
        output.append("Total damaged buildings: ").append(totalDamagedBuildings).append("\n");
        output.append("Total repair cost: ").append(totalRepairCost).append(" IRR\n");
    }
}
