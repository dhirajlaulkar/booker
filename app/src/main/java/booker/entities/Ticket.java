package booker.entities;

import java.util.Date;

public class Ticket {

    private String ticketId;

    private String userId;
    private String source;
    private String destination;
    private String dateOfTravel;
    private Train train;

    public Ticket(){}

    public Ticket(String ticketId,String userId,String source,String destination,String dateOfTravel, Train train){
        this.ticketId =ticketId;
        this.userId =userId;
        this.dateOfTravel=dateOfTravel;
        this.source=source;
        this.destination=destination;
        this.train=train;
    }

    public String getTicketInfo(){
        return String.format("Ticket ID: %s belongs to User %s from %s to 5s on %s", ticketId,userId,source,destination,dateOfTravel);
    }

}
