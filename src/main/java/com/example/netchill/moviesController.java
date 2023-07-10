package com.example.netchill;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.InputStream;
import java.sql.*;

public class moviesController {
    @FXML
    private Text txtDescription;
    @FXML
    private Text txtTime;
    @FXML
    private Text txtTitle;
    @FXML
    private ImageView imPoster;
    @FXML
    private TextField researchInput;

    @FXML
    private TableColumn<Movie, String> availableMovieDescriptionCol;
    @FXML
    private TableColumn<Movie, Integer> availableMovieDurationCol;
    @FXML
    private TableColumn<Movie, String> availableMovieTitleCol;
    @FXML
    private TableView<Movie> availableMoviesTable;



    public ObservableList<Movie> getMovieList()
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

        return listAvailableMovies;
    }

    private ObservableList<Movie> availableMovieList;

    @FXML
    public void showAvailableMovies()
    {
        availableMovieList = getMovieList();

        availableMovieTitleCol.setCellValueFactory(new PropertyValueFactory<>("id_name"));
        availableMovieDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        availableMovieDurationCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        availableMoviesTable.setItems(availableMovieList);
    }

    public void selectAvailableMovie()
    {
        Movie mov = availableMoviesTable.getSelectionModel().getSelectedItem();
        int index = availableMoviesTable.getSelectionModel().getSelectedIndex();

        if((index -1) < -1)
            return;

        txtTitle.setText(mov.getId_name());
        txtDescription.setText(mov.getDescription());
        txtTime.setText(mov.getTime() + "min");

        //query to get the poster
        String sqlQuery = "SELECT `Image_movie` FROM `movie` WHERE ID_name_movie = '"+mov.getId_name()+"'";

        System.out.println(mov.getId_name());

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

                imPoster.setImage(poster);
            }
            con.close();
        } catch (Exception ee) {
            System.out.println("non image " +ee);
        }



    }

    @FXML
    void initialize()
    {
        showAvailableMovies();
    }

}
