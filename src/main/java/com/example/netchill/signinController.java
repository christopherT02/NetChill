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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signinController {
    private Netchill netchill = new Netchill();
    @FXML
    private ToggleGroup cardType;

    @FXML
    private Label required_fields;

    private Parent root;

    private Stage lstage;

    private Scene scene;
    @FXML
    private RadioButton radio_button;

    @FXML
    private Label label_unuse;

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
        border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());

        border.initialize(2);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    void click_button_submit(ActionEvent event) throws SQLException
    {
        boolean new_account = true;

        if(txt_field_name.getText().equals("") || txt_field_email.getText().equals("")|| txt_field_password.getText().equals(""))
        {
            System.out.println("Tous les champs doivent etre remplis ");
            required_fields.setTextFill(Color.RED);
        }
        else///all text_field are filled
        {
            //TODO rajouter des tests sur les saisies (mail)

            try //test if the email adress already exist in the database
            {
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
                    else if(txt_field_email.getText().contains("@cinema.fr"))
                    {
                        new_account = false;
                        System.out.println("Account is not valid\n");
                    }
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }

            //testing if all text_field is properly filled
            if(new_account)
            {
                //verify the email adress
                //format of an email adress
                String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

                //create a pattern object
                Pattern pattern = Pattern.compile(emailRegex);

                //create an object to verify the pattern needed
                Matcher matcher = pattern.matcher(txt_field_email.getText());

                if(!matcher.matches())
                    new_account = false;
            }

            if(new_account)
            {
                //send account data in the DB
                String insertQuery = "INSERT INTO `customer` (`ID_customer`, `Name`, `Email`, `Gift_Card`) VALUES (NULL, ?, ?, ?)";
                String insertQuery2 = "INSERT INTO `account` (`Email`, `Password`) VALUES (?, ?)";
                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", ""))
                {
                    PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
                    PreparedStatement preparedStatement2 = con.prepareStatement(insertQuery2);

                    //Parameters for customer
                    preparedStatement.setString(1, txt_field_name.getText());
                    preparedStatement.setString(2, txt_field_email.getText());
                    preparedStatement.setDouble(3, 0);

                    preparedStatement.executeUpdate();

                    //Parameters for account
                    preparedStatement2.setString(1, txt_field_email.getText());
                    preparedStatement2.setString(2, txt_field_password.getText());
                    preparedStatement2.executeUpdate();

                    System.out.println("Account + Customer data successfully send.");



                        Statement stat = con.createStatement();
                        ResultSet rs = stat.executeQuery("SELECT * FROM `customer` WHERE customer.Email = ' "+txt_field_email.getText()+"'");

                        int id=0;
                        while (rs.next())
                        {
                            id=rs.getInt("ID_customer");
                        }
                    Customer customer = new Customer();
                    customer.setName_customer(txt_field_name.getText());
                    customer.setEmail_customer(txt_field_email.getText());
                    customer.setAmount_gift_card(0);
                    customer.setID_customer(id);
                    netchill.setCustomer(customer);



                    //return to the home page
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
                    root=fxmlLoader.load();
                    Border_modelController border = fxmlLoader.getController();

                    border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
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
    public void update_customer_signin(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS signin : "+netchill.getCustomer().getName_customer());
    }
}

