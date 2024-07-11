import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HospitalAppointment {

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    public static void main(String[] args) {
        try (
            // Connect to MySQL database using try-with-resources
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            Scanner scanner = new Scanner(System.in)
        ) {
            // Prompt user for patient name and appointment time
            System.out.print("Enter patient name: ");
            String patientName = scanner.nextLine().trim(); // trim() to remove leading/trailing spaces
            System.out.print("Enter appointment time: ");
            String appointmentTime = scanner.nextLine().trim(); // trim() to remove leading/trailing spaces

            // Input validation
            if (patientName.isEmpty() || appointmentTime.isEmpty()) {
                System.out.println("Patient name and appointment time cannot be empty.");
                return;
            }

            // Insert data into appointments table
            insertAppointment(conn, patientName, appointmentTime);

            // Display all appointments
            System.out.println("\n-- All Appointments --");
            displayAppointments(conn);

        } catch (SQLException e) {
            System.err.println("Database error:");
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    // Method to insert a new appointment into the database
    private static void insertAppointment(Connection conn, String patientName, String appointmentTime) {
        String sql = "INSERT INTO appointments (patient_name, appointment_time) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, patientName);
            statement.setString(2, appointmentTime);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new appointment was inserted successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting appointment:");
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    // Method to display all appointments from the database
    private static void displayAppointments(Connection conn) {
        String sql = "SELECT appointment_id, patient_name, appointment_time FROM appointments";
        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                int appointmentId = result.getInt("appointment_id");
                String patientName = result.getString("patient_name");
                String appointmentTime = result.getString("appointment_time");
                System.out.println("Appointment ID: " + appointmentId + ", Patient Name: " + patientName + ", Appointment Time: " + appointmentTime);
            }
        } catch (SQLException e) {
            System.err.println("Error displaying appointments:");
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
}
