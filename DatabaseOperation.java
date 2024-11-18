package movieticketbooking;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class DatabaseOperation {
    static final String DB_URL = "jdbc:mysql://localhost/moviedb";
    static final String USER = "root";
    static final String PASS = "GsJm$2408";

    public Connection connectToDatabase() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return conn;
    }

    public int executeUpdate(String sql, Object[] values) {
        int rowsAffected = 0;
        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Update Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return rowsAffected;
    }

    public List<Map<String, Object>> getRecords(String sql) {
        List<Map<String, Object>> records = new ArrayList<>();
        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                records.add(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Query Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return records;
    }

    public int getSeatingCapacity(String sql, int parameter) {
        int seatingCapacity = 0;
        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parameter);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                seatingCapacity = rs.getInt("SeatingCapacity");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching seating capacity: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return seatingCapacity;
    }

    public ArrayList<Integer> getBookedSeats(int showtimeID) {
        String sql = "SELECT SelectedSeats FROM bookings WHERE ShowtimeID = ?";
        ArrayList<Integer> bookedSeats = new ArrayList<>();
        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, showtimeID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookedSeats.add(rs.getInt("SelectedSeats"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching booked seats: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return bookedSeats;
    }

    public int removeBooking(int bookingID) {
        String sql = "DELETE FROM bookings WHERE BookingID = ?";
        int rowsAffected = 0;
        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingID);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error removing booking: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return rowsAffected;
    }

    public Map<String, Object> validatePass(String sql, Object... params) {
        Map<String, Object> result = new HashMap<>();
        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    result.put(metaData.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.isEmpty() ? null : result; // Return null if no record found
    }


    public int fetchUserID(String sql, String username) {
        int userID = 0;
        try (Connection conn = connectToDatabase();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userID = rs.getInt("UserID");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching UserID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return userID;
    }
}
