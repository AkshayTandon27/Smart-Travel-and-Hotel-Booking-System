// HotelBooking.java - Stores a hotel booking (OOP + Encapsulation)
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HotelBooking {

    public enum Status { CONFIRMED, CANCELLED }

    private String bookingId;
    private String customerName;
    private String customerContact;
    private Hotel  hotel;
    private String checkIn;
    private String checkOut;
    private int    nights;
    private int    numGuests;
    private double totalPrice;
    private Status status;
    private String bookingTime;

    // Constructor for NEW bookings (auto-sets booking time)
    public HotelBooking(String bookingId, String customerName, String customerContact,
                        Hotel hotel, String checkIn, String checkOut, int nights, int numGuests) {
        this.bookingId       = bookingId;
        this.customerName    = customerName;
        this.customerContact = customerContact;
        this.hotel           = hotel;
        this.checkIn         = checkIn;
        this.checkOut        = checkOut;
        this.nights          = nights;
        this.numGuests       = numGuests;
        this.totalPrice      = hotel.getPricePerNight() * nights;
        this.status          = Status.CONFIRMED;
        this.bookingTime     = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    // Constructor for LOADING from file (bookingTime is provided)
    public HotelBooking(String bookingId, String customerName, String customerContact,
                        Hotel hotel, String checkIn, String checkOut, int nights, int numGuests, String bookingTime) {
        this(bookingId, customerName, customerContact, hotel, checkIn, checkOut, nights, numGuests);
        this.bookingTime = bookingTime; // override auto-generated time
    }

    // Getters
    public String getBookingId()       { return bookingId; }
    public String getCustomerName()    { return customerName; }
    public String getCustomerContact() { return customerContact; }
    public Hotel  getHotel()           { return hotel; }
    public String getCheckIn()         { return checkIn; }
    public String getCheckOut()        { return checkOut; }
    public int    getNights()          { return nights; }
    public int    getNumGuests()       { return numGuests; }
    public double getTotalPrice()      { return totalPrice; }
    public Status getStatus()          { return status; }
    public String getBookingTime()     { return bookingTime; }

    public void cancel()         { this.status = Status.CANCELLED; }
    public boolean isActive()    { return status == Status.CONFIRMED; }

    @Override
    public String toString() {
        return "HotelBooking[" + bookingId + "] " + customerName
                + " → " + hotel.getName() + " (" + status + ")";
    }
}
