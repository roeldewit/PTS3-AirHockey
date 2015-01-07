package Airhockey.Main;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roel
 */
public class Chatbox {

    private ArrayList<ChatboxLine> lines;

    ListView listView;
    TextField textField;
    BorderPane borderPane;

    public Chatbox() {
        lines = new ArrayList<>();
    }

    public ArrayList<ChatboxLine> getLines() {
        return lines;
    }

    public void writeLine(ChatboxLine chatboxLine) {
        lines.add(chatboxLine);
    }

    public void display2(Stage primaryStage) {

        Stage s = new Stage();

        listView = new ListView();

        textField = new TextField();
        Button btn = new Button();
        btn.setText("send");

        borderPane = new BorderPane();
        HBox bottom = HBoxBuilder.create().children(textField, btn).build();
        borderPane.setCenter(listView);
        borderPane.setBottom(bottom);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textField.clear();
            }
        });

        s.setScene(new Scene(borderPane, 300, 250));
        s.show();
    }
}
