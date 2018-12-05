package com.example.stepcount.dal;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Use ConnectionManager to connect to your database instance.
 *
 * ConnectionManager uses the MySQL Connector/J driver to connect to your local MySQL instance.
 *
 * In our example, we will create a DAO (data access object) java class to interact with each MySQL
 * table. The DAO java classes will use ConnectionManager to open and close connections.
 *
 * Instructions: 1. Install MySQL Community Server. During installation, you will need to set up a
 * user, password, and port. Keep track of these values. 2. Download and install Connector/J:
 * http://dev.mysql.com/downloads/connector/j/ 3. Add the Connector/J JAR to your buildpath. This
 * allows your application to use the Connector/J library. You can add the JAR using either of the
 * following methods: A. When creating a new Java project, on the "Java Settings" page, go to the
 * "Libraries" tab. Click on the "Add External JARs" button. Navigate to the Connector/J JAR. On
 * Windows, this looks something like: C:\Program Files (x86)\MySQL\Connector.J
 * 5.1\mysql-connector-java-5.1.34-bin.jar B. If you already have a Java project created, then go to
 * your project properties. Click on the "Java Build Path" option. Click on the "Libraries" tab,
 * click on the "Add External Jars" button, and navigate to the Connector/J JAR. 4. Update the
 * "private final" variables below.
 */
public class ConnectionManager {

  private static BasicDataSource ds = new BasicDataSource();

  // User to connect to your database instance. By default, this is "root2".
  private final static String user = "root";
  // Password for the user.
  private final static String password = "password";
  // URI to your database server. If running on the same machine, then this is "localhost".
//  private final static String hostName = "west-rds.ctsbqgasqtvo.us-west-2.rds.amazonaws.com";
//  private final static String hostName = "west-rds-medium.ctsbqgasqtvo.us-west-2.rds.amazonaws.com";
//  private final static String hostName = "localhost";
  private final static String hostName = "35.185.240.86"; // GCP
  // Port to your database server. By default, this is 3307.
  private final static int port = 3306;
  // Name of the MySQL schema that contains your tables.
  private final static String schema = "StepApp";

  static {
    ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
    ds.setUrl( "jdbc:mysql://" + hostName + ":" + port + "/" + schema);
    ds.setUsername(user);
    ds.setPassword(password);
    ds.setMinIdle(10);
    ds.setMaxIdle(100);
    ds.setMaxTotal(100);
    ds.setMaxOpenPreparedStatements(50000);
  }

//
  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
  private ConnectionManager() {}

  /**
   * Close the connection to the database instance.
   */
  public void closeConnection(Connection connection) throws SQLException {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
  }
}
