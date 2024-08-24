package com.simple_form.controller;

import com.simple_form.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/upload")
public class UploadFileController {

    @Autowired
    private UploadFileService uploadFileService;

    @PostMapping("/excel")
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
        }

        try {
            uploadFileService.saveExcelData(file);
            return ResponseEntity.ok("File uploaded and data saved successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the file: " + e.getMessage());
        }
    }

    @PostMapping("/MailTheCustomer")
    public String mailTheCustomer() {
        uploadFileService.sendEmails();
        return "Emails sent to all customers!";
    }
}
