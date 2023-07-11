package com.example.netchill;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.w3c.dom.events.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class movieController {

    private Movie movieSelected;

    @FXML
    private ImageView imPoster2;

    @FXML
    private ChoiceBox<String> menuCinema;

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
                    "JOIN room ON movie.ID_name_movie = room.ID_movie_display " +
                    "JOIN cinema ON room.ID_cinema = cinema.ID_cinema " +
                    "WHERE movie.ID_name_movie = '"+movieSelected.getId_name()+"'");

            ArrayList<String> cinemas = new ArrayList<>();

            while(rs.next())
            {
                System.out.println(rs.getString("name_cinema"));
                cinemas.add(rs.getString("name_cinema"));
            }

            if(cinemas.size()==0)
                cinemas.add("NOT AVAILABLE");

            menuCinema.getItems().addAll(cinemas);

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
