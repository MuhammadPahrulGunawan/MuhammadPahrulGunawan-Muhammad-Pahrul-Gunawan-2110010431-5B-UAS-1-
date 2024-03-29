/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author MUHAMMAD PAHRUL GUNAWAN
 */
public class Tema extends javax.swing.JInternalFrame {

    /**
     * Creates new form tema
     */
    private String akses;
    
    public Tema() {
        initComponents();
        // Menghapus item yang ada pada komponen Tema
        Tema.removeAllItems();
        // Menambahkan opsi tema ("Light" dan "Dark") ke dalam komponen Tema
        String[] data = {"Light", "Dark"};
        for (String a : data) {
            Tema.addItem(a);
        }
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
            // Kosong karena tema "Light" tidak memerlukan tindakan khusus
            } else {
            // Memanggil metode "tema()" untuk menangani tema selain "Light"
            tema();
        }
            } catch (Exception e) {
        // Menampilkan pesan kesalahan jika terjadi pengecualian
        JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    //Methode untuk membuat teks menjadi warna biru ketika mode GELAP diterapkan
    private void tema(){
        this.setBackground(Color.BLACK);
        jPanel1.setBackground(Color.BLACK);
        jLabel2.setForeground(Color.BLUE);
        jLabel3.setForeground(Color.BLUE);
        btnOk.setForeground(Color.BLUE);
        ImageIcon icon = new ImageIcon("src/Gambar/icons8-exit-30.png");
        btnLogout.setIcon(icon);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
    }
    
    public void akses(String akses){
        this.akses = akses;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        Tema = new javax.swing.JComboBox();
        btnOk = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();

        jButton2.setText("jButton2");

        setBackground(new java.awt.Color(255, 255, 255));

        Tema.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        Tema.setForeground(new java.awt.Color(0, 0, 255));
        Tema.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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

        jPanel1.setBackground(new java.awt.Color(0, 0, 255));

        jLabel2.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-theme-32.png"))); // NOI18N
        jLabel2.setText("TEMA");

        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/icons8-exit-30.png"))); // NOI18N
        btnLogout.setContentAreaFilled(false);
        btnLogout.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_MacOS_Close_30px_1.png"))); // NOI18N
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 38, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(82, 82, 82)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Lucida Fax", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("SILAHKAN PILIH TEMA");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Tema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(btnOk)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        try {
        // Mendapatkan tema yang dipilih dari komponen Tema
        String tema = Tema.getSelectedItem() != null ? Tema.getSelectedItem().toString() : "";
        // Menulis tema ke dalam file "tema.txt"
        try (FileWriter fw = new FileWriter("src/tema.txt");
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(tema);
        }
        // Menampilkan MenuUtama sesuai dengan hak akses
        if ("Owner".equals(akses)) {
            new MenuUtamaAdmin().setVisible(true);
        } else {
            new MenuUtama().setVisible(true);
        }
    } catch (IOException e) {
        // Menangani kesalahan IO dengan menampilkan pesan dialog
        JOptionPane.showMessageDialog(null, "Error writing theme to file: " + e.getMessage());
    }
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
       this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox Tema;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
