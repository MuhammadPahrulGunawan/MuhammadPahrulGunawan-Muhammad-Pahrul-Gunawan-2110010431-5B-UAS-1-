/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.*;
import net.sf.jasperreports.view.*;
/**
 *
 * @author MUHAMMAD PAHRUL GUNAWAN
 */
public class Penjualan extends javax.swing.JInternalFrame {

    /**
     * Creates new form Penjualan
     */
    private koneksi kon = new koneksi();
    private String sql;
    private Connection con;
    private Statement stat;
    private ResultSet rs;
    private DefaultTableModel model,model2;
    private int id,rowindex;
    private UserSession session = new UserSession();
    private JasperReport jasper;
    private JasperPrint print;
    
    public Penjualan() {
        initComponents();
        con = kon.con;
        stat = kon.stat;
        // Mengambil data buku
        databuku();
        // Membuat nota transaksi baru
        nota();
        // Menetapkan tanggal transaksi
        tgl();
        // Mengatur tampilan header tabel ke tengah
        ((DefaultTableCellRenderer) TabelTransaksi.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        // Menampilkan nama kasir sesuai sesi pengguna
        NamaKasir.setText(session.getNama());
        // Menonaktifkan beberapa komponen (mati() dan mengatur qty dan btnHapus)
        mati();
        btnHapus.setEnabled(false);
        qty.setEnabled(false);
        // Mengatur font
        font();
        // Menghilangkan panel utara pada internal frame (UI tweak)
        ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        // Menetapkan batas tepi internal frame dengan garis hitam
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        try {
            // Membaca tema dari file "tema.txt"
            FileReader fr = new FileReader("src/tema.txt");
            BufferedReader br = new BufferedReader(fr);
            // Memeriksa tema yang dibaca dari file
            if (br.readLine().equals("Light")) {
                // Tidak ada tindakan khusus jika tema adalah "Light"
            } else {
                // Mengaplikasikan tema khusus jika tema bukan "Light"
                tema();
            }
            // Menutup BufferedReader dan FileReader setelah digunakan
            br.close();
            fr.close();
            } catch (Exception e) {
            // Menangani pengecualian dengan menampilkan pesan kesalahan
            JOptionPane.showMessageDialog(null, "Error reading theme from file: " + e.getMessage());
        }
    }
    
    //Methode untuk membuat Text menjadi biru pada saat tema Gelap diterapkan
    private void tema(){
        try{
            jPanel1.setBackground(Color.BLACK);
            jPanel2.setBackground(Color.BLACK);
            jPanel3.setBackground(Color.BLACK);
            jPanel2.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
            jPanel3.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
            Gambar.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
            this.setBackground(Color.BLACK);
            this.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
            jLabel1.setForeground(Color.BLUE);
            jLabel2.setForeground(Color.BLUE);
            jLabel3.setForeground(Color.BLUE);
            jLabel4.setForeground(Color.BLUE);
            jLabel5.setForeground(Color.BLUE);
            jLabel6.setForeground(Color.BLUE);
            jLabel7.setForeground(Color.BLUE);
            jLabel8.setForeground(Color.BLUE);
            jLabel9.setForeground(Color.BLUE);
            jLabel10.setForeground(Color.BLUE);
            Tanggal.setForeground(Color.BLUE);
            jLabel13.setForeground(Color.BLUE);
            NamaKasir.setForeground(Color.BLUE);
            jLabel15.setForeground(Color.BLUE);
            jLabel19.setForeground(Color.BLUE);
            jLabel20.setForeground(Color.BLUE);
            jLabel20.setForeground(Color.BLUE);
            TabelBuku.setBackground(Color.BLACK);
            TabelBuku.setForeground(Color.BLUE);
            TabelBuku.getTableHeader().setForeground(Color.BLACK);
            TabelTransaksi.setBackground(Color.BLACK);
            TabelTransaksi.setForeground(Color.BLUE);
            TabelTransaksi.getTableHeader().setForeground(Color.BLACK);
            btnTambah.setForeground(Color.BLUE);
            btnSubmit.setForeground(Color.BLUE);
            btnHapus.setForeground(Color.BLUE);
            ImageIcon icon = new ImageIcon("src/Gambar/icons8-exit-30.png");
            jButton4.setIcon(icon);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    //Methode untuk mengatur Font pada saat project di run
    private void font(){
        try{
            File file = new File("src/font");
            if(file.canRead()){
                String[] namafile = file.list();
                FileInputStream fis = new FileInputStream("src/font/"+namafile[0]);   
                FileInputStream fis1 = new FileInputStream("src/font/"+namafile[4]);
                Font font = Font.createFont(Font.TRUETYPE_FONT, fis);
                Font font1 = Font.createFont(Font.TRUETYPE_FONT, fis1);
                Font sizeFont = font.deriveFont(24f);
                Font sizeFont1 = font1.deriveFont(14f);
                TabelBuku.setFont(sizeFont1);
                TabelBuku.getTableHeader().setFont(sizeFont1);
                TabelTransaksi.setFont(sizeFont1);
                TabelTransaksi.getTableHeader().setFont(sizeFont1);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void databuku(){
        int no = 1;
        String[] judul = {"No", "Judul", "Penulis", "Harga"};
        // Menggunakan try-with-resources untuk memastikan penutupan otomatis
        try (ResultSet rs = stat.executeQuery("SELECT * FROM buku")) {
        model = new DefaultTableModel(null, judul) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        while (rs.next()) {
            String[] data = {Integer.toString(no++), rs.getString("judul"), rs.getString("penulis"), rs.getString("harga_jual")};
            model.addRow(data);
        }
        TabelBuku.setModel(model);
        // Mengatur tampilan header tabel ke tengah
        ((DefaultTableCellRenderer) TabelBuku.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        // Mengatur seluruh sel dalam tabel ke tengah
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < TabelBuku.getColumnCount(); i++) {
            TabelBuku.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        } catch (SQLException e) {
        // Menangani pengecualian dengan menampilkan pesan kesalahan
        JOptionPane.showMessageDialog(null, "Error retrieving book data: " + e.getMessage());
        }
    }
    
    private void dataqty(){
        qty.removeAllItems();
        try{
            sql = "SELECT stok FROM buku WHERE id='"+id+"'";
            rs = stat.executeQuery(sql);
            while(rs.next()){
               for(int stok=1;stok<=rs.getInt("stok");stok++){
                   qty.addItem(stok);
               }
            }
        }catch(Exception e){
            
        }
    }
    //Menghitung jumlah dari tabel
    private void hitung(){
        int jumlah,total = 0;
        for(int i= 0;i<TabelTransaksi.getRowCount();i++){
         jumlah =  Integer.valueOf(TabelTransaksi.getValueAt(i, 4).toString());
         total += jumlah ;
        }
        TotalHarga.setText(Integer.toString(total));
    }
    //Mengatur Nomor nota secara otomatis
    private void nota(){
       Nota.setEnabled(false);
       try{
           sql = "SELECT MAX(RIGHT(nota,4)) as nota FROM penjualan ORDER BY nota DESC";
           rs = stat.executeQuery(sql);
           if(rs.next()){
               String nota = rs.getString("nota").substring(1);
               String nonota = ""+(Integer.parseInt(nota)+1);
               String nol = "";
               if(nonota.length()==1){
                   nol = "000";
               }else if(nonota.length()==2){
                   nol = "00";
               }else if(nonota.length()==3){
                   nol = "0";
               }else if(nonota.length()==4){
                   nol = "";
               }
               Nota.setText("TB"+nol+nonota);
           }else{
              Nota.setText("TB0001");
           }
       }catch(Exception e){
           JOptionPane.showMessageDialog(null, e.getMessage());
       }
    }
    //Mengatur Hari secara otomatis
    private void tgl(){
       ActionListener al = new ActionListener(){
           public void actionPerformed(ActionEvent a){
               int hari;
               String namahari=null;
               DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
               Date date = new Date();
               hari = date.getDay();
               switch(hari){
                   case 0:
                       namahari = "Minggu";
                       break;
                   case 1:
                       namahari = "Senin";
                       break;
                   case 2:
                       namahari = "Selasa";
                       break;
                   case 3:
                       namahari = "Rabu";
                       break;
                   case 4:
                       namahari = "Kamis";
                       break;
                   case 5:
                       namahari = "Jumat";
                       break;
                   case 6:
                       namahari = "Sabtu";
                       break;
               }
               Tanggal.setText(namahari+","+df.format(date));
           }
       };
       Timer timer = new Timer(1000,al);
       timer.start();
    }
    
    private void mati(){
        Judul.setEnabled(false);
        ISBN.setEnabled(false);
        Penulis.setEnabled(false);
        Penerbit.setEnabled(false);
        Harga.setEnabled(false);
        TotalHarga.setEnabled(false);
        Kembalian.setEnabled(false);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        Nota = new javax.swing.JTextField();
        Tanggal = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        NamaKasir = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelBuku = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        Judul = new javax.swing.JTextField();
        ISBN = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Penulis = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        Penerbit = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        Harga = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        qty = new javax.swing.JComboBox();
        btnTambah = new javax.swing.JButton();
        Gambar = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelTransaksi = new javax.swing.JTable();
        TotalHarga = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        Tunai = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        Kembalian = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setPreferredSize(new java.awt.Dimension(1363, 741));

        jLabel10.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 255));
        jLabel10.setText("NO.NOTA");

        Nota.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Nota.setForeground(new java.awt.Color(0, 0, 255));
        Nota.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Tanggal.setBackground(new java.awt.Color(255, 255, 255));
        Tanggal.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        Tanggal.setForeground(new java.awt.Color(0, 0, 255));
        Tanggal.setText("TANGGAL");

        jLabel13.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 255));
        jLabel13.setText("NAMA KASIR :");

        NamaKasir.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        NamaKasir.setForeground(new java.awt.Color(0, 0, 255));
        NamaKasir.setText("NAMA");

        jPanel1.setBackground(new java.awt.Color(0, 0, 255));

        jLabel15.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-shopping-bag-32.png"))); // NOI18N
        jLabel15.setText("TRANSAKSI PENJUALAN");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-exit-30.png"))); // NOI18N
        jButton4.setContentAreaFilled(false);
        jButton4.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_MacOS_Close_30px_1.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 812, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        TabelBuku.setBackground(new java.awt.Color(0, 0, 255));
        TabelBuku.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        TabelBuku.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        TabelBuku.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TabelBuku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelBukuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelBuku);

        jLabel1.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("JUDUL");

        Judul.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Judul.setForeground(new java.awt.Color(0, 0, 255));
        Judul.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        ISBN.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ISBN.setForeground(new java.awt.Color(0, 0, 255));
        ISBN.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("No ISBN");

        Penulis.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Penulis.setForeground(new java.awt.Color(0, 0, 255));
        Penulis.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("PENULIS");

        Penerbit.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Penerbit.setForeground(new java.awt.Color(0, 0, 255));
        Penerbit.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel4.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("PENERBIT");

        Harga.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Harga.setForeground(new java.awt.Color(0, 0, 255));
        Harga.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setText("HARGA");

        jLabel6.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setText("QTY");

        qty.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        qty.setForeground(new java.awt.Color(0, 0, 255));
        qty.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnTambah.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(0, 0, 255));
        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-add-shopping-cart-32.png"))); // NOI18N
        btnTambah.setText("TAMBAH");
        btnTambah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnTambah.setContentAreaFilled(false);
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        Gambar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ISBN))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(Harga, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(Judul)))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Penerbit)
                            .addComponent(qty, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(Penulis, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Judul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(Penulis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(ISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(Harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(qty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(69, 69, 69))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(Gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTambah))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        TabelTransaksi.setBackground(new java.awt.Color(0, 0, 255));
        TabelTransaksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        TabelTransaksi.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        TabelTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Judul", "Harga", "Qty", "Harga Total"
            }
        ));
        TabelTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelTransaksiMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TabelTransaksi);

        TotalHarga.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        TotalHarga.setForeground(new java.awt.Color(0, 0, 255));
        TotalHarga.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel7.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 255));
        jLabel7.setText("TOTAL HARGA");

        Tunai.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Tunai.setForeground(new java.awt.Color(0, 0, 255));
        Tunai.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Tunai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TunaiKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TunaiKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 255));
        jLabel8.setText("TUNAI");

        Kembalian.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Kembalian.setForeground(new java.awt.Color(0, 0, 255));
        Kembalian.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel9.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 255));
        jLabel9.setText("KEMBALIAN");

        btnSubmit.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(0, 0, 255));
        btnSubmit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-checkout-32.png"))); // NOI18N
        btnSubmit.setText("SUBMIT");
        btnSubmit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSubmit.setContentAreaFilled(false);
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        btnHapus.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(0, 0, 255));
        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-delete-32.png"))); // NOI18N
        btnHapus.setText("HAPUS");
        btnHapus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnHapus.setContentAreaFilled(false);
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 255));
        jLabel19.setText("Rp.");

        jLabel20.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 255));
        jLabel20.setText("Rp.");

        jLabel21.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 255));
        jLabel21.setText("Rp.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel21))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Kembalian, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(TotalHarga))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 42, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Tunai, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(TotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel19)
                                .addComponent(Tunai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20)
                                .addComponent(jLabel8)))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel21)
                            .addComponent(btnHapus))
                        .addGap(50, 50, 50)
                        .addComponent(btnSubmit))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Tanggal)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Nota, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(NamaKasir)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(NamaKasir))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(Nota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addComponent(Tanggal)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TabelBukuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelBukuMouseClicked
        // TODO add your handling code here:
        qty.setEnabled(true);
        try{
            sql = "SELECT * FROM buku WHERE judul='"+TabelBuku.getValueAt(TabelBuku.getSelectedRow(), 1)+"'";
            rs = stat.executeQuery(sql);
            if(rs.next()){
              id = rs.getInt("id");
              String nama = rs.getString("gambar");
              ImageIcon icon = new ImageIcon("src/img/"+nama);
              Rectangle rect = Gambar.getBounds();
              Image scale = icon.getImage().getScaledInstance(rect.width,rect.height,Image.SCALE_SMOOTH);
              icon = new ImageIcon(scale);
              Gambar.setIcon(icon);
              Judul.setText(rs.getString("judul"));
              ISBN.setText(rs.getString("noisbn"));
              Penulis.setText(rs.getString("penulis"));
              Penerbit.setText(rs.getString("penerbit"));
              Harga.setText(rs.getString("harga_jual"));
              dataqty();
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_TabelBukuMouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
    // Menonaktifkan input qty dan tombol hapus
    qty.setEnabled(false);
    btnHapus.setEnabled(false);
    // Memeriksa apakah Judul belum dipilih
    if (Judul.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "SILAHKAN PILIH BUKU YANG INGIN DI TAMBAH");
    } else {
        // Mendapatkan model data dari TabelTransaksi
        DefaultTableModel dataModel = (DefaultTableModel) TabelTransaksi.getModel();
        TabelTransaksi.setAutoCreateColumnsFromModel(true);
        int hargatotal;
        
        hargatotal = Integer.parseInt(Harga.getText()) * Integer.parseInt(qty.getSelectedItem().toString());
        // Menyiapkan data untuk ditambahkan ke tabel
        Object[] data = {"", Judul.getText(), Harga.getText(), qty.getSelectedItem().toString(), Integer.toString(hargatotal)};
        dataModel.addRow(data);
        // Looping untuk menggabungkan baris yang memiliki judul buku yang sama
        for (int barisatas = 0; barisatas < TabelTransaksi.getRowCount(); barisatas++) {
            for (int barisbawah = barisatas + 1; barisbawah < TabelTransaksi.getRowCount(); barisbawah++) {
                if (TabelTransaksi.getValueAt(barisbawah, 1).equals(TabelTransaksi.getValueAt(barisatas, 1))) {
                    int qty = Integer.parseInt(TabelTransaksi.getValueAt(barisatas, 3).toString());
                    TabelTransaksi.setValueAt(qty + 1, barisatas, 3);
                    TabelTransaksi.setValueAt(String.valueOf(Integer.parseInt(Harga.getText()) * Integer.parseInt(TabelTransaksi.getValueAt(barisatas, 3).toString())), barisatas, 4);

                    // Menghapus baris yang sama
                    dataModel.removeRow(barisbawah);
                }
            }
        }
        // Mengatur seluruh kolom tabel menjadi rata tengah
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        TabelTransaksi.getColumnModel().getColumn(0).setCellRenderer(render);
        TabelTransaksi.getColumnModel().getColumn(1).setCellRenderer(render);
        TabelTransaksi.getColumnModel().getColumn(2).setCellRenderer(render);
        TabelTransaksi.getColumnModel().getColumn(3).setCellRenderer(render);
        TabelTransaksi.getColumnModel().getColumn(4).setCellRenderer(render);
        // Mengatur nomor urut pada setiap baris
        for (int i = 0; i < TabelTransaksi.getRowCount(); i++) {
            String nomer = String.valueOf(i + 1);
            dataModel.setValueAt(nomer, i, 0);
        }
        // Menghitung total harga
        hitung();
    }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void TabelTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelTransaksiMouseClicked
       
        btnHapus.setEnabled(true);
        rowindex = TabelTransaksi.convertRowIndexToView(rowindex);
    }//GEN-LAST:event_TabelTransaksiMouseClicked

    private void TunaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TunaiKeyReleased
        // Memeriksa apakah input Tunai kosong
        if (Tunai.getText().length() == 0) {
            // Jika kosong, set Kembalian menjadi kosong juga
            Kembalian.setText("");
        } else {
            // Menghitung jumlah kembalian berdasarkan tunai dan total harga
            int jumlah;
            jumlah = Integer.parseInt(Tunai.getText()) - Integer.parseInt(TotalHarga.getText());
            // Menampilkan jumlah kembalian pada field Kembalian
            Kembalian.setText(Integer.toString(jumlah));
        }
    }//GEN-LAST:event_TunaiKeyReleased

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
    // Memeriksa apakah field Tunai kosong
    if (Tunai.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Ada data yang kosong");
    } else if (Kembalian.getText().substring(0, 1).equals("-")) {
        // Memeriksa apakah uang tunai kurang
        JOptionPane.showMessageDialog(null, "Uang tunai kurang");
    } else {
        try {
            // Menyimpan data penjualan ke tabel penjualan
            sql = "INSERT INTO penjualan(nota,id_kasir,tanggal) VALUE('" + Nota.getText() + "','" + session.getId() + "',CURDATE())";
            stat.execute(sql);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        // Mengupdate stok buku dan menyimpan detail penjualan
        for (int i = 0; i < TabelTransaksi.getRowCount(); i++) {
            int stok, hasil, tot;
            try {
                // Mendapatkan stok buku dari database
                sql = "SELECT stok FROM buku WHERE judul='" + TabelTransaksi.getValueAt(i, 1) + "'";
                rs = stat.executeQuery(sql);
                if (rs.next()) {
                    stok = rs.getInt("stok");
                    hasil = stok - Integer.parseInt(TabelTransaksi.getValueAt(i, 3).toString());
                    // Mengupdate stok buku
                    sql = "UPDATE buku SET stok='" + hasil + "' WHERE judul='" + TabelTransaksi.getValueAt(i, 1) + "'";
                    stat.execute(sql);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            try {
                // Menyimpan detail penjualan ke tabel penjualan_detail
                sql = "INSERT INTO penjualan_detail(id_penjualan,id_buku,qty) VALUE ((SELECT MAX(id) FROM penjualan),(SELECT id FROM buku WHERE judul='"
                        + TabelTransaksi.getValueAt(i, 1) + "'),'" + TabelTransaksi.getValueAt(i, 3) + "')";
                stat.execute(sql);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        try {
            // Menampilkan konfirmasi cetak struk
            if (JOptionPane.showConfirmDialog(null, "TRANSAKSI BERHASIL. INGIN CETAK STRUK?", "Pesan", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                String namafile = "src/Views/cetak.jasper";
                File file = new File(namafile);
                HashMap hm = new HashMap();
                hm.put("nota", Nota.getText());
                hm.put("tunai", Integer.parseInt(Tunai.getText()));
                hm.put("totalharga", Integer.parseInt(TotalHarga.getText()));
                jasper = (JasperReport) JRLoader.loadObject(file);
                print = JasperFillManager.fillReport(jasper, hm, con);
                // Menampilkan struk menggunakan JasperViewer
                JasperViewer.viewReport(print, false);
                JasperViewer.setDefaultLookAndFeelDecorated(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
      }
    }//GEN-LAST:event_btnSubmitActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
    // Menghapus baris terpilih dari tabel transaksi
    ((DefaultTableModel) TabelTransaksi.getModel()).removeRow(TabelTransaksi.getSelectedRow());
    // Menyusun ulang nomor urut pada kolom pertama
    for (int i = 0; i < TabelTransaksi.getRowCount(); i++) {
        String nomer = String.valueOf(i + 1);
        TabelTransaksi.setValueAt(nomer, i, 0);
    }
    // Menghitung ulang total harga setelah menghapus
    hitung();
    // Menonaktifkan tombol hapus setelah penghapusan
    btnHapus.setEnabled(false);
    }//GEN-LAST:event_btnHapusActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void TunaiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TunaiKeyTyped
    if(Character.isAlphabetic(evt.getKeyChar())){
            JOptionPane.showMessageDialog(null, "Hanya karakter angka yang diperbolehkan");
            evt.consume();
        }
    }//GEN-LAST:event_TunaiKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Gambar;
    private javax.swing.JTextField Harga;
    private javax.swing.JTextField ISBN;
    private javax.swing.JTextField Judul;
    private javax.swing.JTextField Kembalian;
    private javax.swing.JLabel NamaKasir;
    private javax.swing.JTextField Nota;
    private javax.swing.JTextField Penerbit;
    private javax.swing.JTextField Penulis;
    private javax.swing.JTable TabelBuku;
    private javax.swing.JTable TabelTransaksi;
    private javax.swing.JLabel Tanggal;
    private javax.swing.JTextField TotalHarga;
    private javax.swing.JTextField Tunai;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JComboBox qty;
    // End of variables declaration//GEN-END:variables
}
