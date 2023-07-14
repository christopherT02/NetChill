package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Payment_pageController implements Initializable {
    @FXML
    private ToggleGroup RadioButton_type_card;

    @FXML
    private RadioButton radio_american;

    @FXML
    private RadioButton radio_mastercard;

    @FXML
    private RadioButton radio_visa;

    @FXML
    private Button button_pay;

    @FXML
    private ChoiceBox<String>choiceBox_day;

    @FXML
    private ChoiceBox<String>choiceBox_month;

    @FXML
    private ChoiceBox<String> choiceBox_year;
    @FXML
    private TextField txt_field_Cardnb;

    @FXML
    private Label label_required;
    @FXML
    private TextField txt_field_Name;

    @FXML
    private Label label_unuse;
    private Customer customer = new Customer();
    @FXML
    private TextField txt_field_cvc;

    @FXML
    private TextField txt_field_dateexp;

    private String[] years = {"2023","2024","2025","2026","2027","2028","2029","2030"} ;
    private String[] month = {"01","02","03","04","05","06","07","08","09","10","11","12"} ;
    private String[] day = {"01"} ;



    /*
    @FXML
    void click_buttonPay(ActionEvent event) throws SQLException, IOException {
        double prix = 20;
        if(txt_field_Name.getText().equals("") || txt_field_cvc.getText().equals("") || txt_field_Cardnb.getText().equals("") || choiceBox_day.getValue().equals("") ||choiceBox_month.getValue().equals("") ||choiceBox_year.getValue().equals("") )
        {
            label_required.setTextFill(Color.web("#ff0000"));
            txt_field_dateexp.setText(choiceBox_year.getValue()+"-"+choiceBox_month.getValue()+"-"+choiceBox_day.getValue());

        }
        else
        {
            String type_card="";
            if(radio_visa.isSelected())
            {
                type_card="Visa";
            }
            if(radio_american.isSelected())
            {
                type_card="American Express";
            }
            if(radio_mastercard.isSelected())
            {
                type_card="Mastercard";
            }
            txt_field_dateexp.setText(choiceBox_year.getValue()+"-"+choiceBox_month.getValue()+"-"+choiceBox_day.getValue());
            boolean good = false;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


                Statement stat = con.createStatement();
                ResultSet rs = stat.executeQuery("SELECT * FROM `bank` ");

                while (rs.next())
                {
                    String dateString="";
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateToCompare = dateFormat.parse(dateString);
                    if(txt_field_Cardnb.getText().equals(rs.getString("Card_number")) && type_card.equals(rs.getString("Type_card")) && txt_field_Name.getText().equals(rs.getString("Name_owner")) && dateToCompare.equals(rs.getDate("Expiration_date"))) //TODO rajouter le fait qu'il a assez d'argent !!
                    {
                        String sql = "UPDATE bank SET Balance = ? WHERE Card_number = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        double final_price=rs.getDouble("Balance") - prix;
                        statement.setDouble(1, final_price);
                        statement.setString(2, txt_field_Cardnb.getText());

                        int rowsUpdated = statement.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Mise à jour réussie !");
                        } else {
                            System.out.println("Aucune mise à jour effectuée.");
                        }
                        good=true;

                    }
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }
        }
    }
*/

    @FXML
    public void update_customer_payment(String name,String email,String card_nb)
    {
        customer.set_all_info_customer(name,email,card_nb);
        label_unuse.setText(customer.getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS Payment : "+customer.getName_customer());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txt_field_dateexp.setDisable(true);
        choiceBox_day.getItems().addAll(day);
        choiceBox_month.getItems().addAll(month);
        choiceBox_year.getItems().addAll(years);
    }


}
