package booker.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class User {

    private String name;

    private String password;

    @JsonProperty("hashedPassword")
    private String hashPassword;

    @JsonProperty("tickets_booked")
    private List<Ticket> ticketsBooked;

    @JsonProperty("user_id")
    private String userId;

    public User(String name, String password, String hashedPassword, List<Ticket> ticketsBooked, String userId) {
        this.name = name;
        this.hashPassword = hashedPassword;
        this.password = password;
        this.ticketsBooked = ticketsBooked;
        this.userId = userId;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public String getUserId() {
        return userId;
    }

    public void printTickets() {
        for (int i = 0; i < ticketsBooked.size(); i++) {
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHashedPassword() {
        return hashPassword;
    }
}