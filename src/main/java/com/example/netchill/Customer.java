package com.example.netchill;

//TODO model in MVC
public class Customer {
    private String name_customer;
    private String Email_customer;
    private Basket basket = new Basket();

    public Customer()
    {
        name_customer="Guest";
        Email_customer="guest@mail.fr";
    }
    public void setCustomer_or_employee(int customer_or_employee) {
        this.customer_or_employee = customer_or_employee;
    }

    public int getCustomer_or_employee() {
        return customer_or_employee;
    }

    private int customer_or_employee;
    public String getName_customer() {
        return name_customer;
    }

    public String getEmail_customer() {
        return Email_customer;
    }


    public void setName_customer(String name_customer) {
        this.name_customer = name_customer;
    }

    public void setEmail_customer(String email_customer) {
        Email_customer = email_customer;
    }


    public void set_all_info_customer(String name,String email)
    {
        name_customer=name;
        Email_customer = email;
    }
}
