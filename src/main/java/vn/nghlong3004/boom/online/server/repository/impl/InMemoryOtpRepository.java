package vn.nghlong3004.boom.online.server.repository.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.nghlong3004.boom.online.server.model.OTPInfo;
import vn.nghlong3004.boom.online.server.repository.OTPRepository;

/**
 * Project: boom-online-server
 *
 * @author nghlong3004
 * @since 12/9/2025
 */
@Repository
@RequiredArgsConstructor
public class InMemoryOtpRepository implements OTPRepository {

  private final Map<String, OTPInfo> storage;

  @Override
  public void saveOtp(String email, String otpCode, LocalDateTime expiryTime) {
    storage.put(email, new OTPInfo(otpCode, expiryTime));
  }

  @Override
  public Optional<OTPInfo> getOtp(String email) {
    return Optional.ofNullable(storage.get(email));
  }

  @Override
  public void deleteOtp(String email) {
    storage.remove(email);
  }
}
