package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class loginController {
    @FXML
    private TextField txt_field_ID;

    private Parent root;

    private Stage lstage;

    private Scene scene;
    @FXML
    private TextField txt_field_password;

    @FXML
    void click_button_home_page(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        root=fxmlLoader.load();
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    void click_button_sign_in(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signin.fxml"));
        root=fxmlLoader.load();
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    void click_button_submit(ActionEvent event) throws SQLException, IOException {
        boolean account_exist = false;
        String name_customer = new String();
        if(txt_field_ID.getText().equals("") || txt_field_password.getText().equals(""))
        {
            System.out.println("Les champs ne peuvent pas être vide"); //idee afficher un label dynamique + améliorer les tests
        }
        else
        {
            System.out.println("OUI");

            try {
                System.out.println("61");
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");
                System.out.println("64");


                Statement stat = con.createStatement();
                ResultSet rs = stat.executeQuery("SELECT * FROM `account` ");
                System.out.println("65");

                while (rs.next())
                {
                    System.out.println("Nous testons\n");
                    if(txt_field_ID.getText().equals(rs.getString("Email")) && txt_field_password.getText().equals(rs.getString("Password")))
                    {

                        System.out.println("OUIIII");
                        ResultSet rs2 = stat.executeQuery("SELECT Name FROM customer WHERE customer.Email = '"+rs.getString("Email")+"'");
                        while(rs2.next()) name_customer = rs2.getString("Name");
                        account_exist=true;
                    }
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }

            if(account_exist) //compte existant
            {
                System.out.println("OUI2");

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                root=fxmlLoader.load();
                HelloController controller = fxmlLoader.getController();
                controller.updateLabel(name_customer); // Mettre à jour le texte du Label

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


}


