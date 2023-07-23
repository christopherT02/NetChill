package com.example.netchill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GiftCardController implements Initializable {

    Netchill netchill = new Netchill();
    @FXML
    private Button button_basket;
    @FXML
    private ChoiceBox<String> choice_box1;

    @FXML
    private ChoiceBox<String> choice_box2;

    @FXML
    private ChoiceBox<String> choice_box3;

    @FXML
    private Label label_finalprice1;

    @FXML
    private Label label_finalprice2;

    @FXML
    private Label label_finalprice3;

    @FXML
    private Label label_unuse;

    private String[] choices = {"0","1","2","3","4","5"} ;

    Parent root;
    Stage lstage;
    Scene scene;

    @FXML
    void click_buttonAdd1(ActionEvent event) {
        int price = Integer.parseInt(choice_box1.getValue());

        //add gift card
        price = price*10;
        label_finalprice1.setText("£"+price);
        int price1=0,price2=0,price3=0;
        int final_price=0;


        price1= Integer.parseInt(choice_box1.getValue());
        price2= Integer.parseInt(choice_box2.getValue());
        price3= Integer.parseInt(choice_box3.getValue());
        final_price=price1*10 + price2*20 + price3*50;
        button_basket.setText("Basket: £"+final_price);
    }

    @FXML
    void click_buttonAdd2(ActionEvent event) {
        //add gift card of 20£
        int price = Integer.parseInt(choice_box2.getValue());
        price = price*20;
        label_finalprice2.setText("£"+price);
        int price1,price2,price3;
        int final_price=0;
        price1=Integer.parseInt(choice_box1.getValue());
        price2= Integer.parseInt(choice_box2.getValue());
        price3= Integer.parseInt(choice_box3.getValue());
        final_price=price1*10 + price2*20 + price3*50;
        button_basket.setText("Basket: £"+final_price);
    }

    @FXML
    void click_buttonAdd3(ActionEvent event) {
        //add gift card of £50
        int price = Integer.parseInt(choice_box3.getValue());
        price = price*50;
        label_finalprice3.setText("£"+price);
        int price1,price2,price3;
        int final_price=0;
        price1=Integer.parseInt(choice_box1.getValue());
        price2= Integer.parseInt(choice_box2.getValue());
        price3= Integer.parseInt(choice_box3.getValue());
        final_price=price1*10 + price2*20 + price3*50;
        button_basket.setText("Basket: £"+final_price);
    }

    @FXML
    void click_button_basket(ActionEvent event) throws IOException {

        //update basket and call border and send the good value
        int price1,price2,price3;
        int final_price=0;
        price1=Integer.parseInt(choice_box1.getValue());
        price2= Integer.parseInt(choice_box2.getValue());
        price3= Integer.parseInt(choice_box3.getValue());
        final_price=price1*10 + price2*20 + price3*50;

        Customer customer=new Customer();
        customer=netchill.getCustomer();
        customer.setAmount_gift_card((double)final_price);
        netchill.setCustomer(customer);


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Border_model.fxml"));
        root=fxmlLoader.load();
        Border_modelController border = fxmlLoader.getController();
        border.update_customer_border(netchill.getCustomer(),netchill.getMovD(),netchill.getTicketList(),netchill.getNb_ticket(),netchill.getID_session_selected(),netchill.getIncrementor(),netchill.getDate_for_ticket());
        border.initialize(10);
        lstage=(Stage)((Node)(event.getSource())).getScene().getWindow();
        scene=new Scene(root);
        lstage.setScene(scene);
        lstage.show();
    }

    @FXML
    public void update_customer_giftCard(Customer custom, Movie mov, ArrayList<Ticket> tickets, int nb_ticket_, int session_selected, int incrementor_, LocalDate date)
    {
        //add gift card of 20£
        netchill.send_all_info_netchill(custom,mov,tickets,nb_ticket_,session_selected,incrementor_,date);
        label_unuse.setText(netchill.getCustomer().getName_customer());
        label_unuse.setVisible(false);
        System.out.println("DANS Payment : "+netchill.getCustomer().getName_customer());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choice_box1.setValue("0");
        choice_box2.setValue("0");
        choice_box3.setValue("0");


        choice_box1.getItems().addAll(choices);
        choice_box2.getItems().addAll(choices);
        choice_box3.getItems().addAll(choices);
    }
}
