package com.isa.customer_service.service;

import com.isa.customer_service.dto.LoginUserDto;
import com.isa.customer_service.dto.VerifyUserDto;
import com.isa.customer_service.dto.UserDto;
import com.isa.customer_service.model.User;
import com.isa.customer_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final EmailService emailService;
    @Autowired
    private ImageService imageService;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public User signup(UserDto input) {
        User user = new User();
        user.setName(input.getName());
        user.setEmail(input.getEmail());
        user.setAddress(input.getAddress());
        user.setPhoneNo(input.getPhoneNo());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }
    public User changeEmail(String email, Authentication authentication) throws UnauthorizedEmailChangeException {
        // Retrieve the current user from the authentication context
        String currentUsername = authentication.getName(); // This gets the email of the authenticated user
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Ensure the user is trying to change their own email (Optional, but good for added security)
        if (!currentUser.getEmail().equals(email)) {
            throw new UnauthorizedEmailChangeException("Unauthorized email change request");
        }

        currentUser.setVerificationCode(generateVerificationCode());
        currentUser.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        sendVerificationEmail(currentUser);
        return userRepository.save(currentUser);
    }

    public User forgotPassword(UserDto input) {
        // Find user by email
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with this email not found"));

        // Verify that the phone number matches the one stored in the user's profile
        if (!user.getPhoneNo().equals(input.getPhoneNo())) {
            throw new InvalidPhoneNumberException("Phone number does not match the email");
        }

        // Clear old verification code and generate a new one
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));  // Set expiration time

        // Send a verification email with the new code
        sendVerificationEmail(user);

        // Save the updated user
        return userRepository.save(user);
    }

    public UserDto authenticate(LoginUserDto input) {
        var user =  userRepository.findByEmail(input.getEmail())
                .orElseThrow(()->new RuntimeException("user not found"));
        if(!user.isEnabled()){
            throw new RuntimeException("Account not verified");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );


        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return UserDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .phoneNo(user.getPhoneNo())
                .address(user.getAddress())
                .build();
    }

//    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
//        // Extract the refresh token from the cookies
//        String refreshToken = jwtService.extractRefreshToken(request);
//
//        if (refreshToken == null || !jwtService.isTokenValid(refreshToken, null)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired refresh token.");
//        }
//
//        String email = jwtService.extractUsername(refreshToken);
//        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Generate a new access token
//        String newAccessToken = jwtService.generateToken(user);
//
//        // Return the new access token in the response body
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, "refreshToken=" + refreshToken + "; HttpOnly; Secure; SameSite=Strict")
//                .body(newAccessToken);
//    }
    public User getUserProfile(Authentication authentication) {
        String email = authentication.getName(); // Get user email from JWT token

        // Find the user by email
        Optional<User> currentUserOptional = userRepository.findByEmail(email);

        // If user not found, throw an exception or handle it appropriately
        return currentUserOptional.orElseThrow(() -> new RuntimeException("User not found"));
    }



    public ResponseEntity<Object> updateUser(String name, String phoneNo, String address, String gender, MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> currentUserOptional = userRepository.findByEmail(email); // Fetch the user by email from the repository

        User currentUser = currentUserOptional.get(); // Get the actual user

        // Update fields
        currentUser.setName(name);
        currentUser.setPhoneNo(phoneNo);
        currentUser.setAddress(address);
        currentUser.setGender(gender);


        if (image != null && !image.isEmpty()) {
            try {
                // Process and save the image
                String imagePath = imageService.saveImage(image);
                currentUser.setImage(imagePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        // Save the updated user
        User savedUser = userRepository.save(currentUser);

        return ResponseEntity.ok(savedUser); // Return the updated user
    }

    public void verifyUser(VerifyUserDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public void resetPassword(VerifyUserDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());

        // Check if the user exists
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the verification code expires or is null
            if (user.getVerificationCodeExpiresAt() == null) {
                throw new RuntimeException("Verification code has not been generated or is invalid.");
            }

            // Check if the verification code has expired
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired.");
            }

            // Validate the verification code
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                // The code matches, so reset the password
                user.setPassword(passwordEncoder.encode(input.getPassword()));

                // Clear the verification code and expiration date (so it can't be reused)
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);

                // Save the user with the new password
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code.");
            }
        } else {
            // If user does not exist
            throw new RuntimeException("User not found.");
        }
    }
    public String resetEmail(VerifyUserDto input, Authentication authentication) {
        String currentUsername = authentication.getName(); // Get user email from JWT token
        User user = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the verification code expires or is null
        if (user.getVerificationCodeExpiresAt() == null) {
            throw new RuntimeException("Verification code has not been generated or is invalid.");
        }

        // Check if the verification code has expired
        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has expired.");
        }

        // Validate the verification code
        if (user.getVerificationCode().equals(input.getVerificationCode())) {
            // The code matches, so reset the email
            user.setEmail(input.getEmail());
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            userRepository.save(user);

            // Generate a new JWT token after email reset
            String newToken = jwtService.generateToken(user);  // Assuming `user` is a UserDetails object
            return newToken;
        } else {
            throw new RuntimeException("Invalid verification code.");
        }
    }


    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
