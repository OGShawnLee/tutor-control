package daima.business.dto;

import daima.business.dto.enumeration.AccountRole;
import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

public class StaffDTO {
  private String email;
  private String workerID;
  private String name;
  private String paternalLastName;
  private String maternalLastName;
  private AccountRole role;
  private String programAcronym;

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

  public AccountRole getRole() {
    return role;
  }

  public String getProgramAcronym() {
    return programAcronym;
  }

  public static class Builder {
    private String email;
    private String workerID;
    private String name;
    private String paternalLastName;
    private String maternalLastName;
    private AccountRole role;

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

    public StaffDTO build() throws InvalidFieldException {
      return new StaffDTO(this);
    }
  }
}
