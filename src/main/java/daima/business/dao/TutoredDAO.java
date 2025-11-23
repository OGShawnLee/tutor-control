package daima.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daima.business.dto.person.TutoredDTO;
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

public class TutoredDAO extends DAOShape<TutoredDTO> {
  private static final Logger LOGGER = LogManager.getLogger(TutoredDAO.class);
  private static final String CREATE_ONE_QUERY
    = "INSERT INTO Tutored (email, enrollment, name, paternal_last_name, maternal_last_name, program_acronym) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_QUERY = "SELECT * FROM Tutored";
  private static final String GET_ONE_QUERY = "SELECT * FROM Tutored WHERE email = ?";
  private static final String GET_ONE_BY_WORKER_ID = "SELECT * FROM Tutored WHERE enrollment = ?";
  private static final TutoredDAO instance = new TutoredDAO();

  public static TutoredDAO getInstance() {
    return instance;
  }

  @Override
  public TutoredDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws InvalidFieldException, SQLException {
    return new TutoredDTO.Builder()
      .setEnrollmentID(resultSet.getString("enrollment"))
      .setName(resultSet.getString("name"))
      .setPaternalLastName(resultSet.getString("paternal_last_name"))
      .setMaternalLastName(resultSet.getString("maternal_last_name"))
      .setProgramAcronym(resultSet.getString("program_acronym"))
      .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
      .build();
  }

  public void createOne(TutoredDTO tutoredDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection(); CallableStatement statement = connection.prepareCall(CREATE_ONE_QUERY)) {
      statement.setString(1, tutoredDTO.getEmail());
      statement.setString(2, tutoredDTO.getEnrollmentID());
      statement.setString(3, tutoredDTO.getName());
      statement.setString(4, tutoredDTO.getPaternalLastName());
      statement.setString(5, tutoredDTO.getMaternalLastName());
      statement.setString(6, tutoredDTO.getProgramAcronym());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el tutorado.");
    }
  }

  public TutoredDTO findOne(String email) throws InvalidFieldException, UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ONE_QUERY)
    ) {
      statement.setString(1, email);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        }
      }

      return null;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el tutorado.");
    }
  }

  public TutoredDTO getOne(String email) throws InvalidFieldException, UserDisplayableException {
    TutoredDTO tutoredDTO = findOne(email);

    if (tutoredDTO == null) {
      throw new UserDisplayableException("No ha sido posible recuperar tutorado porque no existe.");
    }

    return tutoredDTO;
  }

  public ArrayList<TutoredDTO> getAll() throws UserDisplayableException {
    ArrayList<TutoredDTO> staffList = new ArrayList<>();

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
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el tutorado.");
    } catch (InvalidFieldException e) {
      throw ExceptionHandler.handleCorruptedDataException(LOGGER, e);
    }
  }

  public TutoredDTO findOneByEnrollmentID(String enrollmentID) throws InvalidFieldException, UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ONE_BY_WORKER_ID)
    ) {
      statement.setString(1, enrollmentID);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        }
      }

      return null;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el tutorado.");
    }
  }
}
