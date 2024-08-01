package com.customs360.bizz.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class EmailService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	//private static final String CONFIG_FILE_PATH = "D:\\gitlab\\datascheduler\\src\\main\\resources\\email_config.json";
	private static final String CONFIG_FILE_PATH = "/home/ubuntu/dataScheduler/email_config.json";

	public void sendEmailNotification(int fetchCount, int dataListSize, String prefix) {
	    try {
	        // Load email configuration from JSON
	        Gson gson = new Gson();
	        JsonObject emailConfigJson = JsonParser.parseReader(new FileReader(CONFIG_FILE_PATH)).getAsJsonObject();
	        JsonObject smtpConfig = emailConfigJson.getAsJsonObject("smtp");
	        JsonObject springMailConfig = emailConfigJson.getAsJsonObject("spring_mail");

	        // Get SMTP properties
	        Properties props = new Properties();
	        props.put("mail.smtp.host", springMailConfig.get("host").getAsString());
	        props.put("mail.smtp.port", springMailConfig.get("port").getAsInt());
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");

	        // Create session
	        Session session = Session.getInstance(props, new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(springMailConfig.get("username").getAsString(), springMailConfig.get("password").getAsString());
	            }
	        });

	        // Get copy recipients
	        JsonArray copyRecipientsArray = smtpConfig.getAsJsonArray("copy_recipients");
	        List<String> copyRecipientsList = new ArrayList<>();
	        for (JsonElement element : copyRecipientsArray) {
	            copyRecipientsList.add(element.getAsString());
	        }

	        // Get BCC recipients
	        JsonArray bccRecipientsArray = smtpConfig.getAsJsonArray("bcc_recipients");
	        List<String> bccRecipientsList = new ArrayList<>();
	        for (JsonElement element : bccRecipientsArray) {
	            bccRecipientsList.add(element.getAsString());
	        }

	        // Create MimeMessage
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(smtpConfig.get("sender").getAsString()));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(smtpConfig.get("default_to_address").getAsString()));
	        message.setSubject(prefix + " " + smtpConfig.get("subject").getAsString());

	        // Adding CC recipients
	        for (String ccRecipient : copyRecipientsList) {
	            message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccRecipient));
	        }

	        // Adding BCC recipients
	        for (String bccRecipient : bccRecipientsList) {
	            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccRecipient));
	        }

	        // Create the HTML content of the message
	        StringBuilder emailContent = new StringBuilder();
	        emailContent.append("<html><body>");
	        emailContent.append("<p><b>Hi Team,</b></p>");
	        emailContent.append("<p>Please find below the latest update on Data Scheduler.</p>");
	        emailContent.append("<p><b>Count Of Data Inserted:</b> ").append(dataListSize).append("</p>");

	        emailContent.append("<br>");
	        // Start tabular view for disk space analysis
	        emailContent.append("<p><b>Disk Space Analysis:</p></b>");
	        emailContent.append("<table border=\"1\">");
	        emailContent.append("<tr><th>Drive</th><th>Total Space</th><th>Free Space</th><th>Used Space</th></tr>");

	        // Iterate through each driver to create individual pie charts and add row to the table
	        for (File root : File.listRoots()) {
	            long totalSpaceGB = root.getTotalSpace() / (1024 * 1024 * 1024); // Convert bytes to GB
	            long freeSpaceGB = root.getFreeSpace() / (1024 * 1024 * 1024); // Convert bytes to GB
	            long usedSpaceGB = totalSpaceGB - freeSpaceGB;
	            double usedPercentage = ((double) usedSpaceGB / totalSpaceGB) * 100;

	            // Determine cell color based on used space percentage
	            String cellColor;
	            if (usedPercentage <= 50) {
	                cellColor = "#63ff5d"; // Light Green
	            } else if (usedPercentage <= 75) {
	                cellColor = "#ffa55d"; // Light Orange
	            } else {
	                cellColor = "#ff5d5d"; // Red
	            }

	            // Add row to the table with conditional formatting
	            emailContent.append("<tr>")
	                    .append("<td>").append(root.getAbsolutePath()).append("</td>")
	                    .append("<td>").append(totalSpaceGB).append(" GB</td>")
	                    .append("<td>").append(freeSpaceGB).append(" GB</td>")
	                    .append("<td style=\"background-color:").append(cellColor).append(";\">")
	                    .append(usedSpaceGB).append(" GB</td>")
	                    .append("</tr>");
	        }

	        // End tabular view
	        emailContent.append("</table>");

	        // Color Code Legend
	        emailContent.append("<table border=\"0\" style=\"font-size: smaller;\">");
	        emailContent.append("<tr><td style=\"background-color:#63ff5d;width:10px;height:10px;\"></td><td>Below 50%</td>");
	        emailContent.append("<td style=\"background-color:#ffa55d;width:10px;height:10px;\"></td><td>50% to 75%</td>");
	        emailContent.append("<td style=\"background-color:#ff5d5d;width:10px;height:10px;\"></td><td>Above 75%</td></tr>");
	        emailContent.append("</table>");
	        
	         // Disk Space Analysis Chart
	        emailContent.append("<p><b>Disk Space Analysis Chart:</b></p>");
	        emailContent.append("<div style=\"display:flex;\">");

            for (File root : File.listRoots()) {
                DefaultPieDataset dataset = new DefaultPieDataset();
                long totalSpaceGB = root.getTotalSpace() / (1024 * 1024 * 1024);
                long freeSpaceGB = root.getFreeSpace() / (1024 * 1024 * 1024);
                long usedSpaceGB = totalSpaceGB - freeSpaceGB;

                dataset.setValue("Used (" + usedSpaceGB + "GB)", usedSpaceGB);
                dataset.setValue("Free (" + freeSpaceGB + "GB)", freeSpaceGB);

                JFreeChart chart = ChartFactory.createPieChart(
                        root.getAbsolutePath(),
                        dataset,
                        true,
                        true,
                        false);

                ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
                ChartUtils.writeChartAsPNG(chartOut, chart, 300, 200);
                byte[] chartBytes = chartOut.toByteArray();
                String chartBase64 = Base64.getEncoder().encodeToString(chartBytes);

                emailContent.append("<div style=\"margin-right: 20px;\">")
                        .append("<img src=\"data:image/png;base64,").append(chartBase64).append("\" alt=\"Pie Chart\">")
                        .append("</div>");
            }

            emailContent.append("</div>");

	        // End of HTML content
	        emailContent.append("<p>Thanks and regards,</p>");
	        emailContent.append("Analytics Administrator,<br>");
	        emailContent.append("IT Team");
	        emailContent.append("<p><b>Note:</b> This email has been auto-generated from the Bizz Customer Support. <b>Please do not reply to this auto-generated email.</b></p>");
	        emailContent.append("</body></html>");

	        // Create MimeBodyPart
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setContent(emailContent.toString(), "text/html");

	        // Set content
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(messageBodyPart);

	        // Set content
	        message.setContent(multipart);

	        // Send message
	        Transport.send(message);

	        System.out.println("Email notification sent successfully");
	    } catch (Exception e) {
	        System.err.println("Error occurred while sending email notification");
	        e.printStackTrace();
	    }
	}
}
