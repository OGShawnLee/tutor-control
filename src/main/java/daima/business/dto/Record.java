package daima.business.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface Record {
  LocalDateTime getCreatedAt();

  default String getFormattedCreatedAt() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm", Locale.forLanguageTag("es-ES"));
    return getCreatedAt().format(formatter);
  }
}