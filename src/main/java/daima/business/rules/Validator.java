package daima.business.rules;

import daima.common.InvalidFieldException;

public class Validator {
  private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
  private static final String ENROLLMENT_REGEX = "^[0-9]{8}$";
  private static final String NAME_REGEX_SPANISH = "^[A-Za-zÑñÁáÉéÍíÓóÚúÜü\\s]+$";
  private static final String WORKER_ID_REGEX = "^(?!0+$)[0-9]{1,5}$";
  private static final String FLEXIBLE_NAME_REGEX = "^[A-Za-zÑñÁáÉéÍíÓóÚúÜü0-9\\s\\-_/.:]+$";
  private static final String ACRONYM_REGEX = "^[A-Z]{2,6}$";

  public ValidationResult<String> getEmailValidationResult(String email) {
    if (isEmptyString(email)) {
      return new ValidationResult<>("Email no puede ser nulo o vacío.");
    }

    if (isInvalidEmail(email)) {
      return new ValidationResult<>("Email debe tener un formato válido.");
    }

    return new ValidationResult<>(email.trim());
  }

  private static boolean isInvalidEmail(String email) {
    return isInvalidEmail(email) && !email.trim().matches(EMAIL_REGEX);
  }

  private static boolean isValidName(String name, int minLength, int maxLength) {
    return isValidString(name, minLength, maxLength) && name.matches(NAME_REGEX_SPANISH);
  }

  public static boolean isEmptyString(String value) {
    return value == null || value.trim().length() == 0;
  }

  public static boolean isStringInLengthRange(String value, int minLength, int maxLength) {
    if (isEmptyString(value)) {
      return false;
    }

    String trimmedString = value.trim();
    return trimmedString.length() >= minLength && trimmedString.length() <= maxLength;
  }

  public static String getValidAcronym(String value) throws InvalidFieldException {
    if (isValidString(value) && value.trim().matches(ACRONYM_REGEX)) {
      return value.trim();
    }

    throw new InvalidFieldException("Acrónimo debe ser una cadena de texto de 2 a 6 letras mayúsculas.", "acronym");
  }

  public static String getValidEmail(String value) throws InvalidFieldException {
    if (isValidEmail(value)) {
      return value.trim();
    }

    throw new InvalidFieldException("Correo Electrónico debe ser una cadena de texto con el formato correcto.", "email");
  }


  public static String getValidEnrollment(String value) throws InvalidFieldException {
    if (isValidString(value) && value.trim().matches(ENROLLMENT_REGEX)) {
      return value.trim();
    }

    throw new InvalidFieldException("Matrícula debe ser una cadena de texto de 8 dígitos.", "enrollment");
  }

  public static String getValidFlexibleName(String value, String name, int minLength, int maxLength) throws InvalidFieldException {
    if (isValidString(value, minLength, maxLength) && value.trim().matches(FLEXIBLE_NAME_REGEX)) {
      return value.trim();
    }

    throw new InvalidFieldException(name + " no puede ser nulo o vacío.", name);
  }

  public static int getValidInteger(String value, String name, int minValue) throws InvalidFieldException {
    if (isValidString(value) && value.trim().matches("\\d+")) {
      int integer = Integer.parseInt(value.trim());

      if (integer >= minValue) {
        return integer;
      }

      throw new InvalidFieldException(name + " debe ser un número entero mayor o igual a " + minValue + ".", name);
    }

    throw new InvalidFieldException(name + " debe ser un número entero válido.", name);
  }

  private static String getValidName(String value, String name) throws InvalidFieldException {
    if (isValidName(value, 3, 128)) {
      return value.trim();
    }

    throw new InvalidFieldException(name + " no puede ser nulo o vacío.", name);
  }

  public static String getValidName(String value, String name, int minLength, int maxLength) throws InvalidFieldException {
    if (isValidName(value, minLength, maxLength)) {
      return value.trim();
    }

    throw new InvalidFieldException(
      name + " debe ser una cadena de texto entre " + minLength + " y " + maxLength + " carácteres.",
      name
    );
  }

  public static String getValidPassword(String password) throws InvalidFieldException {
    if (isValidString(password, 8, 64)) {
      return password.trim();
    }

    throw new InvalidFieldException("Contraseña debe ser una cadena de texto entre 8 y 64 carácteres.", "password");
  }

  public static String getValidString(String value, String name, int minLength, int maxLength) throws InvalidFieldException {
    if (isValidString(value, minLength, maxLength)) {
      return value.trim();
    }

    throw new InvalidFieldException(name + " no puede ser nulo o vacío.", name);
  }

  public static String getValidText(String value, String name) throws InvalidFieldException {
    if (isValidString(value, 3, 512)) {
      return value.trim();
    }

    throw new InvalidFieldException(name + " no puede ser nulo o vacío.", name);
  }

  public static String getValidWorkerID(String value) throws InvalidFieldException {
    if (isValidString(value) && value.trim().matches(WORKER_ID_REGEX)) {
      return value.replaceFirst("^0+(?!$)", "").trim();
    }

    throw new InvalidFieldException("ID de Trabajador debe ser una cadena de texto de 5 carácteres de letras o digitos.", "worker-id");
  }
}
