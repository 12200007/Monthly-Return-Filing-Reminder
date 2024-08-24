//package com.simple_form.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender emailSender;
//
//    public void sendOTP(String email, String otp) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Your OTP Code");
//        message.setText("Your OTP code is: " + otp + ".  Your OTP will expires in 10 minutes");
//
//        try {
//            emailSender.send(message);
//            System.out.println("OTP email sent to " + email);
//        } catch (Exception e) {
//            System.err.println("Error sending email: " + e.getMessage());
//        }
//    }
//}
