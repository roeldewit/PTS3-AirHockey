/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author martijn
 */
public class CreateAccountController implements Initializable {

    @FXML
    Button btCreate;

    @FXML
    TextField tbUsername;

    @FXML
    TextField tbPassword;

    Database database;
    
    public CreateAccountController()
    {
        database = new Database();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //sbtCreate = new Button();
    }

    public void actionCreateNewAccount() {
        try {
            database.insertUser(tbUsername.getText(), tbPassword.getText());
        } catch (SQLException | IOException ex) {
            Logger.getLogger(CreateAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
