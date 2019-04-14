
package app;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
    // that the variables are final, yet their initialization can throw an Exception)
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


    public enum Expediency {
        OVERNIGHT ("overnight",1.75),
        TWODAY    ("two-day",1.25),
        REGULAR   ("regular", 1.00);

        String name;
        double priceMultiplier;
        Expediency( String _name, double _priceMultiplier ) {
            this.name = _name;
            this.priceMultiplier = _priceMultiplier;
        }
    }

    public enum PackageType {
        // In the future, the package types could be expanded to e.g. "10in x 10in" or "length + width < 100in"
        // Note that envelopes are not letters but package-sized envelopes
        ENVELOPE_SMALL ("envelope - small", 500,  5000),
        ENVELOPE_LARGE ("envelope - large", 800, 10000),
        PACKAGE_SMALL  ("package - small", 1500, 20000),
        PACAKGE_MEDIUM ("package - medium",2000, 40000),
        PACKAGE_LARGE  ("package - large", 3000,120000);

        String name;
        int basePriceCents;
        int maxWeightGrams;
        PackageType( String _name, int _basePriceCents, int _maxWeightGrams ) {
            this.name = _name;
            this.basePriceCents = _basePriceCents;
            this.maxWeightGrams = _maxWeightGrams;
        }
    }


    /* DB SETUP METHODS
     * These methods should not really ever be called during normal program execution.
     * They exist to be called by backend programmers when the format of the DB is
     * updated significantly enough that a full refresh is called for.
     */

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

            setupCustomerHasBankAccountTable();
            setupCustomerHasCreditCardTable();
            setupCustomerHasPhoneTable();
            setupPackageHasSpecialInfoTable();

        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        System.out.println("Finished creating the database");
    }

    private static void setupPackageTable() throws SQLException {
        statement.execute("drop table package if exists;");
        statement.execute("create table package (" +
                "ID                   int primary key," +

                "origin_customer_id   int," +     // origin_customer_id and dest_customer_id are both
                "dest_customer_id     int," +     // foreign keys to the customer table (this constraint is declared below)

                "ship_timestamp       timestamp," +
                "delivery_timestamp   timestamp," +
                "expected_delivery    timestamp," +

                "type                 varchar(16)," +
                "weight               numeric(7,3)," + // Weight in kilograms, up to 9999 kg and accurate to the gram

                "price                numeric(6,2), " + // Price in dollars, up to $9999 and accurate to the cent
                "receiver_pays        boolean, " + // True if package is "pre-paid" (receiver pays)
                "paid_for             boolean, " + // True when the package has been paid for (can be false for monthly-billed customers)
                "signature            image, " +

                "foreign key (origin_customer_id) references customer(id)," +
                "foreign key (dest_customer_id) references customer(id)" +

                ");");
    }
    private static void setupCustomerTable() throws SQLException {
        statement.execute("drop table customer if exists;");
        statement.execute("create table customer (" +
                "ID int primary key," +

                "email varchar(255)," +
                "password varchar(255)," +

                "last_name varchar(255)," +
                "first_name varchar(255)," +

                "addr_line1 varchar(1024)," + // You wouldn't think addresses could get this long, but they can.
                "addr_line2 varchar(1024)," +
                "city varchar(255)," +
                "province varchar(255)," + // When the country is "USA", the province is the state
                "zipcode varchar(255)," + // zipcodes can begin with 0 and can contain dashes
                "country varchar(255)," +

                "unique (email)," +
                          
                ");"
        );

        // CSV Order: ID, lastname, firstname, email, addr_line1, city, province, zip_code, country
        // The CSV does not contain some columns, so default values should be used:
        //    password:     "password"
        //    addr_line2:   null

        populateTableFromCSV("customer", "Phase 2/customerLastFirst.csv",
                "%1, '%4', 'password', '%2', '%3', '%5', null, '%6', '%7', '%8', '%9'");
    }
    private static void setupTripTable() throws SQLException {
        statement.execute("drop table trip if exists;");
        statement.execute("create table trip(" +
                "ID int primary key, " +

                "start_time timestamp, " +
                "end_time timestamp, " +

                "origin int, " +       // Origin and destination are foreign keys to the warehouse table
                "destination int, " +

                "carrier int, " +
                "fail_flag bit, " +

                "foreign key (origin) references warehouse(id), " +
                "foreign key (destination) references warehouse(id), " +
                "foreign key (carrier) references carrier(id)" +

                ");");
    }
    private static void setupCarrierTable() throws SQLException {
        statement.execute("drop table carrier if exists");
        statement.execute("create table carrier(" +
                "ID int primary key, " +
                "type varchar(255), " +
                ");");
    }
    private static void setupWarehouseTable() throws SQLException {
        statement.execute("drop table warehouse if exists;");
        statement.execute("create table warehouse(" +
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
        statement.execute("drop table specialInfo if exists;");
        statement.execute("create table specialinfo(" +
                "ID int primary key, " +
                "info varchar(255), " +
                ");");
    }
    private static void setupCustomerHasPhoneTable() throws SQLException {
        statement.execute("drop table customerHasPhone if exists");
        statement.execute("create table customerHasPhone(" +
                "customer_id int, " +
                "phone_num varchar(255), " +

                "primary key (customer_id, phone_num), "  +
                "foreign key (customer_id) references customer(ID), " +
                ");");
    }
    private static void setupCustomerHasBankAccountTable() throws SQLException {
        statement.execute("drop table customerHasBankAccount if exists");
        statement.execute("create table customerHasBankAccount(" +
                "customer_id int, " +
                "acct_num varchar(255)," +
                "routing_num varchar(255)," +

                "primary key (customer_id, acct_num), " +
                "foreign key (customer_id) references customer(ID), " +
                ");");
    }
    private static void setupCustomerHasCreditCardTable() throws SQLException {
        statement.execute("drop table customerHasCreditCard if exists");
        statement.execute("create table customerHasCreditCard(" +
                "customer_id int, " +

                "card_name varchar(255)," +
                "card_num varchar(255)," +
                "card_expiration varchar(7)," +
                "card_cvv varchar(4)," +

                "primary key (customer_id, card_num)," +
                "foreign key (customer_id) references customer(ID), " +
                ");");
    }
    private static void setupPackageHasSpecialInfoTable() throws SQLException {
        statement.execute("drop table packageHasSpecialInfo if exists");
        statement.execute("create table packageHasSpecialInfo(" +
                "package_id int, " +
                "special_info_id int," +

                "primary key (package_id, special_info_id)," +
                "foreign key (package_id) references package(ID), " +
                "foreign key (special_info_id) references specialInfo(ID), " +
                ");");
    }


    /* Behind-the-scenes utilities, used only privately */

    private static String escapeSingleQuotes(String value) {
        // H2 needs all instances of ' (one single quote) escaped as '' (two single quotes)
        return value.replaceAll("'", "''");
    }

    private static String formatCommand(String formatString, String ... values) {
        // Apply a format string like "select %2 from customer where first_name = '%1' and id < %3"
        // to a list of values like "ID", "Evan", "400"

        // The only difference between this command and String.format is that this command filters
        // all of its arguments through the escapeSingleQuotes method (and, in the future, any other
        // methods necessary to sanitize an input for entry into the database)

        // Todo: Might want to use varargs, though then I'd need to manually cast args to strings before entering

        String result = formatString;

        for(int i = 0; i < values.length; i++) {
            result = result.replaceFirst( "%" + (i+1), escapeSingleQuotes(values[i]) );
        }

        return result;
    }

    private static void populateTableFromCSV(String tablename, String filename, String reformat) {
        // Helper method to populate a table from a CSV file
        // If reformat parameter is specified, it should look something like "'%1', %2, (%3)"
        // The elements from each line of the CSV will be slotted into the %n symbols

        BufferedReader reader;

        try {
            reader = new BufferedReader( new FileReader(filename)); // FileNotFoundException
            String line;   // Line read from the CSV

            while( (line = reader.readLine()) != null) { // IOException

                if(line.trim().startsWith("//"))   // Allow  CSV to comment out lines w/ "//"
                    continue;

                if(!reformat.equals(""))  // If specified, apply the format string
                    line = formatCommand(reformat, line.split(","));

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

    private static ArrayList<String> prettifyResultSet( String format, ResultSet rs ) throws SQLException {
        ArrayList<String> formatted = new ArrayList<>();
        rs.first();

        while(rs.next()) {
            // Start with an empty row and build it up one char at a time
            // by consulting the rs and the format string.

            String thisRow = "";
            String formatCopy = format;

            while( !formatCopy.equals("") ) {

                switch( formatCopy.charAt(0) ) {

                    // For % signs, insert a value from the table
                    case '%':
                        int upTo = formatCopy.indexOf(')'); // Technically this is a bug as "MAX(ID)" is a valid column name
                        String[] typeVar = formatCopy.substring(2, upTo).split(",");
                        formatCopy = formatCopy.substring(upTo+1);

                        switch(typeVar[0].toLowerCase()) {
                            case "int":
                            case "d":
                                thisRow += rs.getInt(typeVar[1]);
                                break;

                            case "str":
                            case "string":
                            case "s":
                                thisRow += rs.getString(typeVar[1]);
                                break;

                            case "timestamp":
                                thisRow += rs.getTimestamp(typeVar[1]).toString();
                                break;

                            default:
                                throw new RuntimeException("Error inside prettifyResultSet(): Unrecognized type <" + typeVar[0] + ">");
                        }
                        break;

                    // Copy the next character literally, no matter what it is
                    case '\\':
                        thisRow += formatCopy.charAt(1);
                        formatCopy = formatCopy.substring(2);
                        break;

                    default:
                        thisRow += formatCopy.charAt(0);
                        formatCopy = formatCopy.substring(1);
                        break;
                }
            }

            formatted.add(thisRow);
        }

        return formatted;
    }

    private static String asLines( ArrayList<String> lines ) {
        if(lines.isEmpty()) return "";

        String result = "";
        for( String l : lines ) {
            result += "\n";
            result += l;
        }

        return result.substring(1);
    }


    /* Manipulations of the customer table
     * LOW-LEVEL FUNCTIONS ONLY: LINK UI TO THESE FUNCTIONS WITH CAUTION, IF AT ALL
     */

    private static int currentMaxCustomerID() throws SQLException {
        ResultSet maxIdResult = statement.executeQuery("select max(ID) from customer;");
        maxIdResult.first();
        return maxIdResult.getInt("MAX(ID)");
    }

    private static int getCustomerByEmail( String email ) throws SQLException {
        // Return the ID of a customer with a given email address, or -1 if no such customer is found
        // (There should never be more than one customer with the same email address)

        String cmdFmt = "select ID from customer where email = '%1';";
        String cmd = formatCommand( cmdFmt, email );

        ResultSet result = statement.executeQuery( cmd );
        if (!result.first()) return -1; // This should never happen
        return result.getInt("ID");
    }

    private static int getCustomerByAddr( String addr_line1, String addr_line2, String city, String province, String zipcode, String country ) throws SQLException {
        // Return the ID of the customer with a given address, or -1 if no such customer is found
        // (There should never (?) be more than one customer with the same address)

        String cmdFmt = "select ID from customer where " +
                "addr_line1 = '%1' and " +
                "addr_line2 = '%2' and " +
                "city = '%3' and " +
                "province = '%4' and " +
                "zipcode = '%5' and " +
                "country = '%6' " +
                ";";

        ;
        String cmd = formatCommand( cmdFmt, addr_line1, addr_line2, city, province, zipcode, country );

        ResultSet result = statement.executeQuery( cmd );
        if(!result.first()) return -1; // No such customer
        return result.getInt("ID");
    }

    private static int addCustomerByInfo( String lastName, String firstName, String email, String password ) throws SQLException {
        // Add a customer with only an email, password, lastname, and firstname
        // Return the ID of the newly-created customer

        int id = currentMaxCustomerID() + 1;

        String cmdFmt = "insert into customer values(%1, '%2', '%3', '%4', '%5', null, null, null, null, null, null);";
        String insertCmd = formatCommand( cmdFmt, Integer.toString(id), email, password, lastName, firstName);

        statement.execute( insertCmd );
        return id;
    }

    private static int addCustomerByAddr( String addr_line1, String addr_line2,String city, String province, String zipcode, String country ) throws SQLException {
        // Add a null customer with only an address (no email, password, lastname, or firstname)
        // Return the ID of the newly-created customer

        int id = currentMaxCustomerID() + 1;

        String cmdFmt = "insert into customer values (%1, null, null, null, null, '%2', '%3', '%4', '%5', '%6', '%7');";
        String cmd = formatCommand( cmdFmt, Integer.toString(id), addr_line1, addr_line2, city, province, zipcode, country );

        statement.execute( cmd );
        return id;
    }

    private static int ensureAddressExists( String addr_line1, String addr_line2, String city, String province, String zipcode, String country ) throws SQLException {
        // Make sure there is a customer with the given address (create a null-customer if necessary)
        // Return the ID of the (possibly-newly-created) customer

        int id = getCustomerByAddr( addr_line1, addr_line2, city, province, zipcode, country );
        if(id >= 0) return id;
        return addCustomerByAddr( addr_line1, addr_line2, city, province, zipcode, country );
    }

    private static boolean linkAddress( String email, String addr_line1, String addr_line2, String city, String province, String zipcode, String country ) throws SQLException {
        // Find the customer with the given email and fill in their address as the given address
        // If there exists a null customer with the given address, delete it
        // Todo: Also update all other tables to relink addrID to emailID

        // TODO (Or maybe it can be left alone)
        // This method is written with the admittedly naive assumption that the customer
        // was honest (and not mistaken) about their address. If it finds a customer A with
        // the right email and another customer B with the right address, it copies the
        // address info to A and deletes B *INDISCRIMINANTLY*.  Thus if A typed in the
        // address of some other customer, that customer will be deleted (!).

        int emailID = getCustomerByEmail( email );
        int addrID = getCustomerByAddr( addr_line1, addr_line2, city, province, zipcode, country );

        if(emailID < 0) // Acting customer not found = Error
            return false;

        if( emailID == addrID ) // This could mean either that the address is already linked (or something else) = Error
            return false;


        // Set the address for the acting customer (this happens regardless of whether a null customer was found for the address)

        String cmdFmt = "update customer set " +
            "addr_line1 = '%1',   " +
            "addr_line2 = '%2',   " +
            "city = '%3',         " +
            "province = '%4',     " +
            "zipcode = '%5',      " +
            "country = '%6'       " +
            "where ID = %7 ;      " ;

        String cmd = formatCommand( cmdFmt, addr_line1, addr_line2, city, province, zipcode, country, Integer.toString(emailID) );

        statement.execute( cmd );


        // Delete any null customers

        if( addrID < 0 )
            return true; // No need to raise error just because no address was deleted

        cmdFmt = "delete from customer where ID = %1;";
        cmd = formatCommand( cmdFmt, Integer.toString(addrID) );


        statement.execute( cmd );
        
        return true;
    }

    private static boolean linkCreditCard( String email, String card_name, String card_num, String expiration, String cvv ) throws SQLException {
        // Link a given customer account to a given credit card number (this DOES NOT delete previously-linked cards)
        // Returns true on success, false on failure

        int id = getCustomerByEmail( email );
        if(id < 0) return false;

        String cmdFmt = "insert into customerHasCreditCard values (%1, '%2', '%3', '%4', '%5');";
        String cmd = formatCommand( cmdFmt, Integer.toString(id), card_name, card_num, expiration, cvv);

        statement.execute( cmd );
        return true;
    }

    private static boolean linkBankAccount( String email, String acct_num, String routing_num ) throws SQLException {
        // Link a given customer account to a bank account number (this DOES delete any previously-linked accounts)
        // Returns true on success, false on failure

        int id = getCustomerByEmail( email );
        if(id < 0) return false;

        String cmdFmt = "delete from customerHasBankAccount where customer_id = %1;";
        String cmd = formatCommand( cmdFmt, Integer.toString(id) );
        statement.execute( cmd );

        cmdFmt = "insert into customerHasBankAccount values( %1, '%2', '%3' );";
        cmd = formatCommand( cmdFmt, Integer.toString(id), acct_num, routing_num );
        statement.execute( cmd );

        return true;
    }

    private static boolean linkPhoneNumber( String email, String phone_num ) throws SQLException {
        // Link a given customer account to a bank account number (this DOES NOT delete any previously-linked phone numbers)
        // Returns true on success, false on failure

        int id = getCustomerByEmail( email );
        if(id < 0) return false;

        String cmdFmt = "insert into customerHasPhone values( %1, '%2' );";
        String cmd = formatCommand( cmdFmt, Integer.toString(id), phone_num );

        statement.execute( cmd );
        return true;
    }


    /* Manipulations of the package table
     * LOW-LEVEL FUNCTIONS ONLY: LINK UI TO THESE FUNCTIONS WITH CAUTION, IF AT ALL
     */

    private static int currentMaxPackageID() throws SQLException {
        ResultSet maxIdResult = statement.executeQuery( "select max(ID) from package;");
        maxIdResult.first();
        return maxIdResult.getInt("MAX(ID)");
    }

    private static int computeDaysBetween( int origin_customer_id, int dest_customer_id ) {
        // Todo: implement this method for real
        return 3;
    }

    private static int computePrice( PackageType type, int weight_in_grams, Expediency expediency ) {
        double priceCents = type.basePriceCents; // For example, 500.00 would be **5** dollars (not 500 dollars)
        priceCents *= expediency.priceMultiplier;
        if( weight_in_grams > type.maxWeightGrams ) priceCents *= 1.5;
        
        return (int) priceCents; // Round down to nearest cent
    }

    private static void createPackage(int origin_customer_id, int dest_customer_id,
                                         long ship_timestamp, Expediency expediency,
                                         PackageType type, int weight_in_grams,
                                         boolean receiver_pays, boolean already_paid ) throws SQLException {

        /* There are a lot of fields in the package table. Here's how each of them is filled in:
         *
         *      ID:                     next available id
         *
         *      origin_customer_id:     passed as argument
         *      dest_customer_id:       passed as argument
         *
         *      ship_timestamp:         passed as argument
         *      delivery_timestamp:     null (gets set when package is delivered)
         *      expected_delivery:      computed from ship_timestamp and expediency*
         *
         *      type:                   passed as argument
         *      weight:                 passed as argument
         *
         *      price:                  computed from type, weight, and expediency*
         *      receiver_pays:          passed as argument
         *      paid_flag:              passed as argument**
         *      signature:              null (gets set when the package is signed for)
         *
         *
         *      * "expediency" is passed as an argument, but is not present in the package table
         *      ** The paid_flag will be true if the customer pays with a credit card and false if they bill monthly to an account
         */

        int id = currentMaxPackageID() + 1;

        int days = expediency == Expediency.OVERNIGHT ? 1 :
                   expediency == Expediency.TWODAY ? 2 :
                   computeDaysBetween( origin_customer_id, dest_customer_id );

        int price = computePrice( type, weight_in_grams, expediency );

        String cmdFmt = "insert into package values(" +

                "%1, " +

                "%2, " +
                "%3, " +

                "dateadd(second, %4, '1970-01-01'), " + // convert unix time to regular time
                "null, " +
                "dateadd(day, %5, dateadd(second, %4, '1970-01-01'))," + // convert unix time to regular time and then add shipping time

                "%6, " +
                "%7, " +

                "%8, " +
                "%9, " +
                "%10, " +
                "null);";

        String cmd = formatCommand( cmdFmt,

                Integer.toString(id),

                Integer.toString(origin_customer_id),
                Integer.toString(dest_customer_id),

                Long.toString(ship_timestamp), Integer.toString(days),

                type.name,
                Double.toString(weight_in_grams / 1000.0 ),

                Double.toString( price / 100.0 ),
                Boolean.toString(receiver_pays),
                Boolean.toString(already_paid));

        statement.execute( cmd );
    }

    private static void scanPackage(
            String o_addr_line1, String o_addr_line2, String o_city, String o_province, String o_zipcode, String o_country,
            String d_addr_line1, String d_addr_line2, String d_city, String d_province, String d_zipcode, String d_country,
            Expediency expediency, PackageType type, int weight_in_grams, boolean receiver_pays, boolean already_paid
    ) throws SQLException {
        /* There are a lot of fields in the package table. Here's how each of them is filled in:
         *
         *      ID:                     next available id
         *
         *      origin_customer_id:     derived from origin address***
         *      dest_customer_id:       derived from destination address***
         *
         *      ship_timestamp:         current time
         *      delivery_timestamp:     null (gets set when package is delivered)
         *      expected_delivery:      derived from ship_timestamp and expediency*
         *
         *      type:                   given on package label (can be verified manually if needed)
         *      weight:                 given on package label (can be verified manually if needed)
         *
         *      price:                  computed from type, weight, and expediency*
         *      receiver_pays:          given on package label
         *      paid_flag:              given on package label**
         *      signature:              null (gets set when the package is signed for)
         *
         *
         *      * "expediency" is given on the package label, but is not present in the package table
         *      ** The paid_flag will be true if the customer pays with a credit card and false if they bill monthly to an account
         *      *** The elements of the origin and destination addresses are on the label, but not present in the package table
         */

        int origin_customer_id = ensureAddressExists(o_addr_line1, o_addr_line2, o_city, o_province, o_zipcode, o_country);
        int dest_customer_id = ensureAddressExists(d_addr_line1, d_addr_line2, d_city, d_province, d_zipcode, d_country);

        createPackage(origin_customer_id, dest_customer_id,
                System.currentTimeMillis()/1000, expediency,
                type, weight_in_grams,
                receiver_pays, already_paid);
    }


    /* Methods to get pretty-prints of various tables and subset of tables */

    public static String prettyPackageList() {
        // Return a String of a newline-separated list of all the packages in the DB

        ArrayList<String> prettified;

        try {
            ResultSet packages = statement.executeQuery("select ID, ship_timestamp from package");
            prettified = prettifyResultSet( "PackageID #%(d,ID) (shipped at #%(timestamp,ship_timestamp))", packages );
            return asLines(prettified);
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return "<SQL error>";
        }
    }

    public static String prettyCustomerList() {
        // Return a String of a newline-separated list of all the customers in the DB

        ArrayList<String> prettified;

        try {
            ResultSet packages = statement.executeQuery("select ID, last_name, first_name from customer");
            prettified = prettifyResultSet( "Customer #%(d,ID): %(s,first_name) %(s,last_name)", packages );
            return asLines(prettified);
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return "<SQL error>";
        }
    }

    public static String prettyCustomerAddressList() {
        // Return a String of a newline-separated list of all the customers in the DB and their addresses

        ArrayList<String> prettified;

        try {
            ResultSet packages = statement.executeQuery("select ID, last_name, first_name, addr_line1, addr_line2, city, province, zipcode, country from customer");
            prettified = prettifyResultSet( "Customer #%(d,ID): %(s,first_name) %(s,last_name) \t >> " +
                    "%(s,addr_line1) (%(s,addr_line2)) | %(s,city), %(s,province) %(s,zipcode): %(s,country)", packages );
            return asLines(prettified);
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return "<SQL error>";
        }
    }

    public static String prettyEmailPasswordList() {
        // Return a String of a newline-separated list of all email and password combinations

        ArrayList<String> prettified;

        try {
            ResultSet packages = statement.executeQuery("select email, password from customer");
            prettified = prettifyResultSet( "%(s,email), %(s,password)", packages );
            return asLines(prettified);
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            return "<SQL error>";
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

    public static boolean checkPassword( String email, String password ) throws SQLException {
        // Return true if the customer with the given email has the given password
        // ( Returns false if the password is incorrect or if no such customer exists )

        String cmdFmt = "select ID from customer where email = '%1' and password = '%2';";
        String cmd = formatCommand( cmdFmt, email, password );

        ResultSet rs = statement.executeQuery( cmd );

        if( rs.first() ) // If there exists a customer with this email and password, the password is correct
            return true;

        return false;
    }

    public static HashMap<String, String> getBankAccountForCustomer( String email ) throws SQLException {
        int id = getCustomerByEmail( email );
        if(id < 0) return null;

        String cmdFmt = "select * from customerHasBankAccount where customer_id = %1;";
        String cmd = formatCommand( cmdFmt, Integer.toString(id) );

        ResultSet rs = statement.executeQuery( cmd );
        rs.first();

        HashMap<String, String> result = new HashMap<>();
        for(String attribute : new String[] {"acct_num", "routing_num"})
            result.put(attribute, rs.getString(attribute));

        return result;
    }

    public static ArrayList<HashMap<String, String>> getCreditCardsForCustomer( String email ) throws SQLException {
        int id = getCustomerByEmail( email );
        if(id < 0) return null;

        String cmdFmt = "select * from customerHasCreditCard where customer_id = %1;";
        String cmd = formatCommand( cmdFmt, Integer.toString(id) );
        ResultSet rs = statement.executeQuery( cmd );

        ArrayList<HashMap<String, String>> result = new ArrayList<>();

        while (rs.next()) {
            HashMap<String, String> thisCard = new HashMap<>();
            for(String attribute : new String[] {"card_num", "card_name", "card_expiration", "card_cvv"})
                thisCard.put(attribute, rs.getString(attribute));
            result.add(thisCard);
        }

        return result;
    }

    public static ArrayList<String> getPhoneNumbersForCustomer( String email ) throws SQLException {
        int id = getCustomerByEmail( email );
        if(id < 0) return null;

        String cmdFmt = "select phone_num from customerHasPhone where customer_id = %1;";
        String cmd = formatCommand( cmdFmt, Integer.toString(id) );
        ResultSet rs = statement.executeQuery( cmd );

        ArrayList<String> result = new ArrayList<>();

        while(rs.next()) {
            result.add(rs.getString("phone_num"));
        }

        return result;
    }

    public static HashMap<String, String> getAddressForCustomer( String email ) throws SQLException {
        int id = getCustomerByEmail( email );
        if(id < 0) return null;

        String cmdFmt = "select addr_line1, addr_line2, city, province, zipcode, country from customer where id = %1;";
        String cmd = formatCommand( cmdFmt, Integer.toString(id) );

        ResultSet rs = statement.executeQuery( cmd );
        rs.first();

        HashMap<String, String> address = new HashMap<String, String>();
        for(String key : new String[] {"addr_line1", "addr_line2", "city", "province", "zipcode", "country"}) {
            address.put( key, rs.getString(key) );
        }

        return address;
    }



    /* Specific query utilities */

    public static ResultSet getLatePackages() throws SQLException {
        return statement.executeQuery("select * from package where delivery_timestamp is null and expected_delivery < current_timestamp");
    }


    /* Main method for testing only */

    public static void main(String[] args) {
        setupDB();

        String first_name = "Evan";
        String last_name = "Rysdam";
        String email = "err2315@rit.edu";
        String password = "evan-is-great";
        String addr_line1 = "60 Colony Manor Drive";
        String addr_line2 = "apt 109";
        String city = "Rochester";
        String province = "New York";
        String zipcode = "14623";
        String country = "USA";

        try {
            addCustomerByInfo(last_name, first_name, email, password);
            addCustomerByAddr(addr_line1, addr_line2, city, province, zipcode, country);
            linkAddress(email, addr_line1, addr_line2, city, province, zipcode, country);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        System.out.println("PACKAGE TABLE PRINTOUT:");
        System.out.println(prettyPackageList());

        System.out.println("CUSTOMER TABLE PRINTOUT:");
        System.out.println(prettyCustomerAddressList());

        try {
            System.out.println();
            System.out.println("Evan's password is \"password\":       " + checkPassword( "err2315@rit.edu", "password" ));
            System.out.println("Evan's password is \"evan-is-great\":  " + checkPassword( "err2315@rit.edu", "evan-is-great"));
            System.out.println("Trying with incorrect email address: " + checkPassword( "err2315@g.rit.edu", "evan-is-great"));
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            String evan = "err2315@rit.edu";
            String des = "desiree310@verizon.net";


            linkCreditCard( des, "Des Dessy", "4", "01/22", "123" );
            linkCreditCard( evan, "Evan R V Rysdam", "1", "11/22", "456");
            linkCreditCard( des, "Des Dessy", "2", "02/69", "122");

            linkBankAccount( evan, "99", "99.1" );
            linkBankAccount( des, "98", "98.1" );
            linkBankAccount( evan, "97", "97.1" );

            linkPhoneNumber( evan, "603-721-9458");
            linkPhoneNumber( des, "111-111-1111");
            linkPhoneNumber( des, "222-222-2222");


            ArrayList<HashMap<String, String>> evanCards = getCreditCardsForCustomer( evan );
            ArrayList<HashMap<String, String>> desCards = getCreditCardsForCustomer( des );

            ArrayList<String> evanPhone = getPhoneNumbersForCustomer( evan );
            ArrayList<String> desPhone = getPhoneNumbersForCustomer( des );

            HashMap<String, String> evanAcct = getBankAccountForCustomer( evan );
            HashMap<String, String> desAcct = getBankAccountForCustomer( des );


            System.out.println();
            System.out.print("\nEvan's cards:");
            for(HashMap<String, String> h : evanCards)
                System.out.print( String.format(" (%s, %s, %s, %s)",
                        h.get("card_name"), h.get("card_num"),
                        h.get("card_expiration"), h.get("card_cvv")) );


            System.out.println();
            System.out.print("\nDes's cards:");
            for(HashMap<String, String> h : desCards)
                System.out.print( String.format(" (%s, %s, %s, %s)",
                        h.get("card_name"), h.get("card_num"),
                        h.get("card_expiration"), h.get("card_cvv")) );

            System.out.println();
            System.out.println();
            System.out.println( String.format("Evan's bank account: %s, %s", evanAcct.get("acct_num"), evanAcct.get("routing_num")) );
            System.out.println( String.format("Des's bank account: %s, %s", desAcct.get("acct_num"), desAcct.get("routing_num")) );

            System.out.println();
            System.out.print("Evan's phone numbers:");
            for(String s : evanPhone) System.out.print(" " + s);
            System.out.println();

            System.out.print("Des's phone numbers:");
            for(String s : desPhone) System.out.print(" " + s);
            System.out.println();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            System.out.println();
            HashMap<String, String> evanAddress = getAddressForCustomer( email );

            System.out.println("EVAN'S ADDRESS");
            for( String key : evanAddress.keySet() ) {
                System.out.println( String.format("%s: %s", key, evanAddress.get(key)) );
            }

        } catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
    }
}
