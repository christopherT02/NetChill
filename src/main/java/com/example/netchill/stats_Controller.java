package com.example.netchill;

import javafx.event.ActionEvent;
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


    public void cinema_stats(ActionEvent event)
    {
        txt_StatsCinema.setText("");

        String sql = "SELECT `nb_sell_cinema` FROM `cinema` WHERE `name_cinema` = '"+ menu_Cinemas.getValue() +"'";
        //add cinemas to the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            while(rs.next())
            {
                System.out.println(rs.getInt("nb_sell_cinema"));

                //display result
                txt_StatsCinema.setText(String.valueOf(rs.getInt("nb_sell_cinema")) + " tickets sold in this cinema.");
            }

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }

    public void room_stats(ActionEvent event)
    {
        txt_StatsRoom.setText("");

        String sql = "SELECT `Nb_sell_place` FROM `room` WHERE `ID_nb_room` = '"+menu_Rooms.getValue()+"'";
        //add cinemas to the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            while(rs.next())
            {
                System.out.println(rs.getInt("Nb_sell_place"));

                //display result
                txt_StatsRoom.setText(String.valueOf(rs.getInt("Nb_sell_place")) + " tickets sold for this room.");
            }

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }

    private void session_stats(ActionEvent event)
    {
        txt_StatsSession.setText("");

        String sql = "SELECT * FROM `session` JOIN seat ON seat.ID_session = session.ID_session WHERE seat.ID_session = '"+ menu_Sessions.getValue() +"'";
        //add cinemas to the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            int cmpt = 0;
            while(rs.next())
            {
                cmpt++;
            }

            //display result
            txt_StatsSession.setText(String.valueOf(cmpt) + " tickets sold for this session.");

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }

    private void movie_stats(ActionEvent event)
    {
        txt_StatsMovie.setText("");

        String sql = "SELECT `Nb_sale` FROM `movie` WHERE `ID_name_movie` =  '"+menu_Movies.getValue()+"'";
        //add cinemas to the choicebox
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/netchill?useSSL=FALSE", "root", "");

            Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            while(rs.next())
            {
                System.out.println(rs.getInt("Nb_sale"));

                //display result
                txt_StatsMovie.setText(String.valueOf(rs.getInt("Nb_sale")) + " tickets sold for this movie.");
            }

            con.close();
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }




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
        menu_Cinemas.setOnAction(this::cinema_stats);


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
        menu_Rooms.setOnAction(this::room_stats);


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
        menu_Sessions.setOnAction(this::session_stats);


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
        menu_Movies.setOnAction(this::movie_stats);

    }




    @FXML
    public void update_customer_stats(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS delete : " + netchill.getCustomer().getName_customer());
    }
}
