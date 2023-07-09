package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class addMovieController {
    private Parent root;
    private Stage lstage;
    private Scene scene;
    @FXML
    private ImageView imViewPoster;
    private Image posterImage;
    private File file;

    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtMovieName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtTime;

    @FXML
    private ChoiceBox<String> menuCinema;
    @FXML
    private ChoiceBox<Integer> menuRoom;

    @FXML
    private Button btnInit;


    // a inclure dans la liaison entre les pages
    public void dragAndDrop()
    {
        //drag & drop for the poster
        imViewPoster.setOnDragOver(event -> {
            if (event.getGestureSource() != imViewPoster && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        //getting of the image
        imViewPoster.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                file = dragboard.getFiles().get(0);
                if (isImageFile(file)) {
                    posterImage = new Image(file.toURI().toString());
                    imViewPoster.setImage(posterImage);

                    System.out.println("chemin : " + file.toURI());
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    //verification of the file's extension
    private boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
    }

    //method that add a Movie in the DataBase
    public void addMovieToBdd() throws FileNotFoundException {
        Movie movieToAdd = new Movie();

        movieToAdd.setId_name(txtMovieName.getText());
        movieToAdd.setDescription(txtDescription.getText());
        movieToAdd.setTime(Integer.parseInt(txtTime.getText()));

        try {
            double num = Double.parseDouble(txtPrice.getText());
            movieToAdd.setPrice(Double.parseDouble(txtPrice.getText()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        }

        System.out.println(movieToAdd.toString());

        InputStream posterB = new FileInputStream(file);


        //send movie data in the DB
        String insertQuery = "INSERT INTO `movie` (`ID_name_movie`, `Time`, `Price`, `Description`, `Discount`, `Image_movie`, `Nb_sale`) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");
             PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {

            //Parameters
            preparedStatement.setString(1, movieToAdd.getId_name());
            preparedStatement.setInt(2, movieToAdd.getTime());
            preparedStatement.setDouble(3, movieToAdd.getPrice());
            preparedStatement.setString(4, movieToAdd.getDescription());
            preparedStatement.setDouble(5, 0);
            preparedStatement.setBlob(6, posterB);
            preparedStatement.setDouble(7, 0);
            preparedStatement.executeUpdate();

            System.out.println("Movie data successfully send.");

        } catch (SQLException e) {
            e.printStackTrace();
        }


        //update room data
        String queryUpdate = "UPDATE `room` SET `ID_movie_display`= ? WHERE ID_nb_room = "+ menuRoom.getValue();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");
             PreparedStatement preparedStatement = con.prepareStatement(queryUpdate)) {

            preparedStatement.setString(1, movieToAdd.getId_name());

            // execute update
            int lignesModifiees = preparedStatement.executeUpdate();

            // Show number of row modified
            System.out.println("Nombre de lignes modifiées : " + lignesModifiees);

            //close connexion and ressources
            preparedStatement.close();
            con.close();

            System.out.println("Room data successfully send.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void displayAvailableCinemas() throws IOException {

        //add existing cinemas into the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM `cinema` ");

            ArrayList<String> cinemas = new ArrayList<>();

            while (rs.next())
            {
                System.out.println(rs.getString("name_cinema"));
                cinemas.add(rs.getString("name_cinema"));
            }
            menuCinema.getItems().addAll(cinemas);
            menuCinema.setOnAction(this::showAvailableRoom);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }

    @FXML
    public void showAvailableRoom(ActionEvent event)
    {
        String cinemaSelected = menuCinema.getValue();
        int id_cinema=0;

        System.out.println("cinema sel : " +cinemaSelected);


        //get the cinema ID
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT `ID_cinema` FROM `cinema` WHERE `name_cinema` = '" + cinemaSelected+"'");
            while(rs.next())
            {
                System.out.println("id cinema : "+ id_cinema);
                id_cinema = rs.getInt("ID_cinema");
                System.out.println("id cinema : "+ id_cinema);
            }
            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

        //add the available room of the selected cinema in the menu
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM `room` WHERE `ID_cinema` = '" + id_cinema+"'");

            ArrayList<Integer> rooms = new ArrayList<>();

            while (rs.next())
            {
                if(rs.getString("ID_movie_display").isEmpty())
                    rooms.add(rs.getInt("ID_nb_room"));

                System.out.println("yyeess " +rs.getInt("ID_nb_room"));
            }
            menuRoom.getItems().addAll(rooms);
            con.close();

        } catch (Exception e3) {
            System.out.println(e3);
        }
    }

    @FXML
    void initialize() throws IOException
    {
        //display available cinemas
        displayAvailableCinemas();

        //activate drag and drop for the poster
        dragAndDrop();
    }









    @FXML
    void click_button_home_page(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        root=fxmlLoader.load();
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }


}