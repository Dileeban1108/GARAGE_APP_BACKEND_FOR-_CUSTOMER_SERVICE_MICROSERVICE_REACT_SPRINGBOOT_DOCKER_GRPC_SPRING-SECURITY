package com.isa.customer_service.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name can't exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email can't exceed 100 characters")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits")
    @Column(name = "phone_no", length = 15)
    private String phoneNo;

    @Size(max = 255, message = "Address can't exceed 255 characters")
    @Column(name = "address")
    private String address;

    @Size(max = 10, message = "Gender must be 'male', 'female', or 'other'")
    @Column(name = "gender")  // New column for gender
    private String gender;

    @Size(max = 255, message = "Image URL or data is too long")
    @Column(name = "image")  // New column for image (could be URL or base64)
    private String image;

    @Column(name = "verification_code")
    private String verificationCode;

    @FutureOrPresent(message = "Verification expiration time must be in the future or present")
    @Column(name = "verification_expiration")
    private LocalDateTime verificationCodeExpiresAt;

    @NotNull(message = "Enabled status is required")
    @Column(name = "is_enabled")
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // Customize authorities if needed
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public User(String name, String email, String phoneNo, String address, String image, String gender) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
        this.gender=gender;
        this.image=image;
    }
}
