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
    private Label label_unuse;







    @FXML
    void click_button_signout(ActionEvent event) throws IOException {
        customer.setName_customer("Guest");
        customer.setEmail_customer("guest@mail.fr");
        customer.setCard_nb_customer("0000000000000000");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        root=fxmlLoader.load();
        Border_modelController border = fxmlLoader.getController();
        border.update_customer_border(customer.getName_customer(), customer.getEmail_customer(), customer.getCard_nb_customer());

        border.initialize(3);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    public void update_customer_custoInfo(String name,String email,String card_nb)
    {
        customer.set_all_info_customer(name,email,card_nb);
        label_unuse.setText(customer.getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS customer info : "+customer.getName_customer());
    }

}
