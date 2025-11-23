package daima.business.dto.person;

import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

public class TutoredDTO extends Person {
  private final String enrollmentID;
  private final String programAcronym;
  private final String tutorID;

  TutoredDTO(Builder builder) throws InvalidFieldException {
    super(builder);
    this.enrollmentID = builder.enrollmentID;
    this.programAcronym = builder.programAcronym;
    this.tutorID = builder.tutorID;
  }

  public String getEnrollmentID() {
    return enrollmentID;
  }

  public String getProgramAcronym() {
    return programAcronym;
  }

  public String getTutorID() {
    return tutorID;
  }

  public static class Builder extends PersonBuilder<Builder> {
    private String enrollmentID;
    private String programAcronym;
    private String tutorID;

    public Builder setEnrollmentID(String enrollmentID) throws InvalidFieldException {
      this.enrollmentID = Validator.getValidEnrollment(enrollmentID);
      super.setEmail(String.format("z%s@estudiantes.uv.mx", this.enrollmentID));
      return this;
    }

    @Deprecated
    @Override
    /*
     * Email is set automatically based on enrollment ID.
     */
    public Builder setEmail(String email) {
      throw new UnsupportedOperationException("Email is set automatically based on enrollment ID.");
    }

    public Builder setProgramAcronym(String programAcronym) throws InvalidFieldException {
      this.programAcronym = Validator.getValidAcronym(programAcronym);
      return this;
    }

    public Builder setTutorID(String tutorID) throws InvalidFieldException {
      this.tutorID = tutorID != null ? Validator.getValidEmail(tutorID) : null;
      return this;
    }

    public TutoredDTO build() throws InvalidFieldException {
      return new TutoredDTO(this);
    }
  }
}
