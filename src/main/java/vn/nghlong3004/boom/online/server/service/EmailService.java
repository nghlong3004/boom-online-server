package vn.nghlong3004.boom.online.server.service;

import java.util.Map;
import vn.nghlong3004.boom.online.server.email.EmailType;

/**
 * Project: boom-online-server
 *
 * @author nghlong3004
 * @since 12/9/2025
 */
public interface EmailService {
  void sendHtmlEmail(String to, String lang, EmailType type, Map<String, String> data);
}
