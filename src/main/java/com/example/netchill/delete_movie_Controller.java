package com.example.netchill;

import Model.Customer;
import Model.Movie;
import Model.Netchill;
import Model.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class delete_movie_Controller {
    @FXML
    private Text txtDescription;
    private Netchill netchill = new Netchill();
    @FXML
    private Label label_unuse;
    @FXML
    private Text txtTime;
    @FXML
    private Text txtTitle;
    @FXML
    private ImageView imPoster;
    @FXML
    private TextField researchInput;
    @FXML
    private Button btn_MoreDetails;

    @FXML
    private TableColumn<Movie, String> availableMovieDescriptionCol;
    @FXML
    private TableColumn<Movie, Integer> availableMovieDurationCol;
    @FXML
    private TableColumn<Movie, String> availableMovieTitleCol;
    @FXML
    private TableView<Movie> availableMoviesTable;


    private ObservableList<Movie> availableMovieList;

    private Parent root;
    private Stage lstage;
    private Scene scene;


    public void getMovieList()
    {
        ObservableList<Movie> listAvailableMovies = FXCollections.observableArrayList();

        String sqlQuery = "SELECT * FROM `movie`";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sqlQuery);
            while(rs.next())
            {

                Movie movD = new Movie(rs.getString("ID_name_movie"),
                        rs.getInt("Time"),
                        rs.getDouble("Price"),
                        rs.getString("Description"));

                listAvailableMovies.add(movD);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        availableMovieList = listAvailableMovies;
    }

    @FXML
    public void researchMovie()
    {
        btn_MoreDetails.setDisable(true);
        txtDescription.setText(null);
        txtTime.setText(null);
        txtTitle.setText(null);
        imPoster.setImage(null);

        ObservableList<Movie> listAvailableMovies = FXCollections.observableArrayList();

        String sqlQuery = "SELECT * FROM `movie`";

        String input = researchInput.getText();
        System.out.println(input);

        //if the input is empty, show all movies available
        if(input.isEmpty())
        {
            getMovieList();
            showAvailableMovies();

            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sqlQuery);
            while(rs.next())
            {
                Movie movD = new Movie(rs.getString("ID_name_movie"),
                        rs.getInt("Time"),
                        rs.getDouble("Price"),
                        rs.getString("Description"));

                //compare the movie get in the DB with the input
                if(movD.getId_name().startsWith(input))
                    listAvailableMovies.add(movD);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        availableMovieList = listAvailableMovies;
        showAvailableMovies();
    }


    @FXML
    public void showAvailableMovies()
    {
        availableMovieTitleCol.setCellValueFactory(new PropertyValueFactory<>("id_name"));
        availableMovieDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        availableMovieDurationCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        availableMoviesTable.setItems(availableMovieList);
    }

    public void selectAvailableMovie()
    {
        btn_MoreDetails.setDisable(false);

        netchill.setMovD(availableMoviesTable.getSelectionModel().getSelectedItem());

        int index = availableMoviesTable.getSelectionModel().getSelectedIndex();

        if((index -1) < -1)
            return;

        txtTitle.setText(netchill.getMovD().getId_name());
        txtTime.setText(netchill.getMovD().getTime() + "min");

        String tempoString = null;
        //if the description is too long, cut the string
        if(netchill.getMovD().getDescription().length() > 750) {
            tempoString = netchill.getMovD().getDescription().substring(0, 750);
            tempoString+="...";
            txtDescription.setText(tempoString);
        }else
            txtDescription.setText(netchill.getMovD().getDescription());


        //query to get the poster
        String sqlQuery = "SELECT `Image_movie` FROM `movie` WHERE ID_name_movie = '"+netchill.getMovD().getId_name()+"'";

        System.out.println(netchill.getMovD().getId_name());

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sqlQuery);
            while(rs.next())
            {
                //convert blob into Image
                Blob tempo = rs.getBlob("Image_movie");
                InputStream tempo2 = tempo.getBinaryStream();
                Image poster = new Image(tempo2);

                netchill.getMovD().setPoster(poster);

                imPoster.setImage(poster);
            }
            con.close();
        } catch (Exception ee) {
            System.out.println("non image " +ee);
        }

    }

    @FXML
    public void delete_buton_Click()
    {
        String query = "DELETE FROM `movie` WHERE `ID_name_movie` = '" + netchill.getMovD().getId_name() + "'";

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
            System.out.println("non image " +ee);
        }

        initialize();
    }


    @FXML
    public void update_customer_deletemovie(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        //update information about customer to know who is connected and recup all info

        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS delete : "+netchill.getCustomer().getName_customer());
    }

    @FXML
    void initialize()
    {
        //initialize info
        getMovieList();
        showAvailableMovies();
        btn_MoreDetails.setDisable(true);
    }
}
