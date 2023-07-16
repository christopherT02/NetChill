package com.example.netchill;

import java.util.ArrayList;

public class Basket {

    private ArrayList<Ticket> all_tickets_basket = new ArrayList<>();

    private int price_giftCard=0;
    public ArrayList<Ticket> getAll_tickets_basket() {
        return all_tickets_basket;
    }

    public void setAll_tickets_basket(ArrayList<Ticket> all_tickets_basket) {
        this.all_tickets_basket = all_tickets_basket;
    }

    public void add_ticket(Ticket newTicket)
    {
        all_tickets_basket.add(newTicket);
    }

    public void remove_ticket()
    {
        all_tickets_basket.remove(0);
    }
}
