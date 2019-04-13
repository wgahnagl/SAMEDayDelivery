package app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by evan on 3/20/19.
 *
 * NOTE: A lot of these methods throw SQLExceptions here. At first glance that might seem
 * to defeat the purpose of this class. The general principle here is that exceptions are
 * thrown all the way up to the application level so that the application can decide what
 * to do with them (i.e. display it to the customer, fail silently, try again, whatever).
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
        try {
            Class.forName (JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
     * These methods should not really ever be called during normal program execution.
     * They exist to be called by backend programmers when the format of the DB is
     * updated significantly enough that a full refresh is called for. */

    public static void setupDB() {
        System.out.println("Setting up the DB ");

        try {
            /* Systematically destroy and then re-create every table in the database */
            setupCarrierTable();
            setupWarehouseTable();
            setupTripTable();
            setupSpecialInfoTable();

            setupCustomerTable();
            setupPackageTable();

            setupCustomerBankAccountTable();
            setupCustomerCreditCardTable();
            setupCustomerPhoneTable();
            setupPackageSpecialInfoTable();

        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        System.out.println("Finished creating the database");
    }

    private static void setupPackageTable() throws SQLException {
        statement.execute("drop table Package if exists;");
        statement.execute("create table Package (" +
                "ID                   int primary key," +

                "origin_customer_id   int," +     // origin_customer_id and dest_customer_id are both
                "dest_customer_id     int," +     // foreign keys to the customer table (this constraint is declared below)

                "ship_timestamp       timestamp," +
                "delivery_timestamp   timestamp," +
                "expected_delivery    timestamp," +

                "size                 varchar(16)," +
                "type                 varchar(16)," +
                "weight               numeric(7,3)," + // Weight in kilograms, up to 9999 kg and accurate to the gram

                "price                numeric(6,2), " + // Price in dollars, up to $9999 and accurate to the cent
                "signature            image, " +

                "foreign key (origin_customer_id) references Customer(id)," +
                "foreign key (dest_customer_id) references Customer(id)" +

                ");");
    }

    private static void setupCustomerTable() throws SQLException {
        statement.execute("drop table Customer if exists;");
        statement.execute("create table Customer (" +
                "ID int primary key," +

                "last_name varchar(255)," +
                "first_name varchar(255)," +
                "email varchar(255)," +

                "addr_line1 varchar(1024)," + // You wouldn't think addresses could get this long, but they can.
                "addr_line2 varchar(1024)," +
                "city varchar(255)," +
                "province varchar(255)," + // When the country is "USA", the province is the state
                "zipcode varchar(255)," + // zipcodes can begin with 0 and can contain dashes
                "country varchar(255)," +
                ");"
        );

        // No addr_line2's in the csv, so put in null
        populateTableFromCSV("customer", "Phase 2/customerLastFirst.csv", "%1, '%2', '%3', '%4', '%5', null, '%6', '%7', '%8', '%9'");
    }

    private static void setupTripTable() throws SQLException {
        statement.execute("drop table Trip if exists;");
        statement.execute("create table Trip(" +
                "ID int primary key, " +

                "start_time timestamp, " +
                "end_time timestamp, " +

                "origin int, " +       // Origin and destination are foreign keys to the warehouse table
                "destination int, " +

                "carrier int, " +
                "fail_flag bit, " +

                "foreign key (origin) references Warehouse(id), " +
                "foreign key (destination) references Warehouse(id), " +
                "foreign key (carrier) references Carrier(id)" +

                ");");
    }

    private static void setupCarrierTable() throws SQLException {
        statement.execute("drop table Carrier if exists");
        statement.execute("create table Carrier(" +
                "ID int primary key, " +
                "type varchar(255), " +
                ");");
    }

    private static void setupWarehouseTable() throws SQLException {
        statement.execute("drop table Warehouse if exists;");
        statement.execute("create table Warehouse(" +
                "ID int primary key, " +
                "address_line1 varchar(255), " +
                "city varchar(255), " +
                "province varchar(255), " +
                "country varchar(255)" +
                ");");

        // ID,addr_line1,city,province,country
        populateTableFromCSV( "Warehouse", "Phase 2/warehouse.csv", "%1, '%2', '%3', '%4', '%5'" );
    }

    private static void setupSpecialInfoTable() throws SQLException {
        statement.execute("drop table SpecialInfo if exists;");
        statement.execute("create table SpecialInfo(" +
                "ID int primary key, " +
                "info varchar(255), " +
                ");");
    }

    private static void setupCustomerPhoneTable() throws SQLException {
        statement.execute("drop table CustomerPhone if exists");
        statement.execute("create table CustomerPhone(" +
                "customer_id int, " +
                "phone_num varchar(255), " +

                "primary key (customer_id, phone_num), "  +
                "foreign key (customer_id) references Customer(ID), " +
                ");");
    }

    private static void setupCustomerBankAccountTable() throws SQLException {
        statement.execute("drop table CustomerBankAccount if exists");
        statement.execute("create table CustomerBankAccount(" +
                "customer_id int, " +
                "acct_number varchar(255)," +

                "primary key (customer_id, acct_number), " +
                "foreign key (customer_id) references customer(ID), " +
                ");");
    }

    private static void setupCustomerCreditCardTable() throws SQLException {
        statement.execute("drop table customerCreditCard if exists");
        statement.execute("create table customerCreditCard(" +
                "customer_id int, " +
                "card_num varchar(255)," +

                "primary key (customer_id, card_num)," +
                "foreign key (customer_id) references Customer(ID), " +
                ");");
    }

    private static void setupPackageSpecialInfoTable() throws SQLException {
        statement.execute("drop table PackageSpecialInfo if exists");
        statement.execute("create table PackageSpecialInfo(" +
                "package_id int, " +
                "special_info_id int," +

                "primary key (package_id, special_info_id)," +
                "foreign key (package_id) references Package(ID), " +
                "foreign key (special_info_id) references SpecialInfo(ID), " +
                ");");
    }


    /* Behind-the-scenes utilities, used only privately */

    private static String escapeSingleQuotes(String value) {
        // H2 needs all instances of ' (one single quote) escaped as '' (two single quotes)
        return value.replaceAll("'", "''");
    }

    private static String formatInputRow(String valueList, String formatString) {
        // Apply a format string like ("%3, %2, %1, '%4', %5")
        // to a list of values like "pie, 32, 42, David Smith, hello"

        String[] values = valueList.split(",");
        String result = formatString;

        for(int i = 0; i < values.length; i++) {
            result = result.replaceFirst( "%" + (i+1), escapeSingleQuotes(values[i]) );
        }

        return result;
    }

    private static void populateTableFromCSV(String tablename, String filename, String reformat) {
        // Helper method to populate a table from a CSV file
        // If reformat parameter is specified, it should look something like "%1, %2, (%3), '%4'"
        // The elements from each line of the CSV will be slotted into the %n symbols

        BufferedReader reader;

        try {
            reader = new BufferedReader( new FileReader(filename)); // FileNotFoundException
            String line;   // Line read from the CSV

            while( (line = reader.readLine()) != null) { // IOException

                if(line.trim().startsWith("//"))   // Allow the CSV to comment out lines with "//"
                    continue;

                if(!reformat.equals(""))  // If specified, apply the format string
                    line = formatInputRow(line, reformat);

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

    private static void populateTableFromCSV(String tablename, String filename) {
        // Two arg-version with no formatting

        populateTableFromCSV(tablename, filename, "");
    }

    private static void prettifyResultSet(ResultSet rs, String format) throws SQLException {
        /* UNDER CONSTRUCTION */

        ArrayList<String> formatted = new ArrayList<>();

        while(rs.next()) {
            // Start with an empty row and build it up one char at a time
            // by consulting the rs and the format string.

            String thisRow = "";
            String formatCopy = format;

            while(!formatCopy.equals("")) {
                switch(formatCopy.charAt(0)) {

                    // For % signs, insert a value from the table
                    case '%':
                        int upTo = formatCopy.indexOf(')');
                        String[] typeVar = formatCopy.substring(2, upTo).split(",");
                        formatCopy = formatCopy.substring(upTo);

                        switch(typeVar[0].toLowerCase()) {
                            case "int":
                            case "d":
                                thisRow += rs.getInt(typeVar[1]);
                        }

                    // Copy the next character literally, no matter what it is
                    case '\\':
                        thisRow += formatCopy.charAt(1);
                        formatCopy = formatCopy.substring(2);
                        break;
                }
            }
        }
    }


    /* General-purpose DB-accessing methods.
     * Use these to implement all the different functionality that is shared between
     * the graphical UI and the command-line UI.
     */

    public static void executeArbitrarySQL(String sql) throws SQLException {
        // This utility, if used at all, will only be accessible to the sysadmin.
        // I don't know if it's a good idea to have even then. We'll see.
        // Also, it should probably return a ResultSet. - Evan (24 Mar 2019)
        statement.execute(sql);
    }

    public static void addCustomer(String lastName, String firstName, String email, String addr_line1, String addr_line2,
                                   String city, String province, String zip, String country) throws SQLException {

        String valuesFmt = "%d,%s,%s,%s,%s,%s,%s,%s,%s,%s"; // Put the parameters into a String
        String rowFmt = "%1, '%2', '%3', '%4', '%5', '%6', '%7', '%8', '%9', '%10'"; // Format that String into a values list
        String insertCmdFmt = "insert into Customer values (%s);"; // Put that values list into an sql statement

        //TODO: is there no auto-increment in H2 ?
        ResultSet maxIdResult = statement.executeQuery("select max(ID) from Customer;");
        maxIdResult.first();
        int maxID = maxIdResult.getInt("MAX(ID)");

        String values = String.format( valuesFmt, maxID+1, lastName, firstName, email, addr_line1, addr_line2, city, province, zip, country);
        String row = formatInputRow( values, rowFmt );
        String insertCmd = String.format( insertCmdFmt, row );

        statement.execute(insertCmd);
    }

    public static void addCreditCard (String name, String number, String expiration, String CVV){
        String valuesFMT = "%d, %s,%s, %s, %s";
        String rowFMT = "%1, %2, %3, %4";
        String insertCmdFmt = "insert into creditCard values (%s);";
    }

    // Todo: Generalize prettyPackageList and prettyCustomerList into prettyResults(ResultSet result, String format)
    // which will take a string like "Package: %(d,id). Ship time: %(timestamp,ship_time)".

    public static String prettyPackageList() {
        // Return a String of a newline-separated list of all the packages in the DB
        // (Consider generalizing this method into a prettyTable method where you pass
        //  it something like ("Package [%d] from %s to %s", "ID", "fromCustomer", "toCustomer"))

        try {
            String result = "";
            ResultSet packages = statement.executeQuery("select ID, ship_timestamp from Package");

            while(packages.next()) {
                int nextID = packages.getInt("ID");
                String nextShipTime = packages.getTimestamp("ship_timestamp").toString();

                result += String.format("Package %d. Ship time: %s\n", nextID, nextShipTime);
            }

            return result;

        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return "<SQL error>";
        }
    }

    public static String prettyCustomerList() {
        // Return a String of a newline-separated list of all the customers in the DB

        try {
            String result = "";
            ResultSet customers = statement.executeQuery("select ID, last_name, first_name from Customer");

            while(customers.next()) {
                int nextID = customers.getInt("ID");
                String nextName = customers.getString("first_name") + " " + customers.getString("last_name");

                result += String.format("Customer #%d (%s)\n", nextID, nextName);
            }

            return result;

        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return "<SQL error>";
        }
    }

    public static ArrayList<String> getCreditCardsForCustomer( int ID ) throws SQLException {

        String sql = String.format("select card_num from CustomerCreditCard where customer_id = %d;", ID);
        ResultSet rs = statement.executeQuery(sql);

        ArrayList<String> result = new ArrayList<>();

        while (rs.next()) {
            result.add(rs.getString("card_num"));
        }

        return result;
    }



    public static ResultSet getLatePackages() throws SQLException {
        return statement.executeQuery("select * from Package where delivery_timestamp is null and expected_delivery < current_timestamp");
    }


    /* Main method for testing only */

    public static void main(String[] args) {
        setupDB();

        //populateTableFromCSV("package", "Phase 2/test_csv");

        System.out.println("PACKAGE TABLE PRINTOUT:");
        System.out.println(prettyPackageList());

        try {
            addCustomer("Rysdam", "Evan", "err2315@g.rit.edu", "49 Mont Vernon Street", "Second floor, room 101", "Milford", "New Hampshire", "03055", "USA");
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        System.out.println("CUSTOMER TABLE PRINTOUT:");
        System.out.println(prettyCustomerList());

        try {
            getLatePackages();
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
