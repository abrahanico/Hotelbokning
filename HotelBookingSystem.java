import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class HotelBookingSystem {
    private ArrayList<Booking> bookings = new ArrayList<>();
    private Random random = new Random();

    private class Booking {
        String guestName;
        String roomType;
        int nights;
        int bookingNumber;

        Booking(String guestName, String roomType, int nights, int bookingNumber) {
            this.guestName = guestName;
            this.roomType = roomType;
            this.nights = nights;
            this.bookingNumber = bookingNumber;
        }

        @Override
        public String toString() {
            return "Bokningsnummer: " + bookingNumber + ", Gäst: " + guestName + ", Rumstyp: " + roomType + ", Nätter: " + nights;
        }
    }

    public void addBooking() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ange gästens namn: ");
        String guestName = scanner.nextLine();

        
        String roomType = selectRoomType(scanner);

        System.out.print("Ange antal nätter: ");
        int nights = scanner.nextInt();
        
        int bookingNumber = generateUniqueBookingNumber();
        
        bookings.add(new Booking(guestName, roomType, nights, bookingNumber));
        System.out.println("Bokning tillagd med nummer: " + bookingNumber);
    }

    private String selectRoomType(Scanner scanner) {
        System.out.println("Välj rumstyp:");
        System.out.println("1. Singel");
        System.out.println("2. Dubbel");
        System.out.println("3. Familjerum");

        int choice = 0;
        while (true) {
            System.out.print("Ditt val: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
                switch (choice) {
                    case 1 -> { return "Singel"; }
                    case 2 -> { return "Dubbel"; }
                    case 3 -> { return "Familjerum"; }
                    default -> System.out.println("Ogiltigt val, försök igen.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ogiltig inmatning. Ange ett nummer mellan 1 och 3.");
                scanner.nextLine(); 
            }
        }
    }

    public void showAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("Inga bokningar hittades.");
        } else {
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        }
    }

    public void searchBookingByNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ange bokningsnummer: ");
        int bookingNumber = scanner.nextInt();
        
        for (Booking booking : bookings) {
            if (booking.bookingNumber == bookingNumber) {
                System.out.println(booking);
                return;
            }
        }
        System.out.println("Bokning hittades inte.");
    }

    public void searchBookingByName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ange gästens namn: ");
        String guestName = scanner.nextLine();
        
        for (Booking booking : bookings) {
            if (booking.guestName.equalsIgnoreCase(guestName)) {
                System.out.println(booking);
                return;
            }
        }
        System.out.println("Bokning hittades inte.");
    }

    private int generateUniqueBookingNumber() {
        int bookingNumber;
        boolean unique;
        do {
            bookingNumber = random.nextInt(1000) + 1;
            unique = true;
            for (Booking booking : bookings) {
                if (booking.bookingNumber == bookingNumber) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);
        return bookingNumber;
    }

    
    public void cancelBooking() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ange bokningsnummer för avbokning: ");
        int bookingNumber = scanner.nextInt();
        
        boolean found = false;
        
       
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).bookingNumber == bookingNumber) {
                bookings.remove(i); 
                System.out.println("Bokning med nummer " + bookingNumber + " har avbokats.");
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("Ingen bokning hittades med bokningsnummer: " + bookingNumber);
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nVälj ett alternativ:");
            System.out.println("1. Lägg till en bokning");
            System.out.println("2. Visa alla bokningar");
            System.out.println("3. Sök via bokningsnummer");
            System.out.println("4. Sök via gästens namn");
            System.out.println("5. Avboka en bokning"); 
            System.out.println("6. Avsluta");
            int choice = scanner.nextInt();
            scanner.nextLine();  
            
            switch (choice) {
                case 1 -> addBooking();
                case 2 -> showAllBookings();
                case 3 -> searchBookingByNumber();
                case 4 -> searchBookingByName();
                case 5 -> cancelBooking(); 
                case 6 -> {
                    System.out.println("Programmet avslutas.");
                    return;
                }
                default -> System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }

    public static void main(String[] args) {
        new HotelBookingSystem().start();
    }
}
