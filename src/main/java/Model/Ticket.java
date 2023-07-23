package Model;

import Model.Movie;

import java.time.LocalDate;

public class Ticket {
    private Movie mv;
    private int id_seat;
    private LocalDate date;
    private int idSession;  //useful to know the broadcast schedule

    ///constructor
    public Ticket() {
    }

    public Ticket(Movie mv, int id_seat, LocalDate date, int idSession) {
        this.mv = mv;
        this.id_seat = id_seat;
        this.date = date;
        this.idSession = idSession;
    }

    ///getter - setter
    public Movie getMv() {return mv;}
    public void setMv(Movie mv) {this.mv = mv;}

    public int getId_seat() {return id_seat;}
    public void setId_seat(int id_seat) {this.id_seat = id_seat;}

    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    public int getIdSession() {return idSession;}
    public void setIdSession(int idSession) {this.idSession = idSession;}
}
