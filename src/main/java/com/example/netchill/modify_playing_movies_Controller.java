package com.example.netchill;

import Model.Customer;
import Model.Movie;
import Model.Netchill;
import Model.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class modify_playing_movies_Controller {
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

    //used in Drag&Drop
    private Image posterImage;
    private File file;

    //table view
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

    //menus
    @FXML
    private ChoiceBox<String> menu_cinema;
    @FXML
    private ChoiceBox<Integer> menu_room;
    @FXML
    private ChoiceBox<Time> menu_session;

    //new input
    @FXML
    private ImageView im_newPoster;
    @FXML
    private TextField txt_newDescription;
    @FXML
    private TextField txt_newDuration;
    @FXML
    private TextField txt_newPrice;
    @FXML
    private TextField txt_newTitle;
    @FXML
    private TextField txt_newDiscount;
    @FXML
    private Rectangle rectDD;

    //buttons
    @FXML
    private Button btn_applyChanges;
    @FXML
    private Button btn_applyMovieModif;


    public void dragAndDrop()
    {
        //drag & drop for the poster
        im_newPoster.setOnDragOver(event -> {
            if (event.getGestureSource() != im_newPoster && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        //getting of the image
        im_newPoster.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                file = dragboard.getFiles().get(0);
                if (isImageFile(file)) {
                    posterImage = new Image(file.toURI().toString());
                    im_newPoster.setImage(posterImage);

                    rectDD.setVisible(false);

                    System.out.println("chemin : " + file.toURI());
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
    //verification of the file's extension
    private boolean isImageFile(File file)
    {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
    }


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
    public void showAvailableMovies()
    {
        availableMovieTitleCol.setCellValueFactory(new PropertyValueFactory<>("id_name"));
        availableMovieDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        availableMovieDurationCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        availableMoviesTable.setItems(availableMovieList);
    }

    @FXML
    public void researchMovie()
    {
        btn_applyChanges.setDisable(true);
        btn_applyMovieModif.setDisable(true);
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

    public void selectAvailableMovie()
    {
        btn_applyMovieModif.setDisable(false);
        btn_applyChanges.setDisable(false);

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

    public void getCinemaInfos()
    {
        menu_cinema.setValue(null);

        //add cinemas where the movie is displayed in the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT cinema.name_cinema FROM cinema");

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

            menu_cinema.getItems().addAll(cinemas);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

        menu_cinema.setOnAction(this::getRoomInfos);
    }

    private void getRoomInfos(ActionEvent event)
    {
        menu_room.setValue(null);
        menu_session.setValue(null);

        menu_room.getItems().clear();
        menu_session.getItems().clear();

        //add room present in the cinema to the corresponding choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT `ID_nb_room` FROM `room` JOIN cinema ON cinema.ID_cinema = room.ID_cinema WHERE cinema.name_cinema = '"+menu_cinema.getValue()+"'");

            ArrayList<Integer> ID_room = new ArrayList<>();

            while(rs.next())
            {
                System.out.println(rs.getInt("ID_nb_room"));

                ID_room.add(rs.getInt("ID_nb_room"));
            }

            menu_room.getItems().addAll(ID_room);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

        menu_room.setOnAction(this::getSessionInfos);
    }

    private void getSessionInfos(ActionEvent event) {
        menu_session.getItems().clear();
        //add schedules where the movie is displayed in the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT session.start " +
                    "FROM cinema " +
                    "JOIN room ON cinema.ID_cinema = room.ID_cinema " +
                    "JOIN session ON room.ID_nb_room = session.ID_nb_room " +
                    "WHERE session.ID_nb_room = '" + menu_room.getValue() +"'");

            ArrayList<Time> schedules = new ArrayList<>();

            while(rs.next())
            {
                System.out.println(rs.getTime("start"));

                schedules.add(rs.getTime("start"));
            }

            menu_session.getItems().addAll(schedules);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }


    @FXML
    public void apply_modifMovieInfos_btnClick()
    {
        // SQL update statement
        String sql1 = "UPDATE `movie` SET `ID_name_movie`= ?, `Time`=?, `Price`=?, `Description`=?, `Image_movie`=? WHERE ID_name_movie='"+netchill.getMovD().getId_name()+"'";
        String sql2 = "UPDATE `movie` SET `ID_name_movie`= ?, `Time`=?, `Price`=?, `Description`=? WHERE ID_name_movie='"+netchill.getMovD().getId_name()+"'";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            PreparedStatement stat;

            if(im_newPoster.getImage() != null)
            {
                System.out.println("poster");
                stat = con.prepareStatement(sql1);
            }
            else {
                System.out.println("no poster");
                stat = con.prepareStatement(sql2);
            }

            // Set the new values for the columns
            if(txt_newTitle.getText().isEmpty())
                stat.setString(1, netchill.getMovD().getId_name());
            else stat.setString(1, txt_newTitle.getText());

            if(txt_newDuration.getText().isEmpty())
                stat.setInt(2, netchill.getMovD().getTime());
            else stat.setInt(2, Integer.parseInt(txt_newDuration.getText()));

            if(txt_newPrice.getText().isEmpty())
                stat.setDouble(3, netchill.getMovD().getPrice());
            else stat.setDouble(3, Double.parseDouble(txt_newPrice.getText()));

            if(txt_newDescription.getText().isEmpty())
                stat.setString(4, netchill.getMovD().getDescription());
            else stat.setString(4, txt_newDescription.getText());

            if(im_newPoster.getImage() != null)
            {
                InputStream posterB = new FileInputStream(file);
                stat.setBlob(5, posterB);
            }


            // Execute the update statement
            int rowsAffected = stat.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

        initialize();
    }

    @FXML
    public void apply_modifChanges_btnClick()
    {
        // SQL update statement
        String sql = "UPDATE `session` SET `ID_name_movie`= ? WHERE start ='"+menu_session.getValue()+"' AND ID_nb_room = '" + menu_room.getValue() +"'";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            PreparedStatement stat;
            stat = con.prepareStatement(sql);

            // Set the new values for the columns
            stat.setString(1, netchill.getMovD().getId_name());

            // Execute the update statement
            int rowsAffected = stat.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

        initialize();
    }

    public void resetInfosDeisplayed()
    {
        txtTitle.setText("");
        txtDescription.setText("");
        txtTime.setText("");
        imPoster.setImage(null);
    }


    @FXML
    public void update_customer_modif_movie(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS delete : "+netchill.getCustomer().getName_customer());
    }

    @FXML
    void initialize()
    {
        menu_cinema.getItems().clear();
        resetInfosDeisplayed();

        dragAndDrop();
        getMovieList();
        showAvailableMovies();
        getCinemaInfos();
        btn_applyMovieModif.setDisable(true);
        btn_applyChanges.setDisable(true);

    }
}
