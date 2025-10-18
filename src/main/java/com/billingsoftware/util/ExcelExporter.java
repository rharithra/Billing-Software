package com.billingsoftware.util;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.poifs.crypt.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;;
public class ExcelExporter {
    public static void exportTableWithPassword(JTable table, File file, String password) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sales Report");

        TableModel model = table.getModel();

        // Write header
        Row header = sheet.createRow(0);
        for (int col = 0; col < model.getColumnCount(); col++) {
            Cell cell = header.createCell(col);
            cell.setCellValue(model.getColumnName(col));
        }

        // Write data
        for (int row = 0; row < model.getRowCount(); row++) {
            Row excelRow = sheet.createRow(row + 1);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = excelRow.createCell(col);
                Object value = model.getValueAt(row, col);
                if (value instanceof Number)
                    cell.setCellValue(Double.parseDouble(value.toString()));
                else
                    cell.setCellValue(value.toString());
            }
        }

        // Save as temp Excel
        File tempFile = File.createTempFile("report", ".xlsx");
        FileOutputStream fos = new FileOutputStream(tempFile);
        workbook.write(fos);
        fos.close();
        workbook.close();

        // Encrypt with password
        POIFSFileSystem fs = new POIFSFileSystem();
        EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
        Encryptor enc = info.getEncryptor();
        enc.confirmPassword(password);

        try (OPCPackage opc = OPCPackage.open(tempFile, PackageAccess.READ_WRITE);
             OutputStream os = enc.getDataStream(fs)) {
            opc.save(os);
        }

        try (FileOutputStream fosEncrypted = new FileOutputStream(file)) {
            fs.writeFilesystem(fosEncrypted);
        }

        tempFile.delete();

            /*JOptionPane.showMessageDialog(null, "Exported successfully with password!");
         catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to export: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }*/
    }
}
