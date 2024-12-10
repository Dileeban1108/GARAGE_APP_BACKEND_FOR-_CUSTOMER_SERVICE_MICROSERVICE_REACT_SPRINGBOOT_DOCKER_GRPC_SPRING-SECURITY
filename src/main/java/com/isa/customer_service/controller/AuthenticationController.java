package com.isa.customer_service.controller;
import com.isa.customer_service.dto.LoginResponse;
import com.isa.customer_service.dto.LoginUserDto;
import com.isa.customer_service.dto.UserDto;
import com.isa.customer_service.dto.VerifyUserDto;
import com.isa.customer_service.model.User;
import com.isa.customer_service.repository.UserRepository;
import com.isa.customer_service.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, EmailService emailService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.emailService=emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) {
        User registeredUser = authenticationService.signup(userDto);
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/changeEmail")
    public ResponseEntity<User> changeEmail(@RequestBody VerifyUserDto request, Authentication authentication) throws UnauthorizedEmailChangeException {
        User user = authenticationService.changeEmail(request.getEmail(), authentication);
        return ResponseEntity.ok(user); // Return the updated user
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<User> forgotPassword(@RequestBody UserDto userDto) {
        User user = authenticationService.forgotPassword(userDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        // Update to accept UserDto since authenticate returns UserDto
        UserDto authenticatedUserDto = authenticationService.authenticate(loginUserDto);

        String jwtToken = authenticatedUserDto.getAccessToken();
        String refreshToken = authenticatedUserDto.getRefreshToken();// You already get the token here
        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setRefreshToken(refreshToken)
                .setExpiresIn(jwtService.getExpirationTime());

        // Log the response
        System.out.printf("loginResponse: %s%n", loginResponse);

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/profile")
    public User getUserProfile(Authentication authentication) {
        return authenticationService.getUserProfile(authentication);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<User> updateUserProfile(
            @RequestParam("name") String name,
            @RequestParam("phoneNo") String phoneNo,
            @RequestParam("address") String address,
            @RequestParam("gender") String gender,
            @RequestParam(value = "image", required = false) MultipartFile image
           ) {

        ResponseEntity<Object> response = authenticationService.updateUser(name,phoneNo,address,gender,image);

        // Check if the update was successful
        if (response.getStatusCode() == HttpStatus.OK) {
            // Safely extract the updated user object
            User updatedUser = (User) response.getBody();
            return ResponseEntity.ok(updatedUser);  // Return the updated user with 200 OK
        }

        // If the update wasn't successful, return an appropriate error response
        return ResponseEntity.status(response.getStatusCode()).body(null);
    }
//    @PostMapping("/refresh-token")
//    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
//        // Extract the refresh token from the cookies
//        String refreshToken = jwtService.extractRefreshToken(request);
//
//        if (refreshToken == null || !jwtService.isTokenValid(refreshToken, null)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired refresh token.");
//        }
//
//        // Extract the username from the refresh token (you may want to verify it further if needed)
//        String username = jwtService.extractUsername(refreshToken);
//        User user = userRepo.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Generate a new access token
//        String newAccessToken = jwtService.generateToken(user);
//
//        // Return the new access token as the response
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, "refreshToken=" + refreshToken + "; HttpOnly; Secure; SameSite=Strict")
//                .body(newAccessToken);
//    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody VerifyUserDto userDto) {
        try {
            authenticationService.resetPassword(userDto);
            return ResponseEntity.ok("password reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/resetEmail")
    public ResponseEntity<?> resetEmail(@RequestBody VerifyUserDto userDto, Authentication authentication) {
        try {
            String newToken = authenticationService.resetEmail(userDto, authentication);  // Get the new token
            return ResponseEntity.ok(newToken);  // Return the new token in the response
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam("toEmail") String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}