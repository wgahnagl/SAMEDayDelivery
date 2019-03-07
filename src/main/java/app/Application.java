package app;

import static spark.Spark.*;
import java.sql.*;

/* As of 3 Mar 2019, the majority of this class is a copy of the H2 example code
   from tutorialspoint.com.  See it here:
   https://www.tutorialspoint.com/h2_database/h2_database_jdbc_connection.htm
 */

public class Application {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./db";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "pa";

    public static String addVisit(String address) {
        // Adds a row of (ID, address, timestamp) to the visits table
        // (Used next available ID, given address, and current timestamp)

        Connection conn = null;
        Statement stmt = null;
        String sql;

        String printedTable = "ID  | ADDRESS    | TIME<br/>------------------------------";

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER); // No clue what this does --Evan

            // STEP 2: Open a connection
            System.out.println("Connecting to the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // STEP 3: Execute a query
            System.out.println("Querying visits table to get next available id...");
            sql = "SELECT * FROM visits ORDER BY ID";
            ResultSet pastVisits = stmt.executeQuery(sql);
            pastVisits.last();
            int last_id = pastVisits.getInt("id");
            pastVisits.close();

            // Add new row to the table, using next available id
            System.out.println("Adding new row to the visits table...");
            int id = last_id + 1;
            long timeStamp = System.currentTimeMillis() / 1000;
            sql = String.format("INSERT INTO visits VALUES(%d, %s, %d)",
                    id, address, timeStamp);
            stmt.executeUpdate(sql);

            // Get the updated table
            System.out.println("Getting the updated table to display");
            sql = "SELECT * FROM visits ORDER BY ID DESC";
            ResultSet allVisits = stmt.executeQuery(sql);

            // Create display-friendly table
            while(allVisits.next()) {
                // Retrieve by column name
                int printID  = allVisits.getInt("id");
                String printAddress = allVisits.getString("address");
                int printTimeStamp = allVisits.getInt("timestamp");

                // Display values
                String printedLine = String.format("<br/>%-3d | %-10s | %d", printID, printAddress, printTimeStamp);
                printedTable += printedLine;
            }
            allVisits.close();

        } catch(SQLException se) {
            // Handle errors for JDBC
            return se.getMessage();
        } catch(Exception e) {
            // Handle errors for Class.forName
            return e.getMessage();
        } finally {
            // finally block used to close resources
            try {
                if(stmt != null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn != null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try

        return printedTable;
    }

    public static void main(String[] args) {
        get("/", (req, res) ->
                String.format("<pre>h--hewwo??? owo;;; mistew posteman???"
                        + "<br/><br/>"
                        + "Anyway, here's your recent visits:"
                        + "<br/><br/>%s",

                        addVisit("null")));
    }
}

//mondi was here ... 