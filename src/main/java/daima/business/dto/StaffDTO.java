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

  public StaffDTO(
    String email,
    String workerID,
    String name,
    String paternalLastName,
    String maternalLastName,
    AccountRole role
  ) throws InvalidFieldException {
    this.email = Validator.getValidEmail(email);
    this.workerID = Validator.getValidWorkerID(workerID);
    this.name = Validator.getValidName(name, "name", 3, 64);
    this.paternalLastName = Validator.getValidName(
      paternalLastName,
      "Apellido Paterno",
      3,
      64
    );

    if (maternalLastName != null && maternalLastName.length() > 0) {
      this.maternalLastName = Validator.getValidName(
        maternalLastName,
        "Apellido Materno",
        3,
        64
      );
    }

    this.role = role;
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
}
