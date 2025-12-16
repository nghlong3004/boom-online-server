package vn.nghlong3004.boom.online.server.model;

import java.time.LocalDateTime;

/**
 * Project: boom-online-server
 *
 * @author nghlong3004
 * @since 12/9/2025
 */
public record OTPInfo(String code, LocalDateTime expiryTime) {}
