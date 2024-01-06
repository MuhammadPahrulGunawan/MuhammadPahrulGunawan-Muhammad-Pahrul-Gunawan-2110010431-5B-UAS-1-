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
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.sql.*;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author MUHAMMAD PAHRUL GUNAWAN
 */
public class DataDistributor extends javax.swing.JInternalFrame {

    /**
     * Creates new form DataDistributor
     */
    private DefaultTableModel model;
    private Connection con;
    private koneksi kon = new koneksi();
    private ResultSet rs;
    private Statement stat;
    private String sql,klik;
    private int id=0;
    
    public DataDistributor() {
    initComponents();
    // Mendapatkan koneksi dan statement dari kelas koneksi
    con = kon.con;
    stat = kon.stat;   
    // Mengatur judul frame
    setTitle("Data Distributor");    
    // Mengatur tampilan tabel
    aturTable();    
    // Menyembunyikan komponen dan tombol OK/Batal
    sembunyi();
    sembunyiOB();
    // Menghilangkan panel atas internal frame
    ((BasicInternalFrameUI) this.getUI()).setNorthPane(null);   
    // Mengatur tampilan border frame
    this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));    
    // Mengatur tampilan font
    font();
    try {
        // Membaca nilai tema dari file "tema.txt"
        FileReader fr = new FileReader("src/tema.txt"); 
        BufferedReader br = new BufferedReader(fr);
        
        // Mengatur tema tampilan berdasarkan nilai yang dibaca
        if (br.readLine().equals("Light")) {
            // Kosong, karena tidak ada pengaturan khusus untuk tema "Light"
        } else {
            // Mengatur tema jika bukan "Light"
            tema();
        }
    } catch (Exception e) {
        // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    }
    //Methode untuk mengatur Font
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
                Font sizeFont1 = font.deriveFont(14f);
                TabelDistributor.setFont(sizeFont1);
                TabelDistributor.getTableHeader().setFont(sizeFont1);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    //Methode untuk membuat teks menjasi warna Biru ketika mode gelap diterapkan
    private void tema(){
        this.setBackground(Color.BLACK);
        jPanel1.setBackground(Color.BLACK);
        jLabel2.setForeground(Color.BLUE);
        jLabel3.setForeground(Color.BLUE);
        jLabel4.setForeground(Color.BLUE);
        btnCari.setForeground(Color.BLUE);
        jLabel6.setForeground(Color.BLUE);
        btnTambah.setForeground(Color.BLUE);
        btnEdit.setForeground(Color.BLUE);
        btnHapus.setForeground(Color.BLUE);
        btnOk.setForeground(Color.BLUE);
        btnBatal.setForeground(Color.BLUE);
        btnCetak.setForeground(Color.BLUE);
        TabelDistributor.setBackground(Color.BLACK);
        TabelDistributor.setForeground(Color.BLUE);
        TabelDistributor.getTableHeader().setForeground(Color.BLACK);
        ImageIcon icon = new ImageIcon("src/Gambar/icons8-exit-30.png");
        jButton7.setIcon(icon);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
    }
    //Mengatur nilai teks pada komponen Nama, Alamat, dan Telp menjadi string kosong.
    //Digunakan untuk mereset nilai input pada form.
    private void reset(){
        Nama.setText("");
        Alamat.setText("");
        Telp.setText("");
    }
    //Menyembunyikan komponen Nama, Alamat, dan Telp.
    //Digunakan untuk menonaktifkan input pada form.
    private void sembunyi(){
            Nama.setEnabled(false);
            Alamat.setEnabled(false);
            Telp.setEnabled(false);
    }
    //Menyembunyikan tombol Tambah, Edit, dan Hapus.
    private void sembunyiCRUD(){
        btnTambah.setEnabled(false);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
    }
    //Menyembunyikan tombol OK dan Batal.
    //Digunakan untuk menonaktifkan operasi OK dan Batal pada form.
    private void sembunyiOB(){
        btnOk.setEnabled(false);
        btnBatal.setEnabled(false);
    }
    //Menampilkan komponen Nama, Alamat, dan Telp.
    //Digunakan untuk mengaktifkan input pada form.
     private void tampil(){
            Nama.setEnabled(true);
            Alamat.setEnabled(true);
            Telp.setEnabled(true);
    }
    //Menampilkan tombol Tambah, Edit, dan Hapus.
    private void tampilCRUD(){
        btnTambah.setEnabled(true);
        btnEdit.setEnabled(true);
        btnHapus.setEnabled(true);
    }
    //Menampilkan tombol OK dan Batal.
     //Digunakan untuk mengaktifkan operasi OK dan Batal pada form.
    private void tampilOB(){
        btnOk.setEnabled(true);
        btnBatal.setEnabled(true);
    }
    //Mengatur Tabel pada Form
    private void aturTable(){
        int no=1;
        String[] judul = {"No","Nama Distributor","Alamat","Telepon"};
        model = new DefaultTableModel(null,judul){
          @Override
          public boolean isCellEditable(int row,int column){
              return false;
          }
        };
        try{
            sql = "SELECT * FROM distributor";
            rs = stat.executeQuery(sql);
            while (rs.next()){
                String[] data = {Integer.toString(no++),rs.getString("nama_distributor"),rs.getString("alamat"),rs.getString("telepon")};
                model.addRow(data);
            }
            TabelDistributor.setModel(model);
            ((DefaultTableCellRenderer)TabelDistributor.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(JLabel.CENTER);
            TabelDistributor.getColumnModel().getColumn(0).setCellRenderer(render);
            TabelDistributor.getColumnModel().getColumn(1).setCellRenderer(render);
            TabelDistributor.getColumnModel().getColumn(2).setCellRenderer(render);
            TabelDistributor.getColumnModel().getColumn(3).setCellRenderer(render);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        TabelDistributor = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Nama = new javax.swing.JTextField();
        Alamat = new javax.swing.JTextField();
        Telp = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnCari = new javax.swing.JLabel();
        Cari = new javax.swing.JTextField();
        btnCetak = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setPreferredSize(new java.awt.Dimension(1363, 741));

        TabelDistributor.setBackground(new java.awt.Color(0, 0, 255));
        TabelDistributor.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        TabelDistributor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        TabelDistributor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelDistributorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelDistributor);

        jLabel2.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("NAMA DISTRIBUTOR");

        jLabel3.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("ALAMAT");

        jLabel4.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("No. TELPON");

        Nama.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Nama.setForeground(new java.awt.Color(0, 0, 255));
        Nama.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Alamat.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Alamat.setForeground(new java.awt.Color(0, 0, 255));
        Alamat.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Telp.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Telp.setForeground(new java.awt.Color(0, 0, 255));
        Telp.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Telp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TelpKeyTyped(evt);
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
        btnCetak.setText("CETAK DATA DISTRIBUTOR");
        btnCetak.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCetak.setContentAreaFilled(false);
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 255));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-exit-30.png"))); // NOI18N
        jButton7.setToolTipText("");
        jButton7.setContentAreaFilled(false);
        jButton7.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_MacOS_Close_30px_1.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-distributor-32.png"))); // NOI18N
        jLabel6.setText("DATA DISTIBUTOR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Nama)
                                    .addComponent(Alamat)
                                    .addComponent(Telp))
                                .addGap(6, 6, 6))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnBatal, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                                .addComponent(btnCetak)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(Alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(Telp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah)
                    .addComponent(btnEdit)
                    .addComponent(btnHapus))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnBatal))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari)
                    .addComponent(btnCetak))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        //Method yang telah dibuat dipanggil kesini
        reset();
        klik = "tambah";
        tampilOB();
        sembunyiCRUD();
        tampil();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    if (Integer.toString(id).equals("0")) {
        // Jika belum ada data yang dipilih untuk diedit, munculkan pesan peringatan
        JOptionPane.showMessageDialog(null, "Silahkan pilih data yang akan diubah");
    } else {
        // Jika data sudah dipilih, atur mode edit, tampilkan tombol OK/Batal, sembunyikan tombol Tambah/Edit/Hapus, dan tampilkan input form
        klik = "edit";
        tampilOB();
        sembunyiCRUD();
        tampil();
    }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
    if (Integer.toString(id).equals("0")) {
        // Jika belum ada data yang dipilih untuk dihapus, munculkan pesan peringatan
        JOptionPane.showMessageDialog(null, "Silahkan pilih data yang akan dihapus");
    } else {
        // Jika data sudah dipilih, tampilkan konfirmasi penghapusan. Jika user menyetujui, hapus data dari database
        if (JOptionPane.showConfirmDialog(null, "Apakah yakin ingin menghapus data ini?", "Peringatan", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                // Query untuk menghapus data distributor berdasarkan id
                sql = "DELETE FROM distributor WHERE id='" + id + "'";
                stat.execute(sql);         
                // Menampilkan pesan sukses
                JOptionPane.showMessageDialog(null, "Data telah terhapus");
                // Melakukan reset dan pengaturan ulang tampilan tabel
                reset();
                model.fireTableDataChanged();
                model.getDataVector().removeAllElements();
                aturTable();    
                // Melakukan reset form, menampilkan tombol Tambah/Edit/Hapus, dan menyembunyikan tombol OK/Batal
                reset();
                sembunyi();
                tampilCRUD();
                sembunyiOB();
            } catch (Exception e) {
                // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else {
            // Jika Kasir tidak menyetujui penghapusan, reset form dan tampilkan kembali tombol Tambah/Edit/Hapus
            reset();
            sembunyi();
            tampilCRUD();
            sembunyiOB();
        }
     }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        //Method yang telah dibuat dipanggil kesini
        reset();
        sembunyi();
        tampilCRUD();
        sembunyiOB();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
    if (klik.equals("tambah")) {
        if (Nama.getText().isEmpty() && Alamat.getText().isEmpty() && Telp.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ada data yang belum di isi");
        } else {
            try {
                // Query untuk menambahkan data distributor baru
                sql = "INSERT INTO distributor(nama_distributor, alamat, telepon) VALUES ('" + Nama.getText() + "','" + Alamat.getText() + "','" + Telp.getText() + "')";
                stat.execute(sql);
                JOptionPane.showMessageDialog(null, "Sukses tambah data");
            } catch (Exception e) {
                // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            // Melakukan pengaturan ulang dan pembaruan tampilan tabel
            model.fireTableDataChanged();
            model.getDataVector().removeAllElements();
            aturTable();
            reset();
            sembunyiOB();
            tampilCRUD();
            sembunyi();
        }
    } else if (klik.equals("edit")) {
        try {
            // Query untuk mengubah data distributor berdasarkan id
            sql = "UPDATE distributor SET nama_distributor='" + Nama.getText() + "', alamat='" + Alamat.getText() + "', telepon='" + Telp.getText() + "' WHERE id='" + id + "'";
            stat.execute(sql);
            JOptionPane.showMessageDialog(null, "Data telah diubah");
        } catch (Exception e) {
            // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        // Melakukan pengaturan ulang dan pembaruan tampilan tabel
        model.fireTableDataChanged();
        model.getDataVector().removeAllElements();
        aturTable();
        reset();
        sembunyiOB();
        tampilCRUD();
        sembunyi();
    }
    }//GEN-LAST:event_btnOkActionPerformed

    private void TabelDistributorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelDistributorMouseClicked
    try {
        // Mendapatkan data distributor berdasarkan nama distributor yang dipilih pada tabel
        sql = "SELECT * FROM distributor WHERE nama_distributor='" + TabelDistributor.getValueAt(TabelDistributor.getSelectedRow(), 1) + "'";
        rs = stat.executeQuery(sql);

        if (rs.next()) {
            // Jika data ditemukan, reset form, tampilkan input form, tombol tambah/edit/hapus, dan sembunyikan tombol OK/Batal
            reset();
            sembunyi();
            tampilCRUD();
            sembunyiOB();
            
            // Mengambil nilai ID dan mengisikan nilai pada input form
            id = rs.getInt("id");
            Nama.setText(rs.getString("nama_distributor"));
            Alamat.setText(rs.getString("alamat"));
            Telp.setText(rs.getString("telepon"));
        }
    } catch (Exception e) {
        // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    }//GEN-LAST:event_TabelDistributorMouseClicked

    private void TelpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TelpKeyTyped
        // Metode ini dipanggil saat pengguna mengetik pada field Telp.
    if (Character.isAlphabetic(evt.getKeyChar())) {
        // Jika karakter yang ditekan adalah huruf, konsumsi event dan tampilkan pesan kesalahan
        evt.consume();
        JOptionPane.showMessageDialog(null, "Hanya karakter angka yang diperbolehkan");
    } else if (Telp.getText().length() >= 13) {
        // Jika panjang input sudah mencapai 13 digit, konsumsi event dan tampilkan pesan kesalahan
        evt.consume();
        JOptionPane.showMessageDialog(null, "Maksimal input 13 digit");
    }
    }//GEN-LAST:event_TelpKeyTyped

    private void CariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CariKeyTyped
        // TODO add your handling code here:
    if (Cari.getText().length() >= 0) {
        // Jika terdapat teks pencarian, atur ulang model tabel dan tampilkan hasil pencarian
        model.fireTableDataChanged();
        model.getDataVector().removeAllElements();
        int no = 1;
        String[] judul = {"No", "Nama Distributor", "Alamat", "Telepon"};
        model = new DefaultTableModel(null, judul) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        try {
            // Query untuk mencari data distributor berdasarkan kriteria pencarian
            sql = "SELECT * FROM distributor WHERE nama_distributor LIKE '%" + Cari.getText() + "%' OR alamat LIKE '%" + Cari.getText() + "%' OR telepon LIKE '%" + Cari.getText() + "%'";
            rs = stat.executeQuery(sql);

            // Mengisi model tabel dengan hasil pencarian
            while (rs.next()) {
                String[] data = {Integer.toString(no++), rs.getString("nama_distributor"), rs.getString("alamat"), rs.getString("telepon")};
                model.addRow(data);
            }
            // Mengatur model tabel dan penataan sel serta header tabel
            TabelDistributor.setModel(model);
            ((DefaultTableCellRenderer) TabelDistributor.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(JLabel.CENTER);
            TabelDistributor.getColumnModel().getColumn(0).setCellRenderer(render);
            TabelDistributor.getColumnModel().getColumn(1).setCellRenderer(render);
            TabelDistributor.getColumnModel().getColumn(2).setCellRenderer(render);
            TabelDistributor.getColumnModel().getColumn(3).setCellRenderer(render);
        } catch (Exception e) {
            // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    } else {
        // Jika teks pencarian kosong, atur ulang model tabel dan tampilkan kembali seluruh data distributor
        model.fireTableDataChanged();
        model.getDataVector().removeAllElements();
        aturTable();
        }
    }//GEN-LAST:event_CariKeyTyped

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
    try {
        // Mendefinisikan path file laporan JasperReports
        String path = "src/Views/distributor.jasper";
        File file = new File(path);

        // Memuat laporan JasperReports dari file
        JasperReport jasper = (JasperReport) JRLoader.loadObject(file);

        // Mengisi laporan dengan data dari database menggunakan koneksi "con"
        JasperPrint print = JasperFillManager.fillReport(jasper, null, con);

        // Menampilkan laporan menggunakan JasperViewer
        JasperViewer.viewReport(print);
    } catch (Exception e) {
        // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    }//GEN-LAST:event_btnCetakActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
    this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Alamat;
    private javax.swing.JTextField Cari;
    private javax.swing.JTextField Nama;
    private javax.swing.JTable TabelDistributor;
    private javax.swing.JTextField Telp;
    private javax.swing.JButton btnBatal;
    private javax.swing.JLabel btnCari;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
