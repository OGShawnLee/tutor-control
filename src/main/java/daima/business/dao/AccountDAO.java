package daima.business.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import daima.business.dto.AccountDTO;
import daima.business.dto.enumeration.AccountRole;
import daima.common.ExceptionHandler;
import daima.common.InvalidFieldException;
import daima.common.UserDisplayableException;
import daima.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO extends DAOShape<AccountDTO> {
  private static final Logger LOGGER = LogManager.getLogger(AccountDAO.class);
  private static final String GET_ONE_QUERY = "SELECT * FROM Account WHERE email = ?";
  private static final String IS_THERE_ONE_ADMIN_QUERY = "SELECT COUNT(*) FROM Account WHERE role = 'ADMIN' LIMIT 1";
  private static final AccountDAO instance = new AccountDAO();

  public static AccountDAO getInstance() {
    return instance;
  }

  @Override
  public AccountDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws InvalidFieldException, SQLException {
    return new AccountDTO(
      resultSet.getString("email"),
      resultSet.getString("password"),
      AccountRole.valueOf(resultSet.getString("role"))
    );
  }

  public AccountDTO findOne(String email) throws InvalidFieldException, UserDisplayableException {
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
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar la cuenta.");
    }
  }

  public boolean isThereAnAdminAccount() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(IS_THERE_ONE_ADMIN_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      if (resultSet.next()) {
        return resultSet.getInt(1) > 0;
      }

      return false;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible verificar la existencia de una cuenta de administrador.");
    }
  }
}
