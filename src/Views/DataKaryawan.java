/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import javax.swing.*;
import java.sql.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.*;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author MUHAMMAD PAHRUL GUNAWAN
 */
public class DataKaryawan extends javax.swing.JInternalFrame {

    /**
     * Creates new form DataKaryawan
     */
    // Deklarasi objek koneksi untuk menghubungkan aplikasi dengan database
    private koneksi kon = new koneksi();
    // Deklarasi objek-objek terkait dengan pengolahan database
    private Connection con; // Objek koneksi ke database
    private Statement stat; // Objek statement untuk menjalankan query SQL
    private ResultSet rs;   // Objek ResultSet untuk menyimpan hasil query
    private DefaultTableModel model; // Objek model untuk tabel
    private String sql;    // String untuk menyimpan query SQL
    private String klik;   // String untuk menyimpan informasi suatu tindakan pengguna
    private int id = 0;    // Variabel untuk menyimpan nilai id yang digunakan dalam operasi CRUD

    
    public DataKaryawan() {
        initComponents();
        // Mengatur judul internal frame
        setTitle("DATA KARYAWAN");
        // Mengambil koneksi dan statement dari objek koneksi
        con = kon.con;
        stat = kon.stat;
        // Mengatur tampilan tabel
        aturTable();
        // Mengisi data combo box
        dataCB();
        // Menyembunyikan objek tertentu
        sembunyiOB();
        // Menyembunyikan elemen lain
        sembunyi();
        // Mengatur tampilan font
        font();
        // Menghilangkan judul internal frame
        ((BasicInternalFrameUI)this.getUI()).setNorthPane(null);
        // Mengatur border internal frame
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        try {
            // Membaca tema dari file
            FileReader fr = new FileReader("src/tema.txt");
            BufferedReader br = new BufferedReader(fr);
            // Jika tema bukan "Light", terapkan tema khusus
            if (!br.readLine().equals("Light")) {
                tema();
            }        
        } catch (Exception e) {
            // Tangani exception jika terjadi kesalahan saat membaca tema dari file
            JOptionPane.showConfirmDialog(null, e.getMessage());
        }
    }
    
    private void font() {
    try {
        // Membaca daftar file font dari direktori "src/font"
        File file = new File("src/font");
        // Memeriksa apakah direktori dapat dibaca
        if (file.canRead()) {
            // Mendapatkan daftar nama file di dalam direktori
            String[] namafile = file.list();
            // Membuka file font dengan FileInputStream
            FileInputStream fis = new FileInputStream("src/font/" + namafile[0]);
            FileInputStream fis1 = new FileInputStream("src/font/" + namafile[4]);
            // Membuat objek Font dari file font
            Font font = Font.createFont(Font.TRUETYPE_FONT, fis);
            Font font1 = Font.createFont(Font.TRUETYPE_FONT, fis1);
            // Mengatur ukuran font
            Font sizeFont = font.deriveFont(24f);
            Font sizeFont1 = font1.deriveFont(14f);
            // Mengatur font TabelKaryawan dan header tabel
            TabelKaryawan.setFont(sizeFont1);
            TabelKaryawan.getTableHeader().setFont(sizeFont1);
        }
    } catch (Exception e) {
        // Menangani exception jika terjadi kesalahan saat memproses font
        JOptionPane.showConfirmDialog(null, e.getMessage());
    }
}
    //Metode untuk memberikan warna BIRU pada setiap Text pada saat Tema GELAP di terapkan
    private void tema(){
        this.setBackground(Color.BLACK);
        jPanel1.setBackground(Color.BLACK);
        jLabel2.setForeground(Color.BLUE);
        jLabel3.setForeground(Color.BLUE);
        jLabel4.setForeground(Color.BLUE);
        jLabel5.setForeground(Color.BLUE);
        jLabel6.setForeground(Color.BLUE);
        jLabel7.setForeground(Color.BLUE);
        btnCari.setForeground(Color.BLUE);
        btnTambah.setForeground(Color.BLUE);
        btnEdit.setForeground(Color.BLUE);
        btnHapus.setForeground(Color.BLUE);
        btnOk.setForeground(Color.BLUE);
        btnBatal.setForeground(Color.BLUE);
        btnCetak.setForeground(Color.BLUE);
        TabelKaryawan.setBackground(Color.BLACK);
        TabelKaryawan.setForeground(Color.BLUE);
        TabelKaryawan.getTableHeader().setForeground(Color.BLACK);
        ImageIcon icon = new ImageIcon("src/Gambar/icons8-exit-30.png");
        jButton6.setIcon(icon);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
    }
    
    //Metode untuk membuat Item dalam ComboBox yaitu "Owner & Kasir"
    private void dataCB(){
        ItemHak.removeAllItems();
        String[] data = {"Owner","Kasir"};
        for(String a : data){
           ItemHak.addItem(a); 
        }
    }
    
    //Method untuk button Batal
    private void reset(){
        User.setText("");
        Pass.setText("");
        Name.setText("");
        Alamat.setText("");
    }
    
    private void sembunyi(){
    // Menonaktifkan komponen-komponen pada antarmuka pengguna
    User.setEnabled(false);      // TextField untuk username
    Pass.setEnabled(false);      // PasswordField untuk password
    Name.setEnabled(false);      // TextField untuk nama
    Alamat.setEnabled(false);    // TextField untuk alamat
    ItemHak.setEnabled(false);   // ComboBox untuk hak akses
    jCheckBox1.setEnabled(false); // CheckBox untuk status aktif
    }
    
    //Methode untuk Menyembunyiskan Button Tambah,Edit,dan Hapus
    private void sembunyiCRUD(){
        btnTambah.setEnabled(false);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
    }
    
    private void sembunyiOB(){
        btnOk.setEnabled(false);
        btnBatal.setEnabled(false);
    }
    
    //Methode untuk Menampilkan Texfield ketika button Tambah di klik
    private void tampil(){
        User.setEnabled(true);
        Pass.setEnabled(true);
        Name.setEnabled(true);
        Alamat.setEnabled(true);
        ItemHak.setEnabled(true);
        jCheckBox1.setEnabled(true);
    }
    
    private void tampilCRUD(){
        btnTambah.setEnabled(true);
        btnEdit.setEnabled(true);
        btnHapus.setEnabled(true);
    }
    
    private void tampilOB(){
        btnOk.setEnabled(true);
        btnBatal.setEnabled(true);
    }
    
    private void aturTable() {
    int no = 1;
    String[] judul = {"No", "Username", "Password", "Nama", "Alamat", "Hak Akses"};
    // Menggunakan DefaultTableModel dengan overridden isCellEditable
    model = new DefaultTableModel(null, judul) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    try {
        // Query untuk mengambil data dari tabel kasir
        sql = "SELECT * FROM kasir";
        rs = stat.executeQuery(sql);
        // Menambahkan data ke model
        while (rs.next()) {
            String[] data = {String.valueOf(no++), rs.getString("username"),
                    rs.getString("password"), rs.getString("nama"),
                    rs.getString("alamat"), rs.getString("hakakses")};
            model.addRow(data);
        }
        // Menetapkan model ke tabel
        TabelKaryawan.setModel(model);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    // Mengatur horizontal alignment pada header tabel dan seluruh kolom
    ((DefaultTableCellRenderer) TabelKaryawan.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    DefaultTableCellRenderer render = new DefaultTableCellRenderer();
    render.setHorizontalAlignment(JLabel.CENTER);
    // Mengatur horizontal alignment untuk setiap kolom
    for (int i = 0; i < judul.length; i++) {
        TabelKaryawan.getColumnModel().getColumn(i).setCellRenderer(render);
    }
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelKaryawan = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        User = new javax.swing.JTextField();
        Name = new javax.swing.JTextField();
        Alamat = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        ItemHak = new javax.swing.JComboBox();
        jSeparator2 = new javax.swing.JSeparator();
        btnCari = new javax.swing.JLabel();
        Cari = new javax.swing.JTextField();
        btnCetak = new javax.swing.JButton();
        Pass = new javax.swing.JPasswordField();
        jCheckBox1 = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1363, 600));
        setRequestFocusEnabled(false);

        jPanel1.setBackground(new java.awt.Color(0, 0, 255));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-hard-working-32.png"))); // NOI18N
        jLabel7.setText("DATA KARYAWAN");

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-exit-30.png"))); // NOI18N
        jButton6.setContentAreaFilled(false);
        jButton6.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_MacOS_Close_30px_1.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TabelKaryawan.setBackground(new java.awt.Color(0, 0, 255));
        TabelKaryawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TabelKaryawan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelKaryawanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelKaryawan);

        jLabel2.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("USERNAME");

        jLabel3.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("PASSWORD");

        jLabel4.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("NAMA");

        jLabel5.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setText("ALAMAT");

        jLabel6.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setText("HAK AKSES");

        User.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        User.setForeground(new java.awt.Color(0, 0, 255));
        User.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Name.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Name.setForeground(new java.awt.Color(0, 0, 255));
        Name.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Alamat.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Alamat.setForeground(new java.awt.Color(0, 0, 255));
        Alamat.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnTambah.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(0, 0, 255));
        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-add-32.png"))); // NOI18N
        btnTambah.setText("TAMBAH");
        btnTambah.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnTambah.setContentAreaFilled(false);
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(0, 0, 255));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-edit-32.png"))); // NOI18N
        btnEdit.setText("EDIT");
        btnEdit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnEdit.setContentAreaFilled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
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

        btnOk.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnOk.setForeground(new java.awt.Color(0, 0, 255));
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-ok-32.png"))); // NOI18N
        btnOk.setText("OK");
        btnOk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnOk.setContentAreaFilled(false);
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnBatal.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnBatal.setForeground(new java.awt.Color(0, 0, 255));
        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-cancel-32.png"))); // NOI18N
        btnBatal.setText("BATAL");
        btnBatal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnBatal.setContentAreaFilled(false);
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        ItemHak.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        ItemHak.setForeground(new java.awt.Color(0, 0, 255));
        ItemHak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));

        btnCari.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnCari.setForeground(new java.awt.Color(0, 0, 255));
        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-search-32.png"))); // NOI18N
        btnCari.setText("CARI");
        btnCari.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        Cari.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Cari.setForeground(new java.awt.Color(0, 0, 255));
        Cari.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CariKeyTyped(evt);
            }
        });

        btnCetak.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnCetak.setForeground(new java.awt.Color(0, 0, 255));
        btnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-printer-32.png"))); // NOI18N
        btnCetak.setText("CETAK DATA KARYAWAN");
        btnCetak.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCetak.setContentAreaFilled(false);
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        Pass.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Pass.setForeground(new java.awt.Color(0, 0, 255));
        Pass.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Pass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                PassKeyTyped(evt);
            }
        });

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(Cari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(112, 112, 112)
                        .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Pass, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
                                    .addComponent(User)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Name)
                                    .addComponent(Alamat))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(ItemHak, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBatal, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                .addGap(229, 229, 229))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jSeparator2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(User, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jCheckBox1))
                    .addComponent(jLabel3)
                    .addComponent(Pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(Alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(ItemHak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah)
                        .addComponent(btnEdit)
                        .addComponent(btnHapus)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnBatal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCetak)
                    .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        //Method yang telah dibuat tinggal panggil saja kesini
        klik = "tambah";
        reset();
        sembunyiCRUD();
        tampilOB();
        tampil();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    // Memeriksa apakah tidak ada data yang dipilih
    if (Integer.toString(id).equals("0")) {
        JOptionPane.showMessageDialog(null, "Silahkan pilih data yang akan diubah");
    } else {
        // Jika ada data yang dipilih, siapkan UI untuk pengeditan
        klik = "edit";
        // Menyembunyikan tombol CRUD
        sembunyiCRUD();
        // Menampilkan tombol OK dan Batal serta mengaktifkan input data
        tampilOB();
        // Mengaktifkan input data pada form
        tampil();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
    // Memeriksa apakah tidak ada data yang dipilih
    if (Integer.toString(id).equals("0")) {
        JOptionPane.showMessageDialog(null, "Silahkan pilih data yang akan dihapus");
    } else {
        // Memunculkan konfirmasi penghapusan
        if (JOptionPane.showConfirmDialog(null, "Apakah yakin ingin menghapus data ini?", "Peringatan", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                // Query untuk menghapus data berdasarkan id
                sql = "DELETE FROM kasir WHERE id='" + id + "'";
                stat.execute(sql);
                // Menampilkan pesan sukses hapus data
                JOptionPane.showMessageDialog(null, "Sukses hapus data");
                // Mengupdate tampilan tabel
                model.fireTableDataChanged();
                model.getDataVector().removeAllElements();
                aturTable();               
                // Mengembalikan form ke kondisi awal
                reset();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // Inisialisasi id menjadi 0, menandakan bahwa tidak ada data yang dipilih
        id = 0;
        // Menyembunyikan elemen-elemen form pengisian data
        sembunyi();
        // Mengembalikan form ke kondisi awal (reset)
        reset();
        // Menyembunyikan elemen-elemen yang terkait dengan operasi (OK dan Batal)
        sembunyiOB();
        // Menampilkan elemen-elemen yang terkait dengan operasi CRUD (Tambah, Edit, Hapus)
        tampilCRUD();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
    // Memeriksa apakah operasi yang dilakukan adalah tambah data
if (klik.equals("tambah")) {
    try {
        // Memeriksa keberadaan username dalam database
        sql = "SELECT username FROM kasir WHERE username='" + User.getText() + "'";
        rs = stat.executeQuery(sql);

        if (rs.next()) {
            // Menampilkan pesan jika username sudah ada
            JOptionPane.showMessageDialog(null, "Username sudah ada");
        } else {
            // Menyusun SQL untuk menambahkan data baru ke dalam tabel
            sql = "INSERT INTO kasir(username,password,nama,alamat,hakakses) VALUES ('" + User.getText() + "','" + Pass.getText() + "','" + Name.getText() + "','" + Alamat.getText() + "','" + ItemHak.getSelectedItem().toString() + "')";
            
            // Mengeksekusi perintah SQL untuk menambahkan data
            stat.execute(sql);
            
            // Menampilkan pesan sukses
            JOptionPane.showMessageDialog(null, "Sukses tambah data");
            
            // Memperbarui tampilan tabel dengan data terbaru
            model.fireTableDataChanged();
            model.getDataVector().removeAllElements();
            aturTable();
            
            // Menyembunyikan elemen operasi OK dan Batal, mereset form, menyembunyikan form, dan menampilkan elemen operasi CRUD
            sembunyiOB();
            reset();
            sembunyi();
            tampilCRUD();
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
} else if (klik.equals("edit")) {
    // Jika operasi yang dilakukan adalah edit data
    try {
        // Menyusun SQL untuk memperbarui data yang sudah ada dalam tabel
        sql = "UPDATE kasir SET username='" + User.getText() + "',password='" + Pass.getText() + "',nama='" + Name.getText() + "',alamat='" + Alamat.getText() + "',hakakses='" + ItemHak.getSelectedItem().toString() + "'WHERE id='" + id + "'";     
        // Mengeksekusi perintah SQL untuk memperbarui data
        stat.execute(sql);
        // Menampilkan pesan sukses
        JOptionPane.showMessageDialog(null, "Sukses edit data");
        // Memperbarui tampilan tabel dengan data terbaru
        model.fireTableDataChanged();
        model.getDataVector().removeAllElements();
        aturTable();       
        // Menyembunyikan elemen operasi OK dan Batal, mereset form, menyembunyikan form, dan menampilkan elemen operasi CRUD
        sembunyiOB();
        reset();
        sembunyi();
        tampilCRUD();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void TabelKaryawanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelKaryawanMouseClicked
       try {
        // Mendapatkan nilai username dari baris yang dipilih dalam tabel
        String selectedUsername = TabelKaryawan.getValueAt(TabelKaryawan.getSelectedRow(), 1).toString();

        // Menyusun SQL untuk mendapatkan data karyawan berdasarkan username
        sql = "SELECT * FROM kasir WHERE username='" + selectedUsername + "'";
        rs = stat.executeQuery(sql);

        // Jika data ditemukan, mengisi elemen form dengan data tersebut
        if (rs.next()) {
            id = rs.getInt("id");
            User.setText(rs.getString("username"));
            Pass.setText(rs.getString("password"));
            Pass.setEchoChar('*');  // Menyembunyikan karakter password dengan tanda bintang (*)
            Name.setText(rs.getString("nama"));
            Alamat.setText(rs.getString("alamat"));
            ItemHak.setSelectedItem(rs.getString("hakakses"));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    }//GEN-LAST:event_TabelKaryawanMouseClicked

    private void CariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CariKeyTyped
        if (Cari.getText().length() == 0) {
    // Jika teks pencarian kosong, tampilkan semua data
    aturTable();
} else {
    try {
        int no = 1;
        String[] judul = {"No", "Username", "Password", "Nama", "Alamat", "Hak Akses"};
        // Buat model tabel baru untuk hasil pencarian
        model = new DefaultTableModel(null, judul) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        try {
            // Susun perintah SQL untuk mencari data berdasarkan username atau nama
            sql = "SELECT * FROM kasir WHERE username LIKE '%" + Cari.getText() + "%' OR nama LIKE '%" + Cari.getText() + "%'";
            rs = stat.executeQuery(sql);

            // Tambahkan hasil pencarian ke dalam model tabel
            while (rs.next()) {
                String[] data = {String.valueOf(no++), rs.getString("username"), rs.getString("password"),
                                 rs.getString("nama"), rs.getString("alamat"), rs.getString("hakakses")};
                model.addRow(data);
            }

            // Set model tabel dengan hasil pencarian
            TabelKaryawan.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        // Set tata letak dan penataan ulang tampilan tabel
        ((DefaultTableCellRenderer) TabelKaryawan.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        TabelKaryawan.getColumnModel().getColumn(0).setCellRenderer(render);
        TabelKaryawan.getColumnModel().getColumn(1).setCellRenderer(render);
        TabelKaryawan.getColumnModel().getColumn(2).setCellRenderer(render);
        TabelKaryawan.getColumnModel().getColumn(3).setCellRenderer(render);
        TabelKaryawan.getColumnModel().getColumn(4).setCellRenderer(render);
        TabelKaryawan.getColumnModel().getColumn(5).setCellRenderer(render);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    }//GEN-LAST:event_CariKeyTyped

    private void PassKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PassKeyTyped
        Pass.setEchoChar('*');
    }//GEN-LAST:event_PassKeyTyped

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        //Membuat Aksi ketika ComboBox di klik agar bisa memilih
        if(jCheckBox1.isSelected()){
            Pass.setEchoChar((char)0);
        }else{
         Pass.setEchoChar('*');   
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
       try {
        // Mendefinisikan path file JasperReport
        String pathToFile = "src/Views/karyawan.jasper";        
        // Membuat objek File dari path
        File reportFile = new File(pathToFile);       
        // Memuat laporan dari file JasperReport
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile);        
        // Mengisi laporan dengan data (null karena tidak ada parameter yang diperlukan)
        JasperPrint print = JasperFillManager.fillReport(jasperReport, null, con);       
        // Menampilkan laporan menggunakan JasperViewer
        JasperViewer.viewReport(print);
        } catch (Exception e) {
            // Menangani kesalahan dengan menampilkan pesan kesalahan
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btnCetakActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Alamat;
    private javax.swing.JTextField Cari;
    private javax.swing.JComboBox ItemHak;
    private javax.swing.JTextField Name;
    private javax.swing.JPasswordField Pass;
    private javax.swing.JTable TabelKaryawan;
    private javax.swing.JTextField User;
    private javax.swing.JButton btnBatal;
    private javax.swing.JLabel btnCari;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
