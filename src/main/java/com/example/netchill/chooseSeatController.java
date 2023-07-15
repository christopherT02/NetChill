package com.example.netchill;

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


    //getter - setter
    public Movie getMovieChoosed() {return movieChoosed;}
    public void setMovieChoosed(Movie movieChoosed) {this.movieChoosed = movieChoosed;}

    public LocalDate getDatePicked() {return datePicked;}
    public void setDatePicked(LocalDate datePicked) {this.datePicked = datePicked;}

    public int getIdSessionSelected() {return idSessionSelected;}
    public void setIdSessionSelected(int idSessionSelected) {this.idSessionSelected = idSessionSelected;}

    public int getNb_places() {return nb_places;}
    public void setNb_places(int nb_places) {this.nb_places = nb_places;}

    public ArrayList<Ticket> getTicketList() {return ticketList;}
    public void setTicketList(ArrayList<Ticket> ticketList) {this.ticketList = ticketList;}

    public int getIncrementor() {return incrementor;}
    public void setIncrementor(int incrementor) {this.incrementor = incrementor;}

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
        incrementor++;
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
            preparedStatement.setInt(1, idSessionSelected);
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
            preparedStatement.setString(1, movieChoosed.getId_name());
            preparedStatement.setInt(2, 0); //TODO : utiliser l'ID du customer connecte
            preparedStatement.setInt(3, nb_places);
            preparedStatement.setDouble(4, movieChoosed.getPrice());
            preparedStatement.setDate(5, Date.valueOf(datePicked));
            preparedStatement.setInt(6, idSessionSelected);
            preparedStatement.setInt(7, id_seatCreated);

            preparedStatement.executeUpdate();

            System.out.println("Ticket data successfully send.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Ticket ticket = new Ticket(movieChoosed, id_seatCreated, datePicked, idSessionSelected);
        ticketList.add(ticket);

        nextPage(event);
    }

    public void nextPage(ActionEvent event) throws IOException
    {
        if(incrementor == nb_places)
        {
            ///call the payement page
            System.out.println("CALL THE PAYMENT PAGE PLZ");
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("choose_seat.fxml"));
            root=fxmlLoader.load();
            chooseSeatController controller = fxmlLoader.getController();

            //give infos about the selected movie to the new page
            controller.setMovieChoosed(movieChoosed);
            //give infos about the selected date chosen
            controller.setDatePicked(datePicked);
            //give info about the selected session
            controller.setIdSessionSelected(idSessionSelected);
            controller.setNb_places(nb_places);
            controller.setIncrementor(incrementor);
            controller.setTicketList(ticketList);
            //call this function because it doesnt work in the "initialize()" function
            controller.init();

            lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
            scene=new Scene(root);
            lstage.setScene(scene);
            lstage.show();
        }
    }

    @FXML
    public void update_customer_chooseSeat(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS choose seat : "+netchill.getCustomer().getName_customer());
    }

}
