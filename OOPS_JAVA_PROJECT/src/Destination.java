// Destination.java - Represents a travel destination (Encapsulation + OOP)
public class Destination {

    // Private fields - Encapsulation
    private String name;
    private String location;
    private String description;
    private double basePrice;
    private String emoji; // Simple visual identifier instead of image file

    // Constructor
    public Destination(String name, String location, String description, double basePrice, String emoji) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.basePrice = basePrice;
        this.emoji = emoji;
    }

    // Getters - Encapsulation
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public double getBasePrice() { return basePrice; }
    public String getEmoji() { return emoji; }

    // @Override toString for easy display
    @Override
    public String toString() {
        return emoji + " " + name + " (" + location + ") - From ₹" + String.format("%.0f", basePrice);
    }
}
