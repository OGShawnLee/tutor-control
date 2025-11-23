package daima.gui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import daima.business.dao.ProgramDAO;
import daima.business.dto.ProgramDTO;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.gui.AlertFacade;
import daima.gui.modal.ModalFacade;
import daima.gui.modal.ModalFacadeConfiguration;

public class ReviewProgramListController extends Controller {
  @FXML
  private TableView<ProgramDTO> tableProgram;
  @FXML
  private TableColumn<ProgramDTO, String> columnAcronym;
  @FXML
  private TableColumn<ProgramDTO, String> columnName;
  @FXML
  private TableColumn<ProgramDTO, String> columnCoordinatorFullName;
  @FXML
  private TableColumn<ProgramDTO, String> columnCoordinatorEmail;

  public void initialize() {
    configureTableColumns();
    setTableItems();
  }

  public void configureTableColumns() {
    columnAcronym.setCellValueFactory(new PropertyValueFactory<>("acronym"));
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnCoordinatorFullName.setCellValueFactory(new PropertyValueFactory<>("coordinatorFullName"));
    columnCoordinatorEmail.setCellValueFactory(new PropertyValueFactory<>("coordinatorEmail"));
  }

  public void setTableItems() {
    try {
      tableProgram.setItems(
        FXCollections.observableList(ProgramDAO.getInstance().getAll())
      );
    } catch (UserDisplayableException | InvalidFieldException e) {
      AlertFacade.showErrorAndWait(
        "No ha sido posible recuperar información debido a un error en la base de datos, intente de nuevo más tarde."
      );
    }
  }

  public void onClickRegisterProgram() {
    ModalFacade.createAndDisplay(
      new ModalFacadeConfiguration(
        "Registrar Programa",
        "GUIRegisterProgramModal",
        this::setTableItems
      )
    );
  }

  public void onClickManageProgram() {
    ProgramDTO selectedProgram = tableProgram.getSelectionModel().getSelectedItem();

    if (selectedProgram == null) {
      AlertFacade.showWarningAndWait(
        "Para realizar esta operación debe seleccionar una fila de la tabla."
      );
    } else {
      ModalFacade.createAndDisplayContextModal(
        new ModalFacadeConfiguration(
          "Gestionar Programa",
          "GUIManageProgramModal",
          this::setTableItems
        ),
        selectedProgram
      );
    }
  }

  public static void navigateToProgramListPage(Stage currentStage) {
    navigateTo(currentStage, "Lista de Programas", "GUIReviewProgramListPage");
  }
}