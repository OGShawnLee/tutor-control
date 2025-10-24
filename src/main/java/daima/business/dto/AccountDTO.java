package daima.business.dto;

import daima.business.dto.enumeration.AccountRole;
import daima.business.rules.Validator;
import daima.common.InvalidFieldException;

import org.mindrot.jbcrypt.BCrypt;

public class AccountDTO {
  private final String email;
  private final String password;
  private final AccountRole role;

  public AccountDTO(String email, String password, AccountRole role) throws InvalidFieldException {
    this.email = Validator.getValidEmail(email);
    this.password = Validator.getValidPassword(password);
    this.role = role;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public AccountRole getRole() {
    return role;
  }

  public boolean hasPasswordMatch(String candidate) {
    return BCrypt.checkpw(candidate, this.password);
  }
}
