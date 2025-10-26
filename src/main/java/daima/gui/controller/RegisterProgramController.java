package daima.gui.controller;


import daima.business.dao.ProgramDAO;
import daima.business.dto.ProgramDTO;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegisterProgramController extends Controller {
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldAcronym;

  public boolean verifyDuplicateProgram(ProgramDTO programDTO) throws InvalidFieldException, UserDisplayableException {
    ProgramDTO existingProgramDTO = ProgramDAO.getInstance().findOne(programDTO.getAcronym());

    if (existingProgramDTO != null) {
      AlertFacade.showErrorAndWait("No ha sido posible registrar programa debido a que ya existe uno con ese acrónimo.");
      return false;
    }

    return true;
  }

  public void registerProgram(ProgramDTO programDTO) throws UserDisplayableException {
    ProgramDAO.getInstance().createOne(programDTO);
    AlertFacade.showInformationAndWait("El programa ha sido registrado exitosamente.");
  }

  public void onClickRegisterProgram() {
    try {
      ProgramDTO programDTO = new ProgramDTO(
        fieldAcronym.getText(),
        fieldName.getText()
      );

      if (verifyDuplicateProgram(programDTO)) {
        registerProgram(programDTO);
      }
    } catch (InvalidFieldException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
