package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class loginController {
    private Netchill netchill = new Netchill();

    @FXML
    private Label required_fields;

    @FXML
    private TextField txt_field_ID;

    private Parent root;

    private Stage lstage;

    private Scene scene;
    @FXML
    private TextField txt_field_password;

    @FXML
    private Label label_unuse_login;

    @FXML
    void click_button_sign_in(ActionEvent event) throws IOException{

       FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        root=fxmlLoader.load();
        Border_modelController border = fxmlLoader.getController();
        border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
        border.initialize(1);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    void click_button_submit(ActionEvent event) throws SQLException, IOException {

        int length = txt_field_ID.getText().length();
        String type_of_email = txt_field_ID.getText().substring(length-10);

        boolean account_exist = false;
        String name_customer = new String();
        if(txt_field_ID.getText().equals("") || txt_field_password.getText().equals(""))
        {
            required_fields.setText("*Required Fields");
            required_fields.setTextFill(Color.RED);

            System.out.println("Les champs ne peuvent pas être vide"); //idee afficher un label dynamique + améliorer les tests
        } else if (type_of_email.equals("@cinema.fr")) {
            //it's an employee
            System.out.println("It's an employee");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


                Statement stat = con.createStatement();
                ResultSet rs = stat.executeQuery("SELECT * FROM `account` ");

                while (rs.next())
                {
                    if(txt_field_ID.getText().equals(rs.getString("Email")) && txt_field_password.getText().equals(rs.getString("Password")))
                    {
                        ResultSet rs2 = stat.executeQuery("SELECT * FROM employee WHERE employee.ID_mail = '"+rs.getString("Email")+"'");
                        while(rs2.next())
                        {
                            Customer custom = netchill.getCustomer();
                            custom.setName_customer(rs2.getString("Name"));
                            custom.setEmail_customer(rs2.getString("ID_mail"));
                            netchill.setCustomer(custom);

                        }
                        account_exist=true;

                    }
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }

            if(account_exist) //compte existant
            {

                System.out.println("Employee connected !");
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
            else
            {
                System.out.println("NONONNN");
                required_fields.setText("Email or Password wrong, try again");
                required_fields.setTextFill(Color.RED);

            }
        } else
        {

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


                Statement stat = con.createStatement();
                ResultSet rs = stat.executeQuery("SELECT * FROM `account` ");

                while (rs.next())
                {
                    if(txt_field_ID.getText().equals(rs.getString("Email")) && txt_field_password.getText().equals(rs.getString("Password")))
                    {

                        ResultSet rs2 = stat.executeQuery("SELECT * FROM customer WHERE customer.Email = '"+rs.getString("Email")+"'");
                        while(rs2.next())
                        {
                            Customer custom = netchill.getCustomer();
                            custom.setName_customer(rs2.getString("Name"));
                            custom.setEmail_customer(rs2.getString("Email"));
                            netchill.setCustomer(custom);


                        }
                        account_exist=true;
                    }
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }

            if(account_exist) //compte existant
            {
                System.out.println(netchill.getCustomer().getName_customer());

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
            else
            {
                System.out.println("NONONNN");
                required_fields.setText("Email or Password wrong, try again");
                required_fields.setTextFill(Color.RED);

            }

        }
    }

    @FXML
    public void update_customer_login(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse_login.setText(netchill.getCustomer().getName_customer());
        label_unuse_login.setVisible(false);
        System.out.println("DANS login : "+netchill.getCustomer().getName_customer());
    }

}


