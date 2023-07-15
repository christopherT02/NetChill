package com.example.netchill;

import javafx.fxml.FXML;

import java.time.LocalDate;
import java.util.ArrayList;

public class Netchill {
    private Customer customer = new Customer();
    private Movie movD = new Movie();
    private LocalDate date_for_ticket;
    private int ID_session_selected;
    private ArrayList<Ticket> ticketList = new ArrayList<>();
    private int nb_ticket;
    private int incrementor = 0;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setMovD(Movie movD) {
        this.movD = movD;
    }

    public void setDate_for_ticket(LocalDate date_for_ticket) {
        this.date_for_ticket = date_for_ticket;
    }

    public void setID_session_selected(int ID_session_selected) {
        this.ID_session_selected = ID_session_selected;
    }

    public void setTicketList(ArrayList<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public void setNb_ticket(int nb_ticket) {
        this.nb_ticket = nb_ticket;
    }

    public void setIncrementor(int incrementor) {
        this.incrementor = incrementor;
    }

    public int getIncrementor() {
        return incrementor;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Movie getMovD() {
        return movD;
    }

    public LocalDate getDate_for_ticket() {
        return date_for_ticket;
    }

    public int getID_session_selected() {
        return ID_session_selected;
    }

    public ArrayList<Ticket> getTicketList() {
        return ticketList;
    }

    public int getNb_ticket() {
        return nb_ticket;
    }

    @FXML
    public void send_all_info_netchill(Customer customer_,Movie movie,ArrayList<Ticket> tickets,int nb_ticket_,int session_selected,int incrementor_,LocalDate date)
    {
        customer=customer_;
        movD=movie;
        ticketList=tickets;
        nb_ticket=nb_ticket_;
        ID_session_selected=session_selected;
        incrementor=incrementor_;
        date_for_ticket=date;
        System.out.println("All information updated");
    }


    public void add_ticket_liste(Ticket newticket)
    {
        ticketList.add(newticket);
    }

}
