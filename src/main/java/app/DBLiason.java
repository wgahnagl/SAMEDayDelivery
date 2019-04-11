
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

                "size                 varchar(16)," +
                "type                 varchar(16)," +
                "weight               numeric(7,3)," + // Weight in kilograms, up to 9999 kg and accurate to the gram

                "price                numeric(6,2), " + // Price in dollars, up to $9999 and accurate to the cent
                "signature            image, " +

                "foreign key (origin_customer_id) references customer(id)," +
                "foreign key (dest_customer_id) references customer(id)" +

                ");");
    }

    private static void setupCustomerTable() throws SQLException {
        statement.execute("drop table customer if exists;");
        statement.execute("create table customer (" +
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

                "unique (email)," +
                          
                ");"
        );

        // No addr_line2's in the csv, so put in null
        populateTableFromCSV("customer", "Phase 2/customerLastFirst.csv", "%1, '%2', '%3', '%4', '%5', null, '%6', '%7', '%8', '%9'");
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
                "acct_number varchar(255)," +

                "primary key (customer_id, acct_number), " +
                "foreign key (customer_id) references customer(ID), " +
                ");");
    }

    private static void setupCustomerHasCreditCardTable() throws SQLException {
        statement.execute("drop table customerHasCreditCard if exists");
        statement.execute("create table customerHasCreditCard(" +
                "customer_id int, " +
                "card_num varchar(255)," +

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

    private static String formatInputRow(String valueList, String formatString) {
        // Apply a format string like ("%3, %2, %1, '%4', %5")
        // to a list of values like "pie, 32, 42, David Smith, hello"

        // TODO: Rename this method to "formatCommand" since there's no reason it can't
        // be that general. Then change all the usages to match.  (It is already used the new
        // way in getCustomerByAddress). Also, switch the arguments to be consistent
        // with the String.format() frame.

        String[] values = valueList.split(",");
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
     *    Create account:            addCustomerByInfo()
     *    Add customer from package: addCustomerByAddr()
     *    Link address:              linkAddress()
     */

    private static int currentMaxCustomerID() throws SQLException {
        ResultSet maxIdResult = statement.executeQuery("select max(ID) from customer;");
        maxIdResult.first();
        return maxIdResult.getInt("MAX(ID)");
    }
        
    private static void addCustomerByInfo( String lastName, String firstName, String email ) throws SQLException {
        String valuesFmt = "%d,%s,%s,%s"; // Put parameters into a String
        String rowFmt = "%1, '%2', '%3', '%4', null, null, null, null, null, null";
        String insertCmdFmt = "insert into customer values (%s);";

        int maxID = currentMaxCustomerID();
        
        String values = String.format( valuesFmt, maxID + 1, lastName, firstName, email );
        String row = formatInputRow( values, rowFmt );
        String insertCmd = String.format( insertCmdFmt, row );

        statement.execute( insertCmd );
    }

    private static void addCustomerByAddr( String addr_line1, String addr_line2,String city, String province, String zipcode, String country ) throws SQLException {
        String valuesFmt = "%d,%s,%s,%s,%s,%s,%s";
        String rowFmt = "%1, null, null, null, '%2', '%3', '%4', '%5', '%6', '%7'";
        String insertCmdFmt = "insert into customer values (%s);";

        int maxID = currentMaxCustomerID();

        String values = String.format( valuesFmt, maxID + 1, addr_line1, addr_line2,
                                       city, province, zipcode, country );
        String row = formatInputRow( values, rowFmt );
        String insertCmd = String.format( insertCmdFmt, row );

        statement.execute( insertCmd );
    }

    private static int getCustomerByEmail( String email ) throws SQLException {
        String valuesFmt = "%s";
        String cmdFmt = "select * from customer where email = '%1';";

        String values = String.format( valuesFmt, email );
        String cmd = formatInputRow( values, cmdFmt );

        ResultSet result = statement.executeQuery( cmd );
        if (!result.first()) return -1; // I this will never happen
        return result.getInt("ID");
    }   
    
    private static int getCustomerByAddr( String addr_line1, String addr_line2, String city, String province, String zipcode, String country ) throws SQLException {
        String valuesFmt = "%s,%s,%s,%s,%s,%s";
        String cmdFmt = "select * from customer where " +
            "addr_line1 = '%1' and " +
            "addr_line2 = '%2' and " +
            "city = '%3' and " +
            "province = '%4' and " +
            "zipcode = '%5' and " +
            "country = '%6' " +
            ";";
        
        String values = String.format( valuesFmt, addr_line1, addr_line2, city, province,
                                       zipcode, country );
        String cmd = formatInputRow( values, cmdFmt );

        ResultSet result = statement.executeQuery( cmd );
        if(!result.first()) return -1; // No such customer
        return result.getInt("ID");
    }

    private static boolean linkAddress( String email, String addr_line1, String addr_line2, String city, String province, String zipcode, String country ) throws SQLException {
        // TODO (Or maybe it can be left alone)
        // This method is written with the admittedly naive assumption that the customer
        // was honest (and not mistaken) about their address. If it finds a customer A with
        // the right email and another customer B with the right address, it copies the
        // address info to A and deletes B *INDISCRIMINANTLY*.  Thus if A typed in the
        // address of some other customer, that customer will be deleted (!).

        int emailID = getCustomerByEmail( email );
        int addrID = getCustomerByAddr( addr_line1, addr_line2, city,
                                        province, zipcode, country );

        if(emailID < 0) // Existing customer not found
            return false;

        if( emailID == addrID ) // This could mean either that the address is already linked (or something else)
            return false;


        // Set the address for the acting customer (this happens regardless of whether a null customer was found for the address)

        String valuesFmt = "%s,%s,%s,%s,%s,%s,%d";
        String cmdFmt = "update customer set " +
            "addr_line1 = '%1',   " +
            "addr_line2 = '%2',   " +
            "city = '%3',         " +
            "province = '%4',     " +
            "zipcode = '%5',      " +
            "country = '%6'       " +
            "where ID = %7 ;      " ;

        String values = String.format( valuesFmt, addr_line1, addr_line2, city, province, zipcode, country, emailID );
        String cmd = formatInputRow( values, cmdFmt );

        statement.execute( cmd );


        // Delete any null customers
        // Todo: Also update all other tables to relink addrID to emailID

        if( addrID < 0 )
            return true; // No need to raise error just because no address was deleted

        valuesFmt = "%d";
        cmdFmt = "delete from customer where ID = %1;";

        values = String.format( valuesFmt, addrID );
        cmd = formatInputRow( values, cmdFmt );

        statement.execute( cmd );
        
        return true;
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

    public static ArrayList<String> getCreditCardsForCustomer( int ID ) throws SQLException {

        String sql = String.format("select card_num from customerHasCreditCard where customer_id = %d;", ID);
        ResultSet rs = statement.executeQuery(sql);

        ArrayList<String> result = new ArrayList<>();

        while (rs.next()) {
            result.add(rs.getString("card_num"));
        }

        return result;
    }



    public static ResultSet getLatePackages() throws SQLException {
        return statement.executeQuery("select * from package where delivery_timestamp is null and expected_delivery < current_timestamp");
    }


    /* Main method for testing only */

    public static void main(String[] args) {
        setupDB();

        String first_name = "Evan";
        String last_name = "Rysdam";
        String email = "err2315@rit.edu";
        String addr_line1 = "60 Colony Manor Drive";
        String addr_line2 = "apt 109";
        String city = "Rochester";
        String province = "New York";
        String zipcode = "14623";
        String country = "USA";

        try {
            addCustomerByInfo(last_name, first_name, email);
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
            getLatePackages();
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
