package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController {

    Customer customer = new Customer();
    @FXML
    private AnchorPane midPane;

    private Parent root;

    private Stage lstage;

    private Scene scene;
    @FXML
    private Label label_unuse;


    @FXML
    private Button button_account;

    public void update_customer_homepage(String name,String email,String card_nb)
    {
        customer.set_all_info_customer(name,email,card_nb);
        label_unuse.setText(customer.getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS HELLO CONTROLLER : "+customer.getName_customer());
    }


}