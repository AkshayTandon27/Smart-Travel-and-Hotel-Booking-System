// DataManager.java - Handles saving and loading all bookings to/from text files
// Demonstrates: File Handling, BufferedReader, BufferedWriter, try-catch, IOException
import java.io.*;
import java.util.ArrayList;

public class DataManager {

    // Data directory and file paths (relative to project working directory)
    private static final String DATA_DIR       = "data";
    private static final String TRIP_FILE      = "data/trip_bookings.txt";
    private static final String HOTEL_FILE     = "data/hotel_bookings.txt";
    private static final String TRANSPORT_FILE = "data/transport_bookings.txt";
    private static final String DELIMITER      = "|";

    // Create the data/ folder if it doesn't exist
    private static void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // ==================== TRIP BOOKINGS ====================

    // Save all trip bookings to a text file (pipe-delimited)
    public static void saveTripBookings(ArrayList<Booking> bookings) {
        ensureDataDir();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRIP_FILE))) {
            for (Booking b : bookings) {
                TravelPackage pkg = b.getTravelPackage();
                // Each booking is stored as one line with | separator
                String line = b.getBookingId()           + "|"
                        + b.getCustomerName()            + "|"
                        + b.getCustomerContact()         + "|"
                        + pkg.getPackageId()             + "|"
                        + pkg.getPackageName()           + "|"
                        + pkg.getDestination()           + "|"
                        + pkg.getSourceCity()            + "|"
                        + pkg.getDurationDays()          + "|"
                        + pkg.getPricePerPerson()        + "|"
                        + b.getTravelDate()              + "|"
                        + b.getNumTravellers()           + "|"
                        + b.getTotalPrice()              + "|"
                        + b.getStatus().toString()       + "|"
                        + b.getBookingTime();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving trip bookings: " + e.getMessage());
        }
    }

    // Load trip bookings from file into the BookingManager
    public static void loadTripBookings(BookingManager manager) {
        File file = new File(TRIP_FILE);
        if (!file.exists()) return; // First run — no file yet, skip silently

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    String[] p = line.split("\\|", -1);
                    if (p.length < 14) continue;

                    String pkgId     = p[3];
                    String pkgName   = p[4];
                    String dest      = p[5];
                    String srcCity   = p[6];
                    int    duration  = Integer.parseInt(p[7].trim());
                    double price     = Double.parseDouble(p[8].trim());
                    int travellers   = Integer.parseInt(p[10].trim());
                    String status    = p[12];
                    String time      = p[13];

                    // Look up the predefined package, or create a stub if not found
                    TravelPackage pkg = manager.getPackageById(pkgId);
                    if (pkg == null) {
                        pkg = new TravelPackage(pkgId, pkgName, dest, srcCity, duration, price, "", "");
                    }

                    Booking b = new Booking(p[0], p[1], p[2], pkg, travellers, p[9], time);
                    if ("CANCELLED".equals(status)) b.cancel();
                    manager.addLoadedTripBooking(b);

                } catch (Exception e) {
                    System.err.println("Skipping bad trip booking line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading trip bookings: " + e.getMessage());
        }
    }

    // ==================== HOTEL BOOKINGS ====================

    public static void saveHotelBookings(ArrayList<HotelBooking> bookings) {
        ensureDataDir();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HOTEL_FILE))) {
            for (HotelBooking b : bookings) {
                Hotel h = b.getHotel();
                String safeAmenities = h.getAmenities().replace("|", "•");
                String line = b.getBookingId()       + "|"
                        + b.getCustomerName()         + "|"
                        + b.getCustomerContact()      + "|"
                        + h.getHotelId()              + "|"
                        + h.getName()                 + "|"
                        + h.getDestination()          + "|"
                        + h.getType()                 + "|"
                        + h.getPricePerNight()        + "|"
                        + h.getRating()               + "|"
                        + safeAmenities               + "|"
                        + b.getCheckIn()              + "|"
                        + b.getCheckOut()             + "|"
                        + b.getNights()               + "|"
                        + b.getNumGuests()            + "|"
                        + b.getTotalPrice()           + "|"
                        + b.getStatus().toString()    + "|"
                        + b.getBookingTime();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving hotel bookings: " + e.getMessage());
        }
    }

    public static void loadHotelBookings(BookingManager manager) {
        File file = new File(HOTEL_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    String[] p = line.split("\\|", -1);
                    if (p.length < 17) continue;

                    int len = p.length;
                    String hotelId   = p[3];
                    String hotelName = p[4];
                    String dest      = p[5];
                    String type      = p[6];
                    double pricePN   = Double.parseDouble(p[7].trim());
                    double rating    = Double.parseDouble(p[8].trim());

                    // Combine any amenity tokens in case the line contained raw pipes
                    StringBuilder amenSb = new StringBuilder();
                    for (int i = 9; i <= len - 8; i++) {
                        if (amenSb.length() > 0) amenSb.append(" • ");
                        amenSb.append(p[i].trim());
                    }
                    String amenities = amenSb.toString();

                    String checkIn   = p[len - 7];
                    String checkOut  = p[len - 6];
                    int    nights    = Integer.parseInt(p[len - 5].trim());
                    int    guests    = Integer.parseInt(p[len - 4].trim());
                    String status    = p[len - 2];
                    String time      = p[len - 1];

                    Hotel hotel = manager.getHotelById(hotelId);
                    if (hotel == null) {
                        hotel = new Hotel(hotelId, hotelName, dest, type, pricePN, rating, amenities, "");
                    }

                    HotelBooking hb = new HotelBooking(p[0], p[1], p[2], hotel, checkIn, checkOut, nights, guests, time);
                    if ("CANCELLED".equals(status)) hb.cancel();
                    manager.addLoadedHotelBooking(hb);

                } catch (Exception e) {
                    System.err.println("Skipping bad hotel booking line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading hotel bookings: " + e.getMessage());
        }
    }

    // ==================== TRANSPORT BOOKINGS ====================

    public static void saveTransportBookings(ArrayList<TransportBooking> bookings) {
        ensureDataDir();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSPORT_FILE))) {
            for (TransportBooking b : bookings) {
                Transport t = b.getTransport();
                String line = b.getBookingId()       + "|"
                        + b.getCustomerName()         + "|"
                        + b.getCustomerContact()      + "|"
                        + t.getTransportId()          + "|"
                        + t.getType()                 + "|"
                        + t.getSource()               + "|"
                        + t.getDestination()          + "|"
                        + t.getProvider()             + "|"
                        + t.getDepartureTime()        + "|"
                        + t.getDuration()             + "|"
                        + t.getPricePerPerson()       + "|"
                        + b.getTravelDate()           + "|"
                        + b.getNumPassengers()        + "|"
                        + b.getTotalPrice()           + "|"
                        + b.getStatus().toString()    + "|"
                        + b.getBookingTime();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving transport bookings: " + e.getMessage());
        }
    }

    public static void loadTransportBookings(BookingManager manager) {
        File file = new File(TRANSPORT_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    String[] p = line.split("\\|", -1);
                    if (p.length < 16) continue;

                    String transportId = p[3];
                    String tType       = p[4];
                    String src         = p[5];
                    String dst         = p[6];
                    String provider    = p[7];
                    String depTime     = p[8];
                    String duration    = p[9];
                    double pricePerP   = Double.parseDouble(p[10].trim());
                    int    passengers  = Integer.parseInt(p[12].trim());
                    String status      = p[14];
                    String time        = p[15];

                    Transport transport = manager.getTransportById(transportId);
                    if (transport == null) {
                        transport = new Transport(transportId, tType, src, dst, provider, depTime, duration, pricePerP);
                    }

                    TransportBooking tb = new TransportBooking(p[0], p[1], p[2], transport, p[11], passengers, time);
                    if ("CANCELLED".equals(status)) tb.cancel();
                    manager.addLoadedTransportBooking(tb);

                } catch (Exception e) {
                    System.err.println("Skipping bad transport booking line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading transport bookings: " + e.getMessage());
        }
    }
}
