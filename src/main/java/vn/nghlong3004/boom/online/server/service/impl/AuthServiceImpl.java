package vn.nghlong3004.boom.online.server.service.impl;

import vn.nghlong3004.boom.online.server.exception.ErrorCode;
import vn.nghlong3004.boom.online.server.exception.ResourceException;
import vn.nghlong3004.boom.online.server.mapper.UserMapper;
import vn.nghlong3004.boom.online.server.model.Role;
import vn.nghlong3004.boom.online.server.model.User;
import com.vn.nghlong3004.boomonline.server.model.request.*;
import vn.nghlong3004.boom.online.server.model.request.*;
import vn.nghlong3004.boom.online.server.model.response.LoginResponse;
import vn.nghlong3004.boom.online.server.model.response.OTPResponse;
import vn.nghlong3004.boom.online.server.repository.UserRepository;
import vn.nghlong3004.boom.online.server.service.AuthService;
import vn.nghlong3004.boom.online.server.service.EmailService;
import vn.nghlong3004.boom.online.server.service.OTPService;
import vn.nghlong3004.boom.online.server.service.TokenService;
import vn.nghlong3004.boom.online.server.service.email.EmailType;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Project: boom-online-server
 *
 * @author nghlong3004
 * @since 12/8/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final EmailService emailService;
  private final OTPService otpService;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public void register(RegisterRequest request) {
    log.info("Processing registration request for email: {}", request.email());
    if (userRepository.existsByEmail(request.email())) {
      log.warn("Registration failed. Email {} already exists.", request.email());
      throw new ResourceException(ErrorCode.EMAIL_ALREADY);
    }
    User user = userMapper.toEntity(request);
    user.setRole(Role.USER);
    user.setPasswordHash(passwordEncoder.encode(request.password()));
    User savedUser = userRepository.save(user);
    log.info("User registered successfully with ID: {}", savedUser.getId());
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    log.info("Processing login request for email: {}", request.email());
    Authentication authentication;
    try {
      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    } catch (BadCredentialsException | UsernameNotFoundException e) {
      throw new ResourceException(ErrorCode.INVALID_CREDENTIALS);
    }
    log.info("Generate Token login request for email: {}", request.email());
    String accessToken = tokenService.generateAccessToken(authentication);
    String refreshToken = tokenService.generateRefreshToken(authentication);

    return new LoginResponse(accessToken, refreshToken);
  }

  @Override
  public OTPResponse forgotPassword(ForgotPasswordRequest request) {
    log.info("Processing forgot password request for email: {}", request.email());
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new ResourceException(ErrorCode.EMAIL_INCORRECT));

    String otp = otpService.generateAndSaveOtp(request.email());

    Map<String, String> data = new HashMap<>();
    data.put("FULL_NAME", user.getFullName());
    data.put("OTP_CODE", otp);
    emailService.sendHtmlEmail(request.email(), request.lang(), EmailType.OTP, data);
    return new OTPResponse(otp);
  }

  @Override
  public OTPResponse verifyOTP(OTPRequest request) {
    log.info("Processing verify OTP request for email: {}", request.email());
    otpService.validateOtp(request.email(), request.OTP());
    return new OTPResponse(otpService.generateExchangeToken(request.email()));
  }

  @Override
  @Transactional
  public void resetPassword(ResetPasswordRequest request) {
    log.info("Processing reset request for email: {}", request.email());
    otpService.validateToken(request.token());
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new ResourceException(ErrorCode.EMAIL_INCORRECT));

    user.setPasswordHash(passwordEncoder.encode(request.newPassword()));

    Map<String, String> data = new HashMap<>();
    data.put("FULL_NAME", user.getFullName());
    emailService.sendHtmlEmail(request.email(), request.lang(), EmailType.RESET_SUCCESS, data);
  }
}
