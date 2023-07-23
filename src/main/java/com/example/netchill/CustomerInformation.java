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
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerInformation {
    private Netchill netchill = new Netchill();

    @FXML
    private Label label_hi;
    @FXML
    private Label info_customer_email;

    @FXML
    private Label info_customer_g;

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
    void click_button_basket(ActionEvent event) throws IOException{
        //click on button go to basket, so we call border and we send the right number in initialize
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        root=fxmlLoader.load();
        Border_modelController border = fxmlLoader.getController();
        border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
        border.initialize(10);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }





    @FXML
    void click_button_signout(ActionEvent event) throws IOException {
        Customer customer = netchill.getCustomer();

        //update info with to be a guest and go to border and send the right number
        customer.setName_customer("Guest");
        customer.setEmail_customer("guest@mail.fr");
        customer.setID_customer(0);

        netchill.setCustomer(customer);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        root=fxmlLoader.load();
        Border_modelController border = fxmlLoader.getController();
        border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
        border.initialize(3);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }


    @FXML
    public void update_customer_custoInfo(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        //update information about customer to know who is connected and recup all info

        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS customer info : "+netchill.getCustomer().getName_customer());
    }


    public void initialize() throws SQLException {
        info_customer_email.setText(netchill.getCustomer().getEmail_customer());
        info_customer_name.setText(netchill.getCustomer().getName_customer());

        label_hi.setText("Hi, "+netchill.getCustomer().getName_customer());

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


            //recup amount of gift card
            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM `customer` WHERE customer.ID_customer = '"+netchill.getCustomer().getID_customer()+"'");

            while (rs.next())
            {
                String tempo = Double.toString(rs.getDouble("Gift_Card"));
                info_customer_g.setText("£"+tempo);
            }

            con.close();
        } catch (Exception e1) {
            System.out.println(e1);
        }

    }
}
