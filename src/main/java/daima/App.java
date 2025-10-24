package daima;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import daima.common.ExceptionHandler;
import daima.gui.AlertFacade;
import daima.business.dao.AccountDAO;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import javafx.application.Platform;

public class App extends Application {
  private static final Logger LOGGER = LogManager.getLogger(App.class);
  private static final String APP_TITLE = "Sistema de Control Interno de Tutorías";
  // NOTE: Change the paths if the project is running in a different environment
  private static final String INIT_PAGE_FILE = "/GUIInitSystemPage.fxml";
  private static final String LOGIN_PAGE_FILE = "/GUILoginPage.fxml";

  @Override
  public void start(Stage stage) {
    configureUncaughtErrorHandler();
    loadApplication(stage);
  }

  private void handleIllegalStateException(IllegalStateException e) {
    LOGGER.fatal("Error al iniciar la aplicación: {}", e.getMessage(), e);
    AlertFacade.showErrorAndWait(
      "Error de estado de interfaz gráfica al iniciar la aplicación. Por favor, comuníquese con el desarrollador si el error persiste."
    );
  }

  private void configureUncaughtErrorHandler() {
    Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread thread, Throwable e) {
        Platform.runLater(() -> {
          AlertFacade.showErrorAndWait(
            ExceptionHandler.handleUnexpectedException(LOGGER, e, "Error de Aplicación.")
          );
        });
      }
    });
  }

  private void loadApplication(Stage stage) {
    try {
      String initialPagePath
        = AccountDAO.getInstance().isThereAnAdminAccount() ? LOGIN_PAGE_FILE : INIT_PAGE_FILE;

      FXMLLoader loader = new FXMLLoader(App.class.getResource(initialPagePath));
      Scene scene = new Scene(loader.load());
      stage.setTitle(APP_TITLE);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      AlertFacade.showErrorAndWait(
        ExceptionHandler.handleGUILoadIOException(LOGGER, e)
      );
    } catch (IllegalStateException e) {
      handleIllegalStateException(e);
    } catch (Exception e) {
      AlertFacade.showErrorAndWait(
        ExceptionHandler.handleUnexpectedException(LOGGER, e, "Error al iniciar la aplicación.")
      );
    }
  }

  public static void main(String[] args) {
    launch(App.class);
  }
}
