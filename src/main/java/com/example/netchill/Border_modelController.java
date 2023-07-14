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

public class Border_modelController {

    Customer customer = new Customer();
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
        int length = button_account.getText().length();
        boolean too_small = true;
        if(length<8)
        {
            too_small=false;
        }
        else
        {
            test_employee = button_account.getText().substring(0,9);

        }

        if(button_account.getText().equals("Account") || button_account.getText().equals("Guest"))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            loginController login = loader.getController();
            login.update_customer_login(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            bpane.setCenter(root);
        } else if (test_employee.equals("Employee_") || too_small) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeInfo.fxml"));
            Parent root = loader.load();
            EmployeeInfo_Controller employeeinfo = loader.getController();
            employeeinfo.update_customer_employeeinfo(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            bpane.setCenter(root);
        } else
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customer_information.fxml"));
            Parent root = loader.load();
            CustomerInformation customerInformation = loader.getController();
            customerInformation.update_customer_custoInfo(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            bpane.setCenter(root);
        }
    }

    @FXML
    void click_button_basket(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(HelloApplication.class.getResource("screen1.fxml"));
        bpane.setCenter(root);
    }

    @FXML
    void click_button_giftCard(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(HelloApplication.class.getResource("screen1.fxml"));
        bpane.setCenter(root);
    }

    /*@FXML

    void click_button_homepage(ActionEvent event)throws IOException {

        HelloController homepage = new HelloController();
        homepage.update_customer_homepage(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("hello-view.fxml"));
        bpane.setCenter(root);
    }*/
    @FXML
    void click_button_homepage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        HelloController helloController = loader.getController();
        helloController.update_customer_homepage(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
        bpane.setCenter(root);
    }


    @FXML
    void click_button_movie(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("movies.fxml"));
        Parent root = loader.load();
        moviesController movies = loader.getController();
        movies.update_customer_movies(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
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
            signin.update_customer_signin(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            bpane.setCenter(root);
        } else if(login_signin==2){
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            loginController login = loader.getController();
            login.update_customer_login(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            bpane.setCenter(root);
        } else if (login_signin==3) { //homepage
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            HelloController helloController = loader.getController();
            helloController.update_customer_homepage(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            bpane.setCenter(root);
        } else if (login_signin==4) {
            login_signin=0;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_movie.fxml"));
            Parent root = loader.load();
            addMovieController control = loader.getController();
            control.update_customer_addMovie(customer.getName_customer(),customer.getEmail_customer(),customer.getCard_nb_customer());
            bpane.setCenter(root);
        }
    }

    @FXML
    void initialize(int i) throws IOException
    {
        System.out.println("Name Border : "+customer.getName_customer());
        button_account.setText(customer.getName_customer());
        login_signin=i;
        test();
    }
    @FXML
    public void update_customer_border(String name,String email,String card_nb)
    {
        customer.set_all_info_customer(name,email,card_nb);
    }

}