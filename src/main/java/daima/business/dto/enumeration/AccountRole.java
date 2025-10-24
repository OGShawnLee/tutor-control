package daima.business.dto.enumeration;

public enum AccountRole {
  ADMIN,
  COORDINATOR,
  SUPERVISOR,
  TUTOR;

  @Override
  public String toString() {
    switch (this) {
      case ADMIN:
        return "Administrador";
      case COORDINATOR:
        return "Coordinador";
      case SUPERVISOR:
        return "Supervisor";
      case TUTOR:
        return "Tutor";
    }

    return "";
  }

  public String toDBString() {
    return this.name();
  }
}
