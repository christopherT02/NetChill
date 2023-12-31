package com.example.netchill;

import Model.Customer;
import Model.Movie;
import Model.Netchill;
import Model.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class chooseSeatController {
    private Netchill netchill = new Netchill();

    @FXML
    private Label label_unuse;

    @FXML
    private AnchorPane midPane;
    @FXML
    private ChoiceBox<Integer> seatChoice;
    @FXML
    private Button btn_nextPage;


    private Parent root;
    private Stage lstage;
    private Scene scene;

    public void init()
    {
        setupTheDisplay();
        netchill.setIncrementor(netchill.getIncrementor()+1);
    }



    public void setupTheDisplay()
    {
        int nbSeats = 0;
        //get the number of seats in the room's session
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT `Nb_place` FROM `room` " +
                    "JOIN session ON session.ID_nb_room = room.ID_nb_room " +
                    "WHERE session.ID_session = '"+ netchill.getID_session_selected() +"'");

            while(rs.next())
            {
                nbSeats = rs.getInt("Nb_place");
            }

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

        System.out.println(nbSeats);

        ArrayList<Integer> vectorSeatsOccupied = new ArrayList<Integer>();

        //get the seats of the sessions that are already occupied thx to ticket table
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM `ticket` " +
                    "JOIN seat ON seat.ID_seat = ticket.ID_seat " +
                    "WHERE `Date_ticket`= '"+netchill.getDate_for_ticket()+"' AND seat.ID_session = '"+netchill.getID_session_selected()+"'");

            while(rs.next())
            {
                vectorSeatsOccupied.add(rs.getInt("seat_number"));
                //System.out.println(rs.getString("ID_name_movie"));
            }


            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }


        seatChoice.getItems().removeAll();
        Integer[] seatAvailable = new Integer[nbSeats];

        for(int i = 0; i<nbSeats; i++)
        {
            //if the seat is not available
            if(vectorSeatsOccupied.contains(i))
                seatAvailable[i] = 0;
            else {
                seatAvailable[i] = 1;
                seatChoice.getItems().add(i);
            }
        }

        displayAllSeats(seatAvailable, nbSeats);
    }

    public void displayAllSeats(Integer[] seatAvailable, int nbSeats)
    {
        Color cl = Color.WHITE;
        Color cll = Color.GRAY;

        int posx = 250;
        int posy = 120;
        System.out.println("ui");
        int cmpte = 0;


        for(int i = 0; i<5; i++)
        {
            for(int j = 0; j<10; j++)
            {
                Rectangle rectSeat = new Rectangle();
                rectSeat.setLayoutX(posx);
                rectSeat.setLayoutY(posy);
                rectSeat.setHeight(20);
                rectSeat.setWidth(20);

                Text txt = new Text();
                txt.setLayoutX(posx+5);
                txt.setLayoutY(posy-10);
                txt.setText(String.valueOf(cmpte));
                txt.setFill(Color.WHITE);


                if(seatAvailable[cmpte] == 1)
                    rectSeat.setFill(cl);
                else rectSeat.setFill(cll);

                midPane.getChildren().add(rectSeat);
                midPane.getChildren().add(txt);

                posx+=115;
                cmpte++;

                if(cmpte==nbSeats)
                    return;
            }
            posx = 250;
            posy+=75;
        }
    }


    @FXML
    public void button_PaymentClick(ActionEvent event) throws IOException
    {
        //add seat data in the DB
        String insertQuery = "INSERT INTO `seat` (`ID_seat`, `ID_session`, `available_seat`, `seat_number`) VALUES (NULL, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");
             PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {

            //Parameters
            preparedStatement.setInt(1, netchill.getID_session_selected());
            preparedStatement.setInt(2, 1);
            preparedStatement.setInt(3, seatChoice.getValue());

            preparedStatement.executeUpdate();

            System.out.println("Seat data successfully send.");

        } catch (SQLException e) {
            e.printStackTrace();
        }


        int id_seatCreated = 0;
        //get the ID of the most recent seat created
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT ID_seat FROM seat ORDER BY ID_seat DESC LIMIT 1");

            while(rs.next())
            {
                id_seatCreated = rs.getInt("ID_seat");
                System.out.println("yeeeees");
            }

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

        //add ticket data in the DB
        String insertQuery2 = "INSERT INTO `ticket` (`ID_ticket`, `ID_name_movie`, `ID_customer`, `nb_customer`, `Price_ticket`, `Date_ticket`, `ID_session`, `ID_seat`) VALUES (NULL, ?, ?, ?, ?, ? ,? ,?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");
             PreparedStatement preparedStatement = con.prepareStatement(insertQuery2)) {

            //Parameters
            preparedStatement.setString(1, netchill.getMovD().getId_name());
            preparedStatement.setInt(2, netchill.getCustomer().getID_customer());
            preparedStatement.setInt(3, netchill.getNb_ticket());
            preparedStatement.setDouble(4, netchill.getMovD().getPrice());
            preparedStatement.setDate(5, Date.valueOf(netchill.getDate_for_ticket()));
            preparedStatement.setInt(6, netchill.getID_session_selected());
            preparedStatement.setInt(7, id_seatCreated);

            preparedStatement.executeUpdate();

            System.out.println("Ticket data successfully send.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Ticket ticket = new Ticket(netchill.getMovD(), id_seatCreated, netchill.getDate_for_ticket(), netchill.getID_session_selected());
        netchill.add_ticket_liste(ticket);

        nextPage(event);
    }

    public void nextPage(ActionEvent event) throws IOException
    {
        //if all tickets have a seat
        if(netchill.getIncrementor() == netchill.getNb_ticket())
        {
            ///call the basket page
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
            root=fxmlLoader.load();
            Border_modelController border = fxmlLoader.getController();
            border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            border.initialize(10);
            lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
            scene=new Scene(root);
            lstage.setScene(scene);
            lstage.show();
        }
        else {
            System.out.println("INCREMENTOR "+netchill.getIncrementor()+" NB TICKET "+netchill.getNb_ticket());
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
            root=fxmlLoader.load();
            Border_modelController border = fxmlLoader.getController();
            border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            border.initialize(6);
            lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
            scene=new Scene(root);
            lstage.setScene(scene);
            lstage.show();
        }
    }

    @FXML
    public void update_customer_chooseSeat(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        //update information about customer to know who is connected and recup all info

        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS choose seat : "+netchill.getCustomer().getName_customer()+ "ID seance choisie "+ netchill.getID_session_selected());
    }

}
