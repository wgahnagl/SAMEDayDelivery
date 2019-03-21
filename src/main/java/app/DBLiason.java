package app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.sql.*;

/**
 * Created by evan on 3/20/19.
 */
public class DBLiason {
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./db";

    // Database credentials
    private static final String USER = "sa";
    private static final String PASS = "pa";

    // Rope that ties Java to SQL
    private static final Connection connection;
    private static final Statement statement;

    // Initialization of the above two variables (a stupid hack to work around the fact
    // that they variables are final, yet their initialization can throw an Exception)
    static {
        Connection connectionTemp = null;
        Statement statementTemp = null;
        try {
            connectionTemp = DriverManager.getConnection(DB_URL, USER, PASS);
            statementTemp = connectionTemp.createStatement();
        } catch(SQLException sqle) {
            System.out.println("FATAL ERROR: Encountered SQLException while establishing connection to the database");
            System.out.println("Stack trace:");
            sqle.printStackTrace();
            System.exit(-1);
        }
        connection = connectionTemp;
        statement = statementTemp;
    }

    /* DB SETUP METHODS
       These methods should not really ever be called during normal program execution.
       They exist to be called by backend programmers when the format of the DB is
       updated significantly enough that a full refresh is called for.

       FORMAT
       The setupDB() method calls in sequence each of the methods below it, which
       should all follow this format:

           private static void setup<TableName>Table (Statement statement) throws SQLException {
               <Code to delete the table>
               <Code to recreate the table>
               <Code to populate the table with our sample data>
           }
     */

    public static void setupDB() {
        System.out.println("Setting up the DB ");

        try {
            /* Systematically destroy and then re-create every table in the database */
            setupCustomerTable();
            setupPackageTable();

        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        System.out.println("Finished creating the database");
    }

    private static void setupPackageTable() throws SQLException {
        statement.execute("drop table package if exists;");
        statement.execute("create table package (" +
                "ID                   int primary key," +

                "price                numeric(6,2), " + // Price in dollars, up to $9999 and accurate to the cent

                "origin_customer_id   int," +     // origin_customer_id and dest_customer_id are both
                "dest_customer_id     int," +     // foreign keys to the customer table (this constraint is declared below)

                "ship_timestamp       timestamp," +
                "delivery_timestamp   timestamp," +
                "expected_delivery    timestamp," +

                "size                 varchar(16)," +
                "type                 varchar(16)," +
                "weight               numeric(7,3)," + // Weight in kilograms, up to 9999 kg and accurate to the gram

                "foreign key (origin_customer_id) references customer(id)," +
                "foreign key (dest_customer_id) references customer(id)" +

                ");");
    }

    private static void setupCustomerTable() throws SQLException {
        statement.execute("drop table customer if exists;");
        statement.execute("create table customer (" +
                "ID int primary key," +
                "name varchar(255)," +
                "addr_line1 varchar(1024)," + // You wouldn't think addresses could get this long, but they can.
                "city varchar(255)," +
                "province varchar(255)," + // When the country is "USA", the province is the state
                "country varchar(255)," +
                ");"
        );
    }

    // Helper method to populate a table from a
    private static void populateTableFromCSV(String tablename, String filename) {
        BufferedReader reader;

        try {
            reader = new BufferedReader( new FileReader(filename)); // FileNotFouneException

            String line;
            while( (line = reader.readLine()) != null) { // IOException
                if(line.trim().startsWith("//")) continue; // Allow the CSV to comment out lines with "//"
                statement.execute( String.format( "insert into %s values (%s);", tablename, line) ); // SQLException
            }

        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return;
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
    }


    /* General-purpose DB-accessing methods.
     * Use these to implement all the different functionality that is shared between
     * the graphical UI and the command-line UI.
     */

    // Get the SQL statement used by the liason.
    // Added by Evan 20 Mar 2019. I have no idea if this is a good way to design this,
    // but it will at least work for now. ("now" = when we're just feeling out the basic
    // design of our application)

    public static Statement getStatement() {
        return statement;
    }

    // Return a String of a newline-separated list of all the packages in the DB
    // (Consider generalizing this method into a prettyTable method where you pass
    //  it something like ("Package [%d] from %s to %s", "ID", "fromCustomer", "toCustomer"))
    public static String prettyPackageList() {
        try {
            String result = "";
            ResultSet packages = statement.executeQuery("select ID, ship_timestamp from package");

            while(packages.next()) {
                int nextID = packages.getInt("ID");
                String nextShipTime = packages.getTimestamp("shiptime").toString();

                result += String.format("Package %d. Ship time: %s\n", nextID, nextShipTime);
            }

            return result;

        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return "<SQL error>";
        }
    }


    /* Main method for testing only */

    public static void main(String[] args) {
        setupDB();

        //populateTableFromCSV("package", "Phase 2/test_csv");

        System.out.println("PACKAGE TABLE PRINTOUT:");
        System.out.println(prettyPackageList());
    }
}
