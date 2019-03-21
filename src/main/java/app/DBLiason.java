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
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./db";

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "pa";


    /* DB SETUP METHODS
       These methods should not really ever be called during normal program execution.
       They exist to be called by backend programmers when the format of the DB is
       updated significantly enough that a full refresh is called for.

       FORMAT
       The setupDB() method calls in sequence each of the methods below it, which
       should all follow this format:

           private static void setup<TableName>Table (Statement stmt) throws SQLException {
               <Code to delete the table>
               <Code to recreate the table>
               <Code to populate the table with our sample data>
           }
     */

    public static void setupDB() {
        System.out.println("Setting up the CB ");

        Connection conn = null;
        Statement stmt = null;
        String sql;

        try {
            System.out.println("Connecting to the database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            /* Systematically destroy and then re-create every table in the database */

            setupPackageTable(stmt);

        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        System.out.println("Finished creating the database");
    }

    private static void setupPackageTable(Statement stmt) throws SQLException {
        stmt.execute("drop table package;");
        stmt.execute("create table package (" +
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
