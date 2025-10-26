package daima.business.dao;

import daima.business.dto.ProgramDTO;
import daima.common.ExceptionHandler;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProgramDAO extends DAOShape<ProgramDTO> {
  private static final String CREATE_ONE_QUERY = "INSERT INTO Program (acronym, name) VALUES (?, ?)";
  private static final String FIND_ONE_QUERY = "SELECT * FROM Program WHERE acronym = ?";
  private static final String GET_ALL_QUERY = "SELECT * FROM Program";
  private static final ProgramDAO instance = new ProgramDAO();

  @Override
  public ProgramDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException, InvalidFieldException {
    return new ProgramDTO(
      resultSet.getString("acronym"),
      resultSet.getString("name")
    );
  }

  public static ProgramDAO getInstance() {
    return instance;
  }

  public void createOne(ProgramDTO programDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_ONE_QUERY)
    ) {
      statement.setString(1, programDTO.getAcronym());
      statement.setString(2, programDTO.getName());

      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible registrar el programa.");
    }
  }

  public ProgramDTO findOne(String acronym) throws InvalidFieldException, UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(FIND_ONE_QUERY)
    ) {
      statement.setString(1, acronym);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return createDTOInstanceFromResultSet(resultSet);
        }
      }

      return null;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el programa.");
    }
  }

  public ArrayList<ProgramDTO> getAll() throws InvalidFieldException, UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      ArrayList<ProgramDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la lista de programas.");
    }
  }
}
