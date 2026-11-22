// BookingManager.java - Central data manager (ArrayList, OOP, business logic)
// Now manages Trip Packages, Hotels AND Transport options + all bookings
import java.util.ArrayList;

public class BookingManager {

    // ---- In-memory data stores ----
    private ArrayList<Booking>          bookings;           // Trip bookings
    private ArrayList<HotelBooking>     hotelBookings;      // Hotel bookings
    private ArrayList<TransportBooking> transportBookings;  // Transport bookings

    private ArrayList<TravelPackage>    travelPackages;     // Predefined packages
    private ArrayList<Destination>      destinations;       // Predefined destinations
    private ArrayList<Hotel>            hotels;             // Predefined hotels
    private ArrayList<Transport>        transports;         // Predefined transport options

    // Booking ID counters (different prefix per type)
    private int bookingCounter          = 1000; // TRV prefix
    private int hotelBookingCounter     = 2000; // HTL prefix
    private int transportBookingCounter = 3000; // TRP prefix

    public BookingManager() {
        bookings           = new ArrayList<>();
        hotelBookings      = new ArrayList<>();
        transportBookings  = new ArrayList<>();
        initializeSampleData();
        // Load saved bookings from files on startup
        DataManager.loadTripBookings(this);
        DataManager.loadHotelBookings(this);
        DataManager.loadTransportBookings(this);
    }

    // ==================== SAMPLE DATA ====================

    private void initializeSampleData() {
        initializeDestinations();
        initializePackages();
        initializeHotels();
        initializeTransport();
    }

    private void initializeDestinations() {
        destinations = new ArrayList<>();
        destinations.add(new Destination("Goa",             "West Coast, India",        "Sun, sand and sea! Pristine beaches, vibrant nightlife and Portuguese heritage.",              8500,  "🏖"));
        destinations.add(new Destination("Manali",          "Himachal Pradesh, India",  "Adventure capital of India. Snow peaks, river rafting and scenic valleys.",                   9200,  "🏔"));
        destinations.add(new Destination("Jaipur",          "Rajasthan, India",         "The Pink City. Forts, palaces, camels and rich Rajasthani culture.",                          6500,  "🏰"));
        destinations.add(new Destination("Kerala",          "South India",              "God's Own Country. Backwaters, houseboat stays and lush greenery.",                           11000, "🌴"));
        destinations.add(new Destination("Varanasi",        "Uttar Pradesh, India",     "One of the world's oldest cities. Ghats, temples and spiritual experience.",                  5500,  "🕌"));
        destinations.add(new Destination("Andaman Islands", "Bay of Bengal, India",     "Crystal clear water, coral reefs and white sandy beaches. Perfect for snorkelling.",         18000, "🐠"));
        destinations.add(new Destination("Leh Ladakh",      "Jammu & Kashmir, India",   "High altitude desert with stunning monasteries, lakes and mountain passes.",                  15000, "🏕"));
        destinations.add(new Destination("Ooty",            "Tamil Nadu, India",        "Queen of Hill Stations. Tea gardens, botanical garden and toy train ride.",                   7000,  "🌿"));
    }

    private void initializePackages() {
        travelPackages = new ArrayList<>();
        travelPackages.add(new TravelPackage("PKG001", "Goa Beach Escape",          "Goa",             "Mumbai",    4, 8500,  "Relax on Calangute & Baga beach, enjoy water sports, flea markets and Goan cuisine.",                           "Hotel (3★) + Breakfast + Sightseeing + Airport Transfer"));
        travelPackages.add(new TravelPackage("PKG002", "Manali Adventure Trip",     "Manali",          "Delhi",     5, 9200,  "Rohtang Pass, Solang Valley, Hidimba Temple and river rafting adventure.",                                      "Hotel (3★) + Meals + Sightseeing + Transport"));
        travelPackages.add(new TravelPackage("PKG003", "Jaipur Royal Tour",         "Jaipur",          "Delhi",     3, 6500,  "Visit Amber Fort, City Palace, Hawa Mahal and experience the Pink City's culture.",                             "Hotel (3★) + Breakfast + City Tour"));
        travelPackages.add(new TravelPackage("PKG004", "Kerala Backwaters Delight", "Kerala",          "Bangalore", 6, 11000, "Alleppey houseboat stay, Munnar tea gardens, Thekkady wildlife and Kovalam beach.",                              "Hotel + Houseboat (1 night) + Meals + All Transfers"));
        travelPackages.add(new TravelPackage("PKG005", "Varanasi Spiritual Journey","Varanasi",        "Lucknow",   3, 5500,  "Ganga Aarti, boat ride on the Ganges, Sarnath visit and temple tours.",                                        "Hotel (2★) + Breakfast + Guide + Boat Ride"));
        travelPackages.add(new TravelPackage("PKG006", "Andaman Island Getaway",    "Andaman Islands", "Chennai",   6, 18000, "Radhanagar Beach, Havelock Island, snorkelling at Elephant Beach and ferry rides.",                              "Resort (3★) + All Meals + Ferry Passes + Water Sports"));
        travelPackages.add(new TravelPackage("PKG007", "Leh Ladakh Expedition",     "Leh Ladakh",      "Delhi",     7, 15000, "Pangong Lake, Nubra Valley, Khardung La Pass and ancient monasteries.",                                        "Hotel + Camps + Meals + Private Vehicle + Inner Line Permits"));
        travelPackages.add(new TravelPackage("PKG008", "Ooty Hill Station Retreat", "Ooty",            "Chennai",   4, 7000,  "Botanical Garden, Ooty Lake, Doddabetta Peak, tea factory visit and Nilgiri toy train.",                        "Hotel (3★) + Breakfast + Sightseeing + Toy Train Ticket"));
    }

    private void initializeHotels() {
        hotels = new ArrayList<>();
        // --- Goa ---
        hotels.add(new Hotel("H001", "Calangute Beach Inn",   "Goa",             "Budget",   2500,  3.8, "WiFi | AC | Parking | TV",                             "Popular budget stay near Calangute Beach, 5 min walk to the sea."));
        hotels.add(new Hotel("H002", "Cidade de Goa",         "Goa",             "Standard", 6500,  4.2, "WiFi | Pool | Restaurant | Gym | Bar",                 "Iconic beachside hotel with stunning sea views and world-class facilities."));
        hotels.add(new Hotel("H003", "W Goa",                 "Goa",             "Luxury",   18000, 4.8, "WiFi | Private Pool | Spa | Fine Dining | Rooftop Bar", "Ultra-luxe cliff-top resort on Vagator overlooking the Arabian Sea."));
        // --- Manali ---
        hotels.add(new Hotel("H004", "Snow Valley Guesthouse","Manali",          "Budget",   1800,  3.5, "WiFi | Heating | Hot Water | TV",                      "Cozy guesthouse with mountain views, 2 km from Mall Road."));
        hotels.add(new Hotel("H005", "The Himalayan Hotel",   "Manali",          "Standard", 5000,  4.1, "WiFi | Restaurant | Bonfire | Heating | Garden",        "Colonial-style hotel with panoramic Himalayan views and warm hospitality."));
        hotels.add(new Hotel("H006", "Span Resort & Spa",     "Manali",          "Luxury",   12000, 4.6, "WiFi | Spa | Pool | Restaurant | Private Cottages",     "Luxury riverside resort surrounded by apple orchards and pine forests."));
        // --- Jaipur ---
        hotels.add(new Hotel("H007", "Mango Hotels Premier",  "Jaipur",          "Budget",   2200,  3.7, "WiFi | AC | Restaurant | Parking",                     "Modern budget hotel centrally located near Hawa Mahal."));
        hotels.add(new Hotel("H008", "ITC Rajputana",         "Jaipur",          "Standard", 7500,  4.4, "WiFi | Pool | Spa | Multi-cuisine | Business Centre",   "Elegant hotel blending Rajasthani heritage with modern amenities."));
        hotels.add(new Hotel("H009", "Rambagh Palace",        "Jaipur",          "Luxury",   35000, 4.9, "WiFi | Heritage Pool | Spa | Polo Ground | Buggy Rides","Former royal palace, now a Taj property — the finest in Jaipur."));
        // --- Kerala ---
        hotels.add(new Hotel("H010", "Coconut Grove Homestay","Kerala",          "Budget",   1500,  4.0, "WiFi | Home-cooked Meals | Garden | Cycling",           "Warm family-run homestay surrounded by coconut groves in Alleppey."));
        hotels.add(new Hotel("H011", "Spice Village",         "Kerala",          "Standard", 8000,  4.5, "WiFi | Pool | Ayurvedic Spa | Organic Restaurant",      "Eco-resort set in a cardamom plantation in the hills of Thekkady."));
        hotels.add(new Hotel("H012", "Kumarakom Lake Resort", "Kerala",          "Luxury",   22000, 4.8, "WiFi | Private Pool | Houseboat | Ayurveda | Infinity Pool", "Award-winning luxury resort on the shores of Vembanad Lake."));
        // --- Varanasi ---
        hotels.add(new Hotel("H013", "Hotel Alka",            "Varanasi",        "Budget",   1200,  3.4, "WiFi | Ghat View | Restaurant | Room Service",          "Simple riverside hotel with direct views of Dashashwamedh Ghat."));
        hotels.add(new Hotel("H014", "BrijRama Palace",       "Varanasi",        "Luxury",   20000, 4.7, "WiFi | Heritage Rooms | Fine Dining | Ghat View | Yoga","250-year-old palace on the ghats, now a heritage luxury hotel."));
        // --- Andaman ---
        hotels.add(new Hotel("H015", "Sea Shell Resort",      "Andaman Islands", "Budget",   3000,  3.9, "WiFi | Beach Access | Restaurant | Water Sports",       "Comfortable budget resort, 500 m from Corbyn's Cove Beach, Port Blair."));
        hotels.add(new Hotel("H016", "Munjoh Ocean Resort",   "Andaman Islands", "Standard", 9000,  4.3, "WiFi | Pool | Beach | Water Sports | Dive School",      "Beachfront resort on Havelock Island with PADI-certified dive centre."));
        hotels.add(new Hotel("H017", "Taj Exotica Andaman",   "Andaman Islands", "Luxury",   28000, 4.9, "WiFi | Private Pool | Snorkelling | Spa | Fine Dining",  "Iconic luxury resort on a secluded beach in Havelock Island."));
        // --- Leh Ladakh ---
        hotels.add(new Hotel("H018", "Stok Palace Heritage",  "Leh Ladakh",      "Standard", 7000,  4.4, "WiFi | Heritage Rooms | Restaurant | Bonfire | Garden", "Converted 19th-century royal palace with views of Stok Kangri."));
        hotels.add(new Hotel("H019", "The Grand Dragon Leh",  "Leh Ladakh",      "Luxury",   15000, 4.6, "WiFi | Heated Pool | Spa | Multi-cuisine | Rooftop Bar","Ladakh's premier luxury hotel with breathtaking Himalayan views."));
        // --- Ooty ---
        hotels.add(new Hotel("H020", "Reflections Guest House","Ooty",           "Budget",   1600,  3.8, "WiFi | Garden | Home Food | Fireplace",                 "Charming colonial-era guesthouse with beautiful garden views."));
        hotels.add(new Hotel("H021", "Savoy Hotel Ooty",      "Ooty",            "Luxury",   12000, 4.5, "WiFi | Heritage Rooms | Billiards | Restaurant | Bonfire","1829 heritage hotel set in rolling green hills, managed by Taj."));
    }

    private void initializeTransport() {
        transports = new ArrayList<>();
        // --- FLIGHTS ---
        transports.add(new Transport("TRP-F01", "Flight", "Mumbai",    "Goa",             "IndiGo 6E-342",    "06:30", "1h 10m",  4200));
        transports.add(new Transport("TRP-F02", "Flight", "Delhi",     "Manali",          "Air India AI-204", "07:00", "1h 25m",  5800));
        transports.add(new Transport("TRP-F03", "Flight", "Delhi",     "Jaipur",          "Air India AI-412", "08:15", "55 mins", 3500));
        transports.add(new Transport("TRP-F04", "Flight", "Bangalore", "Kerala",          "SpiceJet SG-234",  "09:30", "1h 05m",  4800));
        transports.add(new Transport("TRP-F05", "Flight", "Chennai",   "Andaman Islands", "IndiGo 6E-801",    "06:00", "2h 10m",  8900));
        transports.add(new Transport("TRP-F06", "Flight", "Delhi",     "Leh Ladakh",      "Air India AI-446", "06:30", "1h 20m",  7500));
        transports.add(new Transport("TRP-F07", "Flight", "Chennai",   "Ooty",            "Air India AI-553", "07:30", "55 mins", 3200));
        transports.add(new Transport("TRP-F08", "Flight", "Mumbai",    "Jaipur",          "IndiGo 6E-621",    "10:00", "1h 35m",  4500));
        // --- TRAINS ---
        transports.add(new Transport("TRP-T01", "Train",  "Mumbai",    "Goa",             "Konkan Kanya Exp", "23:00", "9h 30m",   650));
        transports.add(new Transport("TRP-T02", "Train",  "Delhi",     "Jaipur",          "Shatabdi Express", "06:05", "4h 15m",   780));
        transports.add(new Transport("TRP-T03", "Train",  "Bangalore", "Kerala",          "Kerala Express",   "19:30", "12h 00m", 1200));
        transports.add(new Transport("TRP-T04", "Train",  "Lucknow",   "Varanasi",        "Vibhuti Express",  "06:00", "3h 30m",   320));
        transports.add(new Transport("TRP-T05", "Train",  "Chennai",   "Ooty",            "Nilgiri Mtn Rail", "07:10", "5h 00m",   200));
        transports.add(new Transport("TRP-T06", "Train",  "Delhi",     "Manali",          "Kalka Mail",       "20:40", "6h (Kalka)",850));
        transports.add(new Transport("TRP-T07", "Train",  "Delhi",     "Leh Ladakh",      "Jammu Mail+Bus",   "21:30", "2 Days",  1200));
        // --- BUSES ---
        transports.add(new Transport("TRP-B01", "Bus",    "Mumbai",    "Goa",             "Neeta Volvo A/C",  "22:00", "12h 00m", 1200));
        transports.add(new Transport("TRP-B02", "Bus",    "Delhi",     "Manali",          "HRTC Volvo A/C",   "17:30", "14h 00m",  900));
        transports.add(new Transport("TRP-B03", "Bus",    "Delhi",     "Jaipur",          "RSRTC Volvo A/C",  "06:00", "5h 00m",   450));
        transports.add(new Transport("TRP-B04", "Bus",    "Lucknow",   "Varanasi",        "UP Roadways",      "05:30", "4h 00m",   200));
        transports.add(new Transport("TRP-B05", "Bus",    "Bangalore", "Ooty",            "KSRTC Volvo",      "21:30", "6h 00m",   600));
        transports.add(new Transport("TRP-B06", "Bus",    "Delhi",     "Leh Ladakh",      "HRTC Deluxe",      "05:00", "2 Days",  1500));
        transports.add(new Transport("TRP-B07", "Bus",    "Chennai",   "Ooty",            "SETC Volvo",       "22:00", "7h 00m",   550));
        // --- CABS ---
        transports.add(new Transport("TRP-C01", "Cab",    "Mumbai",    "Goa",             "Ola Outstation",   "On Demand", "11h",   5500));
        transports.add(new Transport("TRP-C02", "Cab",    "Delhi",     "Jaipur",          "Ola Outstation",   "On Demand", "5h",    2800));
        transports.add(new Transport("TRP-C03", "Cab",    "Bangalore", "Ooty",            "Ola Outstation",   "On Demand", "6h",    3200));
        transports.add(new Transport("TRP-C04", "Cab",    "Delhi",     "Manali",          "Ola Outstation",   "On Demand", "14h",   6500));
        transports.add(new Transport("TRP-C05", "Cab",    "Lucknow",   "Varanasi",        "Ola Outstation",   "On Demand", "3h 30m",2500));
    }

    // ==================== BOOKING ID GENERATORS ====================

    private String generateBookingId()          { return "TRV" + (++bookingCounter); }
    private String generateHotelBookingId()     { return "HTL" + (++hotelBookingCounter); }
    private String generateTransportBookingId() { return "TRP" + (++transportBookingCounter); }

    // ==================== CREATE BOOKINGS ====================

    public Booking createBooking(String name, String contact, TravelPackage pkg,
                                 int travellers, String date) throws InvalidBookingException {
        validateCommonFields(name, contact);
        if (travellers <= 0 || travellers > 20)
            throw new InvalidBookingException("Number of travellers must be between 1 and 20.");
        if (pkg == null)
            throw new InvalidBookingException("Please select a valid travel package.");
        if (date == null || date.trim().isEmpty())
            throw new InvalidBookingException("Travel date cannot be empty.");

        Booking b = new Booking(generateBookingId(), name.trim(), contact.trim(), pkg, travellers, date.trim());
        bookings.add(b);
        return b;
    }

    public HotelBooking createHotelBooking(String name, String contact, Hotel hotel,
                                           String checkIn, String checkOut, int nights, int guests)
                                           throws InvalidBookingException {
        validateCommonFields(name, contact);
        if (hotel == null)   throw new InvalidBookingException("Please select a hotel.");
        if (nights <= 0)     throw new InvalidBookingException("Number of nights must be at least 1.");
        if (guests <= 0)     throw new InvalidBookingException("Number of guests must be at least 1.");
        if (checkIn.trim().isEmpty())  throw new InvalidBookingException("Check-in date cannot be empty.");
        if (checkOut.trim().isEmpty()) throw new InvalidBookingException("Check-out date cannot be empty.");

        HotelBooking hb = new HotelBooking(generateHotelBookingId(), name.trim(), contact.trim(),
                                           hotel, checkIn.trim(), checkOut.trim(), nights, guests);
        hotelBookings.add(hb);
        return hb;
    }

    public TransportBooking createTransportBooking(String name, String contact, Transport transport,
                                                   String date, int passengers)
                                                   throws InvalidBookingException {
        validateCommonFields(name, contact);
        if (transport == null)  throw new InvalidBookingException("Please select a transport option.");
        if (passengers <= 0)    throw new InvalidBookingException("Number of passengers must be at least 1.");
        if (date.trim().isEmpty()) throw new InvalidBookingException("Travel date cannot be empty.");

        TransportBooking tb = new TransportBooking(generateTransportBookingId(), name.trim(), contact.trim(),
                                                   transport, date.trim(), passengers);
        transportBookings.add(tb);
        return tb;
    }

    // Shared validation for name and contact
    private void validateCommonFields(String name, String contact) throws InvalidBookingException {
        if (name == null || name.trim().isEmpty())
            throw new InvalidBookingException("Customer name cannot be empty.");
        if (contact == null || contact.trim().isEmpty())
            throw new InvalidBookingException("Contact number cannot be empty.");
        if (!contact.trim().matches("\\d{10}"))
            throw new InvalidBookingException("Contact must be a valid 10-digit mobile number.");
    }

    // ==================== CANCEL BOOKINGS ====================

    public boolean cancelBooking(String id) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(id) && b.isActive()) { b.cancel(); return true; }
        }
        return false;
    }

    public boolean cancelHotelBooking(String id) {
        for (HotelBooking hb : hotelBookings) {
            if (hb.getBookingId().equals(id) && hb.isActive()) { hb.cancel(); return true; }
        }
        return false;
    }

    public boolean cancelTransportBooking(String id) {
        for (TransportBooking tb : transportBookings) {
            if (tb.getBookingId().equals(id) && tb.isActive()) { tb.cancel(); return true; }
        }
        return false;
    }

    // ==================== SEARCH METHODS ====================

    public ArrayList<TravelPackage> searchPackages(String source, String destination) {
        ArrayList<TravelPackage> results = new ArrayList<>();
        for (TravelPackage pkg : travelPackages) {
            boolean srcMatch  = source.trim().isEmpty()      || pkg.getSourceCity().equalsIgnoreCase(source.trim());
            boolean destMatch = destination.trim().isEmpty() || pkg.getDestination().toLowerCase().contains(destination.trim().toLowerCase());
            if (srcMatch && destMatch) results.add(pkg);
        }
        return results;
    }

    public ArrayList<Hotel> searchHotels(String destination, String type) {
        ArrayList<Hotel> results = new ArrayList<>();
        for (Hotel h : hotels) {
            boolean destMatch = destination.trim().isEmpty() || h.getDestination().equalsIgnoreCase(destination.trim());
            boolean typeMatch = type.trim().isEmpty() || type.equalsIgnoreCase("All Types") || h.getType().equalsIgnoreCase(type.trim());
            if (destMatch && typeMatch) results.add(h);
        }
        return results;
    }

    public ArrayList<Transport> searchTransport(String source, String destination, String type) {
        ArrayList<Transport> results = new ArrayList<>();
        for (Transport t : transports) {
            boolean srcMatch  = source.trim().isEmpty()      || t.getSource().toLowerCase().contains(source.trim().toLowerCase());
            boolean destMatch = destination.trim().isEmpty() || t.getDestination().toLowerCase().contains(destination.trim().toLowerCase());
            boolean typeMatch = type.trim().isEmpty() || type.equalsIgnoreCase("All Types") || t.getType().equalsIgnoreCase(type.trim());
            if (srcMatch && destMatch && typeMatch) results.add(t);
        }
        return results;
    }

    public ArrayList<Destination> filterDestinations(String keyword) {
        ArrayList<Destination> results = new ArrayList<>();
        String kw = keyword.trim().toLowerCase();
        for (Destination d : destinations) {
            if (d.getName().toLowerCase().contains(kw) || d.getLocation().toLowerCase().contains(kw)) {
                results.add(d);
            }
        }
        return results;
    }

    // ==================== LOOKUP BY ID (for DataManager) ====================

    public TravelPackage getPackageById(String id) {
        for (TravelPackage p : travelPackages) if (p.getPackageId().equals(id)) return p;
        return null;
    }

    public Hotel getHotelById(String id) {
        for (Hotel h : hotels) if (h.getHotelId().equals(id)) return h;
        return null;
    }

    public Transport getTransportById(String id) {
        for (Transport t : transports) if (t.getTransportId().equals(id)) return t;
        return null;
    }

    // ==================== ADD LOADED (from file) ====================

    public void addLoadedTripBooking(Booking b) {
        bookings.add(b);
        try {
            int num = Integer.parseInt(b.getBookingId().replaceAll("[^0-9]", ""));
            if (num >= bookingCounter) bookingCounter = num + 1;
        } catch (NumberFormatException ignored) {}
    }

    public void addLoadedHotelBooking(HotelBooking hb) {
        hotelBookings.add(hb);
        try {
            int num = Integer.parseInt(hb.getBookingId().replaceAll("[^0-9]", ""));
            if (num >= hotelBookingCounter) hotelBookingCounter = num + 1;
        } catch (NumberFormatException ignored) {}
    }

    public void addLoadedTransportBooking(TransportBooking tb) {
        transportBookings.add(tb);
        try {
            int num = Integer.parseInt(tb.getBookingId().replaceAll("[^0-9]", ""));
            if (num >= transportBookingCounter) transportBookingCounter = num + 1;
        } catch (NumberFormatException ignored) {}
    }

    // ==================== GETTERS ====================

    public ArrayList<Booking>          getAllBookings()          { return bookings; }
    public ArrayList<HotelBooking>     getAllHotelBookings()     { return hotelBookings; }
    public ArrayList<TransportBooking> getAllTransportBookings() { return transportBookings; }
    public ArrayList<TravelPackage>    getAllPackages()          { return travelPackages; }
    public ArrayList<Destination>      getAllDestinations()      { return destinations; }
    public ArrayList<Hotel>            getAllHotels()            { return hotels; }
    public ArrayList<Transport>        getAllTransports()        { return transports; }

    // Unique destination names list (for hotel filter dropdown)
    public String[] getDestinationNames() {
        String[] names = new String[destinations.size() + 1];
        names[0] = "All Destinations";
        for (int i = 0; i < destinations.size(); i++) names[i + 1] = destinations.get(i).getName();
        return names;
    }
}
