package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Border_modelController {
    private Netchill netchill = new Netchill();
    private int login_signin;

    public void setLogin_signin(int login_signin) {
        this.login_signin = login_signin;
    }

    public int getLogin_signin() {
        return login_signin;
    }
    @FXML
    private BorderPane bpane;
    public void setBpane(Parent root) {
        bpane.setCenter(root);
    }

    public BorderPane getBpane() {
        return bpane;
    }



    @FXML
    void click_button_netchill(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("hello-view.fxml"));
        bpane.setCenter(root);
    }
    @FXML
    private Button button_account;

    @FXML
    void click_button_account(ActionEvent event) throws IOException {

        String test_employee="";
        int length = netchill.getCustomer().getEmail_customer().length();
        test_employee = netchill.getCustomer().getEmail_customer().substring(length-10);



        if(button_account.getText().equals("Account") || button_account.getText().equals("Guest"))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            loginController login = loader.getController();
            //login.update_customer_login(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            login.update_customer_login(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            bpane.setCenter(root);
        } else if (test_employee.equals("@cinema.fr") ) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeInfo.fxml"));
            Parent root = loader.load();
            EmployeeInfo_Controller employeeinfo = loader.getController();
            employeeinfo.update_customer_employeeinfo(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            bpane.setCenter(root);
        } else
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customer_information.fxml"));
            Parent root = loader.load();
            CustomerInformation customerInformation = loader.getController();
            //customerInformation.update_customer_custoInfo(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            customerInformation.update_customer_custoInfo(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            try {
                customerInformation.initialize();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            bpane.setCenter(root);
        }
    }



    @FXML
    void click_button_giftCard(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Gift_card.fxml"));
        Parent root = loader.load();
        GiftCardController helloController = loader.getController();
        //helloController.update_customer_homepage(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
        helloController.update_customer_giftCard(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
        bpane.setCenter(root);

    }


    @FXML
    void click_button_homepage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        HelloController helloController = loader.getController();
        //helloController.update_customer_homepage(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
        helloController.update_customer_homepage(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
        bpane.setCenter(root);
    }


    @FXML
    void click_button_movie(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("movies.fxml"));
        Parent root = loader.load();
        moviesController movies = loader.getController();
        //movies.update_customer_movies(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
        movies.update_customer_movies(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());

        bpane.setCenter(root);
    }

    public void test() throws IOException
    {
        System.out.println("Initialize lance : "+login_signin);
        if(login_signin==1)
        {
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signin.fxml"));
            Parent root = loader.load();
            signinController signin = loader.getController();
            signin.update_customer_signin(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            bpane.setCenter(root);
        } else if(login_signin==2){
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            loginController login = loader.getController();
            //login.update_customer_login(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            login.update_customer_login(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            bpane.setCenter(root);
        } else if (login_signin==3) { //homepage
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            HelloController helloController = loader.getController();
            //helloController.update_customer_homepage(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            helloController.update_customer_homepage(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            bpane.setCenter(root);
        } else if (login_signin==4) { // add a new movie
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_movie.fxml"));
            Parent root = loader.load();
            addMovieController control = loader.getController();
            control.update_customer_addMovie(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            bpane.setCenter(root);
        }
        else if (login_signin==5) { // display one movie
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("movie.fxml"));
            Parent root = loader.load();
            movieController control = loader.getController();
            netchill.setIncrementor(0);
            control.update_customer_movie(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            control.init();
            //give infos about the selected movie to the new page
            //call this function because it doesnt work in the "initialize()" function



            bpane.setCenter(root);
        }
        else if (login_signin==6) { // choose the seat
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("choose_seat.fxml"));
            Parent root = loader.load();
            chooseSeatController control = loader.getController();
            control.update_customer_chooseSeat(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            control.init();
            bpane.setCenter(root);
        }
        else if (login_signin==7) { // payment page
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Payment_page.fxml"));
            Parent root = loader.load();
            Payment_pageController control = loader.getController();
            control.update_customer_payment(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            bpane.setCenter(root);
        }
        else if (login_signin==8) { // delete movie
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("delete_movie.fxml"));
            Parent root = loader.load();
            delete_movie_Controller control = loader.getController();
            control.update_customer_deletemovie(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            bpane.setCenter(root);
        }
        else if (login_signin==9) { // modif movie
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modify_playing_movies.fxml"));
            Parent root = loader.load();
            modify_playing_movies_Controller control = loader.getController();
            control.update_customer_deletemovie(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            bpane.setCenter(root);
        }
        else if (login_signin==10) { // basket
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Basket_customer.fxml"));
            Parent root = loader.load();
            BasketCustomerController control = loader.getController();
            control.update_customer_basket(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
            try {
                control.initialize();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            bpane.setCenter(root);
        }

    }

    @FXML
    void initialize(int i) throws IOException
    {
        System.out.println("Name Border : "+netchill.getCustomer().getName_customer());
        String test_employee="";
        int length = netchill.getCustomer().getEmail_customer().length();
        test_employee = netchill.getCustomer().getEmail_customer().substring(length-10);
        if(test_employee.equals("@cinema.fr"))
        {
            button_account.setText("Emp_"+netchill.getCustomer().getName_customer());
        }
        else
        {
            button_account.setText(netchill.getCustomer().getName_customer());
        }
        login_signin=i;
        test();
    }

    @FXML
    public void update_customer_border(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
    }

}