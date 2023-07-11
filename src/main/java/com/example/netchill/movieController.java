package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.w3c.dom.events.MouseEvent;

import java.sql.*;
import java.util.ArrayList;

public class movieController {

    private Movie movieSelected;

    @FXML
    private ImageView imPoster2;

    @FXML
    private ChoiceBox<String> menuCinema;
    @FXML
    private ChoiceBox<Time> menuSchedule;

    @FXML
    private Spinner<Integer> spinnerNbOfTicket;

    @FXML
    private Text txtDescription2;
    @FXML
    private Text txtTime2;
    @FXML
    private Text txtTitle2;
    @FXML
    private Text txtTotalAmount;
    @FXML
    private Text txtUnitTicketPrice;

    ///getter - setter
    public Movie getMovieSelected() {
        return movieSelected;
    }
    public void setMovieSelected(Movie movieSelected) {
        this.movieSelected = movieSelected;
    }



    public void init_movie_infos()
    {
        txtDescription2.setText(movieSelected.getDescription());
        txtTime2.setText(movieSelected.getTime() + "min");
        txtTitle2.setText(movieSelected.getId_name());
        txtUnitTicketPrice.setText("£"+movieSelected.getPrice());

        imPoster2.setImage(movieSelected.getPoster());
    }

    public void init_cinemas_info()
    {
        //add cinemas where the movie is displayed in the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT cinema.name_cinema " +
                    "FROM movie " +
                    "JOIN session ON movie.ID_name_movie = session.ID_name_movie " +
                    "JOIN room ON session.ID_nb_room = room.ID_nb_room " +
                    "JOIN cinema ON room.ID_cinema = cinema.ID_cinema "+
                    "WHERE movie.ID_name_movie = '"+movieSelected.getId_name()+"'");

            ArrayList<String> cinemas = new ArrayList<>();

            while(rs.next())
            {
                System.out.println(rs.getString("name_cinema"));

                //compare the cinema name with the already saved cinema
                boolean match = false;
                for(String element : cinemas) {
                    if (element.equals(rs.getString("name_cinema"))) {
                        match = true;
                        break;
                    }
                }

                if(!match)
                    cinemas.add(rs.getString("name_cinema"));

            }

            if(cinemas.size()==0)
                cinemas.add("NOT AVAILABLE");

            menuCinema.getItems().addAll(cinemas);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

        menuCinema.setOnAction(this::init_schedules_info);
    }

    //TODO : verify if there is still empty seats in the room's session
    @FXML
    public void init_schedules_info(ActionEvent event)
    {
        //add schedules where the movie is displayed in the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT session.start " +
                    "FROM cinema " +
                    "JOIN room ON cinema.ID_cinema = room.ID_cinema " +
                    "JOIN session ON room.ID_nb_room = session.ID_nb_room " +
                    "WHERE session.ID_name_movie = '"+movieSelected.getId_name()+"'" +
                    "AND cinema.name_cinema = '" + menuCinema.getValue() + "'");

            ArrayList<Time> schedules = new ArrayList<>();

            while(rs.next())
            {
                System.out.println(rs.getTime("start"));

                schedules.add(rs.getTime("start"));
            }


            menuSchedule.getItems().addAll(schedules);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }

    //display the number of tickets you want to buy
    public void showSpinnerValue()
    {
        SpinnerValueFactory<Integer> spinner1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1);
        spinnerNbOfTicket.setValueFactory(spinner1);

        show_total_price();
    }

    @FXML
    public void show_total_price()
    {
        Double total = spinnerNbOfTicket.getValue() * movieSelected.getPrice();

        txtTotalAmount.setText("£" + total);
    }


    public void init()
    {
        init_movie_infos();
        init_cinemas_info();
        showSpinnerValue();
    }
    @FXML
    void initialize()
    {

    }
}
