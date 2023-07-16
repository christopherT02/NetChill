package com.example.netchill;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BasketCustomerController {
    Netchill netchill = new Netchill();
    @FXML
    private Label label_giftCard;

    @FXML
    private Label label_unuse;
    @FXML
    private ListView<String> listView_basket;

    @FXML
    void click_buttonRemove(ActionEvent event) {

    }
    @FXML
    void click_buttonPayment(ActionEvent event) {

    }
    public void initialize() throws SQLException {
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
                    //we look for all tickets of person connected
                    if(rs.getInt("ID_customer")==netchill.getCustomer().getID_customer())
                    {
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

        label_giftCard.setText("£"+netchill.getCustomer().getAmount_gift_card());

    }

    @FXML
    public void update_customer_basket(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS basket : "+netchill.getCustomer().getName_customer()+" ID : "+netchill.getCustomer().getID_customer());
    }
}
