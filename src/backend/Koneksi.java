
package backend;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {
 
    static Connection con;
    
    public static void config(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/brobets", "root", "");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + e.getMessage());
        }
    }
    
    public static Connection getConnection() {
        return con;
    }
    
}
