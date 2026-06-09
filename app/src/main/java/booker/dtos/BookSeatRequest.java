package booker.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookSeatRequest {

    @NotBlank(message = "Username cannot be blank")
    private String name;

    @NotNull(message = "Row is required")
    @Min(value = 0, message = "Row index must be 0 or greater")
    private Integer row;

    @NotNull(message = "Column is required")
    @Min(value = 0, message = "Column index must be 0 or greater")
    private Integer col;

    @NotBlank(message = "Source station cannot be blank")
    private String source;

    @NotBlank(message = "Destination station cannot be blank")
    private String destination;

    @NotBlank(message = "Date of travel cannot be blank")
    private String dateOfTravel;

    public BookSeatRequest() {
    }

    public BookSeatRequest(String name, Integer row, Integer col, String source, String destination, String dateOfTravel) {
        this.name = name;
        this.row = row;
        this.col = col;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }
}
