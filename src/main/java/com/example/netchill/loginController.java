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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class loginController {
    Customer customer = new Customer();
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
        border.update_customer_border(customer.getName_customer(), customer.getEmail_customer(), customer.getCard_nb_customer());

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
                            customer.setName_customer(rs2.getString("Name"));
                            customer.setEmail_customer(rs2.getString("ID_mail"));
                            customer.setCard_nb_customer("0000000000000000");

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
                border.update_customer_border(customer.getName_customer(), customer.getEmail_customer(), customer.getCard_nb_customer());

                border.initialize(3);
                lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
                scene=new Scene(root);
                lstage.setScene(scene);
                lstage.show();
            }
            else
            {
                System.out.println("NONONNN");
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
                            customer.setName_customer(rs2.getString("Name"));
                            customer.setEmail_customer(rs2.getString("Email"));
                            customer.setCard_nb_customer(rs2.getString("Card_number"));

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
                System.out.println(customer.getName_customer());

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
            else
            {
                System.out.println("NONONNN");
            }

        }
    }
    @FXML
    public void update_customer_login(String name,String email,String card_nb)
    {
        customer.set_all_info_customer(name,email,card_nb);
        label_unuse_login.setText(customer.getName_customer());
        label_unuse_login.setVisible(false);
        System.out.println("DANS login : "+customer.getName_customer());
    }

}


