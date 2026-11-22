// TravelPackage.java - Represents a travel package (inherits from Destination concept, uses OOP)
public class TravelPackage {

    // Private fields - Encapsulation
    private String packageId;
    private String packageName;
    private String destination;
    private String sourceCity;
    private int durationDays;
    private double pricePerPerson;
    private String description;
    private String includes; // e.g., "Hotel + Flights + Sightseeing"

    // Constructor
    public TravelPackage(String packageId, String packageName, String destination,
                         String sourceCity, int durationDays, double pricePerPerson,
                         String description, String includes) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.destination = destination;
        this.sourceCity = sourceCity;
        this.durationDays = durationDays;
        this.pricePerPerson = pricePerPerson;
        this.description = description;
        this.includes = includes;
    }

    // Getters
    public String getPackageId() { return packageId; }
    public String getPackageName() { return packageName; }
    public String getDestination() { return destination; }
    public String getSourceCity() { return sourceCity; }
    public int getDurationDays() { return durationDays; }
    public double getPricePerPerson() { return pricePerPerson; }
    public String getDescription() { return description; }
    public String getIncludes() { return includes; }

    // Calculate total price for given number of travellers
    public double getTotalPrice(int travellers) {
        return pricePerPerson * travellers;
    }

    // @Override toString
    @Override
    public String toString() {
        return packageName + " → " + destination + " | " + durationDays + "N/" + (durationDays + 1)
                + "D | ₹" + String.format("%.0f", pricePerPerson) + "/person";
    }
}
