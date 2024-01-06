/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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
public class DataBuku extends javax.swing.JInternalFrame {

    /**
     * Creates new form DataBuku
     */
    
    private Connection con;
    private ResultSet rs;
    private Statement stat;
    private String sql,klik,source,destination,namagambar;
    private koneksi kon = new koneksi();
    private JFileChooser jf = new JFileChooser();
    private DefaultTableModel model;
    private int id=0;
    private LineBorder b;
    
    public DataBuku() {
    initComponents();
    // Mendapatkan koneksi dan statement dari kelas koneksi
    con = kon.con;
    stat = kon.stat;
    // Mengatur judul frame
    setTitle("Data Buku");   
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
        // Menerapkan tema jika bukan "Light"
        if (!br.readLine().equals("Light")) {
            tema();
        }
    } catch (Exception e) {
        // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    }
    //Methode untuk membuat teks menjadi biru ketika tema dalam mode Gelap
    private void tema(){
        this.setBackground(Color.BLACK);
        jPanel1.setBackground(Color.BLACK);
        jPanel2.setBackground(Color.BLACK);
        jLabel2.setBackground(Color.BLUE);
        jLabel2.setForeground(Color.BLUE);
        jLabel3.setForeground(Color.BLUE);
        jLabel5.setForeground(Color.BLUE);
        jLabel6.setForeground(Color.BLUE);
        jLabel7.setForeground(Color.BLUE);
        jLabel8.setForeground(Color.BLUE);
        jLabel9.setForeground(Color.BLUE);
        jLabel10.setForeground(Color.BLUE);
        jLabel11.setForeground(Color.BLUE);
        jLabel12.setForeground(Color.BLUE);
        jLabel13.setForeground(Color.BLUE);
        jLabel14.setForeground(Color.BLUE);
        jLabel15.setForeground(Color.BLUE);
        btnCari.setForeground(Color.BLUE);
        jLabel17.setForeground(Color.BLUE);
        jButton1.setForeground(Color.BLUE);
        btnTambah.setForeground(Color.BLUE);
        btnEdit.setForeground(Color.BLUE);
        btnHapus.setForeground(Color.BLUE);
        btnOk.setForeground(Color.BLUE);
        btnBatal.setForeground(Color.BLUE);
        btnCetak.setForeground(Color.BLUE);
        ImageIcon image = new ImageIcon("src/Gambar/icons8-exit-30.png");
        btnExit.setIcon(image);
        TabelBuku.setBackground(Color.BLACK);
        TabelBuku.setForeground(Color.BLUE);
        TabelBuku.getTableHeader().setBackground(Color.BLACK);
        TabelBuku.getTableHeader().setForeground(Color.BLACK);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
    }
    //Mengatur Font pada teks Tabel
    private void font(){
         File y = new File("src/font");
           if(y.canRead()){
               try{
               String[] namafont = y.list();
               FileInputStream z = new FileInputStream("src/font/"+namafont[0]);
               FileInputStream z1 = new FileInputStream("src/font/"+namafont[4]);
               Font font = Font.createFont(Font.TRUETYPE_FONT, z);
               Font font1 = Font.createFont(Font.TRUETYPE_FONT, z1);
               Font sizeFont = font.deriveFont(24f);
               Font sizeFont1 = font1.deriveFont(14f);
               TabelBuku.setFont(sizeFont1);
               TabelBuku.getTableHeader().setFont(sizeFont1);
               }catch(Exception e){
                   JOptionPane.showMessageDialog(null, e.getMessage());
               }
           }
    }
    
    private void reset(){
        Gambar.setText("");
        Judul.setText("");
        NoISBN.setText("");
        Penulis.setText("");
        Penerbit.setText("");
        Tahun.setText("");
        Stok.setText("");
        HargaPokok.setText("");
        jTextField10.setText("");
        jTextField11.setText("");
        jLabel2.setIcon(null);
    }
    
    private void sembunyi(){
        Gambar.setEnabled(false);
        Judul.setEnabled(false);
        NoISBN.setEnabled(false);
        Penulis.setEnabled(false);
        Penerbit.setEnabled(false);
        Tahun.setEnabled(false);
        Stok.setEnabled(false);
        HargaPokok.setEnabled(false);
        jTextField10.setEnabled(false);
        jTextField11.setEnabled(false);
        jButton1.setEnabled(false);
    }
    
    private void sembunyiOB(){
        btnOk.setEnabled(false);
        btnBatal.setEnabled(false);
    }
    
    private void sembunyiCRUD(){
        btnTambah.setEnabled(false);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
    }
    
    private void tampil(){
        Gambar.setEnabled(false);
        Judul.setEnabled(true);
        NoISBN.setEnabled(true);
        Penulis.setEnabled(true);
        Penerbit.setEnabled(true);
        Tahun.setEnabled(true);
        Stok.setEnabled(true);
        HargaPokok.setEnabled(true);
        jTextField10.setEnabled(true);
        jTextField11.setEnabled(true);
        jButton1.setEnabled(true);
    }
    
    private void tampilOB(){
        btnOk.setEnabled(true);
        btnBatal.setEnabled(true);
    }
    
    private void tampilCRUD(){
        btnTambah.setEnabled(true);
        btnEdit.setEnabled(true);
        btnHapus.setEnabled(true);
    }
    //Mengatur TabelBuku
    private void aturTable(){
       int no=1;
       String[] judul = {"No","Gambar","Judul","No ISBN","Penulis","Penerbit","Tahun","Stok","Harga Pokok","Harga Jual","PPN","Diskon"};
       model = new DefaultTableModel(null,judul){
           @Override
           public Class getColumnClass(int column){
               if(column==1){
                   return ImageIcon.class;
               }
               return Object.class;
           }
           
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
       };
        try{
            sql = "SELECT * FROM buku";
            rs = stat.executeQuery(sql);
            while(rs.next()){
                String source = ("../TokoBuku/src/Gambar/"+rs.getString("gambar"));
                ImageIcon icon = new ImageIcon(source);
                Image scale = icon.getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT);
                icon = new ImageIcon(scale);
                Object[] isi = {Integer.toString(no++),icon,rs.getString("judul"),rs.getString("noisbn"),rs.getString("penulis"),rs.getString("penerbit"),rs.getInt("tahun"),rs.getString("stok"),rs.getString("harga_pokok"),rs.getString("harga_jual"),rs.getString("ppn"),rs.getString("diskon")};
                model.addRow(isi);
            }
          ((DefaultTableCellRenderer)TabelBuku.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           TabelBuku.setModel(model);
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(JLabel.CENTER);
            TabelBuku.getColumnModel().getColumn(0).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(2).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(3).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(4).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(5).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(6).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(7).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(8).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(9).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(10).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(11).setCellRenderer(render);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"gagal"+e.getMessage());
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
        TabelBuku = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Gambar = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Judul = new javax.swing.JTextField();
        NoISBN = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Penulis = new javax.swing.JTextField();
        Penerbit = new javax.swing.JTextField();
        Tahun = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        Stok = new javax.swing.JTextField();
        HargaPokok = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btnCari = new javax.swing.JLabel();
        Cari = new javax.swing.JTextField();
        btnCetak = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        TabelBuku.setBackground(new java.awt.Color(0, 0, 255));
        TabelBuku.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        TabelBuku.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TabelBuku.setRowHeight(100);
        TabelBuku.setSurrendersFocusOnKeystroke(true);
        TabelBuku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelBukuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelBuku);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel2.setToolTipText("Gambar");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel3.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("PILIH GAMBAR");

        Gambar.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Gambar.setForeground(new java.awt.Color(0, 0, 255));

        jButton1.setText("...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setText("JUDUL");

        jLabel7.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 255));
        jLabel7.setText("No ISBN");

        Judul.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Judul.setForeground(new java.awt.Color(0, 0, 255));
        Judul.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        NoISBN.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        NoISBN.setForeground(new java.awt.Color(0, 0, 255));
        NoISBN.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        NoISBN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                NoISBNKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 255));
        jLabel8.setText("PENULIS");

        jLabel9.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 255));
        jLabel9.setText("PENERBIT");

        jLabel10.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 255));
        jLabel10.setText("TAHUN");

        jLabel11.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 255));
        jLabel11.setText("STOK");

        jLabel12.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 255));
        jLabel12.setText("HARGA POKOK");

        Penulis.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Penulis.setForeground(new java.awt.Color(0, 0, 255));
        Penulis.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Penerbit.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Penerbit.setForeground(new java.awt.Color(0, 0, 255));
        Penerbit.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        Tahun.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Tahun.setForeground(new java.awt.Color(0, 0, 255));
        Tahun.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel13.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 255));
        jLabel13.setText("PPN");

        Stok.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        Stok.setForeground(new java.awt.Color(0, 0, 255));
        Stok.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Stok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                StokKeyTyped(evt);
            }
        });

        HargaPokok.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        HargaPokok.setForeground(new java.awt.Color(0, 0, 255));
        HargaPokok.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        HargaPokok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                HargaPokokKeyTyped(evt);
            }
        });

        jTextField10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextField10.setForeground(new java.awt.Color(0, 0, 255));
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField10KeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 255));
        jLabel14.setText("DISKON");

        jTextField11.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextField11.setForeground(new java.awt.Color(0, 0, 255));
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField11KeyTyped(evt);
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

        jLabel5.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setText("%");

        jLabel15.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 255));
        jLabel15.setText("%");

        btnCari.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnCari.setForeground(new java.awt.Color(0, 0, 255));
        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-search-32.png"))); // NOI18N
        btnCari.setText("CARI BUKU");

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
        btnCetak.setText("CETAK DATA BUKU");
        btnCetak.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCetak.setContentAreaFilled(false);
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        jPanel2.setBackground(new java.awt.Color(0, 0, 255));
        jPanel2.setForeground(new java.awt.Color(0, 0, 255));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-exit-30.png"))); // NOI18N
        btnExit.setContentAreaFilled(false);
        btnExit.setFocusable(false);
        btnExit.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_MacOS_Close_30px_1.png"))); // NOI18N
        btnExit.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_MacOS_Close_30px_1.png"))); // NOI18N
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-book-32.png"))); // NOI18N
        jLabel17.setText("DATA BUKU");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator2)
                .addGap(249, 249, 249))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(Judul, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(NoISBN)))
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Gambar)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(62, 62, 62)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Penulis, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(Tahun, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel13)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5))
                                    .addComponent(Penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(Stok, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                    .addComponent(HargaPokok, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addGap(19, 120, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCari)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Gambar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Judul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9)
                                .addComponent(Penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(NoISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(Tahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(Penulis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(Stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14)
                                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel15))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(49, 49, 49)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel12)
                                            .addComponent(HargaPokok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(btnEdit)
                                            .addComponent(btnTambah)
                                            .addComponent(btnHapus)))))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnOk)
                            .addComponent(btnBatal))))
                .addGap(20, 20, 20)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari)
                    .addComponent(btnCetak))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        reset();
        klik = "tambah";
        tampilOB();
        sembunyiCRUD();
        tampil();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if(Integer.toString(id).equals("0")){
            JOptionPane.showMessageDialog(null, "Silahkan pilih data yang akan diubah");
        }else{
        klik = "edit";
        tampilOB();
        sembunyiCRUD();
        tampil();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
    if (klik.equals("tambah")) {
        if (Gambar.getText().isEmpty()) {
            // Menampilkan pesan kesalahan jika ada data yang belum diisi
            JOptionPane.showMessageDialog(null, "Ada data yang belum di isi");
        } else {
            // Mengelola file gambar
            File sumber = new File(source);
            destination = "../TokoBuku/src/Gambar/" + sumber.getName();   
            try {
                // Menyalin file gambar dari sumber ke destinasi
                FileInputStream fis = new FileInputStream(sumber);
                FileOutputStream fos = new FileOutputStream(destination);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            } catch (Exception e) {
                // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            try {
                int hargajual;
                if (jTextField11.getText().equals("0")) {
                    // Menghitung harga jual jika diskon adalah 0
                    hargajual = Integer.parseInt(HargaPokok.getText()) * Integer.parseInt(jTextField10.getText()) / 100 + Integer.parseInt(HargaPokok.getText());
                } else {
                    // Menghitung harga jual jika ada diskon
                    int ppn = Integer.parseInt(HargaPokok.getText()) * Integer.parseInt(jTextField10.getText()) / 100 + Integer.parseInt(HargaPokok.getText());
                    int diskon = ppn * Integer.parseInt(jTextField11.getText()) / 100;
                    hargajual = ppn - diskon;
                }
                // Menyimpan data buku ke database
                sql = "INSERT INTO buku(gambar, judul, noisbn, penulis, penerbit, tahun, stok, harga_pokok, harga_jual, ppn, diskon) VALUE('" + sumber.getName() + "','" + Judul.getText() + "','" + NoISBN.getText() + "','" + Penulis.getText() + "','" + Penerbit.getText() + "','" + Tahun.getText() + "','" + Stok.getText() + "','" + HargaPokok.getText() + "','" + hargajual + "','" + jTextField10.getText() + "','" + jTextField11.getText() + "')";
                stat.execute(sql);
                JOptionPane.showMessageDialog(null, "Sukses tambah data");
            } catch (Exception e) {
                // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        // Mengatur ulang tampilan dan model tabel
        model.fireTableDataChanged();
        model.getDataVector().removeAllElements();
        aturTable();
        reset();
        sembunyiOB();
        tampilCRUD();
        sembunyi();
    } else if (klik.equals("edit")) {
        if (Gambar.getText().equals(namagambar)) {
            try {
                int hargajual;
                File sumber = new File(source);
                if (jTextField11.getText().equals("0")) {
                    // Menghitung harga jual jika diskon adalah 0
                    hargajual = Integer.parseInt(HargaPokok.getText()) * Integer.parseInt(jTextField10.getText()) / 100 + Integer.parseInt(HargaPokok.getText());
                } else {
                    // Menghitung harga jual jika ada diskon
                    int ppn = Integer.parseInt(HargaPokok.getText()) * Integer.parseInt(jTextField10.getText()) / 100 + Integer.parseInt(HargaPokok.getText());
                    int diskon = ppn * Integer.parseInt(jTextField11.getText()) / 100;
                    hargajual = ppn - diskon;
                }
                // Memperbarui data buku ke database jika gambar tidak berubah
                sql = "UPDATE buku set gambar='" + sumber.getName() + "',judul='" + Judul.getText() + "',noisbn='" + NoISBN.getText() + "',penulis='" + Penulis.getText() + "',penerbit='" + Penerbit.getText() + "',tahun='" + Tahun.getText() + "',stok='" + Stok.getText() + "',harga_pokok='" + HargaPokok.getText() + "',harga_jual='" + hargajual + "',ppn='" + jTextField10.getText() + "',diskon='" + jTextField11.getText() + "'WHERE id='" + id + "'";
                stat.execute(sql);
                JOptionPane.showMessageDialog(null, "Data telah diubah");
            } catch (Exception e) {
                // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else {
            try {
                try {
                    // Mengelola file gambar jika gambar berubah
                    File sumber = new File(source);
                    destination = "../TokoBuku/src/Gambar/" + sumber.getName();
                    FileInputStream fis = new FileInputStream(sumber);
                    FileOutputStream fos = new FileOutputStream(destination);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                } catch (Exception e) {
                    // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
                int hargajual;
                File sumber = new File(source);
                if (jTextField11.getText().equals("0")) {
                    // Menghitung harga jual jika diskon adalah 0
                    hargajual = Integer.parseInt(HargaPokok.getText()) * Integer.parseInt(jTextField10.getText()) / 100 + Integer.parseInt(HargaPokok.getText());
                } else {
                    // Menghitung harga jual jika ada diskon
                    int ppn = Integer.parseInt(HargaPokok.getText()) * Integer.parseInt(jTextField10.getText()) / 100 + Integer.parseInt(HargaPokok.getText());
                    int diskon = ppn * Integer.parseInt(jTextField11.getText()) / 100;
                    hargajual = ppn - diskon;
                }
                // Memperbarui data buku ke database jika gambar berubah
                sql = "UPDATE buku set gambar='" + sumber.getName() + "',judul='" + Judul.getText() + "',noisbn='" + NoISBN.getText() + "',penulis='" + Penulis.getText() + "',penerbit='" + Penerbit.getText() + "',tahun='" + Tahun.getText() + "',stok='" + Stok.getText() + "',harga_pokok='" + HargaPokok.getText() + "',harga_jual='" + hargajual + "',ppn='" + jTextField10.getText() + "',diskon='" + jTextField11.getText() + "'WHERE id='" + id + "'";
                stat.execute(sql);
                JOptionPane.showMessageDialog(null, "Data telah diubah");
            } catch (Exception e) {
                // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        // Mengatur ulang tampilan dan model tabel
        model.fireTableDataChanged();
        model.getDataVector().removeAllElements();
        aturTable();
        reset();
        sembunyiOB();
        tampilCRUD();
        sembunyi();
    }
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
    // Memeriksa apakah data buku sudah dipilih
    if (Integer.toString(id).equals("0")) {
        JOptionPane.showMessageDialog(null, "Silahkan pilih data yang akan dihapus");
    } else {
        // Menampilkan konfirmasi penghapusan data
        if (JOptionPane.showConfirmDialog(null, "Apakah yakin ingin menghapus data ini?", "Peringatan", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                // Menghapus data buku dari database
                sql = "DELETE FROM buku WHERE id='" + id + "'";
                stat.execute(sql);
                JOptionPane.showMessageDialog(null, "Data telah terhapus");               
                // Mengatur ulang tampilan dan model tabel
                model.fireTableDataChanged();
                model.getDataVector().removeAllElements();
                aturTable();
                reset();
                sembunyi();
                tampilCRUD();
                sembunyiOB();
            } catch (Exception e) {
                // Menangani kesalahan dan menampilkan pesan kesalahan jika terjadi
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else {
            // Mereset tampilan dan tombol CRUD jika konfirmasi "No"
            reset();
            sembunyi();
            tampilCRUD();
            sembunyiOB();
        }
    }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        reset();
        sembunyi();
        tampilCRUD();
        sembunyiOB();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    int a = jf.showOpenDialog(this);
    if (a == JFileChooser.APPROVE_OPTION) {
    // Mendapatkan batas komponen jLabel2
    Rectangle rect = jLabel2.getBounds();  
    // Menetapkan sumber (source) dan nama gambar
    source = jf.getSelectedFile().getPath();
    namagambar = jf.getSelectedFile().getName();    
    // Mengatur gambar ke ukuran yang sesuai dan menampilkannya di jLabel2
    ImageIcon icon = new ImageIcon(source);
    Image scale = icon.getImage().getScaledInstance(rect.width, rect.height, Image.SCALE_DEFAULT);
    icon = new ImageIcon(scale);
    jLabel2.setIcon(icon);
    
    // Menetapkan teks pada komponen Gambar sesuai dengan nama gambar yang dipilih
    Gambar.setText(namagambar);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TabelBukuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelBukuMouseClicked
        //Mengatur proses Klik pada Tabel
        try{
            sql = "SELECT * FROM buku WHERE judul='"+TabelBuku.getValueAt(TabelBuku.getSelectedRow(),2)+"'";
            rs = stat.executeQuery(sql);
            if(rs.next()){
                Rectangle rect = jLabel2.getBounds();
                source = "../TokoBuku/src/Gambar/"+rs.getString("gambar");
                ImageIcon icon = new ImageIcon(source);
                Image scale = icon.getImage().getScaledInstance(rect.width, rect.height,Image.SCALE_DEFAULT);
                icon = new ImageIcon(scale);
                jLabel2.setIcon(icon);
                tampilCRUD();
                sembunyiOB();
                sembunyi();
                namagambar = rs.getString("gambar");
                Gambar.setText(rs.getString("gambar"));
                id = rs.getInt("id");
                Judul.setText(rs.getString("judul"));
                NoISBN.setText(rs.getString("noisbn"));
                Penulis.setText(rs.getString("penulis"));
                Penerbit.setText(rs.getString("penerbit"));
                Tahun.setText(Integer.toString(rs.getInt("tahun")));
                Stok.setText(rs.getString("stok"));
                HargaPokok.setText(rs.getString("harga_pokok"));
                jTextField10.setText(rs.getString("ppn"));
                jTextField11.setText(rs.getString("diskon"));
            }   
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }//GEN-LAST:event_TabelBukuMouseClicked

    private void StokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StokKeyTyped
       if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Hanya karakter angka yang diperbolehkan");
        }
    }//GEN-LAST:event_StokKeyTyped

    private void HargaPokokKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HargaPokokKeyTyped
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Hanya karakter angka yang diperbolehkan");
        }
    }//GEN-LAST:event_HargaPokokKeyTyped

    private void jTextField10KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyTyped
         if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Hanya karakter angka yang diperbolehkan");
        }
    }//GEN-LAST:event_jTextField10KeyTyped

    private void jTextField11KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyTyped
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Hanya karakter angka yang diperbolehkan");
        }
    }//GEN-LAST:event_jTextField11KeyTyped

    private void NoISBNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoISBNKeyTyped
         if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
            JOptionPane.showMessageDialog(null, "Hanya karakter angka yang diperbolehkan");
        }
    }//GEN-LAST:event_NoISBNKeyTyped

    private void CariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CariKeyTyped
       if(Cari.getText().length()>=0){
       model.fireTableDataChanged();
        model.getDataVector().removeAllElements();
       int no=1;
       String[] judul = {"No","Gambar","Judul","No ISBN","Penulis","Penerbit","Tahun","Stok","Harga Pokok","Harga Jual","PPN","Diskon"};
       model = new DefaultTableModel(null,judul){
           @Override
           public Class getColumnClass(int column){
               if(column==1){
                   return ImageIcon.class;
               }
               return Object.class;
           }
           
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
       };
        try{
            sql = "SELECT * FROM buku WHERE judul LIKE '%"+Cari.getText()+"%'";
            rs = stat.executeQuery(sql);
            while(rs.next()){
                String source = ("../TokoBuku/src/Gambar/"+rs.getString("gambar"));
                ImageIcon icon = new ImageIcon(source);
                Image scale = icon.getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT);
                icon = new ImageIcon(scale);
                Object[] isi = {Integer.toString(no++),icon,rs.getString("judul"),rs.getString("noisbn"),rs.getString("penulis"),rs.getString("penerbit"),rs.getInt("tahun"),rs.getString("stok"),rs.getString("harga_pokok"),rs.getString("harga_jual"),rs.getString("ppn"),rs.getString("diskon")};
                model.addRow(isi);
            }
          ((DefaultTableCellRenderer)TabelBuku.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
           TabelBuku.setModel(model);
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(JLabel.CENTER);
            TabelBuku.getColumnModel().getColumn(0).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(2).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(3).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(4).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(5).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(6).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(7).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(8).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(9).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(10).setCellRenderer(render);
            TabelBuku.getColumnModel().getColumn(11).setCellRenderer(render);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"gagal"+e.getMessage());
        }
       }else{
            model.fireTableDataChanged();
            model.getDataVector().removeAllElements();
           aturTable();
       }
    }//GEN-LAST:event_CariKeyTyped

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        // TODO add your handling code here:
        try{
        String a = "src/Views/buku.jasper";
        File y = new File(a);
        JasperReport report = (JasperReport) JRLoader.loadObject(y);
        JasperPrint print = JasperFillManager.fillReport(report,null, con);
        JasperViewer.viewReport(print);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btnCetakActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed

        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

    }//GEN-LAST:event_formInternalFrameOpened


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Cari;
    private javax.swing.JTextField Gambar;
    private javax.swing.JTextField HargaPokok;
    private javax.swing.JTextField Judul;
    private javax.swing.JTextField NoISBN;
    private javax.swing.JTextField Penerbit;
    private javax.swing.JTextField Penulis;
    private javax.swing.JTextField Stok;
    private javax.swing.JTable TabelBuku;
    private javax.swing.JTextField Tahun;
    private javax.swing.JButton btnBatal;
    private javax.swing.JLabel btnCari;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    // End of variables declaration//GEN-END:variables
}
