package daima.business.dto;

import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

public class ProgramDTO {
  private final String name;
  private final String acronym;

  public ProgramDTO(String name, String acronym) throws InvalidFieldException {
    this.name = Validator.getValidFlexibleName(name, "program-name", 6, 64);
    this.acronym = Validator.getValidAcronym(acronym);
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
