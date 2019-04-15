package app;

import java.sql.SQLException;
import java.util.Scanner;

public class CLI {
    private static boolean adminMode;
    private static Scanner scanner = new Scanner(System.in);

    private static boolean adminCheck() {
        if( adminMode ) {
            return true;
        } else {
            System.out.println("Sorry, you need to be an admin to do that.");
            return false;
        }
    }

    private static void cHelp( ) {
        System.out.println("LIST OF COMMANDS");
        System.out.println("help - See this help message.");
        System.out.println("quit - Exit the program.");
        System.out.println("adminmode - Enter admin mode (you will be asked for a password -- it's 'password').");
        System.out.println("deliverymode - Leave admin mode.");
        System.out.println("markdelivered ID - Mark the package with id ID as delivered.");

        System.out.println("packages - (Admin only) See a list of all packages.");
        System.out.println("customers - (Admin only) See a list of all customers.");
        System.out.println("lostpackages ID - (Admin only) See a list of packages that would be lost if carried number ID were destroyed in a crash.");
        System.out.println("execute SQL - (Admin only) Execute an arbitrary bit of SQL code.");
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

    private static void cMarkDelivered( int id ) {
        try {
            DBLiason.markDelivered(id);
            System.out.println("Marked package as delivered.");
        } catch (SQLException sqle) {
            System.out.println("Sorry, that caused a database exception!");
            sqle.printStackTrace();
        }
    }

    private static void cPackages( ) {
        if(!adminCheck()) return;
        String packages = DBLiason.prettyPackageList();
        System.out.println(packages);
    }

    private static void cCustomers( ) {
        if(!adminCheck()) return;
        String customers = DBLiason.prettyCustomerAddressList();
        System.out.println(customers);
    }

    private static void cExecute( String cmd ) {
        if(!adminCheck()) return;
        System.out.println("Executing: " + cmd);

        try {
            DBLiason.executeArbitrarySQL( cmd );
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private static void cLostPackages( int id ) {
        if(!adminCheck()) return;

        try {
            System.out.println(DBLiason.lostPackages(id));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void main( String[] args ) {

        while(true) {
            System.out.print( adminMode ? " !> " : " > ");
            String line = scanner.nextLine();
            String[] input = line.toLowerCase().split(" ");

            if(input[0].equals("quit")) cQuit();
            else if(input[0].equals("help")) cHelp();
            else if(input[0].equals("adminmode")) cAdminMode();
            else if(input[0].equals("deliverymode")) cDeliveryMode();
            else if(input[0].equals("markdelivered")) cMarkDelivered( Integer.parseInt(input[1]) );

            else if(input[0].equals("packages")) cPackages();
            else if(input[0].equals("customers")) cCustomers();
            else if(input[0].equals("lostpackages")) cLostPackages( Integer.parseInt(input[1]));

            else if(input[0].equals("execute")) cExecute(line.split(" ", 2)[1]);

            else System.out.println("Sorry, I don't know that command.");
        }
    }
}
