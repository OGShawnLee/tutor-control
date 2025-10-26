package daima.business.dto;

import daima.business.dto.enumeration.AccountRole;
import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

import java.time.LocalDateTime;

public class StaffDTO implements Record {
  private String email;
  private String workerID;
  private String name;
  private String paternalLastName;
  private String maternalLastName;
  private AccountRole role;
  private LocalDateTime createdAt;

  StaffDTO(Builder builder) throws InvalidFieldException {
    this.email = Validator.getValidEmail(builder.email);
    this.workerID = Validator.getValidWorkerID(builder.workerID);
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

    this.role = builder.role;
    this.createdAt = builder.createdAt;
  }

  public String getEmail() {
    return email;
  }

  public String getWorkerID() {
    return workerID;
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

  public AccountRole getRole() {
    return role;
  }

  @Override
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static class Builder {
    private String email;
    private String workerID;
    private String name;
    private String paternalLastName;
    private String maternalLastName;
    private AccountRole role;
    private LocalDateTime createdAt;

    public Builder setEmail(String email) throws InvalidFieldException {
      this.email = Validator.getValidEmail(email);
      return this;
    }

    public Builder setWorkerID(String workerID) throws InvalidFieldException {
      this.workerID = Validator.getValidWorkerID(workerID);
      return this;
    }

    public Builder setName(String name) throws InvalidFieldException {
      this.name = Validator.getValidName(name, "name", 3, 64);
      return this;
    }

    public Builder setPaternalLastName(String paternalLastName) throws InvalidFieldException {
      this.paternalLastName = Validator.getValidName(
        paternalLastName,
        "Apellido Paterno",
        3,
        64
      );
      return this;
    }

    public Builder setMaternalLastName(String maternalLastName) throws InvalidFieldException {
      if (maternalLastName != null && maternalLastName.length() > 0) {
        this.maternalLastName = Validator.getValidName(
          maternalLastName,
          "Apellido Materno",
          3,
          64
        );
      }
      return this;
    }

    public Builder setRole(AccountRole role) {
      this.role = role;
      return this;
    }

    public Builder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public StaffDTO build() throws InvalidFieldException {
      return new StaffDTO(this);
    }
  }
}
