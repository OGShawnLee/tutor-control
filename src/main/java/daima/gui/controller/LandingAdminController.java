package daima.gui.controller;

import daima.business.dao.ProgramDAO;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;
import daima.gui.modal.ModalFacade;
import daima.gui.modal.ModalFacadeConfiguration;

public class LandingAdminController extends LandingController {
  public boolean verifyProgramsExistence() {
    try {
      if (ProgramDAO.getInstance().getAll().isEmpty()) {
        AlertFacade.showInformationAndWait("No hay programas académicos registrados en el sistema, por favor registre al menos uno antes de continuar.");
        return false;
      }

      return true;
    } catch (InvalidFieldException | UserDisplayableException e) {
      AlertFacade.showErrorAndWait("Ha ocurrido un error en la base de datos, intente de nuevo más tarde.");
      return false;
    }
  }

  public void onClickReviewStaffList() {
    ReviewStaffListController.navigateToAcademicListPage(getScene());
  }

  public void onClickRegisterStaff() {
    if (verifyProgramsExistence()) {
      ModalFacade.createAndDisplay(
        new ModalFacadeConfiguration("Registrar Personal", "GUIRegisterStaffModal")
      );
    }
  }

  public void onClickReviewTutoredList() {

  }

  public void onClickRegisterTutored() {
    if (verifyProgramsExistence()) {
      ModalFacade.createAndDisplay(
        new ModalFacadeConfiguration("Registrar Tutorados", "GUIRegisterTutoredModal")
      );
    }
  }

  public void onClickReviewProgramList() {
    ReviewProgramListController.navigateToProgramListPage(getScene());
  }

  public void onClickRegisterProgram() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Programa", "GUIRegisterProgramModal")
    );
  }
}