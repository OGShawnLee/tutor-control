package daima.business.dto;

import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

public class ProgramDTO {
  private final String acronym;
  private final String name;

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

  @Override
  public String toString() {
    return String.format("%s (%s)", name, acronym);
  }
}
