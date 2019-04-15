package app;

import java.util.Scanner;

public class CLI {

    private static void cQuit( ) {
        System.exit(0);
    }

    public static void main( String[] args ) {
        Scanner s = new Scanner(System.in);

        while(true) {
            System.out.print(" > ");
            String input = s.nextLine();

            if(input.equals("quit")) cQuit();
            else System.out.println("Sorry, I don't know that command.");
        }
    }
}
