package com.example.netchill;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class stats_Controller {

    private Netchill netchill = new Netchill();
    @FXML
    private Label label_unuse;


    @FXML
    private ChoiceBox<String> menu_Cinemas;

    @FXML
    private ChoiceBox<String> menu_Movies;

    @FXML
    private ChoiceBox<Integer> menu_Rooms;

    @FXML
    private ChoiceBox<Integer> menu_Sessions;

    @FXML
    private Text txt_StatsCinema;
    @FXML
    private Text txt_StatsMovie;
    @FXML
    private Text txt_StatsRoom;
    @FXML
    private Text txt_StatsSession;






    @FXML
    public void initialize()
    {
        String sql = "SELECT `name_cinema` FROM `cinema` WHERE 1";
        //add cinemas to the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            ArrayList<String> cinemas = new ArrayList<>();

            while(rs.next())
            {
                System.out.println(rs.getString("name_cinema"));

                cinemas.add(rs.getString("name_cinema"));
            }

            menu_Cinemas.getItems().addAll(cinemas);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }


        sql = "SELECT `ID_nb_room` FROM `room` WHERE 1";
        //add room to the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            ArrayList<Integer> rooms = new ArrayList<>();

            while(rs.next())
            {
                System.out.println(rs.getString("ID_nb_room"));

                rooms.add(rs.getInt("ID_nb_room"));
            }

            menu_Rooms.getItems().addAll(rooms);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }


        sql = "SELECT `ID_session` FROM `session` WHERE 1";
        //add room to the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            ArrayList<Integer> sessions = new ArrayList<>();

            while(rs.next())
            {
                System.out.println(rs.getString("ID_session"));

                sessions.add(rs.getInt("ID_session"));
            }

            menu_Sessions.getItems().addAll(sessions);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }


        sql = "SELECT `ID_name_movie` FROM `movie` WHERE 1";
        //add cinemas to the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            ArrayList<String> movies = new ArrayList<>();

            while(rs.next())
            {
                System.out.println(rs.getString("ID_name_movie"));

                movies.add(rs.getString("ID_name_movie"));
            }

            menu_Movies.getItems().addAll(movies);

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }

    }

    @FXML
    public void update_customer_stats(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS delete : "+netchill.getCustomer().getName_customer());
    }
}
