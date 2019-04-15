package app;

import java.util.Scanner;

public class CLI {
    private static boolean adminMode;
    private static Scanner scanner = new Scanner(System.in);

    private static void cHelp( ) {
        System.out.println("LIST OF COMMANDS");
        System.out.println("help - See this help message.");
        System.out.println("quit - Exit the program.");
        System.out.println("adminmode - Enter admin mode (you will be asked for a password -- it's 'password').");
        System.out.println("deliverymode - Leave admin mode.");
    }

    private static void cQuit( ) {
        System.exit(0);
    }

    private static void cDeliveryMode( ) {
        System.out.println("You are now logged in as a delivery person.");
        adminMode = false;
    }

    private static void cAdminMode( ) {
        System.out.print(" password? ");
        String attempt = scanner.nextLine();

        if(attempt.equals("password")) {
            System.out.println("You are now logged in as admin.");
            adminMode = true;
        } else {
            System.out.println("Sorry, wrong password.");
        }
    }

    public static void main( String[] args ) {

        while(true) {
            System.out.print( adminMode ? " !> " : " > ");
            String input = scanner.nextLine().toLowerCase();

            if(input.equals("quit")) cQuit();
            else if(input.equals("help")) cHelp();
            else if(input.equals("adminmode")) cAdminMode();
            else if(input.equals("deliverymode")) cDeliveryMode();
            else System.out.println("Sorry, I don't know that command.");
        }
    }
}
