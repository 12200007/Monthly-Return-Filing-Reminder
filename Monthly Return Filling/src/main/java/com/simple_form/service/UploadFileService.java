package com.simple_form.service;

import com.simple_form.model.CustomersDetailsModel;
import com.simple_form.repository.CustomersDetailsRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class UploadFileService {

    @Autowired
    private CustomersDetailsRepository customersDetailsRepository;

    @Autowired
    private JavaMailSender emailSender; // Inject the email sender

    // Method to save customer data from the uploaded Excel file
    public void saveExcelData(MultipartFile file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // Stop processing upon encountering a null row
                if (row == null) {
                    continue; // Skip to the next iteration if the row is null
                }

                CustomersDetailsModel details = new CustomersDetailsModel();
                details.setName(getStringCellValue(row.getCell(0)));
                String emailId = getStringCellValue(row.getCell(1));
                details.setEmailId(emailId); // Set the emailId from the Excel sheet
                details.setPeriodCollectedFor(getStringCellValue(row.getCell(2)));
                details.setDueDate(getDateCellValue(row.getCell(3)));

                // Only save the details if the emailId is not null or empty
                if (emailId != null && !emailId.isEmpty()) {
                    customersDetailsRepository.save(details); // Save to the database if email is valid
                }
            }
        }
    }

    // Method to send email reminders to customers and delete the records afterward
    public void sendEmails() {
        List<CustomersDetailsModel> customers = customersDetailsRepository.findAll();
        for (CustomersDetailsModel customer : customers) {
            String emailId = customer.getEmailId();

            // Log the attempt to send an email
            System.out.println("Attempting to send email to: " + emailId);

            // Skip sending if emailId is null or empty
            if (emailId == null || emailId.isEmpty()) {
                continue; // Skip this customer
            }

            // Construct the email subject and message format
            String subject = "Monthly Return Filing Reminder";
            String message = String.format("Subject: %s\n\n"
                            + "Hello %s,\n\n"
                            + "Your Monthly Return Filing for the month of %s is due on %s. "
                            + "Therefore, you are requested to file the monthly returns on or before "
                            + "due date to avoid attracting any late fines and penalties.\n\n"
                            + "Your cooperation is highly appreciated.\n\n"
                            + "Sales Tax\n"
                            + "Regional Revenue and Customs Office, Mongar",
                    subject, customer.getName(), customer.getPeriodCollectedFor(), customer.getDueDate());

            // Try to send the email and catch any exceptions silently
            try {
                sendEmail(emailId, subject, message);
                // Delete the customer record after successful email sending
                customersDetailsRepository.delete(customer); // Delete from the database
            } catch (Exception e) {
                // Ignore the error; do not log or display it
            }
        }
    }

    // Helper method to send an email
    private void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        emailSender.send(email); // Send the email
    }

    // Helper method to get a string value from an Excel cell
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null; // Handle null cell
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return getDateCellValue(cell); // Format if date
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // Helper method to get a date value from an Excel cell
    private String getDateCellValue(Cell cell) {
        if (cell != null && cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }
        return null; // Safeguard
    }
}
