import java.util.*;

// Room class
class Room {
    private int roomNumber;
    private String type;  // e.g., Single, Double, Suite
    private boolean isAvailable;
    private double pricePerNight;

    public Room(int roomNumber, String type, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getType() {
        return type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean status) {
        this.isAvailable = status;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + type + ") - $" + pricePerNight + " per night - " + (isAvailable ? "Available" : "Booked");
    }
}

// Customer class
class Customer {
    private String name;
    private String phone;

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

// Booking class
class Booking {
    private Customer customer;
    private Room room;
    private Date checkInDate;
    private Date checkOutDate;

    public Booking(Customer customer, Room room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        room.setAvailable(false);
    }

    public double calculateBill() {
        long diff = checkOutDate.getTime() - checkInDate.getTime();
        long nights = diff / (1000 * 60 * 60 * 24);
        if (nights == 0) nights = 1;  // Minimum 1 night
        return nights * room.getPricePerNight();
    }

    @Override
    public String toString() {
        return "Booking for " + customer.getName() + " in " + room + ", from " + checkInDate + " to " + checkOutDate;
    }

    public void checkOut() {
        room.setAvailable(true);
    }
}

// Hotel class
class Hotel {
    private List<Room> rooms;
    private List<Booking> bookings;

    public Hotel() {
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        initializeRooms();
    }

    private void initializeRooms() {
        rooms.add(new Room(101, "Single", 50));
        rooms.add(new Room(102, "Double", 80));
        rooms.add(new Room(201, "Suite", 150));
        // Add more rooms as needed
    }

    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable()) available.add(r);
        }
        return available;
    }

    public boolean bookRoom(Customer customer, int roomNumber, Date checkIn, Date checkOut) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber && r.isAvailable()) {
                Booking booking = new Booking(customer, r, checkIn, checkOut);
                bookings.add(booking);
                System.out.println("Booking successful! Total bill: $" + booking.calculateBill());
                return true;
            }
        }
        System.out.println("Room not available or does not exist.");
        return false;
    }

    public void showBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No current bookings.");
        } else {
            for (Booking b : bookings) {
                System.out.println(b);
            }
        }
    }
}

// Main class
public class HotelManagementSystem {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Hotel Management System");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Show available rooms");
            System.out.println("2. Book a room");
            System.out.println("3. Show all bookings");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    List<Room> availableRooms = hotel.getAvailableRooms();
                    if (availableRooms.isEmpty()) {
                        System.out.println("No rooms available at the moment.");
                    } else {
                        System.out.println("Available Rooms:");
                        for (Room r : availableRooms) {
                            System.out.println(r);
                        }
                    }
                    break;
                case 2:
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter your phone number: ");
                    String phone = scanner.nextLine();
                    Customer customer = new Customer(name, phone);

                    System.out.print("Enter room number to book: ");
                    int roomNum = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter check-in date (yyyy-mm-dd): ");
                    String checkInStr = scanner.nextLine();
                    System.out.print("Enter check-out date (yyyy-mm-dd): ");
                    String checkOutStr = scanner.nextLine();

                    try {
                        Date checkInDate = new GregorianCalendar(
                                Integer.parseInt(checkInStr.substring(0,4)),
                                Integer.parseInt(checkInStr.substring(5,7)) - 1,
                                Integer.parseInt(checkInStr.substring(8,10))
                        ).getTime();

                        Date checkOutDate = new GregorianCalendar(
                                Integer.parseInt(checkOutStr.substring(0,4)),
                                Integer.parseInt(checkOutStr.substring(5,7)) - 1,
                                Integer.parseInt(checkOutStr.substring(8,10))
                        ).getTime();

                        if (checkOutDate.before(checkInDate)) {
                            System.out.println("Check-out date must be after check-in date.");
                        } else {
                            hotel.bookRoom(customer, roomNum, checkInDate, checkOutDate);
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                    }

                    break;
                case 3:
                    hotel.showBookings();
                    break;
                case 4:
                    System.out.println("Thank you for using the Hotel Management System.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
