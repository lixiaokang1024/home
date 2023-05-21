package com.project.lxk.home.util;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 具有唯一编码的 enum
 */
public interface CodableEnum {
  /**
   * 根据唯一编码获取 enum 类型
   */
  static <E extends CodableEnum> E ofCode(Class<E> enumType, Integer code) {
    if (code == null) {
      return null;
    }

    E[] enums = enumType.getEnumConstants();
    for (E enm : enums) {
      if (code == enm.getCode()) {
        return enm;
      }
    }
    return null;
  }

  @JsonValue
  int getCode();

  default String getText(MessageSource messageSource) {
    return getText(messageSource, LocaleContextHolder.getLocale());
  }

  default String getText(MessageSource messageSource, Locale locale) {
    if (!(this instanceof Enum)) {
      throw new IllegalStateException("not an enum");
    }
    Enum<?> self = (Enum<?>) this;
    String key = "constant." + this.getClass().getCanonicalName() + "." + getCode();
    return messageSource.getMessage(key, null, self.name(), locale);
  }
}
