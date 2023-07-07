package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HelloController {

    @FXML
    private AnchorPane midPane;

    @FXML
    private Button button_add=new Button();

    @FXML
    private Button button_account;

    @FXML
    void click_button_account(ActionEvent event) {
        button_add.setLayoutX(700);
        button_add.setLayoutY(400);
        button_add.setText("AZERTTYUUOP");
        midPane.getChildren().add(button_add);
        button_add.setOnAction(even -> { try { click_new_button(even); } catch (IOException e) { throw new RuntimeException(e); } });
    }

    @FXML
    void click_new_button(ActionEvent even)throws IOException
    {
        button_add.setText("GROS ZIZIIIIIIIIII");
    }

}