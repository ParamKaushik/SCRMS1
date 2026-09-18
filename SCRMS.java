import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/*
 * Smart Campus Resource Management System (SCRMS)
 * Single-file Java implementation
 *
 * Features:
 * - Student registration and login
 * - Administrator login
 * - Resource management
 * - Resource search
 * - Availability checking
 * - Booking and cancellation
 * - Booking conflict detection
 * - Basic usage reports
 * - Persistent file-based data storage
 */

public class SCRMS {

    // ============================================================
    // CONSTANTS
    // ============================================================

    static final String DATA_FILE = "scrms_data.dat";

    static final Scanner scanner = new Scanner(System.in);

    static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    static DataStore data;

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(String[] args) {

        data = loadData();

        seedAdminAndResources();

        System.out.println();
        System.out.println("==================================================");
        System.out.println("       SMART CAMPUS RESOURCE MANAGEMENT");
        System.out.println("==================================================");

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("--------------- MAIN MENU ----------------");
            System.out.println("1. Register as Student");
            System.out.println("2. Login");
            System.out.println("3. View Available Resources");
            System.out.println("4. Exit");
            System.out.println("-------------------------------------------");

            int choice = readInt("Enter your choice: ", 1, 4);

            switch (choice) {

                case 1:
                    registerStudent();
                    break;

                case 2:
                    login();
                    break;

                case 3:
                    viewResources();
                    break;

                case 4:
                    saveData();
                    System.out.println("\nThank you for using SCRMS!");
                    running = false;
                    break;
            }
        }
    }

    // ============================================================
    // DATA INITIALIZATION
    // ============================================================

    static void seedAdminAndResources() {

        // Create default administrator if one doesn't exist
        boolean adminExists = false;

        for (User u : data.users) {
            if (u.role.equals("ADMIN")) {
                adminExists = true;
                break;
            }
        }

        if (!adminExists) {

            User admin = new User(
                    data.nextUserId++,
                    "System Administrator",
                    "admin@scrms.com",
                    "admin123",
                    "ADMIN"
            );

            data.users.add(admin);
        }

        // Add sample resources if database is empty
        if (data.resources.isEmpty()) {

            data.resources.add(new Resource(
                    data.nextResourceId++,
                    "LAB-101",
                    "Computer Laboratory",
                    "Laboratory Block A",
                    40,
                    "AVAILABLE"
            ));

            data.resources.add(new Resource(
                    data.nextResourceId++,
                    "LAB-102",
                    "Computer Laboratory",
                    "Laboratory Block A",
                    35,
                    "AVAILABLE"
            ));

            data.resources.add(new Resource(
                    data.nextResourceId++,
                    "ROOM-204",
                    "Classroom",
                    "Academic Block B",
                    60,
                    "AVAILABLE"
            ));

            data.resources.add(new Resource(
                    data.nextResourceId++,
                    "STUDY-01",
                    "Study Room",
                    "Library",
                    10,
                    "AVAILABLE"
            ));

            data.resources.add(new Resource(
                    data.nextResourceId++,
                    "SEMINAR-01",
                    "Seminar Hall",
                    "Main Block",
                    150,
                    "AVAILABLE"
            ));

            saveData();
        }
    }

    // ============================================================
    // STUDENT REGISTRATION
    // ============================================================

    static void registerStudent() {

        System.out.println("\n========== STUDENT REGISTRATION ==========");

        String name = readNonEmpty("Enter your name: ");

        String email;

        while (true) {

            email = readNonEmpty("Enter email: ").toLowerCase();

            if (!email.contains("@") || !email.contains(".")) {
                System.out.println("Invalid email format.");
                continue;
            }

            boolean duplicate = false;

            for (User u : data.users) {
                if (u.email.equalsIgnoreCase(email)) {
                    duplicate = true;
                    break;
                }
            }

            if (duplicate) {
                System.out.println("An account with this email already exists.");
            } else {
                break;
            }
        }

        String password = readNonEmpty("Create password: ");

        User student = new User(
                data.nextUserId++,
                name,
                email,
                password,
                "STUDENT"
        );

        data.users.add(student);
        saveData();

        System.out.println("\nRegistration successful!");
        System.out.println("You can now login using your email and password.");
    }

    // ============================================================
    // LOGIN
    // ============================================================

    static void login() {

        System.out.println("\n================ LOGIN =================");

        String email = readNonEmpty("Email: ").toLowerCase();
        String password = readNonEmpty("Password: ");

        User loggedIn = null;

        for (User u : data.users) {

            if (u.email.equalsIgnoreCase(email)
                    && u.password.equals(password)) {

                loggedIn = u;
                break;
            }
        }

        if (loggedIn == null) {

            System.out.println("\nInvalid email or password.");
            return;
        }

        System.out.println("\nLogin successful!");
        System.out.println("Welcome, " + loggedIn.name + "!");

        if (loggedIn.role.equals("ADMIN")) {
            adminMenu(loggedIn);
        } else {
            studentMenu(loggedIn);
        }
    }

    // ============================================================
    // STUDENT MENU
    // ============================================================

    static void studentMenu(User student) {

        boolean loggedIn = true;

        while (loggedIn) {

            System.out.println();
            System.out.println("==========================================");
            System.out.println("             STUDENT MENU");
            System.out.println("==========================================");
            System.out.println("1. View All Resources");
            System.out.println("2. Search Resources");
            System.out.println("3. Check Resource Availability");
            System.out.println("4. Book a Resource");
            System.out.println("5. View My Bookings");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Logout");
            System.out.println("------------------------------------------");

            int choice = readInt("Enter your choice: ", 1, 7);

            switch (choice) {

                case 1:
                    viewResources();
                    break;

                case 2:
                    searchResources();
                    break;

                case 3:
                    checkAvailability();
                    break;

                case 4:
                    createBooking(student);
                    break;

                case 5:
                    viewMyBookings(student);
                    break;

                case 6:
                    cancelBooking(student);
                    break;

                case 7:
                    loggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;
            }
        }
    }

    // ============================================================
    // RESOURCE DISPLAY
    // ============================================================

    static void viewResources() {

        System.out.println("\n================ RESOURCES ================");

        if (data.resources.isEmpty()) {
            System.out.println("No resources found.");
            return;
        }

        printResourceHeader();

        for (Resource r : data.resources) {
            if (!r.status.equals("INACTIVE")) {
                printResource(r);
            }
        }
    }

    static void printResourceHeader() {

        System.out.printf(
                "%-5s %-12s %-25s %-22s %-10s %-12s%n",
                "ID",
                "CODE",
                "TYPE",
                "LOCATION",
                "CAPACITY",
                "STATUS"
        );

        System.out.println(
                "--------------------------------------------------------------------------------"
        );
    }

    static void printResource(Resource r) {

        System.out.printf(
                "%-5d %-12s %-25s %-22s %-10d %-12s%n",
                r.id,
                r.code,
                r.type,
                r.location,
                r.capacity,
                r.status
        );
    }

    // ============================================================
    // SEARCH RESOURCES
    // ============================================================

    static void searchResources() {

        System.out.println("\n============== SEARCH RESOURCES ==============");

        System.out.println("1. Search by Type");
        System.out.println("2. Search by Location");
        System.out.println("3. Search by Minimum Capacity");
        System.out.println("4. Combined Search");

        int choice = readInt("Choose search type: ", 1, 4);

        boolean found = false;

        printResourceHeader();

        for (Resource r : data.resources) {

            if (r.status.equals("INACTIVE")) {
                continue;
            }

            boolean matches = false;

            if (choice == 1) {

                String type = readNonEmpty("Enter resource type: ");

                matches = r.type.toLowerCase()
                        .contains(type.toLowerCase());

            } else if (choice == 2) {

                String location = readNonEmpty("Enter location: ");

                matches = r.location.toLowerCase()
                        .contains(location.toLowerCase());

            } else if (choice == 3) {

                int capacity =
                        readInt("Enter minimum capacity: ", 1, 10000);

                matches = r.capacity >= capacity;

            } else {

                String type = readNonEmpty("Enter type: ");
                String location = readNonEmpty("Enter location: ");
                int capacity =
                        readInt("Enter minimum capacity: ", 1, 10000);

                matches =
                        r.type.toLowerCase().contains(type.toLowerCase())
                        && r.location.toLowerCase()
                        .contains(location.toLowerCase())
                        && r.capacity >= capacity;
            }

            if (matches) {
                printResource(r);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching resources found.");
        }
    }

    // ============================================================
    // AVAILABILITY
    // ============================================================

    static void checkAvailability() {

        System.out.println("\n=========== CHECK AVAILABILITY ===========");

        viewResources();

        int resourceId =
                readInt("Enter resource ID: ", 1, 100000);

        Resource resource = findResource(resourceId);

        if (resource == null || resource.status.equals("INACTIVE")) {

            System.out.println("Resource not found.");
            return;
        }

        if (resource.status.equals("MAINTENANCE")) {

            System.out.println("This resource is currently under maintenance.");
            return;
        }

        LocalDate date = readDate("Enter date (YYYY-MM-DD): ");
        LocalTime start = readTime("Enter start time (HH:MM): ");
        LocalTime end = readTime("Enter end time (HH:MM): ");

        if (!start.isBefore(end)) {

            System.out.println(
                    "Invalid time range. End time must be after start time."
            );

            return;
        }

        boolean conflict =
                hasBookingConflict(resource.id, date, start, end, -1);

        if (conflict) {

            System.out.println(
                    "\nResource is NOT AVAILABLE during this time."
            );

        } else {

            System.out.println(
                    "\nResource is AVAILABLE during this time."
            );
        }
    }

    // ============================================================
    // CREATE BOOKING
    // ============================================================

    static void createBooking(User student) {

        System.out.println("\n============== CREATE BOOKING ==============");

        viewResources();

        int resourceId =
                readInt("Enter resource ID: ", 1, 100000);

        Resource resource = findResource(resourceId);

        if (resource == null || resource.status.equals("INACTIVE")) {

            System.out.println("Resource not found.");
            return;
        }

        if (resource.status.equals("MAINTENANCE")) {

            System.out.println(
                    "This resource is currently under maintenance."
            );

            return;
        }

        LocalDate date =
                readDate("Enter booking date (YYYY-MM-DD): ");

        LocalTime start =
                readTime("Enter start time (HH:MM): ");

        LocalTime end =
                readTime("Enter end time (HH:MM): ");

        if (!start.isBefore(end)) {

            System.out.println(
                    "Invalid time range. End time must be after start time."
            );

            return;
        }

        if (date.isBefore(LocalDate.now())) {

            System.out.println(
                    "Booking date cannot be in the past."
            );

            return;
        }

        // Check resource conflict
        if (hasBookingConflict(
                resource.id,
                date,
                start,
                end,
                -1)) {

            System.out.println();
            System.out.println("❌ BOOKING REJECTED");
            System.out.println(
                    "The resource is already booked during this time."
            );

            return;
        }

        // Check whether this student already has overlapping booking
        if (hasUserConflict(
                student.id,
                date,
                start,
                end)) {

            System.out.println();
            System.out.println("❌ BOOKING REJECTED");
            System.out.println(
                    "You already have another booking during this time."
            );

            return;
        }

        Booking booking = new Booking(
                data.nextBookingId++,
                student.id,
                resource.id,
                date,
                start,
                end,
                "ACTIVE"
        );

        data.bookings.add(booking);
        saveData();

        System.out.println();
        System.out.println("✅ BOOKING SUCCESSFUL!");
        System.out.println("------------------------------------------");
        System.out.println("Booking ID : " + booking.id);
        System.out.println("Resource   : " + resource.code);
        System.out.println("Date       : " + date);
        System.out.println("Time       : "
                + start.format(TIME_FORMAT)
                + " - "
                + end.format(TIME_FORMAT));
        System.out.println("Status     : ACTIVE");
    }

    // ============================================================
    // CONFLICT DETECTION
    // ============================================================

    /*
     * Two bookings overlap when:
     *
     * requestedStart < existingEnd
     * AND
     * requestedEnd > existingStart
     *
     * Example:
     * 10:00-12:00 and 11:00-13:00 = CONFLICT
     * 10:00-12:00 and 12:00-14:00 = NO CONFLICT
     */

    static boolean hasBookingConflict(
            int resourceId,
            LocalDate date,
            LocalTime requestedStart,
            LocalTime requestedEnd,
            int ignoredBookingId) {

        for (Booking b : data.bookings) {

            if (b.id == ignoredBookingId) {
                continue;
            }

            if (b.resourceId != resourceId) {
                continue;
            }

            if (!b.date.equals(date)) {
                continue;
            }

            if (!b.status.equals("ACTIVE")) {
                continue;
            }

            boolean overlap =
                    requestedStart.isBefore(b.endTime)
                    && requestedEnd.isAfter(b.startTime);

            if (overlap) {
                return true;
            }
        }

        return false;
    }

    static boolean hasUserConflict(
            int userId,
            LocalDate date,
            LocalTime requestedStart,
            LocalTime requestedEnd) {

        for (Booking b : data.bookings) {

            if (b.userId != userId) {
                continue;
            }

            if (!b.date.equals(date)) {
                continue;
            }

            if (!b.status.equals("ACTIVE")) {
                continue;
            }

            boolean overlap =
                    requestedStart.isBefore(b.endTime)
                    && requestedEnd.isAfter(b.startTime);

            if (overlap) {
                return true;
            }
        }

        return false;
    }

    // ============================================================
    // VIEW MY BOOKINGS
    // ============================================================

    static void viewMyBookings(User student) {

        System.out.println("\n================ MY BOOKINGS ================");

        boolean found = false;

        for (Booking b : data.bookings) {

            if (b.userId == student.id) {

                Resource resource = findResource(b.resourceId);

                System.out.println("--------------------------------------------");
                System.out.println("Booking ID : " + b.id);
                System.out.println(
                        "Resource   : "
                        + (resource == null ? "Unknown" : resource.code)
                );
                System.out.println("Date       : " + b.date);
                System.out.println(
                        "Time       : "
                        + b.startTime.format(TIME_FORMAT)
                        + " - "
                        + b.endTime.format(TIME_FORMAT)
                );
                System.out.println("Status     : " + b.status);

                found = true;
            }
        }

        if (!found) {
            System.out.println("You have no bookings.");
        }
    }

    // ============================================================
    // CANCEL BOOKING
    // ============================================================

    static void cancelBooking(User student) {

        System.out.println("\n============== CANCEL BOOKING ==============");

        viewMyBookings(student);

        int bookingId =
                readInt("Enter booking ID to cancel: ", 1, 100000);

        Booking booking = null;

        for (Booking b : data.bookings) {

            if (b.id == bookingId && b.userId == student.id) {
                booking = b;
                break;
            }
        }

        if (booking == null) {

            System.out.println("Booking not found.");
            return;
        }

        if (!booking.status.equals("ACTIVE")) {

            System.out.println(
                    "This booking cannot be cancelled because its status is "
                    + booking.status
            );

            return;
        }

        booking.status = "CANCELLED";

        saveData();

        System.out.println("Booking cancelled successfully.");
    }

    // ============================================================
    // ADMIN MENU
    // ============================================================

    static void adminMenu(User admin) {

        boolean loggedIn = true;

        while (loggedIn) {

            System.out.println();
            System.out.println("==========================================");
            System.out.println("              ADMIN MENU");
            System.out.println("==========================================");
            System.out.println("1. View Resources");
            System.out.println("2. Add Resource");
            System.out.println("3. Update Resource");
            System.out.println("4. Deactivate Resource");
            System.out.println("5. View All Bookings");
            System.out.println("6. Manage Booking Status");
            System.out.println("7. Generate Reports");
            System.out.println("8. Logout");
            System.out.println("------------------------------------------");

            int choice = readInt("Enter your choice: ", 1, 8);

            switch (choice) {

                case 1:
                    viewResources();
                    break;

                case 2:
                    addResource();
                    break;

                case 3:
                    updateResource();
                    break;

                case 4:
                    deactivateResource();
                    break;

                case 5:
                    viewAllBookings();
                    break;

                case 6:
                    manageBookingStatus();
                    break;

                case 7:
                    generateReports();
                    break;

                case 8:
                    loggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;
            }
        }
    }

    // ============================================================
    // ADD RESOURCE
    // ============================================================

    static void addResource() {

        System.out.println("\n============== ADD RESOURCE ==============");

        String code = readNonEmpty("Resource code: ");

        for (Resource r : data.resources) {

            if (r.code.equalsIgnoreCase(code)) {

                System.out.println(
                        "A resource with this code already exists."
                );

                return;
            }
        }

        String type = readNonEmpty("Resource type: ");
        String location = readNonEmpty("Location: ");

        int capacity =
                readInt("Capacity: ", 1, 10000);

        Resource resource = new Resource(
                data.nextResourceId++,
                code,
                type,
                location,
                capacity,
                "AVAILABLE"
        );

        data.resources.add(resource);

        saveData();

        System.out.println("Resource added successfully.");
        printResource(resource);
    }

    // ============================================================
    // UPDATE RESOURCE
    // ============================================================

    static void updateResource() {

        System.out.println("\n============= UPDATE RESOURCE =============");

        viewResources();

        int id =
                readInt("Enter resource ID: ", 1, 100000);

        Resource resource = findResource(id);

        if (resource == null) {

            System.out.println("Resource not found.");
            return;
        }

        System.out.println(
                "Press ENTER to keep the current value."
        );

        System.out.print("New name/code [" + resource.code + "]: ");

        String code = scanner.nextLine().trim();

        if (!code.isEmpty()) {
            resource.code = code;
        }

        System.out.print("New type [" + resource.type + "]: ");

        String type = scanner.nextLine().trim();

        if (!type.isEmpty()) {
            resource.type = type;
        }

        System.out.print(
                "New location [" + resource.location + "]: "
        );

        String location = scanner.nextLine().trim();

        if (!location.isEmpty()) {
            resource.location = location;
        }

        System.out.print(
                "New capacity [" + resource.capacity + "]: "
        );

        String capacityInput = scanner.nextLine().trim();

        if (!capacityInput.isEmpty()) {

            try {

                int capacity = Integer.parseInt(capacityInput);

                if (capacity > 0) {
                    resource.capacity = capacity;
                } else {
                    System.out.println(
                            "Invalid capacity. Keeping old value."
                    );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid number. Keeping old capacity."
                );
            }
        }

        saveData();

        System.out.println("Resource updated successfully.");
    }

    // ============================================================
    // DEACTIVATE RESOURCE
    // ============================================================

    static void deactivateResource() {

        System.out.println(
                "\n=========== DEACTIVATE RESOURCE ==========="
        );

        viewResources();

        int id =
                readInt("Enter resource ID: ", 1, 100000);

        Resource resource = findResource(id);

        if (resource == null) {

            System.out.println("Resource not found.");
            return;
        }

        resource.status = "INACTIVE";

        saveData();

        System.out.println(
                "Resource deactivated successfully."
        );

        System.out.println(
                "Historical bookings have been preserved."
        );
    }

    // ============================================================
    // VIEW ALL BOOKINGS
    // ============================================================

    static void viewAllBookings() {

        System.out.println(
                "\n================ ALL BOOKINGS ================"
        );

        if (data.bookings.isEmpty()) {

            System.out.println("No bookings found.");
            return;
        }

        for (Booking b : data.bookings) {

            User user = findUser(b.userId);
            Resource resource = findResource(b.resourceId);

            System.out.println("--------------------------------------------");
            System.out.println("Booking ID : " + b.id);
            System.out.println(
                    "User       : "
                    + (user == null ? "Unknown" : user.name)
            );
            System.out.println(
                    "Resource   : "
                    + (resource == null ? "Unknown" : resource.code)
            );
            System.out.println("Date       : " + b.date);
            System.out.println(
                    "Time       : "
                    + b.startTime.format(TIME_FORMAT)
                    + " - "
                    + b.endTime.format(TIME_FORMAT)
            );
            System.out.println("Status     : " + b.status);
        }
    }

    // ============================================================
    // MANAGE BOOKING STATUS
    // ============================================================

    static void manageBookingStatus() {

        System.out.println(
                "\n=========== MANAGE BOOKING STATUS ==========="
        );

        viewAllBookings();

        if (data.bookings.isEmpty()) {
            return;
        }

        int id =
                readInt("Enter booking ID: ", 1, 100000);

        Booking booking = findBooking(id);

        if (booking == null) {

            System.out.println("Booking not found.");
            return;
        }

        System.out.println("1. ACTIVE");
        System.out.println("2. COMPLETED");
        System.out.println("3. CANCELLED");

        int choice =
                readInt("Choose new status: ", 1, 3);

        if (choice == 1) {
            booking.status = "ACTIVE";
        } else if (choice == 2) {
            booking.status = "COMPLETED";
        } else {
            booking.status = "CANCELLED";
        }

        saveData();

        System.out.println("Booking status updated.");
    }

    // ============================================================
    // REPORTS
    // ============================================================

    static void generateReports() {

        System.out.println(
                "\n================ REPORTS ================"
        );

        int activeBookings = 0;
        int cancelledBookings = 0;
        int completedBookings = 0;

        for (Booking b : data.bookings) {

            if (b.status.equals("ACTIVE")) {
                activeBookings++;
            } else if (b.status.equals("CANCELLED")) {
                cancelledBookings++;
            } else if (b.status.equals("COMPLETED")) {
                completedBookings++;
            }
        }

        System.out.println(
                "Total Users       : " + data.users.size()
        );

        System.out.println(
                "Total Resources   : " + data.resources.size()
        );

        System.out.println(
                "Total Bookings    : " + data.bookings.size()
        );

        System.out.println(
                "Active Bookings   : " + activeBookings
        );

        System.out.println(
                "Completed Bookings: " + completedBookings
        );

        System.out.println(
                "Cancelled Bookings: " + cancelledBookings
        );

        System.out.println("\nResource Usage:");

        for (Resource r : data.resources) {

            int count = 0;

            for (Booking b : data.bookings) {

                if (b.resourceId == r.id
                        && !b.status.equals("CANCELLED")) {

                    count++;
                }
            }

            System.out.println(
                    r.code + " -> " + count + " booking(s)"
            );
        }
    }

    // ============================================================
    // FIND METHODS
    // ============================================================

    static User findUser(int id) {

        for (User u : data.users) {

            if (u.id == id) {
                return u;
            }
        }

        return null;
    }

    static Resource findResource(int id) {

        for (Resource r : data.resources) {

            if (r.id == id) {
                return r;
            }
        }

        return null;
    }

    static Booking findBooking(int id) {

        for (Booking b : data.bookings) {

            if (b.id == id) {
                return b;
            }
        }

        return null;
    }

    // ============================================================
    // INPUT VALIDATION
    // ============================================================

    static String readNonEmpty(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(
                    "Input cannot be empty. Please try again."
            );
        }
    }

    static int readInt(
            String message,
            int min,
            int max) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                int value = Integer.parseInt(input);

                if (value >= min && value <= max) {
                    return value;
                }

                System.out.println(
                        "Please enter a number between "
                        + min + " and " + max + "."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid number. Please try again."
                );
            }
        }
    }

    static LocalDate readDate(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                return LocalDate.parse(
                        input,
                        DATE_FORMAT
                );

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Use YYYY-MM-DD."
                );
            }
        }
    }

    static LocalTime readTime(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                return LocalTime.parse(
                        input,
                        TIME_FORMAT
                );

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid time. Use HH:MM, for example 14:30."
                );
            }
        }
    }

    // ============================================================
    // FILE STORAGE
    // ============================================================

    static void saveData() {

        try (
                ObjectOutputStream output =
                        new ObjectOutputStream(
                                new FileOutputStream(DATA_FILE)
                        )
        ) {

            output.writeObject(data);

        } catch (IOException e) {

            System.out.println(
                    "Warning: Could not save data."
            );
        }
    }

    static DataStore loadData() {

        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return new DataStore();
        }

        try (
                ObjectInputStream input =
                        new ObjectInputStream(
                                new FileInputStream(file)
                        )
        ) {

            return (DataStore) input.readObject();

        } catch (Exception e) {

            System.out.println(
                    "Existing data could not be loaded."
            );

            System.out.println(
                    "Starting with a fresh database."
            );

            return new DataStore();
        }
    }

    // ============================================================
    // DATA CLASSES
    // ============================================================

    static class DataStore implements Serializable {

        private static final long serialVersionUID = 1L;

        ArrayList<User> users = new ArrayList<>();
        ArrayList<Resource> resources = new ArrayList<>();
        ArrayList<Booking> bookings = new ArrayList<>();

        int nextUserId = 1;
        int nextResourceId = 1;
        int nextBookingId = 1;
    }

    static class User implements Serializable {

        private static final long serialVersionUID = 1L;

        int id;
        String name;
        String email;
        String password;
        String role;

        User(
                int id,
                String name,
                String email,
                String password,
                String role) {

            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
            this.role = role;
        }
    }

    static class Resource implements Serializable {

        private static final long serialVersionUID = 1L;

        int id;
        String code;
        String type;
        String location;
        int capacity;
        String status;

        Resource(
                int id,
                String code,
                String type,
                String location,
                int capacity,
                String status) {

            this.id = id;
            this.code = code;
            this.type = type;
            this.location = location;
            this.capacity = capacity;
            this.status = status;
        }
    }

    static class Booking implements Serializable {

        private static final long serialVersionUID = 1L;

        int id;
        int userId;
        int resourceId;
        LocalDate date;
        LocalTime startTime;
        LocalTime endTime;
        String status;
        LocalDateTime createdAt;

        Booking(
                int id,
                int userId,
                int resourceId,
                LocalDate date,
                LocalTime startTime,
                LocalTime endTime,
                String status) {

            this.id = id;
            this.userId = userId;
            this.resourceId = resourceId;
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.status = status;
            this.createdAt = LocalDateTime.now();
        }
    }
}