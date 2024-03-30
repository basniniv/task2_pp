package jm.task.core.jdbc.util;

import com.sun.istack.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

public class Util {

    //     реализуйте настройку соеденения с БД
    private static final String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final Logger logger = Logger.getLogger(
            UserServiceImpl.class.getName().getClass());


    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            logger.log(Level.INFO, "Не удалось загрузить класс драйвера");
        }
        return connection;
    }

    //Обновление для использования Hibernate
    public static SessionFactory getSessionFactory() {
        Properties settings = new Properties();

        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.URL, URL);
        settings.put(Environment.USER, USERNAME);
        settings.put(Environment.PASS, PASSWORD);

        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "");
        settings.put(Environment.SHOW_SQL,true);

        return new Configuration()
                .setProperties(settings)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

    }



}



