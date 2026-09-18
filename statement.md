# Smart Campus Resource Management System (SCRMS)

## Project Statement

The Smart Campus Resource Management System (SCRMS) is a Java-based command-line application developed to simplify the management and reservation of shared campus resources.

Educational institutions have multiple shared facilities such as classrooms, computer laboratories, study rooms, and seminar halls. When these resources are managed manually, it can become difficult to determine availability, track reservations, and prevent scheduling conflicts.

SCRMS provides a centralized system through which students can view and search campus resources, check their availability, make reservations, view their bookings, and cancel bookings. Administrators can manage resources, monitor bookings, update booking statuses, and view resource usage information.

The system includes input validation and booking conflict detection to improve the reliability of resource reservations. It uses Java object serialization for local data persistence, allowing the application to operate without requiring an external database.

## Objectives

1. To provide a centralized system for managing shared campus resources.
2. To allow students to search and view available resources.
3. To allow students to check resource availability for specific dates and times.
4. To enable students to create, view, and cancel bookings.
5. To prevent overlapping reservations through automated conflict detection.
6. To provide administrators with resource and booking management functionality.
7. To provide resource usage and booking information through reports.
8. To demonstrate Java programming, object-oriented design, validation, data persistence, and software development practices.

## Target Users

### Students

Students can register, log in, search resources, check availability, create bookings, view their reservations, and cancel bookings.

### Administrators

Administrators can manage campus resources, view all bookings, manage booking status, and generate resource usage reports.

## Technology

* Java
* Java Standard Library
* Command-Line Interface
* Java Object Serialization
* GitHub
* Visual Studio Code

## Expected Outcome

The completed system provides a functional command-line solution for campus resource and reservation management. It reduces manual scheduling effort, provides visibility into resource availability, and automatically prevents conflicting reservations.

## Execution

The project can be compiled and executed using:

```bash
javac SCRMS.java
java SCRMS
```

No external Java libraries or database installation are required.
