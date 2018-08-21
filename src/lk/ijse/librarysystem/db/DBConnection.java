/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.librarysystem.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author badhr
 */
public class DBConnection {
    private Connection connection;
    private static DBConnection dbconn;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","noentrance");
    }
    public static DBConnection getDBConnection() throws ClassNotFoundException, SQLException{
        if(dbconn == null){
            dbconn = new DBConnection();
        }
        return dbconn;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }
    
}
