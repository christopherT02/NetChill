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
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeInfo_Controller {

    private Netchill netchill = new Netchill();
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
        border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
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
    public void update_customer_employeeinfo(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS employee info : "+netchill.getCustomer().getName_customer());
        txt_welcome.setText("WELCOME "+netchill.getCustomer().getName_customer()+"!");
    }


}
