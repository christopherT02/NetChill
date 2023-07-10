package com.example.netchill;

import javafx.scene.image.Image;

public class Movie {
    Customer customer = new Customer();
    private String id_name;
    private int time;
    private double price;
    private String description;
    private Image poster;

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

    public Image getPoster() {return poster;}
    public void setPoster(Image poster) {this.poster = poster;}




    @Override
    public String toString() {
        return "Movie{" +
                ", id_name='" + id_name + '\'' +
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
        this.poster = null;
    }

    public Movie() {
    }
}
