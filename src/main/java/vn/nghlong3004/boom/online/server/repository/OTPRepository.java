package vn.nghlong3004.boom.online.server.repository;

import vn.nghlong3004.boom.online.server.model.OTPInfo;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Project: boom-online-server
 *
 * @author nghlong3004
 * @since 12/9/2025
 */
public interface OTPRepository {
  void saveOtp(String email, String otpCode, LocalDateTime expiryTime);

  Optional<OTPInfo> getOtp(String email);

  void deleteOtp(String email);
}
