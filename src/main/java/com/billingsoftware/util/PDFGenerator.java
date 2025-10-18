package com.billingsoftware.util;

import com.billingsoftware.model.Product;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class PDFGenerator {

    public static void generateInvoice(int billNo, String customer, String type,
                                       List<Product> products, List<Integer> qtyList, List<Double> priceList,
                                       double total, double paid, double balance, double cashReturn) {

        try {
            // A5 layout with 0.5 inch margins
            Document document = new Document(PageSize.A5, 36, 36, 36, 36);
            String fileName = "Invoice_Bill_" + billNo + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font shopFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph shopName = new Paragraph("My Supermarket", shopFont);
            shopName.setAlignment(Element.ALIGN_CENTER);
            document.add(shopName);

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Billing Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            // Bill Info
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Bill No: " + billNo, infoFont));
            document.add(new Paragraph("Customer: " + customer, infoFont));
            document.add(new Paragraph("Type: " + type, infoFont));
            document.add(new Paragraph("Date: " + new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new java.util.Date()), infoFont));
            document.add(new Paragraph(" "));

            // Product Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 1.5f, 2f, 2f});

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Stream.of("Product", "Quantity", "Price", "Subtotal").forEach(col -> {
                PdfPCell cell = new PdfPCell(new Phrase(col, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            });

            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                int qty = qtyList.get(i);
                double price = priceList.get(i);
                double subtotal = qty * price;

                table.addCell(p.getName());
                table.addCell(String.valueOf(qty));
                table.addCell("₹" + price);
                table.addCell("₹" + subtotal);
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // Summary Section
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            document.add(new Paragraph("Total Bill  : ₹" + total, boldFont));
            document.add(new Paragraph("Amount Paid : ₹" + paid, boldFont));
            if (cashReturn > 0) {
                document.add(new Paragraph("Cash Return : ₹" + cashReturn, boldFont));
            } else {
                document.add(new Paragraph("Balance     : ₹" + balance, boldFont));
            }

            // Footer
            document.add(new Paragraph(" "));
            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));

            Paragraph footer = new Paragraph("Thank you for your purchase!\nPlease retain this bill for future reference.",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            System.out.println("✅ Invoice generated: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*public static void generateInvoice(int billNo, String customer, String type,
                                       List<Product> products, List<Integer> qtyList, List<Double> priceList,
                                       double total, double paid, double balance) {

        try {
            //Document document = new Document();
            Document document = new Document(PageSize.A5, 36, 36, 36, 36);
            String fileName = "Invoice_Bill_" + billNo + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Billing Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Bill No: " + billNo));
            document.add(new Paragraph("Customer: " + customer));
            document.add(new Paragraph("Type: " + type));
            document.add(new Paragraph("Date: " + new Date()));
            document.add(new Paragraph(" "));

            // Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Product");
            table.addCell("Quantity");
            table.addCell("Price");
            table.addCell("Subtotal");

            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                int qty = qtyList.get(i);
                double price = priceList.get(i);
                double subtotal = qty * price;

                table.addCell(p.getName());
                table.addCell(String.valueOf(qty));
                table.addCell("₹" + String.format("%.2f", price));
                table.addCell("₹" + String.format("%.2f", subtotal));
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // Summary Section
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidths(new float[]{2f, 1f});
            summaryTable.setWidthPercentage(40);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            summaryTable.addCell(new Phrase("Total Bill:", labelFont));
            summaryTable.addCell(new Phrase("₹" + String.format("%.2f", total), valueFont));

            summaryTable.addCell(new Phrase("Amount Paid:", labelFont));
            summaryTable.addCell(new Phrase("₹" + String.format("%.2f", paid), valueFont));

            // Calculate cash return
            double cashReturn = paid - total;
            if (cashReturn > 0) {
                summaryTable.addCell(new Phrase("Cash Return:", labelFont));
                summaryTable.addCell(new Phrase("₹" + String.format("%.2f", cashReturn), valueFont));
            }

            // Show balance if it's a credit bill
            if (balance > 0) {
                summaryTable.addCell(new Phrase("Remaining Balance:", labelFont));
                summaryTable.addCell(new Phrase("₹" + String.format("%.2f", balance), valueFont));
            }

            document.add(summaryTable);

            document.add(new Paragraph(" "));
            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));

            Paragraph footer = new Paragraph("Thank you for your purchase!\nPlease retain this bill for future reference.",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);


            document.close();

            System.out.println("✅ Invoice generated: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
