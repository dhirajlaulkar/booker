package booker;

import booker.entities.User;
import booker.services.UserBookingService;
import booker.util.UserServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        UserBookingService userBookingService;

        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            System.out.println("There is Something Wrong");
            return;
        }
        while (option != 7) {
            System.out.println("Choose option");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.println("Enter your name");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter your password");
                    String passwordToSignUp = scanner.next();
                    User userToSignUp = new User(nameToSignUp, passwordToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(),
                            UUID.randomUUID().toString());
                    userBookingService.signUp(userToSignUp);
                    break;

                case 2:
                    System.out.println("Enter your name");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter your password");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(nameToLogin, passwordToLogin,
                            UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(),
                            UUID.randomUUID().toString());
                    try {
                        userBookingService = new UserBookingService(userToLogin);
                    } catch (IOException ex) {
                        return;
                    }
                    break;
                case 3:
                    System.out.println("Fetch Bookings");
                    userBookingService.fetchBooking();
                    break;

            }
        }

    }
}