package com.vn.nghlong3004.boomonline.server.repository.impl;

import com.vn.nghlong3004.boomonline.server.model.OTPInfo;
import com.vn.nghlong3004.boomonline.server.repository.OTPRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/**
 * Project: boom-online-server
 *
 * @author nghlong3004
 * @since 12/9/2025
 */
@Repository
public class InMemoryOtpRepository implements OTPRepository {

  private final Map<String, OTPInfo> storage = new ConcurrentHashMap<>();

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
