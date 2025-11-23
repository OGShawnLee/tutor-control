package daima.gui.controller;

import daima.business.dao.ProgramDAO;
import daima.business.dao.StaffDAO;
import daima.business.dto.ProgramDTO;
import daima.business.dto.person.StaffDTO;
import daima.business.dto.enumeration.AccountRole;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.controlsfx.control.CheckComboBox;

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
  @FXML
  private CheckComboBox<ProgramDTO> fieldProgram;

  public void initialize() {
    configureFieldRole();
    configureFieldProgram();
    configureFieldProgramOnSelectRole();
  }

  public void configureFieldProgramOnSelectRole() {
    fieldRole.setOnAction(event -> {
      AccountRole selectedRole = fieldRole.getValue();
      fieldProgram.setDisable(selectedRole != AccountRole.COORDINATOR && selectedRole != AccountRole.TUTOR);
    });
  }

  public void configureFieldRole() {
    fieldRole.getItems().addAll(AccountRole.values());
    fieldRole.setValue(AccountRole.ADMIN);
  }

  public void configureFieldProgram() {
    try {
      fieldProgram.getItems().addAll(ProgramDAO.getInstance().getAll());
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar los programas académicos, intente de nuevo más tarde.");
    } catch (InvalidFieldException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
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

  public void registerTutor(StaffDTO staffDTO) throws UserDisplayableException {
    ArrayList<ProgramDTO> programDTOList = new ArrayList<>(fieldProgram.getCheckModel().getCheckedItems());

    if (programDTOList.isEmpty()) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar tutor debido a que no se ha seleccionado ningún programa académico que tutoreé.");
      return;
    }

    if (programDTOList.size() > 3) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar tutor debido a que solo puede tutorear hasta tres programas académicos.");
      return;
    }

    StaffDAO.getInstance().createOneTutor(staffDTO, programDTOList);
    AlertFacade.showInformationAndWait("El tutor ha sido registrado exitosamente.");
  }

  public void registerCoordinator(StaffDTO staffDTO) throws UserDisplayableException {
    ArrayList<ProgramDTO> programDTOList = new ArrayList<>(fieldProgram.getCheckModel().getCheckedItems());

    if (programDTOList.size() > 1) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar coordinador debido a que solo puede coordinar un programa académico.");
      return;
    }

    ProgramDTO programDTO = programDTOList.get(0);

    if (programDTO == null) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar coordinador debido a que no se ha seleccionado un programa académico que coordine.");
      return;
    }

    StaffDAO.getInstance().createOneCoordinator(staffDTO, programDTO);
    AlertFacade.showInformationAndWait("El coordinador ha sido registrado exitosamente.");
  }

  public void registerStaff(StaffDTO staffDTO) throws UserDisplayableException {
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
        if (staffDTO.getRole() == AccountRole.COORDINATOR) {
          registerCoordinator(staffDTO);
        } else if (staffDTO.getRole() == AccountRole.TUTOR) {
          registerTutor(staffDTO);
        } else {
          registerStaff(staffDTO);
        }
      }
    } catch (InvalidFieldException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
