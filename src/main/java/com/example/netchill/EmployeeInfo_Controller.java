package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeInfo_Controller {

    Customer customer = new Customer();
    @FXML
    private Label txt_welcome;

    Parent root;
    Scene scene;
    Stage lstage;

    @FXML
    private Label label_unuse;
    @FXML
    void click_add_movie(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        root=fxmlLoader.load();
        Border_modelController border = fxmlLoader.getController();
        border.update_customer_border(customer.getName_customer(), customer.getEmail_customer(), customer.getCard_nb_customer());

        border.initialize(4);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    void click_playing_film(ActionEvent event) {

    }
    @FXML
    void click_delete_movie(ActionEvent event) {

    }

    @FXML
    void click_stat_per_cinema(ActionEvent event) {

    }

    @FXML
    void click_stat_per_movie(ActionEvent event) {

    }

    @FXML
    void click_stat_per_room(ActionEvent event) {

    }

    @FXML
    public void update_customer_employeeinfo(String name,String email,String card_nb)
    {
        customer.set_all_info_customer(name,email,card_nb);
        label_unuse.setText(customer.getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS employee info : "+customer.getName_customer());
        txt_welcome.setText("WELCOME "+customer.getName_customer()+"!");
    }


}
