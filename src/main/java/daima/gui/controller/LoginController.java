package daima.gui.controller;

import daima.business.AuthClient;
import daima.business.dao.AccountDAO;
import daima.business.dto.AccountDTO;
import daima.business.rules.Validator;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class LoginController extends Controller {
  @FXML
  public TextField fieldEmail;
  @FXML
  public PasswordField fieldPassword;

  private void logIn(AccountDTO accountDTO, String password) {
    if (accountDTO == null) {
      AlertFacade.showErrorAndWait("Las credenciales que ha introducido son invalidas, intente de nuevo.");
    } else if (accountDTO.hasPasswordMatch(password)) {
      AuthClient.getInstance().setCurrentUser(accountDTO);
      navigateToLandingPage();
    } else {
      AlertFacade.showErrorAndWait("Las credenciales que ha introducido son invalidas, intente de nuevo.");
    }
  }

  @FXML
  public void onClickLogin() {
    try {
      String email = Validator.getValidEmail(this.fieldEmail.getText());
      String password = Validator.getValidPassword(this.fieldPassword.getText());
      AccountDTO accountDTO = new AccountDAO().findOne(email);

      logIn(accountDTO, password);
    } catch (InvalidFieldException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
