package jm.task.core.jdbc.dao;

import com.sun.istack.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import jm.task.core.jdbc.model.User;

import java.util.List;
import jm.task.core.jdbc.util.Util;

public class UserDaoJDBCImpl implements UserDao {
  private final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName().getClass());

  public UserDaoJDBCImpl() {}

  public void createUsersTable() {
    try (Connection connection = Util.getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute(
          "CREATE TABLE IF NOT EXISTS users"
              + "(id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, "
              + "name VARCHAR(45) NOT NULL, "
              + "lastName VARCHAR(45) NOT NULL,"
              + "age TINYINT NOT NULL)");
    } catch (SQLException e) {
      logger.log(Level.INFO, "При создании таблицы произошла ошибка");
    }
  }

  public void dropUsersTable() {
    try (Connection connection = Util.getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute("DROP TABLE IF EXISTS users");
    } catch (SQLException e) {
      logger.log(Level.INFO, "При удалении пользователей произошла ошибка");
    }
  }

  public void saveUser(String name, String lastName, byte age) {
    try (Connection connection = Util.getConnection();
        PreparedStatement preparedStatement =
            connection.prepareStatement(
                "INSERT INTO users (NAME, LASTNAME, AGE) VALUES (?, ?, ?)")) {
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, lastName);
      preparedStatement.setByte(3, age);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      logger.log(Level.INFO, "При создании пользователя произошла ошибка");
    }
  }

  public void removeUserById(long id) {
    try (Connection connection = Util.getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute("DELETE FROM users WHERE ID = id");
    } catch (SQLException e) {
      logger.log(Level.INFO, "При удалении пользователя по id произошла ошибка");
    }
  }

  public List<User> getAllUsers() {
    List<User> userList = new ArrayList<>();
    try (Connection connection = Util.getConnection();
        Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
      while (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getLong("ID"));
        user.setName(resultSet.getString("NAME"));
        user.setLastName(resultSet.getString("LASTNAME"));
        user.setAge(resultSet.getByte("AGE"));

        userList.add(user);
      }
    } catch (SQLException e) {
      logger.log(Level.INFO, "При запросе данных из  таблицы произошла ошибка");
    }
    userList.forEach(System.out::println);
    return userList;
  }

  public void cleanUsersTable() {
    try (Connection connection = Util.getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute("DELETE FROM users");
    } catch (SQLException e) {
      logger.log(Level.INFO, "При очистке таблицы произошла ошибка");
    }
  }
}
