/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author MUHAMMAD PAHRUL GUNAWAN
 */
public class koneksi {
    public Connection con;
    public Statement stat;
    
    public koneksi(){
        try{
            //Memanggil Librarie 
            Class.forName("com.mysql.jdbc.Driver");
            //Mendeklarasikan Koneksi ke database
            con = DriverManager.getConnection("jdbc:mysql://localhost/toko_buku","root","");
            stat = con.createStatement();
            System.out.println("Sukses");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(0);
        }
    }
}
