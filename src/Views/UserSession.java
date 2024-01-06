    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

/**
 *
 * @author MUHAMMAD PAHRUL GUNAWAN
 */
public class UserSession {
    private static int id;
    private static String username,password,nama,alamat;
    
    public static void setId(int id){
        UserSession.id = id;
    }

    public static void setUsername(String username){
        UserSession.username = username;
    }
    
    public static void setPassword(String password){
        UserSession.password = password;
    }
    
    public static void setNama(String nama){
        UserSession.nama = nama;
    }
    
    public static void setAlamat(String alamat){
        UserSession.alamat = alamat;
    }
    
    public int getId(){
        return id;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getNama(){
        return nama;
    }
    
    public String getAlamat(){
        return alamat;
    }
}
