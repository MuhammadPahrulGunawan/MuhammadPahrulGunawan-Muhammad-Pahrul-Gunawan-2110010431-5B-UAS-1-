    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;
import java.awt.Color;
import java.awt.Font;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import javax.swing.table.*;
import javax.swing.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author MUHAMMAD PAHRUL GUNAWAN
 */
public class DataPasok extends javax.swing.JInternalFrame {

    /**
     * Creates new form DataPasok
     */
    // Objek Connection untuk mengelola koneksi ke database
    private Connection con;
    // Objek Statement untuk mengeksekusi pernyataan SQL ke database
    private Statement stat;
    // Objek ResultSet untuk menampung hasil dari eksekusi query database
    private ResultSet rs;
    // Objek koneksi dari kelas koneksi, digunakan untuk membuat koneksi ke database
    private koneksi kon = new koneksi();
    // String untuk menyimpan pernyataan SQL
    private String sql, klik, judul;
    // Variabel untuk menyimpan id, digunakan untuk identifikasi unik dalam database
    private int id = 0;
    // Objek DefaultTableModel untuk mengelola data model tabel
    private DefaultTableModel model;
    
    public DataPasok() {
    initComponents();
    setTitle("Data Pasok");
    con = kon.con;
    stat = kon.stat;
    dataCB();
    dataCB1();
    sembunyiOB();
    sembunyi();
    aturTable();
    ((BasicInternalFrameUI)this.getUI()).setNorthPane(null);
    this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
    font();
    try {
        FileReader fr = new FileReader("src/tema.txt"); 
        BufferedReader br = new BufferedReader(fr);
        if(br.readLine().equals("Light")){
            // Tema default jika tema yang disimpan adalah Light
        } else {
            tema();
        }
    } catch(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    //Methode untuk mengatur tampilan Font
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
                TabelPasok.setFont(sizeFont1);
                TabelPasok.getTableHeader().setFont(sizeFont1);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    //Mengatur Tema
    private void tema(){
        this.setBackground(Color.BLACK);
        jPanel1.setBackground(Color.BLACK);
        jLabel2.setForeground(Color.BLUE);
        jLabel3.setForeground(Color.BLUE);
        jLabel4.setForeground(Color.BLUE);
        jLabel5.setForeground(Color.BLUE);
        jLabel6.setForeground(Color.BLUE);
        jLabel7.setForeground(Color.BLUE);
        btnTambah.setForeground(Color.BLUE);
        btnEdit.setForeground(Color.BLUE);
        btnHapus.setForeground(Color.BLUE);
        btnOk.setForeground(Color.BLUE);
        btnBatal.setForeground(Color.BLUE);
        CetakData.setForeground(Color.BLUE);
        ImageIcon icon = new ImageIcon("src/Gambar/icons8-exit-30.png");
        jButton7.setIcon(icon);
        TabelPasok.setBackground(Color.BLACK);
        TabelPasok.setForeground(Color.BLUE);
        TabelPasok.getTableHeader().setForeground(Color.BLACK);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
    }
    
    private void dataCB(){
        Nama.removeAllItems();
        try{
            sql="SELECT nama_distributor FROM distributor";
            rs = stat.executeQuery(sql);
            while(rs.next()){
                Nama.addItem(rs.getString("nama_distributor"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void dataCB1(){
        JudulBuku.removeAllItems();
        try{
            sql="SELECT judul FROM buku";
            rs = stat.executeQuery(sql);
            while(rs.next()){
                JudulBuku.addItem(rs.getString("judul"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    //Methode untuk mengatur tabel
    private void aturTable(){
        int no=1;
        String[] judul = {"No","Nama Distributor","Judul Buku","Jumlah","Tanggal"};
        model = new DefaultTableModel(null,judul){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        try{
            sql = "SELECT nama_distributor,judul,jumlah,tanggal FROM distributor,buku,pasok WHERE distributor.id=id_distributor AND buku.id=id_buku";
            rs = stat.executeQuery(sql);
            while(rs.next()){
                String[] data = {Integer.toString(no++),rs.getString("nama_distributor"),rs.getString("judul"),rs.getString("jumlah"),rs.getString("tanggal")};
                model.addRow(data);
            }
            TabelPasok.setModel(model);
            ((DefaultTableCellRenderer)TabelPasok.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(JLabel.CENTER);
            TabelPasok.getColumnModel().getColumn(0).setCellRenderer(render);
            TabelPasok.getColumnModel().getColumn(1).setCellRenderer(render);
            TabelPasok.getColumnModel().getColumn(2).setCellRenderer(render);
            TabelPasok.getColumnModel().getColumn(3).setCellRenderer(render);
            TabelPasok.getColumnModel().getColumn(4).setCellRenderer(render);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    // Menyembunyikan tombol-tombol CRUD (Create, Read, Update, Delete).
    private void reset(){
        Jumlah.setText("");
        Nama.setSelectedItem("");
        JudulBuku.setSelectedItem("");
    }
    //Menyembunyikan tombol OK dan Batal.
    private void sembunyi(){
        Nama.setEnabled(false);
        JudulBuku.setEnabled(false);
        Jumlah.setEnabled(false);
        Tanggal.setEnabled(false);
    }
    //Menampilkan komponen-komponen (JComboBox, JTextField) agar dapat diakses dan diisi.
    private void sembunyiCRUD(){
      btnTambah.setEnabled(false);
      btnEdit.setEnabled(false);
      btnHapus.setEnabled(false);
    }
    
    private void sembunyiOB(){
      btnOk.setEnabled(false);
      btnBatal.setEnabled(false);
    }
    //Menampilkan komponen-komponen (JComboBox, JTextField) agar dapat diakses dan diisi.
    private void tampil(){
        Nama.setEnabled(true);
        JudulBuku.setEnabled(true);
        Jumlah.setEnabled(true);
        Tanggal.setEnabled(true);
    }
    //Menampilkan tombol-tombol CRUD (Create, Read, Update, Delete).
    private void tampilCRUD(){
      btnTambah.setEnabled(true);
      btnEdit.setEnabled(true);
      btnHapus.setEnabled(true);
    }
    
    private void tampilOB(){
      btnOk.setEnabled(true);
      btnBatal.setEnabled(true);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Nama = new javax.swing.JComboBox();
        JudulBuku = new javax.swing.JComboBox();
        Jumlah = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelPasok = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Cari = new javax.swing.JTextField();
        CetakData = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        Tanggal = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setPreferredSize(new java.awt.Dimension(1363, 741));

        jLabel2.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("NAMA DISTRIBUTOR");

        jLabel3.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("JUDUL BUKU");

        jLabel4.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("JUMLAH");

        Nama.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Nama.setForeground(new java.awt.Color(0, 0, 255));

        JudulBuku.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        JudulBuku.setForeground(new java.awt.Color(0, 0, 255));

        Jumlah.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Jumlah.setForeground(new java.awt.Color(0, 0, 255));
        Jumlah.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Jumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                JumlahKeyTyped(evt);
            }
        });

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

        TabelPasok.setBackground(new java.awt.Color(0, 0, 255));
        TabelPasok.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        TabelPasok.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TabelPasok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelPasokMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelPasok);

        jLabel5.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setText("Tanggal");

        jLabel6.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-search-32.png"))); // NOI18N
        jLabel6.setText("CARI");
        jLabel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        Cari.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Cari.setForeground(new java.awt.Color(0, 0, 255));
        Cari.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CariKeyTyped(evt);
            }
        });

        CetakData.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        CetakData.setForeground(new java.awt.Color(0, 0, 255));
        CetakData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-printer-32.png"))); // NOI18N
        CetakData.setText("CETAK DATA PEMASOK");
        CetakData.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        CetakData.setContentAreaFilled(false);
        CetakData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CetakDataActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 255));
        jPanel1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-Pemasok-32 (1).png"))); // NOI18N
        jLabel7.setText("DATA PEMASOK");

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-exit-30.png"))); // NOI18N
        jButton7.setContentAreaFilled(false);
        jButton7.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_MacOS_Close_30px_1.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 546, Short.MAX_VALUE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JudulBuku, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Nama, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Jumlah)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnBatal, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CetakData)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JudulBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(Jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHapus)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah)
                        .addComponent(btnEdit)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnBatal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CetakData)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // Method yang telah dibuat tinggal di panggil kesini
        klik = "tambah";
        reset();
        sembunyiCRUD();
        tampilOB();
        tampil();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if(Integer.toString(id).equals("0")){
            JOptionPane.showMessageDialog(null, "Silahkan pilih data yang ingin di ubah");
        }else{
        klik = "edit";
        sembunyiCRUD();
        tampilOB();
        tampil();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
    if (Integer.toString(id).equals("0")) {
        JOptionPane.showMessageDialog(null, "Silahkan pilih data yang ingin dihapus");
    } else {
        int selectedOption = JOptionPane.showConfirmDialog(null, "Apakah yakin ingin menghapus data ini?", "Peringatan", JOptionPane.YES_NO_OPTION);
        
        if (selectedOption == JOptionPane.YES_OPTION) {
            try {
                // Mengambil stok awal dari database
                String sql1 = "SELECT stok FROM buku WHERE judul='" + JudulBuku.getSelectedItem() + "'";
                rs = stat.executeQuery(sql1);

                if (rs.next()) {
                    // Menghitung stok akhir setelah dikurangi Jumlah
                    int stokakhir = rs.getInt("stok") - Integer.parseInt(Jumlah.getText());

                    // Update stok buku ke database
                    String updateStok = "UPDATE buku SET stok='" + stokakhir + "' WHERE judul='" + JudulBuku.getSelectedItem() + "'";
                    stat.execute(updateStok);

                    // Hapus data pasok dari database
                    String deletePasok = "DELETE FROM pasok WHERE id='" + id + "'";
                    stat.execute(deletePasok);

                    JOptionPane.showMessageDialog(null, "Data telah dihapus");
                }
                // Reset dan atur ulang tabel
                reset();
                model.fireTableDataChanged();
                model.getDataVector().removeAllElements();
                aturTable();
                // Sembunyikan komponen, tampilkan tombol CRUD, dan sembunyikan tombol OK dan Batal
                sembunyi();
                tampilCRUD();
                sembunyiOB();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else {
            // Jika pengguna tidak setuju dengan penghapusan, reset komponen dan tampilkan tombol CRUD
            reset();
            sembunyi();
            tampilCRUD();
            sembunyiOB();
        }
    }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        if (klik.equals("tambah")) {
        if (Jumlah.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ada data yang belum diisi");
        } else {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String sql1, sql2;
                int stokawal, stokakhir;
                
                // Mengambil stok awal dari database
                sql = "SELECT stok FROM buku WHERE judul='" + JudulBuku.getSelectedItem().toString() + "'";
                rs = stat.executeQuery(sql);
                
                if (rs.next()) {
                    stokawal = rs.getInt("stok");
                    stokakhir = stokawal + Integer.parseInt(Jumlah.getText());
                    
                    // Menambahkan data pasok ke database
                    sql1 = "INSERT INTO pasok(id_distributor, id_buku, jumlah, tanggal) VALUE((SELECT id FROM distributor WHERE nama_distributor='" + Nama.getSelectedItem() + "'), (SELECT id FROM buku WHERE judul='" + JudulBuku.getSelectedItem() + "'), '" + Jumlah.getText() + "', '" + df.format(Tanggal.getDate()) + "')";
                    stat.execute(sql1);
                    
                    // Mengupdate stok buku ke database
                    sql2 = "UPDATE buku SET stok='" + stokakhir + "' WHERE id=(SELECT id FROM buku WHERE judul='" + JudulBuku.getSelectedItem() + "')";
                    stat.execute(sql2);
                    
                    JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
                    model.fireTableDataChanged();
                    model.getDataVector().removeAllElements();
                    aturTable();
                    reset();
                    sembunyi();
                    sembunyiOB();
                    tampilCRUD();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    } else if (klik.equals("edit")) {
        try {
            if (JudulBuku.getSelectedItem().toString().equals(judul)) {
                // Jika judul buku tidak berubah, lakukan perubahan data
                sql = "UPDATE pasok SET id_distributor=(SELECT id FROM distributor WHERE nama_distributor='" + Nama.getSelectedItem() + "'), id_buku=(SELECT id FROM buku WHERE judul='" + JudulBuku.getSelectedItem() + "'), jumlah='" + Jumlah.getText() + "' WHERE id='" + id + "'";
                stat.execute(sql);
                JOptionPane.showMessageDialog(null, "Sukses ubah data judul sama");
            } else {
                // Jika judul berubah, hitung stok akhir berdasarkan perubahan
                sql = "SELECT stok FROM buku WHERE judul='" + judul + "'";
                rs = stat.executeQuery(sql);

                if (rs.next()) {
                    int stokawal, stokakhir, stokakhir1;
                    stokawal = rs.getInt("stok");

                    // Menghitung stok akhir sebelum perubahan
                    sql = "SELECT jumlah FROM pasok WHERE id_buku=(SELECT id FROM buku WHERE judul='" + judul + "')";
                    rs = stat.executeQuery(sql);

                    if (rs.next()) {
                        stokakhir = stokawal - rs.getInt("jumlah");

                        // Update stok buku sebelum perubahan
                        sql = "UPDATE buku SET stok='" + stokakhir + "' WHERE judul='" + judul + "'";
                        stat.execute(sql);

                        // Update data pasok dengan judul baru
                        sql = "UPDATE pasok SET id_distributor=(SELECT id FROM distributor WHERE nama_distributor='" + Nama.getSelectedItem() + "'), id_buku=(SELECT id FROM buku WHERE judul='" + JudulBuku.getSelectedItem() + "'), jumlah='" + Jumlah.getText() + "' WHERE id='" + id + "'";
                        stat.execute(sql);

                        // Menghitung stok akhir setelah perubahan
                        sql = "SELECT stok FROM buku WHERE judul='" + JudulBuku.getSelectedItem() + "'";
                        rs = stat.executeQuery(sql);

                        if (rs.next()) {
                            stokakhir1 = rs.getInt("stok") + Integer.parseInt(Jumlah.getText());

                            // Update stok buku setelah perubahan
                            sql = "UPDATE buku SET stok='" + stokakhir1 + "' WHERE judul='" + JudulBuku.getSelectedItem() + "'";
                            stat.execute(sql);
                        }
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
        
    }//GEN-LAST:event_btnOkActionPerformed

    private void TabelPasokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelPasokMouseClicked
      try{
              sql = "SELECT pasok.id,nama_distributor,judul,jumlah,tanggal FROM distributor,buku,pasok WHERE distributor.id=(SELECT id FROM distributor WHERE nama_distributor='"+TabelPasok.getValueAt(TabelPasok.getSelectedRow(), 1)+"') AND buku.id=(SELECT id FROM buku WHERE judul='"+TabelPasok.getValueAt(TabelPasok.getSelectedRow(), 2)+"') AND distributor.id=id_distributor AND buku.id=id_buku";
              rs = stat.executeQuery(sql);
              if(rs.next()){
                  reset();
                  tampilCRUD();
                  sembunyiOB();
                  sembunyi();
                  id = rs.getInt("id");
                  judul = rs.getString("judul");
                  Nama.setSelectedItem(rs.getString("nama_distributor"));
                  JudulBuku.setSelectedItem(rs.getString("judul"));
                  Jumlah.setText(rs.getString("jumlah"));
                  //jCalendarCombo1.setDate(rs.getDate("tanggal"));
                  //((JTextField)jDateChooser)
                  Date date = (Date) new SimpleDateFormat("yyyy-MM-dd").parse((String) TabelPasok.getValueAt(TabelPasok.getSelectedRow(), 4).toString());
                  Tanggal.setDate(date);
              }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
    }//GEN-LAST:event_TabelPasokMouseClicked

    private void JumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JumlahKeyTyped
    if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Hanya karakter angka yang diperbolehkan");    
        }
    }//GEN-LAST:event_JumlahKeyTyped

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        reset();
        sembunyi();
        sembunyiOB();
        tampilCRUD();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void CariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CariKeyTyped
    if (Cari.getText().isEmpty()) {
        // Jika teks pencarian kosong, tampilkan semua data pasok
        model.fireTableDataChanged();
        model.getDataVector().removeAllElements();
        aturTable();
    } else {
        // Jika terdapat teks pencarian, tampilkan data sesuai dengan kriteria pencarian
        model.fireTableDataChanged();
        model.getDataVector().removeAllElements();
        int no = 1;
        String[] judul = {"No", "Nama Distributor", "Judul Buku", "Jumlah", "Tanggal"};
        model = new DefaultTableModel(null, judul) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        try {
            // Query untuk mendapatkan data sesuai dengan kriteria pencarian
            sql = "SELECT nama_distributor, judul, jumlah, tanggal FROM distributor, buku, pasok WHERE (nama_distributor LIKE '%" + Cari.getText() + "%' OR judul LIKE '%" + Cari.getText() + "%') AND distributor.id = id_distributor AND buku.id = id_buku";
            rs = stat.executeQuery(sql);
            // Mengisi model tabel dengan data hasil query
            while (rs.next()) {
                String[] data = {Integer.toString(no++), rs.getString("nama_distributor"), rs.getString("judul"), rs.getString("jumlah"), rs.getString("tanggal")};
                model.addRow(data);
            }
            // Mengatur model tabel dan penataan sel serta header tabel
            TabelPasok.setModel(model);
            ((DefaultTableCellRenderer) TabelPasok.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(JLabel.CENTER);
            TabelPasok.getColumnModel().getColumn(0).setCellRenderer(render);
            TabelPasok.getColumnModel().getColumn(1).setCellRenderer(render);
            TabelPasok.getColumnModel().getColumn(2).setCellRenderer(render);
            TabelPasok.getColumnModel().getColumn(3).setCellRenderer(render);
            TabelPasok.getColumnModel().getColumn(4).setCellRenderer(render);
        } catch (Exception e) {
            // Tampilkan pesan kesalahan jika terjadi exception
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    }//GEN-LAST:event_CariKeyTyped

    private void CetakDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CetakDataActionPerformed
    try {
        // Mendefinisikan path file laporan JasperReports
        String path = "src/Views/pasok.jasper";
        File file = new File(path);
        // Memuat laporan JasperReports dari file
        JasperReport report = (JasperReport) JRLoader.loadObject(file);
        // Mengisi laporan dengan data dari database menggunakan koneksi "con"
        JasperPrint print = JasperFillManager.fillReport(report, null, con);
        // Menampilkan laporan menggunakan JasperViewer
        JasperViewer.viewReport(print);
    } catch (Exception e) {
        // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
        JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_CetakDataActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
    this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Cari;
    private javax.swing.JButton CetakData;
    private javax.swing.JComboBox JudulBuku;
    private javax.swing.JTextField Jumlah;
    private javax.swing.JComboBox Nama;
    private javax.swing.JTable TabelPasok;
    private com.toedter.calendar.JDateChooser Tanggal;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton jButton7;
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
