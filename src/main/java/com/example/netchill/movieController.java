package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class movieController {

    private Netchill netchill = new Netchill();
    @FXML
    private Label label_unuse;

    @FXML
    private DatePicker datePck;
    @FXML
    private ImageView imPoster2;

    @FXML
    private Button btnCS;

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

    private boolean check1 = false;
    private boolean check2 = false;


    private Parent root;
    private Stage lstage;
    private Scene scene;





    public void init_movie_infos()
    {
        txtDescription2.setText(netchill.getMovD().getDescription());
        txtTime2.setText(netchill.getMovD().getTime() + "min");
        txtTitle2.setText(netchill.getMovD().getId_name());
        txtUnitTicketPrice.setText("£"+netchill.getMovD().getPrice());

        imPoster2.setImage(netchill.getMovD().getPoster());
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
                    "WHERE movie.ID_name_movie = '"+netchill.getMovD().getId_name()+"'");

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
    //TODO : verify the selected date (day and hour)
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
                    "WHERE session.ID_name_movie = '"+netchill.getMovD().getId_name()+"'" +
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

        menuSchedule.setOnAction(this::setCheck1);
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
        Double total = spinnerNbOfTicket.getValue() * netchill.getMovD().getPrice();

        txtTotalAmount.setText("£" + total);
    }

    public void changeStateBtn()
    {
        if(check1 && check2)
            btnCS.setDisable(false);
    }

    public void setCheck1(ActionEvent event){check1=true;
        changeStateBtn();

        //get session ID
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT session.ID_session " +
                    "FROM cinema " +
                    "JOIN room ON cinema.ID_cinema = room.ID_cinema " +
                    "JOIN session ON room.ID_nb_room = session.ID_nb_room " +
                    "WHERE session.ID_name_movie = '"+ netchill.getMovD().getId_name()+"' AND session.start = '"+menuSchedule.getValue()+"'");

            while(rs.next())
            {
                System.out.println("-----------------------" + rs.getInt("ID_session"));
                netchill.setID_session_selected(rs.getInt("ID_session"));
            }

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

    }
    @FXML
    public void setCheck2(ActionEvent event){check2=true;
        changeStateBtn();
        netchill.setDate_for_ticket(datePck.getValue());

        System.out.println(netchill.getDate_for_ticket());
    }


    public void init()
    {
        init_movie_infos();
        init_cinemas_info();
        showSpinnerValue();
        btnCS.setDisable(true);
    }


    @FXML
    public void update_customer_movie(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS movie : "+netchill.getCustomer().getName_customer() + "FILM "+netchill.getMovD().getId_name());
    }





    @FXML
    public void btn_chooseSeat_click(ActionEvent event) throws IOException
    {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        root=fxmlLoader.load();
        Border_modelController border = fxmlLoader.getController();
        netchill.setNb_ticket(spinnerNbOfTicket.getValue());
        border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
        border.initialize(6);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();


    }


}
