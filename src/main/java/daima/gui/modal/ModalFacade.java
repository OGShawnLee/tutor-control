package daima.gui.modal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import daima.common.ExceptionHandler;
import daima.gui.AlertFacade;

import java.io.IOException;

/** ModalFacade is a utility class that simplifies the creation and display of modals in the GUI.
 * It handles the loading of FXML resources, displaying modals, and managing context for controllers.
 */
public class ModalFacade {
  private final static Logger LOGGER = LogManager.getLogger(ModalFacade.class);
  private final Modal modal;

  /**
   * Constructs a ModalFacade with the given configuration.
   *
   * @param configuration the configuration for the modal
   * @throws IOException if there is an error loading the FXML resource
   */
  private ModalFacade(ModalFacadeConfiguration configuration) throws IOException {
    this.modal = new Modal(configuration);
  }

  private Modal getModal() {
    return modal;
  }

  private void display() {
    getModal().showAndWait();
  }

  public static void createAndDisplay(ModalFacadeConfiguration configuration) {
    try {
      ModalFacade modalFacade = new ModalFacade(configuration);
      modalFacade.display();
    } catch (IOException e) {
      AlertFacade.showErrorAndWait(
        ExceptionHandler.handleGUILoadIOException(LOGGER, e).getMessage()
      );
    }
  }
}