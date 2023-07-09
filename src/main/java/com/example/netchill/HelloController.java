package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    Customer customer = new Customer();
    @FXML
    private AnchorPane midPane;

    private Parent root;

    private Stage lstage;

    private Scene scene;

    @FXML
    private Button button_account;

    @FXML
    void click_button_account(ActionEvent event) throws IOException{
        if(button_account.getText().equals("Account"))
        {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            root=fxmlLoader.load();
            lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
            scene=new Scene(root);
            lstage.setScene(scene);
            lstage.show();
        }
        else
        {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("customer_information.fxml"));
            root=fxmlLoader.load();
            lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
            scene=new Scene(root);
            lstage.setScene(scene);
            lstage.show();
        }

    }


    @FXML
    void click_addMovie(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add_movie.fxml"));
        root=fxmlLoader.load();
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    public void updateLabel(String name)
    {
        button_account.setText(name);
    }

}