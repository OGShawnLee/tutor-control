package daima.gui.controller;

import daima.business.dao.ProgramDAO;
import daima.business.dao.TutoredDAO;
import daima.business.dto.ProgramDTO;
import daima.business.dto.person.TutoredDTO;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class RegisterTutoredController extends Controller {
  @FXML
  private TextField fieldEnrollmentID;
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldPaternalLastName;
  @FXML
  private TextField fieldMaternalLastName;
  @FXML
  private ComboBox<ProgramDTO> fieldProgram;

  public void initialize() {
    configureFieldProgram();
  }

  public void configureFieldProgram() {
    try {
      fieldProgram.getItems().addAll(ProgramDAO.getInstance().getAll());
      fieldProgram.setValue(fieldProgram.getItems().get(0));
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait("No ha sido posible cargar los programas académicos, intente de nuevo más tarde.");
    } catch (InvalidFieldException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }

  public boolean verifyDuplicateTutored(String email, String enrollmentID) throws InvalidFieldException, UserDisplayableException {
    TutoredDTO existingTutoredDTO = TutoredDAO.getInstance().findOne(email);

    if (existingTutoredDTO != null) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar tutorado debido a que ya existe uno con esa matrícula.");
      return false;
    }

    existingTutoredDTO = TutoredDAO.getInstance().findOneByEnrollmentID(enrollmentID);

    if (existingTutoredDTO != null) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar tutorado debido a que ya existe uno con ese correo electrónico.");
      return false;
    }

    return true;
  }

  public void registerTutored(TutoredDTO tutoredDTO) throws InvalidFieldException, UserDisplayableException {
    TutoredDAO.getInstance().createOne(tutoredDTO);
    AlertFacade.showInformationAndWait("El tutorado ha sido registrado exitosamente.");
  }

  public void onClickRegisterTutored() {
    try {
      TutoredDTO tutoredDTO = new TutoredDTO.Builder()
        .setEnrollmentID(this.fieldEnrollmentID.getText())
        .setName(this.fieldName.getText())
        .setPaternalLastName(this.fieldPaternalLastName.getText())
        .setMaternalLastName(this.fieldMaternalLastName.getText())
        .setProgramAcronym(this.fieldProgram.getValue().getAcronym())
        .build();

      if (verifyDuplicateTutored(tutoredDTO.getEmail(), tutoredDTO.getEnrollmentID())) {
        registerTutored(tutoredDTO);
      }
    } catch (InvalidFieldException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
