package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
            setupPackageTable();

        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        System.out.println("Finished creating the database");
    }

    private static void setupPackageTable() throws SQLException {
        statement.execute("drop table package;");
        statement.execute("create table package (" +
                "ID int primary key," +
                "shiptime timestamp" +
                ");");
    }


    /* General-purpose DB-accessing methods.
     * Use these to implement all the different functionality that is shared between
     * the graphical UI and the command-line UI.
     */




    /* Main method for testing only */

    public static void main(String[] args) {
        setupDB();
    }
}
