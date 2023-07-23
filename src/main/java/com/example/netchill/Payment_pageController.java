package com.example.netchill;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Payment_pageController implements Initializable {
    double price = 0;
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
    private Label label_required_discount;
    @FXML
    private TextField txt_field_Name;
    @FXML
    private Label label_unuse;
    @FXML
    private Label label_required_gift;
    @FXML
    private TextField txt_field_discount;

    @FXML
    private TextField txt_field_gc;
    @FXML
    private ListView<String> listView_basket;

    @FXML
    private Label label_giftCard;
    private Netchill netchill = new Netchill();
    @FXML
    private TextField txt_field_cvc;
    @FXML
    private TextField txt_field_dateexp;

    private String[] years = {"2023","2024","2025","2026","2027","2028","2029","2030"} ;
    private String[] month = {"01","02","03","04","05","06","07","08","09","10","11","12"} ;
    private String[] day = {"01"} ;

    private Parent root;
    private Stage lstage;
    private Scene scene;



    @FXML
    void click_button_Gift(ActionEvent event) throws IOException, SQLException{
        double value = Double.parseDouble(txt_field_gc.getText());
        System.out.println("Amount saisie :"+value);
        double amount_gift_card_paid=0;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM `customer` WHERE customer.ID_customer = '"+netchill.getCustomer().getID_customer()+"'");

            while (rs.next())
            {
                amount_gift_card_paid = rs.getDouble("Gift_Card");
            }

            con.close();
        } catch (Exception e1) {
            System.out.println(e1);
        }
        if(value > amount_gift_card_paid)
        {
            label_required_gift.setText("Amount cannot be superior than £"+amount_gift_card_paid);
        label_required_gift.setVisible(true);
        } else if (value>price) {
            label_required_gift.setText("Amount cannot be superior than the basket £"+price);
            label_required_gift.setVisible(true);

        } else
        {

            price=price-value;
            button_pay.setText("Pay: £"+price);
            label_required_gift.setText(value+" has been remove from your payment");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


                Statement stat = con.createStatement();
                ResultSet rs = stat.executeQuery("SELECT * FROM `customer` ");

                String sql = "UPDATE customer SET Gift_Card = ? WHERE ID_customer = ?";
                PreparedStatement statement = con.prepareStatement(sql);
                amount_gift_card_paid= amount_gift_card_paid - value;
                statement.setDouble(1, amount_gift_card_paid);
                statement.setInt(2, netchill.getCustomer().getID_customer());
                System.out.println("ID OF THE CUSTOMER WHO IS PAYING IS :"+netchill.getCustomer().getID_customer());
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Update success !");
                } else {
                    System.out.println("No update, check your code");
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }


        }
        txt_field_gc.setText("0");
    }

    @FXML
    void click_button_apply(ActionEvent event) throws SQLException{
        //try discount
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM `discount` ");

            int find = 0;
            int its_guest=0;
            while (rs.next())
            {
                its_guest=0;
                if(netchill.getCustomer().getName_customer().equals("Guest"))
                {
                    find=3;
                    its_guest=1;
                    label_required_discount.setText("You cannot use discount as guest");
                    label_required_discount.setVisible(true);

                }

                if(txt_field_discount.getText().equals(rs.getString("Code_discount")) && its_guest==0)
                {
                    find = 1;
                    price = price - (rs.getDouble("Value_discount")*price/100);

                }
                else
                {
                    System.out.println("Not all the condition has been fulfilled correctly (verify your balance");
                }
            }
            if(find==1)
            {
                label_required_discount.setText("Discount of "+txt_field_discount.getText()+"% apply!");
                label_required_discount.setVisible(true);
                button_pay.setText("Pay: £"+price);

            } else if (find==2) {

                label_required_discount.setText("Code unknown");
                label_required_discount.setVisible(true);

            }

            con.close();
        } catch (Exception e1) {
            System.out.println(e1);
        }
    }

    @FXML
    void click_buttonRemove(ActionEvent event) {
        Customer customer = netchill.getCustomer();
        price=price-netchill.getCustomer().getAmount_gift_card();
        customer.setAmount_gift_card(0);
        netchill.setCustomer(customer);
        label_giftCard.setText("£0.0");
        button_pay.setText("Pay: £"+price);
    }

    @FXML
    void click_buttonPay(ActionEvent event) throws SQLException, IOException {


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
                    Date date = rs.getDate("Expiration_date");
                    String date_field = date.toString();

                    if(txt_field_Cardnb.getText().equals(rs.getString("Card_number")) && type_card.equals(rs.getString("Type_card")) && txt_field_Name.getText().equals(rs.getString("Name_owner")) && date_field.equals(txt_field_dateexp.getText()) && rs.getDouble("Balance")>price &&txt_field_cvc.getText().equals(rs.getString("CVC")))
                    {
                        String sql = "UPDATE bank SET Balance = ? WHERE Card_number = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        double final_price=rs.getDouble("Balance") - price;
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
                    else
                    {
                        label_required.setText("Error, please try again");
                        System.out.println("Not all the condition has been fulfilled correctly (verify your balance)");
                    }
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }

            //update ticket state
            if(good)
            {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

                    // SQL update statement
                    String sql = "UPDATE `ticket` SET `state`= 1 WHERE ID_customer='"+netchill.getCustomer().getID_customer()+"'";
                    PreparedStatement stat = con.prepareStatement(sql);

                    // Execute the update statement
                    int rowsAffected = stat.executeUpdate();

                    System.out.println("Rows affected: " + rowsAffected);

                    con.close();
                } catch (Exception e1) {
                    System.out.println(e1);
                }

                update_cinema_sell_data();
                update_movie_sell_data();
                update_room_sell_data();
            }

            if(good)
            {
                return_main_page(event);
            }
        }
    }

    public void update_cinema_sell_data()
    {
        //to go through each tickets
        for(int i = 0; i<netchill.getTicketList().size(); i++)
        {
            //update sold tickets for cinema
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

                String sqlQuery = "SELECT * FROM `cinema` JOIN room ON room.ID_cinema = cinema.ID_cinema JOIN session ON session.ID_nb_room = room.ID_nb_room WHERE session.ID_session = '"+netchill.getTicketList().get(i).getIdSession()+"'";
                Statement state = con.createStatement();
                ResultSet rs = state.executeQuery(sqlQuery);

                while (rs.next())
                {
                    // SQL update statement
                    String sql = "UPDATE `cinema` SET nb_sell_cinema = ? WHERE name_cinema = '" + rs.getString("name_cinema") + "'";
                    PreparedStatement stat = con.prepareStatement(sql);

                    stat.setInt(1, rs.getInt("nb_sell_cinema") + 1);

                    // Execute the update statement
                    int rowsAffected = stat.executeUpdate();

                    System.out.println("execute 2nd query : " + rowsAffected);
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }
        }
    }

    public void update_movie_sell_data()
    {
        //to go through each tickets
        for(int i = 0; i<netchill.getTicketList().size(); i++)
        {
            //update sold tickets for cinema
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

                String sqlQuery = "SELECT * FROM `movie` WHERE ID_name_movie = '"+netchill.getTicketList().get(i).getMv().getId_name()+"'";
                Statement state = con.createStatement();
                ResultSet rs = state.executeQuery(sqlQuery);

                while (rs.next())
                {
                    // SQL update statement
                    String sql = "UPDATE `movie` SET Nb_sale = ? WHERE ID_name_movie = '" + rs.getString("ID_name_movie") + "'";
                    PreparedStatement stat = con.prepareStatement(sql);

                    stat.setInt(1, rs.getInt("Nb_sale") + 1);

                    // Execute the update statement
                    int rowsAffected = stat.executeUpdate();

                    System.out.println("execute 2nd query : " + rowsAffected);
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }
        }
    }

    public void update_room_sell_data()
    {
        //to go through each tickets
        for(int i = 0; i<netchill.getTicketList().size(); i++)
        {
            //update sold tickets for room
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

                String sqlQuery = "SELECT * FROM `room` JOIN session ON session.ID_nb_room = room.ID_nb_room WHERE session.ID_session = '"+netchill.getTicketList().get(i).getIdSession()+"'";
                Statement state = con.createStatement();
                ResultSet rs = state.executeQuery(sqlQuery);

                while (rs.next())
                {
                    // SQL update statement
                    String sql = "UPDATE `room` SET Nb_sell_place = ? WHERE ID_nb_room = '" + rs.getInt("ID_nb_room") + "'";
                    PreparedStatement stat = con.prepareStatement(sql);

                    stat.setInt(1, rs.getInt("Nb_sell_place") + 1);

                    // Execute the update statement
                    int rowsAffected = stat.executeUpdate();

                    System.out.println("execute 2nd query : " + rowsAffected);
                }

                con.close();
            } catch (Exception e1) {
                System.out.println(e1);
            }
        }
    }



    public void return_main_page(ActionEvent event) throws IOException
    {
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
    public void update_customer_payment(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS Payment : "+netchill.getCustomer().getName_customer());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        label_required_gift.setVisible(false);
        label_required_discount.setVisible(false);
        txt_field_dateexp.setDisable(true);
        choiceBox_day.getItems().addAll(day);
        choiceBox_month.getItems().addAll(month);
        choiceBox_year.getItems().addAll(years);

    }


        public void initialize() throws SQLException {
        price=netchill.getCustomer().getAmount_gift_card();
        ArrayList<String> panierItems = new ArrayList<>();
        panierItems.add("Number of Ticket                    Movie                    Date                    Time                   Number of seat                    Price");

       /* if(netchill.getCustomer().getName_customer().equals("Guest"))
        {
            for(int i=0;i<netchill.getTicketList().size();i++)
            {
                String sentence;
                sentence=Integer.toString(netchill.getTicketList().get(i).getIdSession());
                sentence=sentence+"                                      ";
                sentence=sentence+netchill.getTicketList().get(i).getMv().getId_name();
                sentence=sentence+"                   ";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                sentence=sentence+netchill.getTicketList().get(i).getDate().format(formatter);
                sentence=sentence+"                    ";
                SimpleDateFormat time_movie = new SimpleDateFormat("hh:mm:ss");
                sentence=sentence+time_movie.format(rs.getTime("start"));
                sentence=sentence+"                    ";
                sentence=sentence+Integer.toString(netchill.getTicketList().get(i).getId_seat());
                sentence=sentence+"                    ";
                sentence=sentence+Double.toString(netchill.getTicketList().get(i).getMv().getPrice());
                panierItems.add(sentence);
            }
        }
        else
        {*/
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");


            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM `ticket` JOIN session ON session.ID_session = ticket.ID_session");

            while (rs.next())
            {
                //we look for all not paid tickets of person connected
                if(rs.getInt("ID_customer")==netchill.getCustomer().getID_customer() && rs.getInt("state")==0)
                {
                    price=price + rs.getDouble("Price_ticket");
                    System.out.println("DANS BDD "+rs.getInt("ID_customer")+ "DANS LA CLasse "+netchill.getCustomer().getID_customer());
                    String sentence;
                    sentence=Integer.toString(rs.getInt("ID_session"));
                    sentence=sentence+"                                           ";
                    sentence=sentence+rs.getString("ID_name_movie");
                    sentence=sentence+"              ";
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    sentence=sentence+formatter.format(rs.getDate("Date_ticket"));
                    sentence=sentence+"               ";
                    SimpleDateFormat time_movie = new SimpleDateFormat("hh:mm:ss");
                    sentence=sentence+time_movie.format(rs.getTime("start"));
                    sentence=sentence+"                    ";
                    sentence=sentence+Integer.toString(rs.getInt("ID_seat"));
                    sentence=sentence+"                              ";
                    sentence=sentence+Double.toString(rs.getDouble("Price_ticket"));
                    panierItems.add(sentence);
                }
            }

            con.close();
        } catch (Exception e1) {
            System.out.println(e1);
        }
        //}


        // observable list
        ObservableList<String> observableList = FXCollections.observableArrayList(panierItems);

        // display it on the screen
        listView_basket.setItems(observableList);
        button_pay.setText("Pay: £"+Double.toString(price));
            label_giftCard.setText("£"+Double.toString(netchill.getCustomer().getAmount_gift_card()));

    }

}
