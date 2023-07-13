package com.example.netchill;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class chooseSeatController {
    private Movie movieChoosed = null;
    private LocalDate datePicked = null;
    private int idSessionSelected;


    //getter - setter
    public Movie getMovieChoosed() {return movieChoosed;}
    public void setMovieChoosed(Movie movieChoosed) {this.movieChoosed = movieChoosed;}

    public LocalDate getDatePicked() {return datePicked;}
    public void setDatePicked(LocalDate datePicked) {this.datePicked = datePicked;}

    public int getIdSessionSelected() {return idSessionSelected;}
    public void setIdSessionSelected(int idSessionSelected) {this.idSessionSelected = idSessionSelected;}


    @FXML
    private AnchorPane midPane;
    @FXML
    private ChoiceBox<Integer> seatChoice;
    @FXML
    private Button btn_nextPage;

    public void init()
    {
        setupTheDisplay();
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
                    "WHERE session.ID_session = '"+ idSessionSelected +"'");

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
                    "WHERE `Date_ticket`= '"+datePicked+"' AND seat.ID_session = '"+idSessionSelected+"'");

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
        Color cl = Color.BLACK;
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
    public void button_PaymentClick()
    {
        //add seat data in the DB
        String insertQuery = "INSERT INTO `seat` (`ID_seat`, `ID_session`, `available_seat`, `seat_number`) VALUES (NULL, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");
             PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {

            //Parameters
            preparedStatement.setInt(1, idSessionSelected);
            preparedStatement.setInt(2, 1);
            preparedStatement.setInt(3, seatChoice.getValue());

            preparedStatement.executeUpdate();

            System.out.println("Seat data successfully send.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
