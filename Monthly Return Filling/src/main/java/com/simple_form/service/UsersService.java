//package com.simple_form.service;
//
//import com.simple_form.exception.InvalidUserDataException;
//import com.simple_form.model.UsersModel;
//import com.simple_form.repository.UsersRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.Random;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//public class UsersService {
//
//    @Autowired
//    private UsersRepository usersRepository;
//
//    @Autowired
//
//
//    @Autowired
//    private EmailService emailService;
//
//    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    // Use a thread-safe map to temporarily store user data during OTP verification
//    private ConcurrentHashMap<String, UsersModel> temporaryUserStorage = new ConcurrentHashMap<>();
//
//    public void registerUser(UsersModel user) {
//        if (usersRepository.findByEmail(user.getEmail()).isPresent()) {
//            throw new InvalidUserDataException("The Email is already registered");
//        }
//
//        generateAndSendOTP(user);
//
//        // Store the user data temporarily until OTP is verified
//        temporaryUserStorage.put(user.getEmail(), user);
//    }
//
//    private void generateAndSendOTP(UsersModel user) {
//        String otp = String.format("%06d", new Random().nextInt(999999));  // Generate a 6-digit OTP
//        UsersOTP usersOTP = new UsersOTP();
//        usersOTP.setEmail(user.getEmail());
//        usersOTP.setOtp(otp);
//        usersOTP.setExpiryDate(LocalDateTime.now().plusMinutes(10));  // OTP is valid for 10 minutes
//
//        userOTPRepository.save(usersOTP);
//        emailService.sendOTP(user.getEmail(), otp);
//    }
//
//    public void verifyOTP(String email, String otp) {
//        Optional<UsersOTP> optionalUserOTP = userOTPRepository.findById(email);
//        if (!optionalUserOTP.isPresent()) {
//            throw new InvalidUserDataException("Invalid email");
//        }
//
//        UsersOTP userOTP = optionalUserOTP.get();
//        if (userOTP.getExpiryDate().isBefore(LocalDateTime.now()) || !userOTP.getOtp().equals(otp)) {
//            throw new InvalidUserDataException("Invalid or expired OTP");
//        }
//
//        // Retrieve and remove the user data from temporary storage upon successful OTP verification
//        UsersModel user = temporaryUserStorage.remove(email);
//        if (user == null) {
//            throw new InvalidUserDataException("User data not found or already verified");
//        }
//
//        // Save the user permanently in the users repository
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        usersRepository.save(user);
//
//        // Delete the OTP record now that verification is complete
//        userOTPRepository.delete(userOTP);
//    }
//
//    public void initiatePasswordReset(String email) {
//        Optional<UsersModel> userOptional = usersRepository.findByEmail(email);
//        if (!userOptional.isPresent()) {
//            throw new InvalidUserDataException("Email not registered");
//        }
//
//        // Use the existing method to generate and send an OTP for password reset
//        generateAndSendOTP(userOptional.get());
//    }
//
//    public void resetPassword(String email, String otp, String newPassword) {
//        Optional<UsersOTP> optionalUserOTP = userOTPRepository.findById(email);
//        if (!optionalUserOTP.isPresent()) {
//            throw new InvalidUserDataException("Invalid email");
//        }
//
//        UsersOTP userOTP = optionalUserOTP.get();
//        if (userOTP.getExpiryDate().isBefore(LocalDateTime.now()) || !userOTP.getOtp().equals(otp)) {
//            throw new InvalidUserDataException("Invalid or expired OTP");
//        }
//
//        // OTP verified, reset the user's password
//        Optional<UsersModel> userOptional = usersRepository.findByEmail(email);
//        if (!userOptional.isPresent()) {
//            throw new InvalidUserDataException("User data not found");
//        }
//
//        UsersModel user = userOptional.get();
//        user.setPassword(passwordEncoder.encode(newPassword));
//        usersRepository.save(user);
//
//        // Delete the OTP record after password reset
//        userOTPRepository.delete(userOTP);
//    }
//}
