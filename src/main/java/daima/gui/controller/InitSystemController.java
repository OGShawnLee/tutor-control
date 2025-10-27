package daima.gui.controller;

import daima.business.dao.StaffDAO;
import daima.business.dto.person.StaffDTO;
import daima.business.dto.enumeration.AccountRole;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InitSystemController extends Controller {
  @FXML
  private TextField fieldEmail;
  @FXML
  private TextField fieldWorkerID;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldPaternalLastName;
  @FXML
  private TextField fieldMaternalLastName;

  public void initialize() {
    AlertFacade.showInformationAndWait("Bienvenido a su Sistema de Control Interno de Tutorías.");
    AlertFacade.showInformationAndWait("Por favor, cree su cuenta de administrador para iniciar el sistema.");
  }

  /**
   * Verifies if an admin account with the given email already exists and shows an error.
   *
   * @param email the email to check for duplicates.
   * @param workerID the worker ID to check for duplicates.
   * @return true if no duplicate account exists, false otherwise.
   * @throws UserDisplayableException if an error occurs while checking for duplicates.
   * @throws InvalidFieldException if there is an error creating the DTO.
   */
  public boolean verifyDuplicateAccount(String email, String workerID) throws InvalidFieldException, UserDisplayableException {
    StaffDTO existingStaffDTO = StaffDAO.getInstance().findOne(email);

    if (existingStaffDTO != null) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar administrador debido a que ya existe una cuenta con ese correo electrónico.");
      return false;
    }

    existingStaffDTO = StaffDAO.getInstance().findOneByWorkerID(workerID);

    if (existingStaffDTO != null) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar administrador debido a que ya existe una cuenta con ese número de empleado.");
      return false;
    }

    return true;
  }

  public void registerAdmin(StaffDTO staffDTO) throws InvalidFieldException, UserDisplayableException {
    StaffDAO.getInstance().createOne(staffDTO);
    AlertFacade.showSuccessAndWait("Su cuenta de administrador ha sido registrada existosamente.");
    AlertFacade.showSuccessAndWait("Inicie sesión con sus credenciales.");
  }

  public void onClickRegisterAdmin() {
    try {
      StaffDTO staffDTO = new StaffDTO.Builder()
        .setEmail(fieldEmail.getText())
        .setWorkerID(fieldWorkerID.getText())
        .setName(fieldName.getText())
        .setPaternalLastName(fieldPaternalLastName.getText())
        .setMaternalLastName(fieldMaternalLastName.getText())
        .setRole(AccountRole.ADMIN)
        .build();

      if (verifyDuplicateAccount(staffDTO.getEmail(), staffDTO.getWorkerID())) {
        registerAdmin(staffDTO);
        navigateFromThisPageTo("Login Page", "GUILoginPage");
      }
    } catch (InvalidFieldException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
