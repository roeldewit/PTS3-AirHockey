package Airhockey.Main;

import Airhockey.Utils.Database;
import java.awt.Point;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author martijn
 */
public class Login extends Application {

    Database database;

//    JButton blogin = new JButton("Login");
//    JPanel panel = new JPanel();
//    JTextField txuser = new JTextField(15);
//    JPasswordField pass = new JPasswordField(15);
    @Override
    public void start(Stage primaryStage) {
        Login(primaryStage);
    }

    public void Login(Stage primaryStage) {
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10, 50, 50, 50));

        //Adding HBox
        HBox hb = new HBox();
        hb.setPadding(new Insets(20, 20, 20, 30));

        //Adding GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        //Implementing Nodes for GridPane
        Label lblUserName = new Label();
        final TextField txtUserName = new TextField();
        txtUserName.setText("username");
        Label lblPassword = new Label();
        final PasswordField pf = new PasswordField();
        pf.setText("password");
        Button btnLogin = new Button("Login");
        final Label lblMessage = new Label();

        //Adding Nodes to GridPane layout
        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtUserName, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(pf, 1, 1);
        gridPane.add(btnLogin, 1, 2);
        gridPane.add(lblMessage, 1, 2);

        //Reflection for gridPane
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPane.setEffect(r);

        //DropShadow effect 
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);

        //Adding text and DropShadow effect to it
        Text text = new Text("AIR HOCKEY");
        text.setFont(Font.font("Roboto", FontWeight.BOLD, 34));
        //text.setEffect(dropShadow);

        text.setFill(Color.web("#009587"));

        //lblPassword.setEffect(r);
        //Adding text to HBox
        hb.getChildren().add(text);

        //Action for btnLogin
        btnLogin.setOnAction(new EventHandler() {

            @Override
            public void handle(Event event) {
//                String checkUser = txtUserName.getText().toString();
//                String checkPw = pf.getText().toString();
//                if (checkUser.equals("User1") && checkPw.equals("PW")) {
//                    Renderer r = new Renderer(primaryStage);
//                } else {
//                    lblMessage.setText("Incorrect user or pw.");
//                    lblMessage.setTextFill(Color.RED);
//                }
//                txtUserName.setText("");
//                pf.setText("");

                primaryStage.close();
                Lobby l = new Lobby(primaryStage);
                //Renderer r = new Renderer(primaryStage);
            }

        });

        //Add HBox and GridPane layout to BorderPane Layout
        bp.setTop(hb);
        bp.setCenter(gridPane);

        //Adding BorderPane to the scene and loading CSS
        Scene scene = new Scene(bp);
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void actionlogin() {
//        blogin.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent ae) {
//                String puname = txuser.getText();
//                String ppaswd = pass.getText();
//                
//                if (db.LoginCheckTest(puname, ppaswd)) {
//                    Game game = new Game(new String[100]);
//                    Lobby lobby = new Lobby();
//                    lobby.getChatbox().display();
//                    dispose();
//                } else {
//                    
//                    JOptionPane.showMessageDialog(null, "Wrong Password / Username");
//                    txuser.setText("");
//                    pass.setText("");
//                    txuser.requestFocus();
//                }
//
//            }
//        });
    }
}
