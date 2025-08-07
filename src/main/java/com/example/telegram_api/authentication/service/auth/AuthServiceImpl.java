package com.example.telegram_api.authentication.service.auth;
import com.example.telegram_api.authentication.auth.InfoChangePassword;
import com.example.telegram_api.authentication.repository.AuthRepository;
import com.example.telegram_api.exception.NotFoundExceptionClass;
import com.example.telegram_api.jwt.JwtTokenUtils;
import com.example.telegram_api.model.entity.Users;
import com.example.telegram_api.model.entity.Role;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;


    @Value("${myAdmin.username}")
    private String adminUsername;
    @Value("${myAdmin.password}")
    private String adminPassword;

    public AuthServiceImpl(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtTokenUtils jwtTokenUtils) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @PostConstruct
    public void createAdmin() {
       if (authRepository.findAll().isEmpty()){
           Users user = new Users();
           user.setUsername(adminUsername);
           String pass = passwordEncoder.encode(adminPassword);
           user.setEmail("admin_example@gmail.com");
           user.setFullname("Kim pitu");
           user.setProfile("");
           user.setPassword(pass);
           user.setRole(Role.ADMIN);
           authRepository.save(user);
       }else{
           System.out.println("[] => [<< already created admin >>]");
       }
    }

    @PostConstruct
    public void createAuthor() {
        if (authRepository.findAll().size() < 2){
            Users user = new Users();
            user.setUsername("User");
            String pass = passwordEncoder.encode(adminPassword);
            user.setPassword(pass);
            user.setEmail("user_example@gmail.com");
            user.setFullname("Kim pitu");
            user.setProfile("");
            user.setRole(Role.USER);
            authRepository.save(user);
        }else{
            System.out.println("[] -> [<< already created User >>]");
        }
    }

    @Override
    public Users getUserByUsername(String username){
        Users users = authRepository.findByUsername(username);
        return users;
    }

    @Override
    public void changePassword(UUID id, InfoChangePassword password) {
        Optional<Users> adminOptional = authRepository.findById(id);
        if (adminOptional.isPresent()) {
            Users user = adminOptional.get();
            String pass = passwordEncoder.encode(password.getCurrentPassword());
            String newpass = passwordEncoder.encode(password.getNewPassword());

            if (!passwordEncoder.matches(password.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Current Password isn't correct. Please Try Again.");
            }

            if (passwordEncoder.matches(password.getNewPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Your new password is still the same as your old password");
            }

            if (!password.getNewPassword().equals(password.getConfirmPassword())) {
                throw new IllegalArgumentException("Your confirm password does not match with your new password");
            }

            user.setPassword(newpass);
            authRepository.save(user);
        } else {
            throw new NotFoundExceptionClass("Users not found with ID: " + id);
        }
    }

    @Override
    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return authRepository.findByUsername(username);
        } else {
            throw new RuntimeException("Invalid authentication principal");
        }
    }
}
