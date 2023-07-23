package com.example.netchill;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
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
    private Button btn_del;

    private int id_TicketSel;

    private Parent root;
    private Stage lstage;
    private Scene scene;

    @FXML
    void click_buttonRemove(ActionEvent event) {
        //button to remove gift card in the basket
        Customer customer = netchill.getCustomer();
        customer.setAmount_gift_card(0);
        netchill.setCustomer(customer);
        label_giftCard.setText("£0.0");
    }
    @FXML
    void click_buttonPayment(ActionEvent event) throws IOException {
        ///call the payment page
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        root=fxmlLoader.load();
        Border_modelController border = fxmlLoader.getController();
        border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
        //we send 7 to be understand by border_model
        border.initialize(7);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }
    public void initialize() throws SQLException
    {
        btn_del.setDisable(true);

        //creation of basket thanks to database and display it in the ArrayList
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
                        System.out.println("DANS BDD "+rs.getInt("ID_customer")+ "DANS LA CLasse "+netchill.getCustomer().getID_customer());
                        String sentence;
                        sentence=Integer.toString(rs.getInt("ID_ticket"));
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
    public void selectTicket()
    {
        btn_del.setDisable(false);

        String tempo = listView_basket.getSelectionModel().getSelectedItem();
        int index = listView_basket.getSelectionModel().getSelectedIndex();

        if((index -1) < -1)
            return;

        System.out.println("TEMPOOOOO : " + tempo);

        //put each element of the string into a String tab
        String[] elements = tempo.split("\\s+");

        if(elements.length > 0) {
            String firstValue = elements[0];
            System.out.println("first value : " + firstValue);
        } else {
            System.out.println("String has no value");
        }

        id_TicketSel = Integer.parseInt(elements[0]);
    }

    @FXML
    public void btn_delete() throws SQLException
    {
        //delete from the DB and from the basket of the customer
        String query = "DELETE FROM `ticket` WHERE `ID_ticket` = '" + id_TicketSel + "'";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            int rs = stat.executeUpdate(query);

            if(rs>0)
                System.out.println("Line successfully deleted");
            else
                System.out.println("No line deleted");

            con.close();
        } catch (Exception ee) {
            System.out.println("non ticket " +ee);
        }

        btn_del.setDisable(true);
        initialize();
    }


    @FXML
    public void update_customer_basket(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        //update information about customer to know who is connected and recup all info

        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS basket : "+netchill.getCustomer().getName_customer()+" ID : "+netchill.getCustomer().getID_customer());
    }
}

//TODO : be able to delete a ticker from the basket
