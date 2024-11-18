package movieticketbooking;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class MovieTicketBookingSystem {
    private static DatabaseOperation db = new DatabaseOperation();
    private static int loggedInUserID = -1; // User session management

    public static void main(String[] args) {
        showMainMenu();
    }

    // Main Menu
    private static void showMainMenu() {
        JFrame mainMenu = new JFrame("Movie Ticket Booking System");
        mainMenu.setSize(400, 300);
        mainMenu.setLayout(new GridLayout(3, 1));

        JButton adminButton = new JButton("Admin Login");
        JButton userButton = new JButton("User Login");
        JButton exitButton = new JButton("Exit");

        mainMenu.add(adminButton);
        mainMenu.add(userButton);
        mainMenu.add(exitButton);

        adminButton.addActionListener(e -> {
            mainMenu.dispose();
            showAdminLogin();
        });

        userButton.addActionListener(e -> {
            mainMenu.dispose();
            showUserLogin();
        });

        exitButton.addActionListener(e -> System.exit(0));

        mainMenu.setVisible(true);
    }

    // ADMIN FUNCTIONS
    private static void showAdminLogin() {
        JFrame adminLogin = new JFrame("Admin Login");
        adminLogin.setSize(300, 200);
        adminLogin.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Admin Name:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        adminLogin.add(usernameLabel);
        adminLogin.add(usernameField);
        adminLogin.add(passwordLabel);
        adminLogin.add(passwordField);
        adminLogin.add(loginButton);
        adminLogin.add(backButton);

        loginButton.addActionListener(e -> {
            String adminName = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String sql = "SELECT Admin_name, Password FROM admin WHERE Admin_name = ?";
            Map<String, Object> admin = db.validatePass(sql, adminName);

            if (admin != null && password.equals(admin.get("Password"))) {
                JOptionPane.showMessageDialog(adminLogin, "Login successful!");
                adminLogin.dispose();
                showAdminDashboard();
            } else {
                JOptionPane.showMessageDialog(adminLogin, "Invalid credentials!");
            }
        });

        backButton.addActionListener(e -> {
            adminLogin.dispose();
            showMainMenu();
        });

        adminLogin.setVisible(true);
    }



    private static void showAdminDashboard() {
        JFrame adminDashboard = new JFrame("Admin Dashboard");
        adminDashboard.setSize(400, 400);
        adminDashboard.setLayout(new GridLayout(5, 1));

        JButton addMovieButton = new JButton("Add Movies");
        JButton addTheaterButton = new JButton("Add Theaters");
        JButton addShowtimeButton = new JButton("Add Showtimes");
        JButton viewMoviesButton = new JButton("View Movies and Showtimes");
        JButton logoutButton = new JButton("Logout");

        adminDashboard.add(addMovieButton);
        adminDashboard.add(addTheaterButton);
        adminDashboard.add(addShowtimeButton);
        adminDashboard.add(viewMoviesButton);
        adminDashboard.add(logoutButton);

        addMovieButton.addActionListener(e -> {
            adminDashboard.dispose();
            addMovie();
        });

        addTheaterButton.addActionListener(e -> {
            adminDashboard.dispose();
            addTheater();
        });

        addShowtimeButton.addActionListener(e -> {
            adminDashboard.dispose();
            addShowtime();
        });

        viewMoviesButton.addActionListener(e -> {
            adminDashboard.dispose();
            viewMoviesAndShowtimes();
        });

        logoutButton.addActionListener(e -> {
            adminDashboard.dispose();
            showMainMenu();
        });

        adminDashboard.setVisible(true);
    }

    private static void addMovie() {
        JFrame addMovieFrame = new JFrame("Add Movie");
        addMovieFrame.setSize(300, 400);
        addMovieFrame.setLayout(new GridLayout(6, 2));
        JLabel movieidLabel = new JLabel("Movie ID:");
        JTextField movieidField = new JTextField();
        JLabel titleLabel = new JLabel("Movie Title:");
        JTextField titleField = new JTextField();
        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreField = new JTextField();
        JLabel durationLabel = new JLabel("Duration (mins):");
        JTextField durationField = new JTextField();
        JLabel synopsisLabel = new JLabel("Synopsis:");
        JTextField synopsisField = new JTextField();
        JLabel ratingLabel = new JLabel("Rating (0-10):");
        JTextField ratingField = new JTextField();

        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        addMovieFrame.add(movieidLabel);
        addMovieFrame.add(movieidField);
        addMovieFrame.add(titleLabel);
        addMovieFrame.add(titleField);
        addMovieFrame.add(genreLabel);
        addMovieFrame.add(genreField);
        addMovieFrame.add(durationLabel);
        addMovieFrame.add(durationField);
        addMovieFrame.add(synopsisLabel);
        addMovieFrame.add(synopsisField);
        addMovieFrame.add(ratingLabel);
        addMovieFrame.add(ratingField);
        addMovieFrame.add(submitButton);
        addMovieFrame.add(cancelButton);

        submitButton.addActionListener(e -> {
        	int movie_id;
            String title = titleField.getText();
            String genre = genreField.getText();
            int duration;
            double rating;
            try {
            	movie_id = Integer.parseInt(movieidField.getText());
                duration = Integer.parseInt(durationField.getText());
                rating = Double.parseDouble(ratingField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addMovieFrame, "Invalid duration or rating. Please enter numbers.");
                return;
            }
            String synopsis = synopsisField.getText();

            String sql = "INSERT INTO movies (movie_ID, title, genre, Duration, Synopsis, rating) VALUES (?, ?, ?, ?, ?, ?)";
            Object[] values = {movie_id,title, genre, duration, synopsis, rating};
            int rowsAffected = db.executeUpdate(sql, values);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(addMovieFrame, "Movie added successfully!");
            } else {
                JOptionPane.showMessageDialog(addMovieFrame, "Failed to add the movie.");
            }
            addMovieFrame.dispose();
            showAdminDashboard();
        });

        cancelButton.addActionListener(e -> {
            addMovieFrame.dispose();
            showAdminDashboard();
        });

        addMovieFrame.setVisible(true);
    }

    private static void addTheater() {
        JFrame addTheaterFrame = new JFrame("Add Theater");
        addTheaterFrame.setSize(300, 400);
        addTheaterFrame.setLayout(new GridLayout(5, 2));
        
        JLabel idLabel = new JLabel("Theater ID:");
        JTextField idField = new JTextField();
        JLabel nameLabel = new JLabel("Theater Name:");
        JTextField nameField = new JTextField();
        JLabel capacityLabel = new JLabel("Seating Capacity:");
        JTextField capacityField = new JTextField();
        JLabel locationLabel = new JLabel("Location:");
        JTextField locationField = new JTextField();
        JLabel screenLabel = new JLabel("Screen ID:");
        JTextField screenField = new JTextField();
        JLabel foodLabel = new JLabel("Food Add On:");
        JTextField foodField = new JTextField();

        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        
        addTheaterFrame.add(idLabel);
        addTheaterFrame.add(idField);
        addTheaterFrame.add(nameLabel);
        addTheaterFrame.add(nameField);
        addTheaterFrame.add(capacityLabel);
        addTheaterFrame.add(capacityField);
        addTheaterFrame.add(locationLabel);
        addTheaterFrame.add(locationField);
        addTheaterFrame.add(screenLabel);
        addTheaterFrame.add(screenField);
        addTheaterFrame.add(foodLabel);
        addTheaterFrame.add(foodField);
        addTheaterFrame.add(submitButton);
        addTheaterFrame.add(cancelButton);

        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            int capacity;
            try {
                capacity = Integer.parseInt(capacityField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addTheaterFrame, "Invalid capacity. Please enter a number.");
                return;
            }
            String location = locationField.getText();
            String foodaddon=foodField.getText();
            int theatreID,screenID;
            try {
            	theatreID = Integer.parseInt(screenField.getText());
                screenID = Integer.parseInt(screenField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addTheaterFrame, "Invalid screen ID. Please enter a number.");
                return;
            }

            String sql = "INSERT INTO theatre (theatre_id,theatre_name, seating_capacity, location, screen_id,food_add_on) VALUES (?,?,?, ?, ?, ?)";
            Object[] values = {theatreID, name, capacity, location, screenID,foodaddon};
            int rowsAffected = db.executeUpdate(sql, values);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(addTheaterFrame, "Theater added successfully!");
            } else {
                JOptionPane.showMessageDialog(addTheaterFrame, "Failed to add the theater.");
            }
            addTheaterFrame.dispose();
            showAdminDashboard();
        });

        cancelButton.addActionListener(e -> {
            addTheaterFrame.dispose();
            showAdminDashboard();
        });

        addTheaterFrame.setVisible(true);
    }

    private static void addShowtime() {
        JFrame addShowtimeFrame = new JFrame("Add Showtime");
        addShowtimeFrame.setSize(300, 400);
        addShowtimeFrame.setLayout(new GridLayout(6, 2));
        
        JLabel showtimeIDLabel = new JLabel("Showtime ID:");
        JTextField showtimeIDField = new JTextField();
        JLabel movieIDLabel = new JLabel("Movie ID:");
        JTextField movieIDField = new JTextField();
        JLabel theaterIDLabel = new JLabel("Theater ID:");
        JTextField theaterIDField = new JTextField();
        JLabel screenIDLabel = new JLabel("Screen ID:");
        JTextField screenIDField = new JTextField();
        JLabel dayLabel = new JLabel("Day:");
        JTextField dayField = new JTextField();
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();

        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        
        addShowtimeFrame.add(showtimeIDLabel);
        addShowtimeFrame.add(showtimeIDField);
        addShowtimeFrame.add(movieIDLabel);
        addShowtimeFrame.add(movieIDField);
        addShowtimeFrame.add(theaterIDLabel);
        addShowtimeFrame.add(theaterIDField);
        addShowtimeFrame.add(screenIDLabel);
        addShowtimeFrame.add(screenIDField);
        addShowtimeFrame.add(dayLabel);
        addShowtimeFrame.add(dayField);
        addShowtimeFrame.add(dateLabel);
        addShowtimeFrame.add(dateField);
        addShowtimeFrame.add(submitButton);
        addShowtimeFrame.add(cancelButton);

        submitButton.addActionListener(e -> {
            int showtimeID, movieID, theaterID, screenID;
            String day = dayField.getText();
            String date = dateField.getText();

            try {
            	showtimeID = Integer.parseInt(showtimeIDField.getText());
                movieID = Integer.parseInt(movieIDField.getText());
                theaterID = Integer.parseInt(theaterIDField.getText());
                screenID = Integer.parseInt(screenIDField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addShowtimeFrame, "Invalid Movie ID, Theater ID, or Screen ID.");
                return;
            }

            String sql = "INSERT INTO showtimes (showtime_id, movie_ID, theatre_id, screen_id, day, date) VALUES (?,?, ?, ?, ?, ?)";
            Object[] values = {showtimeID,movieID, theaterID, screenID, day, date};
            int rowsAffected = db.executeUpdate(sql, values);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(addShowtimeFrame, "Showtime added successfully!");
            } else {
                JOptionPane.showMessageDialog(addShowtimeFrame, "Failed to add the showtime.");
            }
            addShowtimeFrame.dispose();
            showAdminDashboard();
        });

        cancelButton.addActionListener(e -> {
            addShowtimeFrame.dispose();
            showAdminDashboard();
        });

        addShowtimeFrame.setVisible(true);
    }

    private static void viewMoviesAndShowtimes() {
        JFrame viewMoviesFrame = new JFrame("Movies and Showtimes");
        viewMoviesFrame.setSize(600, 600);
        viewMoviesFrame.setLayout(new GridLayout(0, 1));

        List<Map<String, Object>> movies = db.getRecords("SELECT * FROM movies");
        for (Map<String, Object> movie : movies) {
            String movieDetails = "Movie ID: " + movie.get("movie_ID") +
                                  ", Title: " + movie.get("title") +
                                  ", Genre: " + movie.get("genre") +
                                  ", Duration: " + movie.get("Duration") +
                                  ", Rating: " + movie.get("rating");
            JLabel movieLabel = new JLabel(movieDetails);
            viewMoviesFrame.add(movieLabel);

            int movieID = (int) movie.get("movie_ID");
            List<Map<String, Object>> showtimes = db.getRecords("SELECT * FROM showtimes WHERE movie_ID = " + movieID);
            for (Map<String, Object> showtime : showtimes) {
                String showtimeDetails = "    Showtime ID: " + showtime.get("showtime_id") +
                                         ", Theater ID: " + showtime.get("theatre_id") +
                                         ", Screen ID: " + showtime.get("screen_id") +
                                         ", Day: " + showtime.get("day") +
                                         ", Date: " + showtime.get("date");
                JLabel showtimeLabel = new JLabel(showtimeDetails);
                viewMoviesFrame.add(showtimeLabel);
            }
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            viewMoviesFrame.dispose();
            showAdminDashboard();
        });

        viewMoviesFrame.add(backButton);
        viewMoviesFrame.setVisible(true);
    }

    // USER FUNCTIONS
    private static void showUserLogin() {
        JFrame userLogin = new JFrame("User Login");
        userLogin.setSize(300, 200);
        userLogin.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        userLogin.add(usernameLabel);
        userLogin.add(usernameField);
        userLogin.add(passwordLabel);
        userLogin.add(passwordField);
        userLogin.add(loginButton);
        userLogin.add(backButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String sql = "SELECT UserID, Password FROM users WHERE Username = ?";
            Map<String, Object> user = db.validatePass(sql, username);

            if (user != null && password.equals(user.get("Password"))) {
                loggedInUserID = (int) user.get("UserID");
                JOptionPane.showMessageDialog(userLogin, "Login successful!");
                userLogin.dispose();
                showUserDashboard();
            } else {
                JOptionPane.showMessageDialog(userLogin, "Invalid credentials!");
            }
        });

        backButton.addActionListener(e -> {
            userLogin.dispose();
            showMainMenu();
        });

        userLogin.setVisible(true);
    }



    private static void showUserDashboard() {
        JFrame userDashboard = new JFrame("User Dashboard");
        userDashboard.setSize(400, 400);
        userDashboard.setLayout(new GridLayout(4, 1));

        JButton viewShowtimesButton = new JButton("View Showtimes");
        JButton bookTicketButton = new JButton("Book Ticket");
        JButton viewBookingsButton = new JButton("View Bookings");
        JButton logoutButton = new JButton("Logout");

        userDashboard.add(viewShowtimesButton);
        userDashboard.add(bookTicketButton);
        userDashboard.add(viewBookingsButton);
        userDashboard.add(logoutButton);

        viewShowtimesButton.addActionListener(e -> {
            userDashboard.dispose();
            viewUserShowtimes();
        });

        bookTicketButton.addActionListener(e -> {
            userDashboard.dispose();
            bookTicket();
        });

        viewBookingsButton.addActionListener(e -> {
            userDashboard.dispose();
            viewUserBookings();
        });

        logoutButton.addActionListener(e -> {
            loggedInUserID = -1;
            userDashboard.dispose();
            showMainMenu();
        });

        userDashboard.setVisible(true);
    }

    private static void viewUserShowtimes() {
        JFrame viewShowtimesFrame = new JFrame("Available Showtimes");
        viewShowtimesFrame.setSize(600, 600);
        viewShowtimesFrame.setLayout(new GridLayout(0, 1));

        List<Map<String, Object>> showtimes = db.getRecords("SELECT * FROM showtimes");
        for (Map<String, Object> showtime : showtimes) {
            String showtimeDetails = "Showtime ID: " + showtime.get("showtime_id") +
                                     ", Movie ID: " + showtime.get("movie_ID") +
                                     ", Theater ID: " + showtime.get("theatre_id") +
                                     ", Screen ID: " + showtime.get("screen_id") +
                                     ", Day: " + showtime.get("day") +
                                     ", Date: " + showtime.get("date");
            JLabel showtimeLabel = new JLabel(showtimeDetails);
            viewShowtimesFrame.add(showtimeLabel);
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            viewShowtimesFrame.dispose();
            showUserDashboard();
        });

        viewShowtimesFrame.add(backButton);
        viewShowtimesFrame.setVisible(true);
    }

    private static void bookTicket() {
        JFrame bookTicketFrame = new JFrame("Book Ticket");
        bookTicketFrame.setSize(300, 300);
        bookTicketFrame.setLayout(new GridLayout(4, 2));

        JLabel showtimeIDLabel = new JLabel("Showtime ID:");
        JTextField showtimeIDField = new JTextField();
        JLabel seatsLabel = new JLabel("Seats (e.g., A1,A2):");
        JTextField seatsField = new JTextField();

        JButton bookButton = new JButton("Book");
        JButton cancelButton = new JButton("Cancel");

        bookTicketFrame.add(showtimeIDLabel);
        bookTicketFrame.add(showtimeIDField);
        bookTicketFrame.add(seatsLabel);
        bookTicketFrame.add(seatsField);
        bookTicketFrame.add(bookButton);
        bookTicketFrame.add(cancelButton);

        bookButton.addActionListener(e -> {
            int showtimeID;
            String selectedSeats = seatsField.getText();

            try {
                showtimeID = Integer.parseInt(showtimeIDField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(bookTicketFrame, "Invalid Showtime ID.");
                return;
            }

            String sql = "INSERT INTO tickets (UserID, showtime_id, selected_seats, payment_status, availability_status) VALUES (?, ?, ?, 'Paid', 'Confirmed')";
            Object[] values = {loggedInUserID, showtimeID, selectedSeats};
            int rowsAffected = db.executeUpdate(sql, values);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(bookTicketFrame, "Ticket booked successfully!");
            } else {
                JOptionPane.showMessageDialog(bookTicketFrame, "Failed to book ticket.");
            }
            bookTicketFrame.dispose();
            showUserDashboard();
        });

        cancelButton.addActionListener(e -> {
            bookTicketFrame.dispose();
            showUserDashboard();
        });

        bookTicketFrame.setVisible(true);
    }

    private static void viewUserBookings() {
        JFrame viewBookingsFrame = new JFrame("My Bookings");
        viewBookingsFrame.setSize(600, 600);
        viewBookingsFrame.setLayout(new GridLayout(0, 1));

        List<Map<String, Object>> bookings = db.getRecords("SELECT * FROM tickets WHERE UserID = " + loggedInUserID);
        for (Map<String, Object> booking : bookings) {
            String bookingDetails = " Showtime ID: " + booking.get("showtime_id") +
                                    ", Seats: " + booking.get("selected_seats") +
                                    ", Status: " + booking.get("availability_status");
            JLabel bookingLabel = new JLabel(bookingDetails);
            viewBookingsFrame.add(bookingLabel);
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            viewBookingsFrame.dispose();
            showUserDashboard();
        });

        viewBookingsFrame.add(backButton);
        viewBookingsFrame.setVisible(true);
    }
}
