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
import java.sql.*;
import java.util.HashMap;
import javax.swing.*;
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
public class Pemasukan extends javax.swing.JInternalFrame {

    /**
     * Creates new form Pemasukan
     */
    private koneksi kon = new koneksi();
    private Connection con;
    private Statement stat;
    private ResultSet rs;
    private String sql;
    private DefaultTableModel model,model1;
    private JasperReport jasper;
    private JasperPrint print;
    
    public Pemasukan() {
        initComponents();
        // Mengambil koneksi dan statement dari objek kon
        con = kon.con;
        stat = kon.stat;
        // Memanggil metode harian() untuk melakukan tindakan terkait harian
        harian();
        // Memanggil metode font() untuk melakukan tindakan terkait font
        font();
        // Menghilangkan panel utara pada internal frame (UI tweak)
        ((BasicInternalFrameUI)this.getUI()).setNorthPane(null);
        // Menetapkan batas tepi internal frame dengan garis hitam
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        try{
            // Membaca tema dari file "tema.txt"
            FileReader fr = new FileReader("src/tema.txt");
            BufferedReader br = new BufferedReader(fr);
            // Memeriksa tema yang dibaca dari file
            if(br.readLine().equals("Light")){
                // Tidak ada tindakan khusus jika tema adalah "Light"
            } else {
                // Memanggil metode tema() jika tema bukan "Light"
                tema();
            }
            // Menutup BufferedReader dan FileReader setelah digunakan
            br.close();
            fr.close();
        } catch(Exception e){
            // Menangani pengecualian dengan menampilkan pesan kesalahan
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    //Methode untuk mengatur FONT
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
                TabelHarian.getTableHeader().setFont(sizeFont1);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    //Methode untuk memebuat text menjadi BIRU pada saat mode GELAP diterapkan
    private void tema(){
        this.setBackground(Color.BLACK);
        jPanel1.setBackground(Color.BLACK);
        jPanel3.setBackground(Color.BLACK);
        jLabel5.setForeground(Color.BLUE);
        jLabel6.setForeground(Color.BLUE);
        jLabel7.setForeground(Color.BLUE);
        jTabbedPane1.setForeground(Color.BLACK);
        btnCetak.setForeground(Color.BLUE);
        TabelHarian.setBackground(Color.BLACK);
        TabelHarian.setForeground(Color.BLUE);
        TabelHarian.getTableHeader().setForeground(Color.BLACK);
        ImageIcon icon = new ImageIcon("src/Gambar/icons8-exit-30.png");
        jButton4.setIcon(icon);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
    }
    
    private void harian(){
        try {
        String[] judul = {"No", "Nota", "Judul", "Harga", "Qty", "Harga Total", "Tanggal"};
        model = new DefaultTableModel(null, judul) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sql = "SELECT judul, harga_jual, qty, tanggal, nota FROM penjualan_detail, penjualan, buku WHERE tanggal=CURDATE() AND buku.id=id_buku AND penjualan.id=penjualan_detail.id_penjualan";
        rs = stat.executeQuery(sql);
        while (rs.next()) {
            String[] data = {"", rs.getString("nota"), rs.getString("judul"), rs.getString("harga_jual"), rs.getString("qty"), "", rs.getString("tanggal")};
            model.addRow(data);
        }
        TabelHarian.setModel(model);
        for (int i = 0; i < TabelHarian.getRowCount(); i++) {
            String nomer = String.valueOf(i + 1);
            int total = (Integer.parseInt(TabelHarian.getValueAt(i, 3).toString()) * Integer.parseInt(TabelHarian.getValueAt(i, 4).toString()));
            TabelHarian.setValueAt(nomer, i, 0);
            TabelHarian.setValueAt(total, i, 5);
        }
        ((DefaultTableCellRenderer) TabelHarian.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < TabelHarian.getColumnCount(); i++) {
            TabelHarian.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        try {
            sql = "SELECT SUM(harga_jual * qty) AS tot FROM penjualan, penjualan_detail, buku WHERE penjualan.tanggal=CURDATE() AND penjualan.id=id_penjualan AND buku.id=id_buku";
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                jLabel6.setText(rs.getString("tot"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error calculating total: " + e.getMessage());
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error in daily report: " + e.getMessage());
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelHarian = new javax.swing.JTable();
        btnCetak = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1363, 741));

        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 255));
        jTabbedPane1.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        TabelHarian.setBackground(new java.awt.Color(51, 51, 255));
        TabelHarian.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        TabelHarian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(TabelHarian);

        btnCetak.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        btnCetak.setForeground(new java.awt.Color(0, 0, 255));
        btnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-printer-32.png"))); // NOI18N
        btnCetak.setText("CETAK");
        btnCetak.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnCetak.setContentAreaFilled(false);
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setText("TOTAL :");

        jLabel6.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setText("total");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(btnCetak))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("LAPORAN", jPanel1);

        jPanel3.setBackground(new java.awt.Color(0, 0, 255));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-income-32.png"))); // NOI18N
        jLabel7.setText("PEMASUKAN");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-exit-30.png"))); // NOI18N
        jButton4.setContentAreaFilled(false);
        jButton4.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_MacOS_Close_30px_1.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1)
                .addGap(124, 124, 124))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
     this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
      try {
        // Membuat objek File yang merepresentasikan file harian.jasper
        File file = new File("src/Views/harian.jasper");
        // Membuat objek HashMap untuk mengirim parameter ke laporan JasperReport
        HashMap hm = new HashMap();
        // Memuat JasperReport dari file
        jasper = (JasperReport) JRLoader.loadObject(file);
        // Mengisi laporan JasperReport dengan data menggunakan koneksi database
        print = JasperFillManager.fillReport(jasper, hm, con);
        // Menampilkan laporan menggunakan JasperViewer
        JasperViewer.viewReport(print, false);
        // Mengatur tampilan default look-and-feel untuk JasperViewer
        JasperViewer.setDefaultLookAndFeelDecorated(true);
    } catch (Exception e) {
        // Menangani pengecualian dengan menampilkan pesan kesalahan
        JOptionPane.showMessageDialog(null, "Error generating report: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCetakActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelHarian;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
