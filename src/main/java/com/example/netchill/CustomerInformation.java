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

public class CustomerInformation {
    Customer customer = new Customer();
    @FXML
    private Button button_account;

    @FXML
    private Label info_customer_adress;

    @FXML
    private Label info_customer_card_nb;

    @FXML
    private Label info_customer_email;

    @FXML
    private Label info_customer_gift_card;

    @FXML
    private Label info_customer_name;

    private Parent root;

    private Stage lstage;

    private Scene scene;
    @FXML
    private AnchorPane midPane;

    @FXML
    void click_button_account(ActionEvent event) {

    }

    @FXML
    void click_button_basket(ActionEvent event) {

    }

    @FXML
    void click_button_giftCard(ActionEvent event) {

    }

    @FXML
    void click_button_homepage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        root=fxmlLoader.load();
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    void click_button_signout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        root=fxmlLoader.load();
        HelloController controller = fxmlLoader.getController();
        controller.updateLabel("Account"); // Mettre à jour le texte du Label
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    void click_button_movie(ActionEvent event) {

    }
}
