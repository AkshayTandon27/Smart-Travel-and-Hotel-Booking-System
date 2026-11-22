// Hotel.java - Represents a hotel (OOP + Encapsulation)
public class Hotel {

    private String hotelId;
    private String name;
    private String destination;
    private String type;          // "Budget", "Standard", "Luxury"
    private double pricePerNight;
    private double rating;        // 1.0 to 5.0
    private String amenities;     // e.g. "WiFi | Pool | AC | Breakfast"
    private String description;

    public Hotel(String hotelId, String name, String destination, String type,
                 double pricePerNight, double rating, String amenities, String description) {
        this.hotelId = hotelId;
        this.name = name;
        this.destination = destination;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.rating = rating;
        this.amenities = amenities;
        this.description = description;
    }

    // Getters
    public String getHotelId()       { return hotelId; }
    public String getName()          { return name; }
    public String getDestination()   { return destination; }
    public String getType()          { return type; }
    public double getPricePerNight() { return pricePerNight; }
    public double getRating()        { return rating; }
    public String getAmenities()     { return amenities; }
    public String getDescription()   { return description; }

    // Returns star string like "★★★★☆"
    public String getStarString() {
        int full = (int) rating;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < full; i++) sb.append("★");
        for (int i = full; i < 5; i++) sb.append("☆");
        return sb.toString();
    }

    // Returns badge colour hint based on type
    public String getTypeBadge() {
        switch (type) {
            case "Budget":   return "🟢 Budget";
            case "Standard": return "🔵 Standard";
            case "Luxury":   return "🟡 Luxury";
            default:         return type;
        }
    }

    @Override
    public String toString() {
        return name + " (" + destination + ") | " + type + " | ₹" + String.format("%.0f", pricePerNight) + "/night";
    }
}
