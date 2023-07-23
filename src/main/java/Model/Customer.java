package Model;

//TODO model in MVC
public class Customer {
    private String name_customer;
    private String Email_customer;
    private Basket basket = new Basket();

    private double amount_gift_card=0;
    private int ID_customer;
    public Customer()
    {
        //before be connected we are known as Guest
        name_customer="Guest";
        Email_customer="guest@mail.fr";
    }

    public void setAmount_gift_card(double amount_gift_card) {
        this.amount_gift_card = amount_gift_card;
    }

    public double getAmount_gift_card() {
        return amount_gift_card;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public void setID_customer(int ID_customer) {
        this.ID_customer = ID_customer;
    }

    public Basket getBasket() {
        return basket;
    }

    public int getID_customer() {
        return ID_customer;
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


    public void set_all_info_customer(String name,String email,int ID,double price_gift)
    {
        //general setter
        name_customer=name;
        Email_customer = email;
        ID_customer=ID;
        amount_gift_card=price_gift;
    }
}
