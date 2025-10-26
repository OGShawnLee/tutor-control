package daima.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import daima.business.dao.StaffDAO;
import daima.business.dto.StaffDTO;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;
import daima.gui.modal.ModalFacade;
import daima.gui.modal.ModalFacadeConfiguration;

public class ReviewStaffListController extends Controller {
  @FXML
  private TableView<StaffDTO> tableStaff;
  @FXML
  private TableColumn<StaffDTO, String> columnStaffID;
  @FXML
  private TableColumn<StaffDTO, String> columnEmail;
  @FXML
  private TableColumn<StaffDTO, String> columnFullName;
  @FXML
  private TableColumn<StaffDTO, String> columnRole;
  @FXML
  private TableColumn<StaffDTO, String> columnCreatedAt;

  public void initialize() {
    configureTableColumns();
    setTableItems();
  }

  public void configureTableColumns() {
    columnStaffID.setCellValueFactory(new PropertyValueFactory<>("workerID"));
    columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    columnRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    columnCreatedAt.setCellValueFactory(new PropertyValueFactory<>("formattedCreatedAt"));
  }

  public void setTableItems() {
    try {
      tableStaff.setItems(
        FXCollections.observableList(StaffDAO.getInstance().getAll())
      );
    } catch (UserDisplayableException e) {
      AlertFacade.showErrorAndWait(
        "No ha sido posible recuperar información debido a un error en la base de datos, intente de nuevo más tarde."
      );
    }
  }

  public void onClickRegisterStaff() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration(
        "Registrar Trabajador",
        "GUIRegisterStaffModal",
        this::setTableItems
      )
    );
  }

  public void onClickManageStaff() {
    StaffDTO selectedStaff = tableStaff.getSelectionModel().getSelectedItem();

    if (selectedStaff == null) {
      AlertFacade.showWarningAndWait(
        "Para realizar esta operación debe seleccionar una fila de la tabla."
      );
    } else {
      // TODO: Add Manage Staff Use Case
    }
  }

  public void onClickAssignProgram() {
    StaffDTO selectedStaff = tableStaff.getSelectionModel().getSelectedItem();

    if (selectedStaff == null) {
      AlertFacade.showWarningAndWait(
        "Para realizar esta operación debe seleccionar una fila de la tabla."
      );
    } else {
      // TODO: Add Manage Staff Use Case
    }
  }

  public static void navigateToAcademicListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Académicos", "GUIReviewStaffListPage");
  }
}