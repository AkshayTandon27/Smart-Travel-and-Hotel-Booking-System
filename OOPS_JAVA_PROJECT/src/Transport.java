// Transport.java - Represents a transport option (Flight / Train / Bus / Cab)
public class Transport {

    private String transportId;
    private String type;          // "Flight", "Train", "Bus", "Cab"
    private String source;
    private String destination;
    private String provider;      // e.g. "IndiGo 6E-342", "Shatabdi Express"
    private String departureTime; // e.g. "06:30"
    private String duration;      // e.g. "1h 10m"
    private double pricePerPerson;

    public Transport(String transportId, String type, String source, String destination,
                     String provider, String departureTime, String duration, double pricePerPerson) {
        this.transportId   = transportId;
        this.type          = type;
        this.source        = source;
        this.destination   = destination;
        this.provider      = provider;
        this.departureTime = departureTime;
        this.duration      = duration;
        this.pricePerPerson = pricePerPerson;
    }

    // Getters
    public String getTransportId()     { return transportId; }
    public String getType()            { return type; }
    public String getSource()          { return source; }
    public String getDestination()     { return destination; }
    public String getProvider()        { return provider; }
    public String getDepartureTime()   { return departureTime; }
    public String getDuration()        { return duration; }
    public double getPricePerPerson()  { return pricePerPerson; }

    // Calculate total fare
    public double getTotalPrice(int passengers) { return pricePerPerson * passengers; }

    // Emoji based on transport type
    public String getTypeEmoji() {
        switch (type) {
            case "Flight": return "✈";
            case "Train":  return "🚆";
            case "Bus":    return "🚌";
            case "Cab":    return "🚕";
            default:       return "🚗";
        }
    }

    @Override
    public String toString() {
        return getTypeEmoji() + " " + type + " | " + provider + " | "
                + source + " → " + destination + " | " + departureTime
                + " | " + duration + " | ₹" + String.format("%.0f", pricePerPerson);
    }
}
