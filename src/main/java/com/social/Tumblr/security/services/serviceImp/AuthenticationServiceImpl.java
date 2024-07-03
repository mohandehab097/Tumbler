package com.social.Tumblr.security.services.serviceImp;

import com.social.Tumblr.security.exceptions.EmailAlreadyExistsException;
import com.social.Tumblr.security.exceptions.PhoneNumberAlreadyExistsException;
import com.social.Tumblr.security.exceptions.UserNameAlreadyExistsException;
import com.social.Tumblr.security.models.enums.Role;
import com.social.Tumblr.security.models.repositeries.UserRepository;
import com.social.Tumblr.security.models.dtos.response.AuthenticationResponseDto;
import com.social.Tumblr.security.models.dtos.request.LoginRequestDto;
import com.social.Tumblr.security.models.dtos.request.RegisterRequestDto;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.mappers.UserMapper;
import com.social.Tumblr.security.services.service.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private ImagesService imagesService;

    @Override
    public void register(RegisterRequestDto registerRequestDto, MultipartFile imageFile) {
        logger.info("Registering user with email: {}", registerRequestDto.getEmail());
        String imageFileName = imagesService.uploadImage(imageFile);
        checkUserDetailsUniqueness(registerRequestDto);
        Users user = CreateUser(imageFileName,registerRequestDto);
        String otp = emailVerificationService.SaveVerificationOtp(user);
        emailVerificationService.emailVerification(user, otp);
    }

    private Users CreateUser(String imageFileName,RegisterRequestDto registerRequestDto){
        Users user = userMapper.registerRequestDtoToUser(registerRequestDto);
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(Role.USER);
        user.setImage(imageFileName);
        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmail());
        return user;
    }


    private void checkUserDetailsUniqueness(RegisterRequestDto registerRequestDto) {
        if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already used by another user");
        }
        if (registerRequestDto.getPhoneNumber() != null &&
                userRepository.findByPhoneNumber(registerRequestDto.getPhoneNumber()).isPresent()) {
            throw new PhoneNumberAlreadyExistsException("Phone Number is already used by another user");
        }
        if (userRepository.findByFullName(registerRequestDto.getFullName()).isPresent()) {
            throw new UserNameAlreadyExistsException("Username is already used by another user");
        }
    }


    @Override
    public AuthenticationResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            logger.info("User login attempt: {}", loginRequestDto.getEmail());
            authenticateUser(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            Users user = getUserByEmail(loginRequestDto.getEmail());
            String token = generateAndSaveToken(user);
            logger.info("User logged in successfully: {}", user.getEmail());
            return createAuthenticationResponse(token);
        } catch (LockedException | DisabledException | BadCredentialsException ex) {
            handleLoginException(loginRequestDto.getEmail(), ex);
            throw ex;
        }
    }

    private void authenticateUser(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    private Users getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    private String generateAndSaveToken(Users user) {
        String token = jwtService.generateToken(user);
        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, token);
        return token;
    }

    private void handleLoginException(String email, RuntimeException ex) {
        logger.error("Login attempt failed: {}", email, ex);
    }

    private AuthenticationResponseDto createAuthenticationResponse(String token) {
        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setToken(token);
        return authenticationResponseDto;
    }

    public String verifyEmail(String otp){
       return emailVerificationService.verifyEmail(otp);
    }


}
