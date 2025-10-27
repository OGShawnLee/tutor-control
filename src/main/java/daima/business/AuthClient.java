package daima.business;

import daima.business.dto.AccountDTO;
import daima.common.UserDisplayableException;

/*
  * AuthClient is a singleton class that manages the authentication state of the application.
  * It holds information about the currently logged-in user and provides methods to access and modify this information.
 */
public class AuthClient {
  private AccountDTO currentUser;
  private static AuthClient instance;

  private AuthClient() {
    this.currentUser = null;
  }

  public static AuthClient getInstance() {
    if (instance == null) {
      instance = new AuthClient();
    }

    return instance;
  }

  public AccountDTO getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(AccountDTO currentUser) {
    this.currentUser = currentUser;
  }

  public void getCurrentStaffDTO() throws UserDisplayableException {

  }
}
