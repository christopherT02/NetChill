package com.example.netchill;

public class Customer {
    private String name_customer;
    private String Email_customer;
    private String Card_nb_customer;

    public String getName_customer() {
        return name_customer;
    }

    public String getEmail_customer() {
        return Email_customer;
    }

    public String getCard_nb_customer() {
        return Card_nb_customer;
    }

    public void setName_customer(String name_customer) {
        this.name_customer = name_customer;
    }

    public void setEmail_customer(String email_customer) {
        Email_customer = email_customer;
    }

    public void setCard_nb_customer(String card_nb_customer) {
        Card_nb_customer = card_nb_customer;
    }

    public void set_all_info_customer(String name,String email,String card_nb_customer)
    {
        name_customer=name;
        Email_customer = email;
        Card_nb_customer = card_nb_customer;
    }
}
