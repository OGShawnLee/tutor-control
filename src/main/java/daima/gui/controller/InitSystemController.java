package daima.gui.controller;

import daima.business.dao.StaffDAO;
import daima.business.dto.StaffDTO;
import daima.business.dto.enumeration.AccountRole;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InitSystemController extends Controller {
  private static final Logger LOGGER = LogManager.getLogger(InitSystemController.class);
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
    AlertFacade.showSuccessAndWait("Reinicie el sistema para continuar.");
  }

  public void onClickRegisterAdmin() {
    try {
      StaffDTO staffDTO = new StaffDTO(
        this.fieldEmail.getText(),
        this.fieldWorkerID.getText(),
        this.fieldName.getText(),
        this.fieldPaternalLastName.getText(),
        this.fieldMaternalLastName.getText(),
        AccountRole.ADMIN
      );

      if (verifyDuplicateAccount(staffDTO.getEmail(), staffDTO.getWorkerID())) {
        registerAdmin(staffDTO);
        navigateToLandingPage();
      }
    } catch (InvalidFieldException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
