package daima.business.dto.person;

import daima.business.dto.Record;
import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

import java.time.LocalDateTime;

abstract class Person implements Record {
  protected String email;
  protected String name;
  protected String paternalLastName;
  protected String maternalLastName;
  protected LocalDateTime createdAt;

  public Person(PersonBuilder builder) throws InvalidFieldException {
    this.email = Validator.getValidEmail(builder.email);
    this.name = Validator.getValidName(builder.name, "name", 3, 64);
    this.paternalLastName = Validator.getValidName(
      builder.paternalLastName,
      "Apellido Paterno",
      3,
      64
    );

    if (builder.maternalLastName != null && builder.maternalLastName.length() > 0) {
      this.maternalLastName = Validator.getValidName(
        builder.maternalLastName,
        "Apellido Materno",
        3,
        64
      );
    }

    this.createdAt = builder.createdAt;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPaternalLastName() {
    return paternalLastName;
  }

  public String getMaternalLastName() {
    return maternalLastName;
  }

  public String getFullName() {
    if (maternalLastName == null || maternalLastName.isEmpty()) {
      return String.format(
        "%s %s",
        name,
        paternalLastName
      );
    } else {
      return String.format(
        "%s %s %s",
        name,
        paternalLastName,
        maternalLastName
      );
    }
  }

  @Override
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
