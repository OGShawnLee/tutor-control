package daima.gui.controller;

import daima.gui.modal.ModalFacade;
import daima.gui.modal.ModalFacadeConfiguration;

public class LandingAdminController extends LandingController {
  public void onClickReviewStaffList() {
    ReviewStaffListController.navigateToAcademicListPage(getScene());
  }

  public void onClickRegisterStaff() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Personal", "GUIRegisterStaffModal")
    );
  }

  public void onClickReviewTutoredList() {

  }

  public void onClickRegisterTutored() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Tutorados", "GUIRegisterTutoredModal")
    );
  }

  public void onClickReviewProgramList() {

  }

  public void onClickRegisterProgram() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration("Registrar Programa", "GUIRegisterProgramModal")
    );
  }
}