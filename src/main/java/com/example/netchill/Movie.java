package com.example.netchill;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Movie implements Initializable {
    Customer customer = new Customer();
    private String id_name;
    private int time;

    private double price;
    private String description;

    //getter-setter
    public String getId_name() {
        return id_name;
    }
    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id_name='" + id_name + '\'' +
                ", time=" + time +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }

    public Movie(String id_name, int time, double price, String description) {
        this.id_name = id_name;
        this.time = time;
        this.price = price;
        this.description = description;
    }

    public Movie() {
    }

    public void update_customer_movie(String name,String email,String card_nb)
    {
        customer.set_all_info_customer(name,email,card_nb);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Name "+customer.getName_customer());
    }
}
