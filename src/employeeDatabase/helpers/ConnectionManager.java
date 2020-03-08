/* We can get connection to database from this class, all information that we need for connecting
* is present into "database.properties" file.
*/
package employeeDatabase.helpers;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ConnectionManager {

   private static Connection connection = null;

   public static Connection getConnection() {

        if (connection == null){
            try {
                Properties connectionProperties = new Properties();
                connectionProperties.load(new FileReader("src/EmployeeDatabase/Resources/database.properties"));
                Class.forName(connectionProperties.getProperty("DatabaseDriver"));
                connection = DriverManager.getConnection(connectionProperties.getProperty("ConnectionURL"),
                        connectionProperties.getProperty("UserName"),
                        connectionProperties.getProperty("Password"));
            } catch (ClassNotFoundException | SQLException | IOException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}