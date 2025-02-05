package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 1. تنظیم مشخصات پایگاه داده
        String jdbcURL = "jdbc:mysql://localhost:3306/ap_proj?useUnicode=true&characterEncoding=UTF-8";
        String dbUser = "root";
        String dbPassword = "FarhadKabiri";

        // 2. تعریف نقاط مبدأ و مقصد زلزله
        RLocation point1 = new RLocation("Point1", 36.0, 50.72, 10.0);
        RLocation point2 = new RLocation("Point2", 35.82, 51.75, 15.0);

        // 3. مقداردهی عمق زلزله
        double depth = 10.0;
        double magnitude = 6.5;

        // 4. ایجاد مدل منبع زلزله
        RSingleLineSourceModel sourceModel = new RSingleLineSourceModel(point1, point2, depth);
        RLocation hypocenterLocation = sourceModel.getOutputLocation();

        // 5. ایجاد نمونه‌ای از DamageANDLossModel
        DamageANDLossModel model = new DamageANDLossModel(jdbcURL, dbUser, dbPassword);

        // 6. اجرا و پردازش داده‌ها
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            Map<String, Map<String, Object>> neighborhoodData = model.getNeighborhoodData(connection, hypocenterLocation, magnitude);
            model.saveAndPrintNeighborhoodData(neighborhoodData);
            model.saveAndPrintTotalData(neighborhoodData);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 7. دریافت خروجی مدل
        String modelOutput = model.getOutput().replace("\n", " ");
        System.out.println(model.getOutput());

        // 8. تعریف پرسش
        String question = "Considering the earthquake in Tehran, which neighborhoods will have more loss according to this data?";

        // 9. تولید ورودی برای مدل هوش مصنوعی
        String prompt1 = PromptFormatter.formatPrompt(question, modelOutput);
        // String prompt2 = question;

        // 10. دریافت پاسخ از مدل هوش مصنوعی
        ConnectAI connectAI = new ConnectAI();
        String modelResponse = connectAI.getModelResponse(prompt1);

        // 11. نمایش پاسخ
        System.out.println("Question:\n" + question + "\n\nModel Response:\n" + modelResponse);
    }
}
