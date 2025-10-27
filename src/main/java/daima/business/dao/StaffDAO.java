package daima.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daima.business.dto.person.StaffDTO;
import daima.business.dto.enumeration.AccountRole;
import daima.common.ExceptionHandler;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.db.DBConnector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class StaffDAO extends DAOShape<StaffDTO> {
  private static final Logger LOGGER = LogManager.getLogger(StaffDAO.class);
  private static final String CREATE_ONE_QUERY
    = "INSERT INTO Staff (email, staff_id, name, paternal_last_name, maternal_last_name, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
  private static final String CREATE_ONE_COORDINATOR_QUERY =
    "CALL create_coordinator(?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Staff";
  private static final String GET_ONE_QUERY = "SELECT * FROM Staff WHERE email = ?";
  private static final String GET_ONE_BY_WORKER_ID = "SELECT * FROM Staff WHERE staff_id = ?";
  private static final StaffDAO instance = new StaffDAO();

  public static StaffDAO getInstance() {
    return instance;
  }

  @Override
  public StaffDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws InvalidFieldException, SQLException {
    return new StaffDTO.Builder()
      .setEmail(resultSet.getString("email"))
      .setWorkerID(resultSet.getString("staff_id"))
      .setName(resultSet.getString("name"))
      .setPaternalLastName(resultSet.getString("paternal_last_name"))
      .setMaternalLastName(resultSet.getString("maternal_last_name"))
      .setRole(AccountRole.valueOf(resultSet.getString("role")))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  public void createOne(StaffDTO staffDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection(); CallableStatement statement = connection.prepareCall(CREATE_ONE_QUERY)) {
      statement.setString(1, staffDTO.getEmail());
      statement.setString(2, staffDTO.getWorkerID());
      statement.setString(3, staffDTO.getName());
      statement.setString(4, staffDTO.getPaternalLastName());
      statement.setString(5, staffDTO.getMaternalLastName());
      statement.setString(6, StaffDAO.createHashedPassword(staffDTO.getWorkerID()));
      statement.setString(7, staffDTO.getRole().toDBString());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el personal académico.");
    }
  }

  public StaffDTO findOne(String email) throws InvalidFieldException, UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ONE_QUERY)) {
      statement.setString(1, email);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        }
      }

      return null;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el personal académico.");
    }
  }

  public StaffDTO getOne(String email) throws InvalidFieldException, UserDisplayableException {
    StaffDTO staffDTO = findOne(email);

    if (staffDTO == null) {
      throw new UserDisplayableException("No ha sido posible recuperar personal académico porque no existe.");
    }

    return staffDTO;
  }

  public ArrayList<StaffDTO> getAll() throws UserDisplayableException {
    ArrayList<StaffDTO> staffList = new ArrayList<>();

    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      while (resultSet.next()) {
        staffList.add(createDTOInstanceFromResultSet(resultSet));
      }

      return staffList;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el personal académico.");
    } catch (InvalidFieldException e) {
      throw ExceptionHandler.handleCorruptedDataException(LOGGER, e);
    }
  }

  public StaffDTO findOneByWorkerID(String workerID) throws InvalidFieldException, UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ONE_BY_WORKER_ID)
    ) {
      statement.setString(1, workerID);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        }
      }

      return null;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el personal académico.");
    }
  }

  private static String createHashedPassword(String workerID) {
    return BCrypt.hashpw(workerID + "@Password", BCrypt.gensalt());
  }
}
