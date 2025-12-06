


package Trainticket_booking_sql_3;

import java.sql.*;


import java.util.Scanner;

public class train {

    private static final String URL = "jdbc:mysql://localhost:3306/traindb";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void viewTrains() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM trains")) {

            System.out.println("\n--- Available Trains ---");
            while (rs.next()) {
                System.out.println("Train No: " + rs.getInt("trainNo") +
                        " | Name: " + rs.getString("trainName") +
                        " | From: " + rs.getString("source") +
                        " | To: " + rs.getString("destination") +
                        " | Seats Left: " + rs.getInt("seats"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookTicket(Scanner sc) {
        System.out.print("\nEnter Train Number: ");
        int tNo = sc.nextInt();
        sc.nextLine();

        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT seats FROM trains WHERE trainNo = ?");
            ps.setInt(1, tNo);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Train not found!");
                return;
            }

            int seats = rs.getInt("seats");
            if (seats <= 0) {
                System.out.println("No seats available!");
                return;
            }

            System.out.print("Enter Passenger Name: ");
            String name = sc.nextLine();

            ps = conn.prepareStatement("INSERT INTO tickets(passengerName, trainNo) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setInt(2, tNo);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("Booking failed. Please try again.");
                return;
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();
            int ticketId = -1;
            if (generatedKeys.next()) {
                ticketId = generatedKeys.getInt(1);
            }

            ps = conn.prepareStatement("UPDATE trains SET seats = seats - 1 WHERE trainNo = ?");
            ps.setInt(1, tNo);
            ps.executeUpdate();

            System.out.println("Ticket Booked Successfully!");
            System.out.println("Ticket ID: " + ticketId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cancelTicket(Scanner sc) {
        System.out.print("\nEnter Ticket ID to Cancel: ");
        int id = sc.nextInt();
        sc.nextLine();

        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT trainNo FROM tickets WHERE ticketId = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Ticket not found!");
                return;
            }

            int trainNo = rs.getInt("trainNo");

            ps = conn.prepareStatement("DELETE FROM tickets WHERE ticketId = ?");
            ps.setInt(1, id);
            int deleted = ps.executeUpdate();

            if (deleted == 0) {
                System.out.println("Failed to cancel ticket.");
                return;
            }

            ps = conn.prepareStatement("UPDATE trains SET seats = seats + 1 WHERE trainNo = ?");
            ps.setInt(1, trainNo);
            ps.executeUpdate();

            System.out.println("Ticket Cancelled Successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewBookings() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT t.ticketId, t.passengerName, t.trainNo, tr.trainName " +
                     "FROM tickets t JOIN trains tr ON t.trainNo = tr.trainNo")) {

            System.out.println("\n--- All Bookings ---");
            boolean hasBookings = false;
            while (rs.next()) {
                hasBookings = true;
                System.out.println("Ticket ID: " + rs.getInt("ticketId") +
                        " | Passenger: " + rs.getString("passengerName") +
                        " | Train No: " + rs.getInt("trainNo") +
                        " | Train Name: " + rs.getString("trainName"));
            }
            if (!hasBookings) {
                System.out.println("No bookings yet!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== RAILWAY TICKET BOOKING SYSTEM ====");
            System.out.println("1. View Trains");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewTrains();
                    break;
                case 2:
                    bookTicket(sc);
                    break;
                case 3:
                    cancelTicket(sc);
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    System.out.println("Thank you for using Railway Booking System!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
