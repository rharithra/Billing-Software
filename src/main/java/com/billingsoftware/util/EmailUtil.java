package com.billingsoftware.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
public class EmailUtil {
    public static void sendBillEmail(String recipientEmail, String subject, String content) {
        final String senderEmail = "rharithra9@gmail.com";
        final String senderPassword = "gtfi upjs gqdt gcql"; // use app password if using Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "My Supermarket"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
            //message.setText(content);
            //Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendBillEmail(String toEmail, String customerName, int billNo, double totalAmount, double paidAmount, double balanceAmount, List<String> productNames, List<Integer> quantities, List<Double> prices) {
        String subject = "🧾 Your Bill from My Supermarket - Bill No: " + billNo;

        StringBuilder itemsHtml = new StringBuilder();
        itemsHtml.append("<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse; width: 100%;'>");
        itemsHtml.append("<tr style='background-color: #f2f2f2;'><th>Product</th><th>Quantity</th><th>Price</th><th>Total</th></tr>");

        for (int i = 0; i < productNames.size(); i++) {
            double itemTotal = quantities.get(i) * prices.get(i);
            itemsHtml.append("<tr><td>").append(productNames.get(i)).append("</td><td>")
                    .append(quantities.get(i)).append("</td><td>₹")
                    .append(prices.get(i)).append("</td><td>₹")
                    .append(itemTotal).append("</td></tr>");
        }
        itemsHtml.append("</table>");

        String body = "<html><body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #2E86C1;'>🛒 Thank You for Shopping with My Supermarket</h2>"
                + "<p><strong>Customer:</strong> " + customerName + "<br>"
                + "<strong>Bill No:</strong> " + billNo + "<br>"
                + "<strong>Total:</strong> ₹" + totalAmount + "<br>"
                + "<strong>Paid:</strong> ₹" + paidAmount + "<br>"
                + "<strong>Balance:</strong> ₹" + balanceAmount + "</p>"
                + "<h3 style='color: #117A65;'>🧾 Items Purchased:</h3>"
                + itemsHtml
                + "<p style='margin-top: 20px;'>Visit Again!<br><strong>My Supermarket</strong></p>"
                + "<p style='font-size: 12px; color: #999;'>Printed on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")) + "</p>"
                + "</body></html>";

        sendBillEmail(toEmail, subject, body);
    }

}
