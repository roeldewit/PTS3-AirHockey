package Airhockey.Utils;

import Airhockey.User.User;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author martijn
 */
public class Database {

    private Properties properties;
    private Connection connection;

    public Database() {
        try {
            inputProperties();
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void inputProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("database.properties")) {
            props.load(in);
        }

        this.properties = props;
        configure(props);
    }

    private final boolean configure(Properties props) throws IOException {
        try {
            initializeConnection();
            return isCorrectlyConfigured();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            this.properties = null;
            return false;
        } finally {
            closeConnection();
        }
    }

    private void initializeConnection() throws SQLException, FileNotFoundException, IOException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        String driver = this.properties.getProperty("driver");
        if (driver != null) {
            System.setProperty("jdbc.drivers", driver);
        }
        String url = this.properties.getProperty("url");
        String username = this.properties.getProperty("username");
        String password = this.properties.getProperty("password");

        //this.conn = DriverManager.getConnection(url, username, password);
        OracleDataSource ds;
        ds = new OracleDataSource();
        ds.setURL(url);
        this.connection = ds.getConnection(username, password);
    }

    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            connection = null;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private boolean isCorrectlyConfigured() {
        if (properties == null) {
            return false;
        }
        if (!properties.containsKey("driver")) {
            return false;
        }
        if (!properties.containsKey("url")) {
            return false;
        }
        if (!properties.containsKey("username")) {
            return false;
        }
        if (!properties.containsKey("password")) {
            return false;
        }
        return true;
    }

    public boolean loginCheckTest(String username, String password) {
        if (username.equals("Test") && password.equals("1234")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean loginCheck(String username, String password) throws SQLException, IOException {
        String usernameDB = null;
        String passwordDB = null;

        boolean succes = false;

        initializeConnection();

        Statement stmt = null;

        String query = "SELECT USERNAME, PASSWORD FROM DBI296122.AH_USERS";

        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                usernameDB = rs.getString("USERNAME");
                passwordDB = rs.getString("PASSWORD");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (username == usernameDB && password == passwordDB) {
                succes = true;
            }
        }
        return succes;
    }

    public void updateRating(User user, int score) throws SQLException, IOException {
        int score1 = score;
        int score2 = 15;
        int score3 = 15;
        int score4 = 15;
        int score5 = 15;

        String username = user.getUsername();

        Statement statement = null;

        String query = String.format("SELECT * FROM DBI296122.USERS WHERE \"username\" = '%s'", username);

        try {
            initializeConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                score2 = resultSet.getInt("score1");
                score3 = resultSet.getInt("score2");
                score4 = resultSet.getInt("score3");
                score5 = resultSet.getInt("score4");
            }

            double rating = ScoreCalculator.calculateRating(score1, score2, score3, score4, score5);

            query = String.format("UPDATE DBI296122.\"USERS\" SET \"rating\"= %f, \"score1\" = %d, \"score2\" = %d, \"score3\" = %d, \"score4\" = %d, \"score5\" = %d WHERE \"username\" = '%s'", rating, score1, score2, score3, score4, score5, username);

            statement.executeQuery(query);
        } catch (SQLException exception) {
            System.out.println("SQL Exception: " + exception.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void insertUser(User user) {
        throw new UnsupportedOperationException();
    }

    public User getUser(String username) {
        throw new UnsupportedOperationException();
    }

    public void deleteUser(String username) {
        throw new UnsupportedOperationException();
    }
}
