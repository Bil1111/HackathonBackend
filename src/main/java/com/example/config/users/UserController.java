package com.example.config.users;

import com.example.config.token.JwtTokenProvider;
import com.example.config.users.recoverPassword.PasswordResetRequest;
import com.example.config.users.recoverPassword.ResetPasswordRequest;
import com.example.config.users.recoverPassword.SMSRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> customer = userService.getUserById(id);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> customers = userService.getAllUsers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUserDetails(@RequestBody UserRegistrationRequest request, @PathVariable("id") Long id) {
        userService.updateUserDetails(request, id);
        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationRequest request,
                                               BindingResult bindingResult) {
        try {
            userService.registerValidation(bindingResult);
            userService.checkingUserLoginEmail(request.getLogin(),request.getEmail());
            userService.registerUser(request);
            return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid UserRegistrationRequest request,
                                               BindingResult bindingResult) {
        try {
            userService.registerValidation(bindingResult);
            userService.checkingUserLoginEmail(request.getLogin(),request.getEmail());
            userService.registerAdmin(request);
            return new ResponseEntity<>("Admin added successfully", HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody @Valid UserLoginRequest request,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Якщо є помилки валідації, повертаємо їх
            Map<String, String> errorMessages = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<User> user = userService.findByLogin(request.getLogin());
            if (user.isPresent() && userService.loginUser(request.getLogin(), request.getPassword())) {
                User loggedInUser = user.get();
                // Генерація токену після успішного входу
                String token = jwtTokenProvider.generateToken(loggedInUser); // Генерація токену
                Map<String, String> response = new HashMap<>();
                response.put("token", token); // Повертаємо токен у відповіді
                if (loggedInUser.getRole().equals(Role.ADMIN)) {
                    response.put("redirect", "/admin"); // Адмін
                } else {
                    response.put("redirect", "/about"); // Звичайний користувач
                }
                return new ResponseEntity<>(response, HttpStatus.OK); // Повертаємо ResponseEntity з Map
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (UserNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage()); // Помилка у форматі Map
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/register/google")
    public ResponseEntity<String> registerGoogle(@AuthenticationPrincipal OAuth2User principal) {
        // Отримуємо дані користувача, наприклад, ім'я, email
        String name = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        // Створити чи оновити користувача у базі даних, якщо потрібно
        userService.registerOrUpdateUserFromGoogle(name, email);

        return new ResponseEntity<>("Hello, " + name + " (" + email + ")", HttpStatus.OK);
    }

    @PostMapping("/request-password-reset-email")
    public ResponseEntity<String> requestPasswordResetEmail(@RequestBody PasswordResetRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return new ResponseEntity<>("Email cannot be empty", HttpStatus.BAD_REQUEST);
        }

        try {
            userService.requestPasswordResetEmail(request.getEmail());
            return new ResponseEntity<>("Password reset link sent to your email", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/request-password-reset-phone")
    public ResponseEntity<String> requestPasswordResetByPhone(@RequestBody SMSRequest smsRequest) {
        try {
            userService.requestPasswordResetByPhone(smsRequest.getPhoneNumber());
            return new ResponseEntity<>("Password reset code sent via SMS", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password-email")
    public ResponseEntity<String> resetPasswordEmail(@RequestParam String token,
                                                @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            userService.resetPassword(token, resetPasswordRequest.getNewPassword());
            return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password-phone")
    public ResponseEntity<String> resetPasswordPhone(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            userService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
            return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}