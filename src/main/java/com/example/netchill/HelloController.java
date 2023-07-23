package com.example.netchill;

import Model.Customer;
import Model.Movie;
import Model.Netchill;
import Model.Ticket;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;

public class HelloController{

    private Netchill netchill = new Netchill();

    private Parent root;

    private Stage lstage;

    private Scene scene;
    @FXML
    private Label label_unuse;


    @FXML
    private Label label_left;

    @FXML
    private Label label_middle;

    @FXML
    private Label label_right;

    @FXML
    private AnchorPane pane1;

    @FXML
    private AnchorPane pane2;

    @FXML
    private AnchorPane pane3;


    int show=0;
    @FXML
    void clck_bck(ActionEvent event) {
        //click on left button to show the previous movie
        if(show==1)
        {
            translateAnimation(0.5,pane3,1400);
            show--;
            label_left.setTextFill(Color.RED);
            label_right.setTextFill(Color.WHITE);

            label_middle.setTextFill(Color.WHITE);
        } else if (show==2) {
            translateAnimation(0.5,pane2,1400);
            show--;
            label_left.setTextFill(Color.WHITE);
            label_right.setTextFill(Color.WHITE);

            label_middle.setTextFill(Color.RED);
        }
    }

    @FXML
    void clck_nxt(ActionEvent event) {
        //see the next movie (button right)
        if(show==0)
        {
            translateAnimation(0.5,pane3,-1400);
            show++;
            label_left.setTextFill(Color.WHITE);
            label_right.setTextFill(Color.WHITE);

            label_middle.setTextFill(Color.RED);
        } else if (show==1) {
            translateAnimation(0.5,pane2,-1400);
            show++;
            label_left.setTextFill(Color.WHITE);
            label_right.setTextFill(Color.RED);

            label_middle.setTextFill(Color.WHITE);
        }
    }

    public void translateAnimation(double duration,Node node,double width)
    {
        //transition between two pictures
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(duration),node);
        translateTransition.setByX(width);
        translateTransition.play();
    }
    @FXML
    public void update_customer_homepage(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        //update information about customer to know who is connected and recup all info

        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS HELLO CONTROLLER : "+netchill.getCustomer().getName_customer());
    }



    public void initialize() {

        translateAnimation(0,pane2,1400);
        translateAnimation(0,pane3,1400);

    }
}