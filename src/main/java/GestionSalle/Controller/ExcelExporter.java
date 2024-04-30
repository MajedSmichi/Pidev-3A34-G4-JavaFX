package GestionSalle.Controller;

import GestionSalle.Entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {
public void export(List<User> users, String filename) {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Users");

    // Create header row
    Row headerRow = sheet.createRow(0);
    Cell cell = headerRow.createCell(0);
    cell.setCellValue("Name");
    cell = headerRow.createCell(1);
    cell.setCellValue("Phone Number");
    cell = headerRow.createCell(2);
    cell.setCellValue("Code"); // Added this line

    // Fill data
    for (int i = 0; i < users.size(); i++) {
        User user = users.get(i);
        Row row = sheet.createRow(i + 1);
        row.createCell(0).setCellValue(user.getNom());
        row.createCell(1).setCellValue(user.getNumTele());
        row.createCell(2).setCellValue(user.getId() * 33+17); // Added this line
    }

    // Write to file
    try (FileOutputStream outputStream = new FileOutputStream(filename)) {
        workbook.write(outputStream);
    } catch (IOException e) {
        e.printStackTrace();
    }
}}

