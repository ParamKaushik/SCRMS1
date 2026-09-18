# Smart Campus Resource Management System (SCRMS)

## 1. Project Overview

The **Smart Campus Resource Management System (SCRMS)** is a command-line based Java application designed to manage shared resources in an educational campus.

The system allows students to view and search campus resources, check availability, make bookings, view their bookings, and cancel reservations. Administrators can manage campus resources, view and manage bookings, and generate resource usage reports.

The application also validates user input and prevents conflicting bookings for the same resource and time period.

---

## 2. Problem Statement

Educational institutions contain many shared resources such as classrooms, laboratories, study rooms, seminar halls, and other facilities. Managing these resources manually can result in scheduling conflicts, difficulty tracking reservations, and inefficient resource utilization.

SCRMS provides a centralized system for managing these resources and their reservations through a simple command-line interface.

---

## 3. Objectives

* Provide centralized management of campus resources.
* Allow students to search and view available resources.
* Allow students to check resource availability.
* Enable students to create and cancel bookings.
* Prevent overlapping bookings and scheduling conflicts.
* Provide administrators with resource management functions.
* Provide administrators with booking and usage information.
* Validate user input and provide meaningful error messages.
* Demonstrate object-oriented programming, data management, validation, and application logic using Java.

---

## 4. Main Features

### Student Features

* Student registration
* Student login
* View available resources
* Search resources by type and location
* Filter resources using capacity
* Check resource availability
* Book a resource for a specific date and time
* View personal bookings
* Cancel bookings
* Automatic booking conflict detection

### Administrator Features

* Administrator login
* View campus resources
* Add new resources
* Update existing resources
* Deactivate resources
* View all bookings
* Manage booking status
* Generate resource usage reports
* View booking statistics

### Validation and Error Handling

The system validates:

* Empty input
* Invalid menu selections
* Invalid numeric values
* Invalid dates
* Invalid time formats
* Invalid booking time ranges
* Booking conflicts
* Duplicate/overlapping personal bookings
* Invalid resource selections
* Unauthorized administrative operations

---

## 5. Technologies Used

* **Programming Language:** Java
* **Application Type:** Command-Line Interface (CLI)
* **Development Environment:** Visual Studio Code
* **Java Version:** Java 8 or later
* **Data Storage:** Java object serialization using a local data file
* **Libraries:** Java Standard Library only

No external database or third-party Java libraries are required.

---

## 6. System Requirements

The following are required to run the project:

* Java Development Kit (JDK) 8 or later
* A command-line terminal
* A computer running Windows, Linux, or macOS
* The `SCRMS.java` source file

To verify that Java is installed, run:

```bash
java -version
```

To verify that the Java compiler is installed, run:

```bash
javac -version
```

---

## 7. Installation and Setup

### Step 1: Download or Clone the Repository

Download this repository or clone it using Git:

```bash
git clone https://github.com/YOUR-GITHUB-USERNAME/SCRMS.git
```

Then enter the project directory:

```bash
cd SCRMS
```

If the repository was downloaded as a ZIP file, extract it and open a terminal inside the extracted project folder.

### Step 2: Compile the Program

Run:

```bash
javac SCRMS.java
```

If compilation is successful, no error message will be displayed.

### Step 3: Run the Application

Run:

```bash
java SCRMS
```

The main menu will then appear in the terminal.

---

## 8. How to Use the Application

When the application starts, the main menu provides the following options:

```text
1. Register as Student
2. Login
3. View Available Resources
4. Exit
```

### Student Workflow

1. Select **Register as Student**.
2. Enter the student's name, email, and password.
3. Select **Login**.
4. Enter the registered credentials.
5. Use the Student Menu to:

   * View resources
   * Search resources
   * Check availability
   * Create bookings
   * View bookings
   * Cancel bookings
   * Logout

### Booking Example

A student can select a resource and enter:

```text
Date: 2026-09-20
Start Time: 10:00
End Time: 12:00
```

The system checks whether the resource is already booked during the requested period.

If another booking overlaps with the requested time, the system rejects the booking.

---

## 9. Default Administrator Account

A default administrator account is created automatically when the application is initialized.

```text
Email: admin@scrms.com
Password: admin123
```

After logging in as an administrator, the Admin Menu provides resource management, booking management, and reporting functions.

---

## 10. Sample Resources

The application initializes sample campus resources including:

* LAB-101 - Computer Laboratory
* LAB-102 - Computer Laboratory
* ROOM-204 - Classroom
* STUDY-01 - Study Room
* SEMINAR-01 - Seminar Hall

These resources can be viewed and managed through the application.

---

## 11. Data Persistence

SCRMS uses Java object serialization for local data persistence.

During execution, the application creates a file named:

```text
scrms_data.dat
```

This file stores application data such as:

* Users
* Resources
* Bookings
* Internal ID counters

The file is automatically created when required, so no separate database installation is necessary.

The generated `.dat` file is local runtime data and is not required in the source repository.

---

## 12. Booking Conflict Detection

The system prevents overlapping reservations for the same resource.

Two bookings are considered conflicting when:

```text
requestedStart < existingEnd
AND
requestedEnd > existingStart
```

For example, if a resource is booked from:

```text
10:00 - 12:00
```

another booking for:

```text
11:00 - 13:00
```

will be rejected because the time periods overlap.

A booking from:

```text
12:00 - 14:00
```

does not overlap with the previous booking and can be accepted.

---

## 13. Project Structure

The current project is intentionally implemented as a compact single-file Java application for simple command-line execution.

```text
SCRMS/
│
├── README.md
├── SCRMS.java
└── scrms_data.dat        # Generated automatically during execution
```

`SCRMS.java` contains the main application logic and supporting classes for:

* Application control
* Data storage
* Users
* Resources
* Bookings
* Student operations
* Administrator operations
* Validation
* Reporting

---

## 14. Testing

The application was tested through command-line execution.

### Test 1: Application Startup

**Input:**

```text
java SCRMS
```

**Expected Result:**

The main menu is displayed successfully.

### Test 2: Resource Display

Selecting:

```text
3. View Available Resources
```

displays the available campus resources.

### Test 3: Student Registration

A new student can register by providing valid name, email, and password information.

**Expected Result:** Student registration is completed successfully.

### Test 4: Student Login

A registered student can log in using the registered credentials.

**Expected Result:** The Student Menu is displayed.

### Test 5: Booking

A student can create a booking for an available resource and valid date/time.

**Expected Result:** A booking ID is generated and the booking is stored.

### Test 6: Booking Conflict

An attempt was made to book the same resource during an overlapping time period.

**Expected Result:** The system rejects the booking and displays a booking conflict message.

### Test 7: Booking Viewing

The student can view their existing bookings.

**Expected Result:** The previously created booking is displayed.

---

## 15. Error Handling

The application handles invalid user input without terminating the program unnecessarily.

Examples include:

* Invalid menu choices
* Invalid integer input
* Empty strings
* Invalid dates
* Invalid times
* End time earlier than or equal to start time
* Invalid resource IDs
* Attempting to book unavailable resources
* Attempting to cancel invalid bookings

---

## 16. Design Approach

The application follows a structured approach in which different responsibilities are separated into classes and methods within the Java source file.

The major logical components are:

```text
User Management
       |
       v
Resource Management
       |
       v
Availability Checking
       |
       v
Booking Management
       |
       v
Reporting
       |
       v
File-Based Data Storage
```

The system uses object-oriented concepts such as:

* Classes and objects
* Encapsulation
* Methods
* Enumerated values
* Serialization
* Collections
* Modular methods

---

## 17. Limitations

* The current version uses local file-based storage rather than a centralized database.
* The application is command-line based and does not provide a graphical user interface.
* The system is designed as a local application rather than a multi-user network service.
* The current implementation keeps the project compact in a single Java source file.

---

## 18. Future Enhancements

Possible future improvements include:

* Integration with a relational database such as MySQL or SQLite.
* Web or graphical user interface.
* Email or notification support for bookings.
* Advanced administrator analytics.
* Calendar-based resource scheduling.
* Multiple campus/location support.
* Improved authentication and password security.
* Role support for additional campus users such as faculty and staff.
* Automated unit and integration testing.

---

## 19. Learning Outcomes

Through this project, the following concepts are demonstrated:

* Java programming
* Object-oriented programming
* Command-line application development
* Input validation
* Exception handling
* Data persistence
* Resource and reservation management
* Conflict detection algorithms
* Software design and documentation
* Testing and debugging
* GitHub-based project management

---

## 20. Author

**Smart Campus Resource Management System (SCRMS)**

Developed as a college project for the VITyarthi Build Your Own Project course.

