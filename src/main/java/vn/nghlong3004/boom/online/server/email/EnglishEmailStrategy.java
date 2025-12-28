package vn.nghlong3004.boom.online.server.email;

import org.springframework.stereotype.Service;
import vn.nghlong3004.boom.online.server.constant.LocaleConstant;

/**
 * Project: boom-online-server
 *
 * @author nghlong3004
 * @since 12/9/2025
 */
@Service
public class EnglishEmailStrategy implements EmailLocaleStrategy {

  @Override
  public String getSupportedLanguage() {
    return LocaleConstant.ENGLISH;
  }

  @Override
  public String getSubject(EmailType type) {
    return switch (type) {
      case OTP -> LocaleConstant.OTP_SUBJECT_EN;
      case WELCOME -> LocaleConstant.WELCOME_SUBJECT_EN;
      case RESET_SUCCESS -> LocaleConstant.RESET_SUCCESS_SUBJECT_EN;
    };
  }

  @Override
  public String getTemplatePath(EmailType type) {
    return switch (type) {
      case OTP -> LocaleConstant.OTP_TEMPLATE_EN;
      case WELCOME -> LocaleConstant.WELCOME_TEMPLATE_EN;
      case RESET_SUCCESS -> LocaleConstant.RESET_SUCCESS_TEMPLATE_EN;
    };
  }
}
