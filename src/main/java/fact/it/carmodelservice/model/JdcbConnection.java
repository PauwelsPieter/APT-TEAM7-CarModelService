package fact.it.carmodelservice.model;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;

public class JdcbConnection {
        private static final Logger LOGGER =
                Logger.getLogger(JdcbConnection.class.getName());
        private static Optional<Connection> connection = Optional.empty();



        public static Optional<Connection> getConnection() {
            if (connection.isEmpty()) {
//                System.out.println("jdbc:postgresql://"+System.getenv("POSTGRES_HOST")+":"+System.getenv("POSTGRES_PORT")+"/postgres");
//                String url = "jdbc:postgresql://"+System.getenv("POSTGRES_HOST")+":"+System.getenv("POSTGRES_PORT")+"/postgres";
//                String user = System.getenv("POSTGRES_USER");
//                String password = System.getenv("POSTGRES_PASSWORD");

                String url = "jdbc:postgresql://localhost:5432/postgres";
                String user = "postgres";
                String password = "testgresql";

                try {
                    connection = Optional.ofNullable(
                            DriverManager.getConnection(url, user, password));
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }

            return connection;
        }
}
