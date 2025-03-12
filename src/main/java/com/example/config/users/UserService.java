package com.example.config.users;

import com.example.config.token.JwtTokenProvider;
import com.example.config.users.recoverPassword.EmailService;
import com.example.config.users.recoverPassword.SMSService;
import com.example.config.users.recoverPassword.TokenUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private SMSService smsService;
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUserDetails(UserRegistrationRequest request, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        userOptional.ifPresentOrElse(user -> {
            if (StringUtils.isNotBlank(request.getFirstName())) {
                user.setFirstName(request.getFirstName());
            }
            if (StringUtils.isNotBlank(request.getLastName())) {
                user.setLastName(request.getLastName());
            }
            if (StringUtils.isNotBlank(request.getEmail())) {
                user.setEmail(request.getEmail());
            }
            if (StringUtils.isNotBlank(request.getPhoneNumber())) {
                user.setPhoneNumber(request.getPhoneNumber());
            }
            if (StringUtils.isNotBlank(request.getLogin())) {
                user.setLogin(request.getLogin());
            }
            if (StringUtils.isNotBlank(request.getPassword())) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            userRepository.save(user);
        }, () -> {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User with id " + userId + " not found"
            );
        });
    }
    public void registerUser(UserRegistrationRequest request) {
        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getLogin(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER
        );
        // Зберігаємо користувача в базу
        userRepository.save(user);
        // Генеруємо токен
        String token = jwtTokenProvider.generateToken(user); // передаємо об'єкт користувача
        // Зберігаємо токен в базі даних
        user.setAuthToken(token); // Зберігаємо токен в користувача
        System.out.println(user);
        userRepository.save(user); // Оновлюємо користувача в базі
    }

    public void registerAdmin(UserRegistrationRequest request) {
        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getLogin(),
                passwordEncoder.encode(request.getPassword()),
                Role.ADMIN
        );
        userRepository.save(user);
        String token = jwtTokenProvider.generateToken(user);
        user.setAuthToken(token);
        userRepository.save(user);
    }

    public boolean loginUser(String login, String password) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User with login " + login + " not found"
            );
        }
        boolean isPasswordValid = passwordEncoder.matches(password, user.getPassword());
        if (isPasswordValid) {
            // Генерація токену після перевірки паролю
            String token = jwtTokenProvider.generateToken(user);
            user.setAuthToken(token);  // Оновлення токену для користувача
            userRepository.save(user); // Зберігаємо оновленого користувача
        }
        return isPasswordValid;
    }
    public void registerValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Якщо є помилки валідації, кидаємо виняток
            StringBuilder errorMessages = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n");
            }
            throw new ValidationException(errorMessages.toString());  // Створюємо виняток для валідації
        }
    }

    public void checkingUserLoginEmail(String login, String email){
        if (findByLogin(login).isPresent()) {
            throw new UserAlreadyExistsException("User with this login already exists");
        }else if (findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
    }
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(userRepository.findByLogin(login));
    }


    public User registerOrUpdateUserFromGoogle(String name, String email, String token) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(email));

        String[] nameParts = name.split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Оновлюємо ім'я, якщо воно ще не збережене
            if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
                user.setFirstName(firstName);
            }
            if (user.getLastName() == null || user.getLastName().isEmpty()) {
                user.setLastName(lastName);
            }
            user.setAuthToken(token);
            userRepository.save(user);
            return user;
        } else {
            User newUser = new User();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            newUser.setLogin(email);
            newUser.setRole(Role.USER);
            userRepository.save(newUser);
            return newUser;
        }
    }


    public void requestPasswordResetEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            String resetToken = tokenUtil.generatePasswordResetToken(user.get().getId());
            emailService.sendPasswordResetEmail(email, resetToken);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public void requestPasswordResetByPhone(String phoneNumber) {
        Optional<User> user = findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            String resetToken = tokenUtil.generatePasswordResetToken(user.get().getId());
            smsService.sendPasswordResetCode(phoneNumber, resetToken);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return Optional.ofNullable(userRepository.findByPhoneNumber(phoneNumber));
    }

    public void resetPassword(String token, String newPassword) {
        if (!tokenUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token");
        }

        Long userId = tokenUtil.getUserIdFromToken(token);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

}