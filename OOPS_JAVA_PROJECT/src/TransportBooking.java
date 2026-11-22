// TransportBooking.java - Stores a transport booking
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransportBooking {

    public enum Status { CONFIRMED, CANCELLED }

    private String    bookingId;
    private String    customerName;
    private String    customerContact;
    private Transport transport;
    private String    travelDate;
    private int       numPassengers;
    private double    totalPrice;
    private Status    status;
    private String    bookingTime;

    // Constructor for NEW bookings
    public TransportBooking(String bookingId, String customerName, String customerContact,
                            Transport transport, String travelDate, int numPassengers) {
        this.bookingId       = bookingId;
        this.customerName    = customerName;
        this.customerContact = customerContact;
        this.transport       = transport;
        this.travelDate      = travelDate;
        this.numPassengers   = numPassengers;
        this.totalPrice      = transport.getTotalPrice(numPassengers);
        this.status          = Status.CONFIRMED;
        this.bookingTime     = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    // Constructor for LOADING from file
    public TransportBooking(String bookingId, String customerName, String customerContact,
                            Transport transport, String travelDate, int numPassengers, String bookingTime) {
        this(bookingId, customerName, customerContact, transport, travelDate, numPassengers);
        this.bookingTime = bookingTime;
    }

    // Getters
    public String    getBookingId()       { return bookingId; }
    public String    getCustomerName()    { return customerName; }
    public String    getCustomerContact() { return customerContact; }
    public Transport getTransport()       { return transport; }
    public String    getTravelDate()      { return travelDate; }
    public int       getNumPassengers()   { return numPassengers; }
    public double    getTotalPrice()      { return totalPrice; }
    public Status    getStatus()          { return status; }
    public String    getBookingTime()     { return bookingTime; }

    public void cancel()       { this.status = Status.CANCELLED; }
    public boolean isActive()  { return status == Status.CONFIRMED; }

    @Override
    public String toString() {
        return "TransportBooking[" + bookingId + "] " + customerName
                + " → " + transport.getSource() + "→" + transport.getDestination() + " (" + status + ")";
    }
}
