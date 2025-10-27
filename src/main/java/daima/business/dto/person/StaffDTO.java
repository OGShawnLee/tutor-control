package daima.business.dto.person;

import daima.business.dto.enumeration.AccountRole;
import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

public class StaffDTO extends Person {
  private final String workerID;
  private final AccountRole role;

  StaffDTO(Builder builder) throws InvalidFieldException {
    super(builder);
    this.workerID = Validator.getValidWorkerID(builder.workerID);
    this.role = builder.role;
  }

  public String getWorkerID() {
    return workerID;
  }

  public AccountRole getRole() {
    return role;
  }

  public static class Builder extends PersonBuilder<Builder> {
    private String workerID;
    private AccountRole role;

    public Builder setWorkerID(String workerID) throws InvalidFieldException {
      this.workerID = Validator.getValidWorkerID(workerID);
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
