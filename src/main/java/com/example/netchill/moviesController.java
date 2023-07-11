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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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

    private Movie movD = new Movie();

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
                movD = new Movie(rs.getString("ID_name_movie"),
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
                movD = new Movie(rs.getString("ID_name_movie"),
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
        movD = availableMoviesTable.getSelectionModel().getSelectedItem();

        int index = availableMoviesTable.getSelectionModel().getSelectedIndex();

        if((index -1) < -1)
            return;

        txtTitle.setText(movD.getId_name());
        txtTime.setText(movD.getTime() + "min");

        //TODO cut hte string after the nth character
        //if(movD.getDescription().length() < 500)
            txtDescription.setText(movD.getDescription());
        //else{
            //String tempo = movD.getDescription().split(500);
        //}

        //query to get the poster
        String sqlQuery = "SELECT `Image_movie` FROM `movie` WHERE ID_name_movie = '"+movD.getId_name()+"'";

        System.out.println(movD.getId_name());

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

                movD.setPoster(poster);

                imPoster.setImage(poster);
            }
            con.close();
        } catch (Exception ee) {
            System.out.println("non image " +ee);
        }

    }

    //change to the new window
    @FXML
    public void btn_moreDetails_click(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("movie.fxml"));
        root=fxmlLoader.load();
        movieController controller = fxmlLoader.getController();

        //give infos about the selected movie to the new page
        controller.setMovieSelected(movD);
        //call this function because it doesnt work in the "initialize()" function
        controller.init();

        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }



    @FXML
    void initialize()
    {
        getMovieList();
        showAvailableMovies();
    }

}
