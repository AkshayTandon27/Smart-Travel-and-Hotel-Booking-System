# TripVista – Smart Travel Management System

A Java Swing desktop application for travel exploration and booking.

## How to Run (VS Code)

1. Install **JDK 11 
   *(The machine currently only has JRE; a JDK is required for compilation.)*
2. Install the **Extension Pack for Java** from VS Code Marketplace.
3. Open this folder in VS Code: `File → Open Folder → OOPS_JAVA_PROJECT`
4. Press **F5** or click the ▷ Run button — VS Code compiles and launches the app.

## Manual Compilation (Terminal)

```bash
# From the OOPS_JAVA_PROJECT folder:
mkdir out
javac -encoding UTF-8 -d out src\*.java
java -cp out Main
```

## Project Structure

```
OOPS_JAVA_PROJECT/
├── src/
│   ├── Main.java                  # Entry point
│   ├── MainFrame.java             # Main window, navigation
│   ├── HomePanel.java             # Dashboard / Home screen
│   ├── ExplorePanel.java          # Explore destinations
│   ├── SearchPanel.java           # Search & view packages
│   ├── BookingPanel.java          # Booking form
│   ├── MyBookingsPanel.java       # View & cancel bookings (JTable)
│   ├── BookingManager.java        # Data + business logic
│   ├── Booking.java               # Booking model
│   ├── TravelPackage.java         # Package model
│   ├── Destination.java           # Destination model
│   ├── UIConstants.java           # Colours, fonts, helpers
│   └── InvalidBookingException.java  # Custom exception
├── out/                           # Compiled .class files (auto-created)
└── .vscode/
    ├── settings.json
    └── launch.json
```
