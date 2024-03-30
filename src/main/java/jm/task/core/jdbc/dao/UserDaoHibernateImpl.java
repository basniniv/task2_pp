package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.logging.Level;
import com.sun.istack.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName().getClass());

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users" +
                    "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(50) NOT NULL, lastName VARCHAR(100) NOT NULL, " +
                    "age TINYINT NOT NULL)").executeUpdate();
            session.getTransaction().commit();
            logger.log(Level.INFO, "Таблица users успешно создана");
        } catch (HibernateException e) {
            logger.log(Level.INFO, "При создании таблицы произошла ошибка");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            session.getTransaction().commit();
            logger.log(Level.INFO, "Таблица users успешно удалена");
        } catch (HibernateException e) {
            logger.log(Level.INFO, "Ошибка при удалении таблицы users");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            User user1 = new User(name, lastName, age);
            transaction = session.beginTransaction();
            session.save(user1);
            session.getTransaction().commit();
            logger.log(Level.INFO,
                    "Пользователь успешно добавлен: " + name + " " + lastName + ", возраст: "
                            + age);
        } catch (HibernateException e) {
            logger.log(Level.INFO, "При создании пользователя произошла ошибка");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        String sql = "DELETE FROM users WHERE ID = id";

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            logger.log(Level.INFO, "При удалении пользователя по id произошла ошибка");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List <User> userList = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            userList =  session.createQuery("FROM User").getResultList();
        } catch (HibernateException e) {
            logger.log(Level.INFO, "При запросе данных из  таблицы произошла ошибка");
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DELETE FROM users").executeUpdate();
            session.getTransaction().commit();
            logger.log(Level.INFO, "Таблица пользователей успешно очищена");
        } catch (HibernateException e) {
            logger.log(Level.INFO, "При очистке таблицы пользователей возникла ошибка");
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

}
