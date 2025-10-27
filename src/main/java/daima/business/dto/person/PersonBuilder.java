package daima.business.dto.person;

import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

import java.time.LocalDateTime;

abstract class PersonBuilder<T extends PersonBuilder<T>> {
  String email;
  String name;
  String paternalLastName;
  String maternalLastName;
  LocalDateTime createdAt;

  public T getSelf() {
    return (T) this;
  }

  public T setEmail(String email) throws InvalidFieldException {
    this.email = Validator.getValidEmail(email);
    return getSelf();
  }

  public T setName(String name) throws InvalidFieldException {
    this.name = Validator.getValidName(name, "name", 3, 64);
    return getSelf();
  }

  public T setPaternalLastName(String paternalLastName) throws InvalidFieldException {
    this.paternalLastName = Validator.getValidName(
      paternalLastName,
      "Apellido Paterno",
      3,
      64
    );
    return getSelf();
  }

  public T setMaternalLastName(String maternalLastName) throws InvalidFieldException {
    if (maternalLastName != null && maternalLastName.length() > 0) {
      this.maternalLastName = Validator.getValidName(
        maternalLastName,
        "Apellido Materno",
        3,
        64
      );
    }
    return getSelf();
  }


  public T setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return getSelf();
  }

  public abstract Person build() throws InvalidFieldException;
}
