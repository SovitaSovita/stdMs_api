package com.example.telegram_api.authentication.controller;
import com.example.telegram_api.authentication.auth.AuthenticationRequest;
import com.example.telegram_api.authentication.auth.InfoChangePassword;
import com.example.telegram_api.authentication.repository.AuthRepository;
import com.example.telegram_api.authentication.service.auth.AuthService;
import com.example.telegram_api.exception.NotFoundExceptionClass;
import com.example.telegram_api.jwt.JwtResponse;
import com.example.telegram_api.jwt.JwtTokenUtils;
import com.example.telegram_api.model.entity.Users;
import com.example.telegram_api.model.response.ApiResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authorization", description = "🔏")
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    @Value("${jwt.secret}")
    private String secret;

    public AuthController(AuthService authService, JwtTokenUtils jwtTokenUtils, PasswordEncoder passwordEncoder, AuthRepository authRepository) {
        this.authService = authService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.passwordEncoder = passwordEncoder;
        this.authRepository = authRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest jwtRequest) {
        try {
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());//                    emailService.sendEmail("sovitasovita28@gmail.com", "Digital TTEST", "Welcome test");
        } catch (Exception e) {
//            e.printStackTrace();
            throw new IllegalArgumentException("Invalid username or password");
        }

        final Users user = authService.getUserByUsername(jwtRequest.getUsername());
        final String token = jwtTokenUtils.generateToken(user);
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String role = claims.get("role", String.class);
        return ResponseEntity.ok(new JwtResponse(LocalDateTime.now(),
                jwtRequest.getUsername(),
                token,
                role,
                user.getEmail(),
                user.getProfile(),
                user.getFullname()
        ));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            Users user = authRepository.findByUsername(username);
//                    .orElseThrow(() -> new NotFoundExceptionClass("Invalid phone number or password"));
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new NotFoundExceptionClass("Invalid username or password");
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PutMapping("/change-password")
    @Operation(summary = "change password")
    @SecurityRequirement(name = "bearerAuth")
    public ApiResponse<?> changePassword(@Valid @RequestBody InfoChangePassword changePassword){
        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID id = currentUser.getId();
        authService.changePassword(id,changePassword);
        return ApiResponse.builder()
                .date(LocalDateTime.now())
                .message("successfully change password")
                .build();
    }


//    @PostMapping("/send-email")
//    @SecurityRequirement(name = "bearerAuth")
//    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
//        return "Email sent successfully!";
//    }



}

