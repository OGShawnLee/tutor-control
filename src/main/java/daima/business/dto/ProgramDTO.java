package daima.business.dto;

import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

import java.time.LocalDateTime;

public class ProgramDTO {
  private int id;
  private final String acronym;
  private final String name;
  private LocalDateTime createdAt;

  public ProgramDTO(String acronym, String name) throws InvalidFieldException {
    this.acronym = Validator.getValidAcronym(acronym);
    this.name = Validator.getValidFlexibleName(name, "program-name", 6, 64);
  }

  public String getAcronym() {
    return acronym;
  }

  public String getName() {
    return name;
  }

  public String getCoordinatorFullName() {
    return coordinatorFullName;
  }

  public void setCoordinatorFullName(String coordinatorFullName) {
    this.coordinatorFullName = coordinatorFullName;
  }

  public String getCoordinatorEmail() {
    return coordinatorEmail;
  }

  public void setCoordinatorEmail(String coordinatorEmail) {
    this.coordinatorEmail = coordinatorEmail;
  }

  @Override
  public String toString() {
    return String.format("%s (%s)", name, acronym);
  }
}
