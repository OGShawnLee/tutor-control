package daima.gui.controller;

import daima.business.dao.ProgramDAO;
import daima.business.dto.ProgramDTO;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ManageProgramController extends Controller implements ContextController<ProgramDTO> {
  @FXML
  private TextField fieldName;
  @FXML
  private TextField fieldAcronym;
  private ProgramDTO initialProgramDTO;

  public void configureFields() {
    if (initialProgramDTO != null) {
      fieldAcronym.setText(initialProgramDTO.getAcronym());
      fieldName.setText(initialProgramDTO.getName());
    }
  }

  @Override
  public void setContext(ProgramDTO programDTO) {
    this.initialProgramDTO = programDTO;
    configureFields();
  }

  public void updateProgram(ProgramDTO programDTO) throws UserDisplayableException {
    ProgramDAO.getInstance().updateOne(programDTO);
    AlertFacade.showInformationAndWait("El programa ha sido modificado exitosamente.");
  }

  public void onClickUpdateProgram() {
    try {
      updateProgram(
        new ProgramDTO(
          fieldAcronym.getText(),
          fieldName.getText()
        )
      );
    } catch (InvalidFieldException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait(e.getMessage());
    }
  }
}
