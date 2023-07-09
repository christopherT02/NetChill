package com.example.netchill;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

//import javax.imageio.ImageIO;
import java.io.*;
import java.sql.*;

//import java.awt.image.BufferedImage;


public class addMovieController {

    Customer customer = new Customer();
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



    // a include dans la liaison ente les pages
    public void dragAndDrop()
    {
        //Gestion du glisser-déposer pour l'image
        imViewPoster.setOnDragOver(event -> {
            if (event.getGestureSource() != imViewPoster && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

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

    private boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
    }

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


        //send to the DB
        String insertQuery = "INSERT INTO `movie` (`ID_name_movie`, `Time`, `Price`, `Description`, `Discount`, `Image_movie`, `Nb_sale`) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");
             PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {

            // Paramètres pour le premier enregistrement
            preparedStatement.setString(1, movieToAdd.getId_name());
            preparedStatement.setInt(2, movieToAdd.getTime());
            preparedStatement.setDouble(3, movieToAdd.getPrice());
            preparedStatement.setString(4, movieToAdd.getDescription());
            preparedStatement.setDouble(5, 0);
            preparedStatement.setBlob(6, posterB);
            preparedStatement.setDouble(7, 0);
            preparedStatement.executeUpdate();

            System.out.println("Insertion des données réussie.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}