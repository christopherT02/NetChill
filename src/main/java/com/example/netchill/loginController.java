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
        if(txt_field_ID.equals("") || txt_field_password.equals(""))
        {
            System.out.println("Les champs ne peuvent pas être vide"); //idee afficher un label dynamique + améliorer les tests
        }
        else
        {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


                Statement stat = con.createStatement();
                ResultSet rs = stat.executeQuery("SELECT * FROM `account` ");
                while (rs.next())
                {
                    System.out.println(rs.getString("custname"));
                    if(txt_field_ID.equals(rs.getString("Email")) && txt_field_password.equals(rs.getString("Password")))
                    {
                        ResultSet rs2 = stat.executeQuery("SELECT Name FROM customer WHERE customer.Email = rs.getString(\"Email\")");
                        name_customer = rs2.getString("Name");
                        account_exist=true;
                    }
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }

            if(account_exist) //compte existant
            {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Detail_payment.fxml"));
                root=fxmlLoader.load();
                HelloController controller = fxmlLoader.getController();
                controller.updateLabel(name_customer); // Mettre à jour le texte du Label

                lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
                scene=new Scene(root);
                lstage.setScene(scene);
                lstage.show();
            }

        }
    }


}


