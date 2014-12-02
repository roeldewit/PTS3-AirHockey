package Airhockey.Utils;

import Airhockey.User.User;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.pool.OracleDataSource;

/**
 * Class containing all the methods for communicating with the database
 *
 * @author martijn
 */
public class Database {

    private Properties properties;
    private Connection connection;

    /**
     * Constructor
     */
    public Database() {
        try {
            inputProperties();
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Input properties
     *
     * @throws FileNotFoundException File not found Exception
     * @throws IOException IO Exception
     */
    private void inputProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("database.properties")) {
            props.load(in);
        }

        this.properties = props;
        configure(props);
    }

    /**
     * Configure the database connection
     *
     * @param props Properties
     * @return Boolean indicating if the connection is successful configured
     * @throws IOException IO Exception
     */
    private boolean configure(Properties props) throws IOException {
        try {
            initialiseConnection();
            return isCorrectlyConfigured();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            this.properties = null;
            return false;
        } finally {
            closeConnection();
        }
    }

    /**
     * Initialise the connection to the database
     *
     * @throws SQLException SQL Exception
     * @throws FileNotFoundException File not found exception
     * @throws IOException IO Exception
     */
    private void initialiseConnection() throws SQLException, FileNotFoundException, IOException {
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

    /**
     * Close connection
     */
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

    /**
     * Check if the database is correctly configured
     *
     * @return Boolean indicating if the database is correctly configured
     */
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

    // Method only used for testing purposes
    public boolean loginCheckTest(String username, String password) {
        if (username.equals("Test") && password.equals("1234")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the username corresponds with the given password
     *
     * @param username Username
     * @param password Password
     * @return Boolean indicating if the log-in is successful
     * @throws SQLException SQL Exception
     * @throws IOException IO Exception
     */
    public boolean loginCheck(String username, String password) throws SQLException, IOException {
        String usernameDB = null;
        String passwordDB = null;

        boolean succes = false;

        initialiseConnection();

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

    /**
     * Update the rating of a user
     *
     * @param user User
     * @param score Score of the last played game
     * @throws SQLException SQL Exception
     * @throws IOException IO Exception
     */
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
            initialiseConnection();
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

    /**
     * Insert user
     *
     * @param user User
     */
    public void insertUser(User user) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get user
     *
     * @param username Username
     * @return User
     */
    public User getUser(String username) {
        throw new UnsupportedOperationException();
    }

    /**
     * Delete user
     *
     * @param username Username
     */
    public void deleteUser(String username) {
        throw new UnsupportedOperationException();
    }
}
