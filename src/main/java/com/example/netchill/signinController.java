package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class signinController {
    Customer customer = new Customer();

    @FXML
    private ToggleGroup cardType;

    private Parent root;

    private Stage lstage;

    private Scene scene;
    @FXML
    private RadioButton radio_button;

    @FXML
    private Label label_unuse;
    @FXML
    private TextField txt_field_cardNumber;

    @FXML
    private TextField txt_field_email;

    @FXML
    private TextField txt_field_name;

    @FXML
    private TextField txt_field_password;

    @FXML
    void click_button_gotoLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        root=fxmlLoader.load();
        Border_modelController border = fxmlLoader.getController();
        border.update_customer_border(customer.getName_customer(), customer.getEmail_customer(), customer.getCard_nb_customer());

        border.initialize(2);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    void click_button_submit(ActionEvent event) throws SQLException {


        boolean new_account = true;

        if(txt_field_name.getText().equals("") || txt_field_email.getText().equals("")|| txt_field_password.getText().equals("") || txt_field_cardNumber.getText().equals(""))
        {
            System.out.println("Tous les champs doivent etre remplis ");
        }
        else///all text_field are filled
        {
            //TODO rajouter des tests sur les saisies (mail) + test de l'argent en banque

            try { //test if the email adress already exist in the database
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


                Statement stat = con.createStatement();
                ResultSet rs = stat.executeQuery("SELECT * FROM `account` ");

                while (rs.next())
                {
                    if(txt_field_email.getText().equals(rs.getString("Email")) )
                    {
                        new_account = false;
                        System.out.println("Account already exist !\n");
                        //TODO AFFICHER DE MANIERE DYNAMIQUE UN MESSAGE QUI DIT QUE LE COMPTE EXISTE DEJA + rajouter l'interdiction de faire un mail qui fini par @cinema.fr
                    }
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }

            if(new_account)
            {


            //send movie data in the DB
            String insertQuery = "INSERT INTO `customer` (`ID_customer`, `Name`, `Email`, `Card_number`) VALUES (NULL, ?, ?, ?)";
            String insertQuery2 = "INSERT INTO `account` (`Email`, `Password`) VALUES (?, ?)";
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", ""))
            {
                PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
                PreparedStatement preparedStatement2 = con.prepareStatement(insertQuery2);

                //Parameters for customer
                preparedStatement.setString(1, txt_field_name.getText());
                preparedStatement.setString(2, txt_field_email.getText());
                preparedStatement.setString(3, txt_field_cardNumber.getText());
                preparedStatement.executeUpdate();

                //Parameters for customer
                preparedStatement2.setString(1, txt_field_email.getText());
                preparedStatement2.setString(2, txt_field_password.getText());
                preparedStatement2.executeUpdate();

                System.out.println("Account + Customer data successfully send.");

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                root=fxmlLoader.load();
                Border_modelController border = fxmlLoader.getController();
                border.update_customer_border(customer.getName_customer(), customer.getEmail_customer(), customer.getCard_nb_customer());

                border.initialize(3);
                lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
                scene=new Scene(root);
                lstage.setScene(scene);
                lstage.show();



            } catch (Exception e1) {
                System.out.println(e1);
            }
            }
        }
    }


    @FXML
    public void update_customer_signin(String name,String email,String card_nb)
    {
        customer.set_all_info_customer(name,email,card_nb);
        label_unuse.setText(customer.getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS signin : "+customer.getName_customer());
    }
}

