// Booking.java - Represents a trip package booking (updated with file-load constructor)
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {

    public enum Status { CONFIRMED, CANCELLED }

    private String        bookingId;
    private String        customerName;
    private String        customerContact;
    private TravelPackage travelPackage;
    private int           numTravellers;
    private double        totalPrice;
    private String        travelDate;
    private Status        status;
    private String        bookingTime;

    // Constructor for NEW bookings (auto-generates booking time)
    public Booking(String bookingId, String customerName, String customerContact,
                   TravelPackage travelPackage, int numTravellers, String travelDate) {
        this.bookingId       = bookingId;
        this.customerName    = customerName;
        this.customerContact = customerContact;
        this.travelPackage   = travelPackage;
        this.numTravellers   = numTravellers;
        this.travelDate      = travelDate;
        this.totalPrice      = travelPackage.getTotalPrice(numTravellers);
        this.status          = Status.CONFIRMED;
        this.bookingTime     = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    // Constructor for LOADING from file (bookingTime is provided from stored data)
    public Booking(String bookingId, String customerName, String customerContact,
                   TravelPackage travelPackage, int numTravellers, String travelDate, String bookingTime) {
        this(bookingId, customerName, customerContact, travelPackage, numTravellers, travelDate);
        this.bookingTime = bookingTime; // override the auto-generated time
    }

    // Getters
    public String        getBookingId()       { return bookingId; }
    public String        getCustomerName()    { return customerName; }
    public String        getCustomerContact() { return customerContact; }
    public TravelPackage getTravelPackage()   { return travelPackage; }
    public int           getNumTravellers()   { return numTravellers; }
    public double        getTotalPrice()      { return totalPrice; }
    public String        getTravelDate()      { return travelDate; }
    public Status        getStatus()          { return status; }
    public String        getBookingTime()     { return bookingTime; }

    public void cancel()      { this.status = Status.CANCELLED; }
    public boolean isActive() { return status == Status.CONFIRMED; }

    @Override
    public String toString() {
        return "Booking[" + bookingId + "] " + customerName
                + " → " + travelPackage.getDestination() + " (" + status + ")";
    }
}
