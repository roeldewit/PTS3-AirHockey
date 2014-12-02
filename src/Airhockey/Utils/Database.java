package Airhockey.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.pool.OracleDataSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author martijn
 */
public class Database {

    private Properties props;
    private Connection conn;

    public Database() {
        try {
            InputProps();
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void InputProps() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("database.properties")) {
            props.load(in);
        }

        this.props = props;
        configure(props);
    }

    public final boolean configure(Properties props) throws IOException {
        try {
            initConnection();
            return isCorrectlyConfigured();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            this.props = null;
            return false;
        } finally {
            closeConnection();
        }
    }

    private void initConnection() throws SQLException, FileNotFoundException, IOException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        String driver = this.props.getProperty("driver");
        if (driver != null) {
            System.setProperty("jdbc.drivers", driver);
        }
        String url = this.props.getProperty("url");
        String username = this.props.getProperty("username");
        String password = this.props.getProperty("password");

        //this.conn = DriverManager.getConnection(url, username, password);
        OracleDataSource ds;
        ds = new OracleDataSource();
        ds.setURL(url);
        this.conn = ds.getConnection(username, password);
    }

    private void closeConnection() {
        try {
            if(conn != null)
            {
                conn.close();
            }
            conn = null;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public boolean isCorrectlyConfigured() {
        if (props == null) {
            return false;
        }
        if (!props.containsKey("driver")) {
            return false;
        }
        if (!props.containsKey("url")) {
            return false;
        }
        if (!props.containsKey("username")) {
            return false;
        }
        if (!props.containsKey("password")) {
            return false;
        }
        return true;
    }

    public boolean LoginCheckTest(String username, String password) {
        if (username.equals("Test") && password.equals("1234")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean LoginCheck(String username, String password) throws SQLException, IOException {
        String usernameDB = null;
        String passwordDB = null;

        boolean succes = false;

        initConnection();

        Statement stmt = null;

        String query = "SELECT USERNAME, PASSWORD FROM DBI296122.AH_USERS";

        try {
            stmt = conn.createStatement();
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

}
