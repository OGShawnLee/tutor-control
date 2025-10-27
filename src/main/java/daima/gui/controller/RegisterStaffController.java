package daima.gui.controller;

import daima.business.dao.StaffDAO;
import daima.business.dto.person.StaffDTO;
import daima.business.dto.enumeration.AccountRole;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class RegisterStaffController extends Controller {
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
  @FXML
  private ComboBox<AccountRole> fieldRole;

  public void initialize() {
    configureFieldRole();
  }

  public void configureFieldRole() {
    fieldRole.getItems().addAll(AccountRole.values());
    fieldRole.setValue(AccountRole.TUTOR);
  }

  public boolean verifyDuplicateStaff(String email, String workerID) throws InvalidFieldException, UserDisplayableException {
    StaffDTO existingStaffDTO = StaffDAO.getInstance().findOne(email);

    if (existingStaffDTO != null) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar trabajador debido a que ya existe una cuenta con ese correo electrónico.");
      return false;
    }

    existingStaffDTO = StaffDAO.getInstance().findOneByWorkerID(workerID);

    if (existingStaffDTO != null) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar trabajador debido a que ya existe una cuenta con ese número de empleado.");
      return false;
    }

    return true;
  }

  public void registerStaff(StaffDTO staffDTO) throws InvalidFieldException, UserDisplayableException {
    StaffDAO.getInstance().createOne(staffDTO);
    AlertFacade.showInformationAndWait("El trabajador ha sido registrado exitosamente.");
  }

  public void onClickRegisterStaff() {
    try {
      StaffDTO staffDTO = new StaffDTO.Builder()
        .setEmail(this.fieldEmail.getText())
        .setWorkerID(this.fieldWorkerID.getText())
        .setName(this.fieldName.getText())
        .setPaternalLastName(this.fieldPaternalLastName.getText())
        .setMaternalLastName(this.fieldMaternalLastName.getText())
        .setRole(this.fieldRole.getValue())
        .build();

      if (verifyDuplicateStaff(staffDTO.getEmail(), staffDTO.getWorkerID())) {
        registerStaff(staffDTO);
      }
    } catch (InvalidFieldException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
