package daima.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import daima.common.ExceptionHandler;
import daima.common.UserDisplayableException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {
  private static final Logger LOGGER = LogManager.getLogger(DBConnector.class);
  private static DBConnector instance;
  private final String URL;
  private final String USERNAME;
  private final String PASSWORD;
  // NOTE: Change the path if the project is running in a different environment
  private final String DB_PROPERTIES_FILE = "src/main/resources/db.properties";

  private DBConnector() throws UserDisplayableException {
    Properties properties = new Properties();

    try (FileInputStream input = new FileInputStream(DB_PROPERTIES_FILE)) {
      properties.load(input);

      this.URL = properties.getProperty("db.url");
      this.USERNAME = properties.getProperty("db.username");
      this.PASSWORD = properties.getProperty("db.password");

      handlePropertiesVerification();
    } catch (FileNotFoundException e) {
      throw handleConfigurationFileNotFound(e);
    } catch (IOException e) {
      throw getUserDisplayableExceptionFromDBInitIOException(e);
    }
  }

  private UserDisplayableException getUserDisplayableExceptionFromDBInitIOException(IOException e) {
    return ExceptionHandler.handleIOException(LOGGER, e, "No ha sido posible cargar la configuración de la base de datos.");
  }

  private UserDisplayableException handleConfigurationFileNotFound(FileNotFoundException e) {
    LOGGER.fatal("No se ha encontrado el archivo de configuración de la base de datos: db.properties", e);
    return new UserDisplayableException(
      "No se ha encontrado el archivo de configuración de la base de datos. Por favor, comuníquese con el desarrollador del sistema."
    );
  }

  private void handlePropertiesVerification() throws UserDisplayableException {
    if (URL == null || USERNAME == null || PASSWORD == null) {
      LOGGER.fatal("Las propiedades de conexión a la base de datos no están configuradas correctamente. Revisar db.properties.");
      throw new UserDisplayableException(
        "Las propiedades de conexión a la base de datos no están configuradas correctamente. Por favor, comuníquese con el desarrollador del sistema."
      );
    }
  }

  public static synchronized DBConnector getInstance() throws UserDisplayableException {
    if (instance == null) {
      instance = new DBConnector();
    }

    return instance;
  }

  public Connection getConnection() throws UserDisplayableException {
    try {
      return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e);
    }
  }
}