import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class HotelBookingSystem {
    private ArrayList<String> guestNames = new ArrayList<>();
    private ArrayList<String> roomTypes = new ArrayList<>();
    private ArrayList<Integer> nightsList = new ArrayList<>();
    private ArrayList<Integer> bookingNumbers = new ArrayList<>();
    private Random random = new Random();

    public void addBooking() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ange gästens för- och efternamn: ");
        String guestName = scanner.nextLine();

        String roomType = chooseRoomType(scanner);

        System.out.print("Ange antal nätter för besök: ");
        int nights = 0;
        try {
            nights = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Fel inmatning för antalet nätter. Försök igen.");
            scanner.nextLine();
            return;
        }

        int bookingNumber = createUniqueBookingNumber();

        guestNames.add(guestName);
        roomTypes.add(roomType);
        nightsList.add(nights);
        bookingNumbers.add(bookingNumber);

        System.out.println("Bokning tillagd. Bokningsnummer: " + bookingNumber);

        System.out.print("Vill du spara bokningen till fil? (ja/nej): ");
        scanner.nextLine(); 
        if (scanner.nextLine().trim().equalsIgnoreCase("ja")) {
            saveBookingsToFile();
        } else {
            System.out.println("Bokningen sparades inte till fil.");
        }
    }

    private String chooseRoomType(Scanner scanner) {
        System.out.println("Välj en rumstyp:");
        System.out.println("1. Singelrum");
        System.out.println("2. Dubbelrum");
        System.out.println("3. Familjerum");

        while (true) {
            System.out.print("Ditt val: ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Rensa scanner
            } catch (InputMismatchException e) {
                System.out.println("Ogiltig inmatning. Ange ett nummer mellan 1 och 3.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1: return "Singelrum";
                case 2: return "Dubbelrum";
                case 3: return "Familjerum";
                default: System.out.println("Ogiltigt val. Försök igen.");
            }
        }
    }

    public void showAllBookings() {
        if (guestNames.isEmpty()) {
            System.out.println("Inga bokning hittades.");
            return;
        }

        for (int i = 0; i < guestNames.size(); i++) {
            System.out.println("Bokningsnummer: " + bookingNumbers.get(i) +
                    ", Gäst: " + guestNames.get(i) +
                    ", Rum: " + roomTypes.get(i) +
                    ", Nätter: " + nightsList.get(i));
        }
    }

    public void searchBookingByNumber() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ange bokningsnummer: ");
        int bookingNumber;
        try {
            bookingNumber = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Fel inmatning. Ange ett giltigt bokningsnummer.");
            return;
        }

        for (int i = 0; i < bookingNumbers.size(); i++) {
            if (bookingNumbers.get(i) == bookingNumber) {
                System.out.println("Bokning hittad: ");
                System.out.println("Gäst: " + guestNames.get(i) +
                        ", Rum: " + roomTypes.get(i) +
                        ", Nätter: " + nightsList.get(i));
                return;
            }
        }
        System.out.println("Ingen bokning hittades med angivet nummer.");
    }

    public void searchBookingByName() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ange gästens namn att söka efter: ");
        String guestName = scanner.nextLine();

        for (int i = 0; i < guestNames.size(); i++) {
            if (guestNames.get(i).equalsIgnoreCase(guestName)) {
                System.out.println("Bokning hittad:");
                System.out.println("Bokningsnummer: " + bookingNumbers.get(i) +
                        ", Rum: " + roomTypes.get(i) +
                        ", Nätter: " + nightsList.get(i));
                return;
            }
        }
        System.out.println("Ingen bokning hittades för angivet namn.");
    }

    private int createUniqueBookingNumber() {
        int bookingNumber;
        do {
            bookingNumber = random.nextInt(1000) + 1;
        } while (bookingNumbers.contains(bookingNumber));
        return bookingNumber;
    }

    public void cancelBooking() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ange bokningsnummer att avboka: ");
        int bookingNumber;
        try {
            bookingNumber = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Fel inmatning. Ange ett giltigt nummer.");
            return;
        }

        for (int i = 0; i < bookingNumbers.size(); i++) {
            if (bookingNumbers.get(i) == bookingNumber) {
                guestNames.remove(i);
                roomTypes.remove(i);
                nightsList.remove(i);
                bookingNumbers.remove(i);
                System.out.println("Bokning med nummer " + bookingNumber + " avbokad.");
                saveBookingsToFile();
                return;
            }
        }
        System.out.println("Ingen bokning hittades med angivet nummer.");
    }

    public void saveBookingsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookings.txt"))) {
            for (int i = 0; i < guestNames.size(); i++) {
                writer.write(bookingNumbers.get(i) + ";" + guestNames.get(i) + ";" + roomTypes.get(i) + ";" + nightsList.get(i));
                writer.newLine();
            }
            System.out.println("Bokningar sparade till fil.");
        } catch (IOException e) {
            System.out.println("Ett fel inträffade när bokningar skulle sparas: " + e.getMessage());
        }
    }

    public void loadBookingsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) {
            guestNames.clear();
            roomTypes.clear();
            nightsList.clear();
            bookingNumbers.clear();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                bookingNumbers.add(Integer.parseInt(data[0]));
                guestNames.add(data[1]);
                roomTypes.add(data[2]);
                nightsList.add(Integer.parseInt(data[3]));
            }
            System.out.println("Bokningar lästa från fil.");
            showAllBookings();
        } catch (IOException e) {
            System.out.println("Ett fel inträffade när bokningar skulle läsas: " + e.getMessage());
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nVälj ett alternativ:");
            System.out.println("1. Ny bokning");
            System.out.println("2. Visa alla bokningar");
            System.out.println("3. Sök bokning med bokningsnummer");
            System.out.println("4. Sök bokning med namn");
            System.out.println("5. Avboka en bokning");
            System.out.println("6. Spara bokningar till fil");
            System.out.println("7. Läs bokningar från fil");
            System.out.println("8. Avsluta");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Fel val, försök igen.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1: addBooking(); break;
                case 2: showAllBookings(); break;
                case 3: searchBookingByNumber(); break;
                case 4: searchBookingByName(); break;
                case 5: cancelBooking(); break;
                case 6: saveBookingsToFile(); break;
                case 7: loadBookingsFromFile(); break;
                case 8:
                    System.out.println("Program avslutas.");
                    return;
                default:
                    System.out.println("Ogiltigt val. Försök igen.");
            }
        }
    }

    public static void main(String[] args) {
        HotelBookingSystem system = new HotelBookingSystem();
        system.start();
    }
}
