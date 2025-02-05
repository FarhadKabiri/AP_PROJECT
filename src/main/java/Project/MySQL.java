package Project;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

public class MySQL {
    public static void main(String[] args) {
        // مسیر فایل اکسل
        String excelFilePath = "C:/Users/HP/Desktop/buildings_data.xlsx";
        String jdbcURL = "jdbc:mysql://localhost:3306/ap_proj?useUnicode=true&characterEncoding=UTF-8";
        String dbUser = "root";
        String dbPassword = "FarhadKabiri";

        try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(fis);
             Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {

            // خواندن شیت اول اکسل
            Sheet sheet = workbook.getSheetAt(0);
            String sql = "INSERT IGNORE INTO buildings (building_code, latitude, longitude, number_of_stories, footprint_area, pga_complete_damage, district, neighborhood, repair_cost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // خواندن داده‌ها از اکسل و وارد کردن آن‌ها در جدول buildings
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String buildingCode = row.getCell(0).getStringCellValue();
                double latitude = row.getCell(1).getNumericCellValue();
                double longitude = row.getCell(2).getNumericCellValue();
                int stories = (int) row.getCell(3).getNumericCellValue();
                double footprintArea = row.getCell(4).getNumericCellValue();
                double pga = row.getCell(5).getNumericCellValue();
                int district = (int) row.getCell(6).getNumericCellValue();
                String neighborhood = row.getCell(7).getStringCellValue();
                double repairCost = row.getCell(8).getNumericCellValue();

                statement.setString(1, buildingCode);
                statement.setDouble(2, latitude);
                statement.setDouble(3, longitude);
                statement.setInt(4, stories);
                statement.setDouble(5, footprintArea);
                statement.setDouble(6, pga);
                statement.setInt(7, district);
                statement.setString(8, neighborhood);
                statement.setDouble(9, repairCost);

                statement.executeUpdate();
            }

            System.out.println("✅ داده‌های اکسل با موفقیت در MySQL ذخیره شد.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
