package com.billingsoftware.util;

import com.billingsoftware.model.Bill;
import com.billingsoftware.model.BillItem;
import com.itextpdf.text.*;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import com.billingsoftware.dao.CustomerDAO;
import com.billingsoftware.dao.BillDAO;
import java.util.List;

public class ReceiptGenerator {
    /*public static void printReceipt(String customerName, double paidAmount, double newBalance, LocalDate date,
                                    int collectionId, int originalBillNo, double totalBillAmount, double totalPaidSoFar) {
        try {
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String filename = "CustomerReceipt_" + customerName.replaceAll("\\s+", "_") + "_" + System.currentTimeMillis() + ".pdf";

            Document document = new Document(PageSize.A5, 36, 36, 50, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            // Title
            Paragraph title = new Paragraph("COLLECTION RECEIPT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            // Header info
            document.add(new Paragraph("No. : " + collectionId, labelFont));
            document.add(new Paragraph("Date: " + date, labelFont));
            document.add(new Paragraph("To: " + customerName, labelFont));
            document.add(new Paragraph(" "));

            // Collection Summary
            PdfPTable summaryTable = new PdfPTable(4);
            summaryTable.setWidthPercentage(100);
            summaryTable.setSpacingBefore(10f);
            summaryTable.addCell(new PdfPCell(new Phrase("Date", tableFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("Bill No", tableFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("Bill Amount", tableFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("Paid", tableFont)));

            summaryTable.addCell(new PdfPCell(new Phrase(date.toString(), tableFont)));
            summaryTable.addCell(new PdfPCell(new Phrase(String.valueOf(originalBillNo), tableFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("₹" + totalBillAmount, tableFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("₹" + paidAmount, tableFont)));

            document.add(summaryTable);

            // Collection Total
            document.add(new Paragraph("\nTotal Collection in this payment: ₹" + paidAmount, labelFont));
            document.add(new Paragraph("Remaining Balance: ₹" + newBalance, labelFont));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("RUPEES " + convertToWords(paidAmount).toUpperCase() + " ONLY", tableFont));
            document.add(new Paragraph(" "));

            Paragraph thanks = new Paragraph("Printed Date & Time : " +
                    java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")), labelFont);
            document.add(thanks);

            document.add(new Paragraph(" "));
            Paragraph footer = new Paragraph("THANKS FOR COMING!!", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            JOptionPane.showMessageDialog(null, "Receipt saved as: " + filename);

        } catch (IOException fileOpenException) {
            JOptionPane.showMessageDialog(null, "Close the previously opened PDF before saving again.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to generate PDF receipt.");
        }
    }*/
    public static String convertToWords(double amount) {
        // You can use Apache Commons or write a custom logic.
        // Placeholder:
        return new java.text.DecimalFormat("###").format(amount);
    }


    public static void printReceipt(int receiptNo, String customerName, int billNo, double billAmount, double paidAmount, double remainingBalance, LocalDate date) {
        try {
            // Unique filename with timestamp
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String filename = "CustomerReceipt_" + customerName.replaceAll("\\s+", "_") + "_" + System.currentTimeMillis() + ".pdf";

            Document document = new Document(PageSize.A5, 36, 36, 50, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filename));

            document.open();

            // Title
            Paragraph title = new Paragraph("COLLECTION RECEIPT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            // Header Info
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("No. : " + receiptNo, font));
            document.add(new Paragraph("Date: " + date, font));
            document.add(new Paragraph("To: " + customerName, font));

            document.add(new Paragraph(" "));
            // Bill Summary Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 2f, 3f, 2f});
            table.setSpacingBefore(10);

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            table.addCell(new PdfPCell(new Phrase("Date", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Bill No", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Bill Amount", boldFont)));
            table.addCell(new PdfPCell(new Phrase("Paid", boldFont)));

            table.addCell(new PdfPCell(new Phrase(date.toString(), font)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(billNo), font)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", billAmount), font)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", billAmount - remainingBalance - paidAmount), font)));

            table.addCell(new PdfPCell(new Phrase(date.toString(), font)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(billNo), font)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", billAmount), font)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", paidAmount), font)));

            document.add(table);
            document.add(new Paragraph(" "));

            // Summary Info
            document.add(new Paragraph("Total Collection in this payment: " + paidAmount, font));
            document.add(new Paragraph("Remaining Balance: " + remainingBalance, font));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("RUPEES " + (int) paidAmount + " ONLY", font));
            document.add(new Paragraph(" "));

            String timeStr = new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(new java.util.Date());
            document.add(new Paragraph("Printed Date & Time : " + timeStr, font));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("THANKS FOR COMING!!", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13)));

            document.close();
            JOptionPane.showMessageDialog(null, "✅ Receipt saved: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Failed to generate PDF receipt.");
        }

        System.out.println("Customer Name: " + customerName);
        System.out.println("Fetching email from DB...");

        String customerEmail = CustomerDAO.getEmailByCustomerName(customerName);
        System.out.println("Fetched Email: " + customerEmail); // << Print this

        if (customerEmail != null && !customerEmail.isEmpty()) {

        /*String subject = "Your Bill from My Supermarket - Bill No: " + billNo;
        String body = "Dear " + customerName + ",\n\n"
                + "Thank you for shopping with us.\n"
                + "Bill No: " + billNo + "\n"
                + "Total: " + billAmount + "\n"
                + "Paid: " + paidAmount + "\n"
                + "Balance: " + remainingBalance + "\n\n"
                + "Visit Again!\nMy Supermarket";*/
            List<BillItem> billItems = BillDAO.getBillItemsByBillNo(billNo);
            /*String subject = "Your Bill from My Supermarket - Bill No: " + billNo;

            StringBuilder htmlBody = new StringBuilder();
            htmlBody.append("<html><body>");
            htmlBody.append("<h2>Thank you for shopping with My Supermarket</h2>");
            htmlBody.append("<p><b>Customer:</b> ").append(customerName).append("<br>");
            htmlBody.append("<b>Bill No:</b> ").append(billNo).append("<br>");
            htmlBody.append("<b>Total:</b> ₹").append(paidAmount).append("<br>");
            htmlBody.append("<b>Paid:</b> ₹").append(paidAmount).append("<br>");
            htmlBody.append("<b>Balance:</b> ₹").append(remainingBalance).append("</p>");

            htmlBody.append("<h3>Items Purchased:</h3>");
            htmlBody.append("<table border='1' cellpadding='5' cellspacing='0'>");
            htmlBody.append("<tr><th>Product</th><th>Quantity</th><th>Price</th><th>Total</th></tr>");

            for (BillItem item : billItems) {
                double total = item.getQuantity() * item.getPrice();
                htmlBody.append("<tr>")
                        .append("<td>").append(item.getProductName()).append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        .append("<td>").append("₹").append(item.getPrice()).append("</td>")
                        .append("<td>").append("₹").append(total).append("</td>")
                        .append("</tr>");
            }

            htmlBody.append("</table>");
            htmlBody.append("<p>Visit Again!<br>My Supermarket</p>");
            htmlBody.append("</body></html>");*/
            String subject = "🧾 Your Invoice from My Supermarket - Bill No: " + billNo;

            StringBuilder body = new StringBuilder();
            body.append("<html><body style='font-family: Arial, sans-serif;'>")
                    .append("<h2 style='color: #2E86C1;'>🛒 Thank You for Shopping with My Supermarket</h2>")
                    .append("<p><strong>Customer:</strong> ").append(customerName).append("<br>")
                    .append("<strong>Bill No:</strong> ").append(billNo).append("<br>")
                    .append("<strong>Total:</strong> ₹").append(paidAmount).append("<br>")
                    .append("<strong>Paid:</strong> ₹").append(paidAmount).append("<br>")
                    .append("<strong>Balance:</strong> ₹").append(remainingBalance).append("</p>")

                    .append("<h3 style='color: #117A65;'>🧾 Items Purchased:</h3>")
                    .append("<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse; width: 100%;'>")
                    .append("<tr style='background-color: #f2f2f2;'>")
                    .append("<th>Product</th><th>Quantity</th><th>Price</th><th>Total</th></tr>");

            for (BillItem item : billItems) {
                double lineTotal = item.getPrice() * item.getQuantity();
                body.append("<tr>")
                        .append("<td>").append(item.getProductName()).append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        .append("<td>₹").append(item.getPrice()).append("</td>")
                        .append("<td>₹").append(lineTotal).append("</td>")
                        .append("</tr>");
            }

            body.append("</table>")
                    .append("<p style='margin-top: 20px;'>Visit Again!<br><strong>My Supermarket</strong></p>")
                    .append("<p style='font-size: 12px; color: #999;'>Printed on: ")
                    .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")))
                    .append("</p>")
                    .append("</body></html>");

            System.out.println("Sending email...");

        //if (customerEmail != null && !customerEmail.isEmpty()) {
            EmailUtil.sendBillEmail(customerEmail, subject, body.toString());
        }
        else {
            System.out.println("No email found for this customer. Email not sent.");
        }
    }

}
