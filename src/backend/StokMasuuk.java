
package backend;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StokMasuuk extends javax.swing.JPanel {
private Map<String, String> barangMap = new HashMap<>();

    public StokMasuuk() {
        
        initComponents();
        loadDataStokMasuk();
        label_username.setText(Login.Session.getUsername());
    }
    private void loadBarangKeComboBox() {
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    barangMap.clear();

    try {
        Koneksi.config();
        Connection conn = Koneksi.getConnection();
        String sql = "SELECT id_barang, nama_barang FROM barang";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            String idBarang = rs.getString("id_barang");
            String namaBarang = rs.getString("nama_barang");
            barangMap.put(namaBarang, idBarang);
            model.addElement(namaBarang);
        }

        cmb_pilihbarang.setModel(model);
        cmb_pilihbarang.setSelectedIndex(-1); 

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data barang: " + e.getMessage());
    }
}

private void formWindowOpened(java.awt.event.WindowEvent evt) {
    loadBarangKeComboBox();
}
private void loadDataStokMasuk() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Stok Masuk");
    model.addColumn("Nama Barang");
    model.addColumn("Qty");
    model.addColumn("Harga");
    model.addColumn("Pemasok");

    try {
        Koneksi.config();
        Connection conn = Koneksi.getConnection();
        String sql = "SELECT sm.id_stok_masuk, b.nama_barang, sm.qty, sm.harga, sm.id_pemasok " +
                     "FROM stok_masuk sm JOIN barang b ON sm.id_barang = b.id_barang";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_stok_masuk"),
                rs.getString("nama_barang"),
                rs.getInt("qty"),
                rs.getInt("harga"),
                rs.getString("pemasok")
            });
        }

        jTable_custom1.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data stok masuk: " + e.getMessage());
    }
}
private void cariBarang() {
    String keyword = txt_search.getText().trim();

    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Stok Masuk");
    model.addColumn("Nama Barang");
    model.addColumn("Qty");
    model.addColumn("Harga");
    model.addColumn("Pemasok");

    try {
        Koneksi.config();
        Connection conn = Koneksi.getConnection();
        String sql = "SELECT sm.id_stok_masuk, b.nama_barang, sm.qty, sm.harga, sm.pemasok " +
                     "FROM stok_masuk sm " +
                     "JOIN barang b ON sm.id_barang = b.id_barang " +
                     "WHERE b.nama_barang LIKE ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + keyword + "%");
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_stok_masuk"),
                rs.getString("nama_barang"),
                rs.getInt("qty"),
                rs.getInt("harga"),
                rs.getString("pemasok")
            });
        }

        jTable_custom1.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal mencari data: " + e.getMessage());
    }
}


 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        page_mainn = new javax.swing.JPanel();
        page_stokmasuk = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        btn_search = new javax.swing.JButton();
        txt_search = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        label_username = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_custom1 = new custom.JTable_custom();
        jLabel24 = new javax.swing.JLabel();
        page_tambahstokmasuk = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        form_tambah = new javax.swing.JPanel();
        txt_jumlahbarang = new javax.swing.JTextField();
        txt_harga = new javax.swing.JTextField();
        txt_pemasok = new javax.swing.JTextField();
        cmb_pilihbarang = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        page_mainn.setLayout(new java.awt.CardLayout());

        page_stokmasuk.setBackground(new java.awt.Color(255, 244, 232));
        page_stokmasuk.setForeground(new java.awt.Color(230, 230, 230));
        page_stokmasuk.setPreferredSize(new java.awt.Dimension(836, 666));
        page_stokmasuk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_stokmasuk.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        btn_search.setContentAreaFilled(false);

        btn_search.setBorderPainted(false);
        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search.png"))); // NOI18N
        btn_search.setBorder(null);
        btn_search.setContentAreaFilled(false);
        btn_search.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search Select.png"))); // NOI18N
        page_stokmasuk.add(btn_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 50, 40));

        txt_search.setBackground(new java.awt.Color(238, 236, 227));
        txt_search.setBorder(null);
        txt_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchActionPerformed(evt);
            }
        });
        page_stokmasuk.add(txt_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 280, 20));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Search.png"))); // NOI18N
        page_stokmasuk.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 400, -1));

        btn_tambah.setContentAreaFilled(false);

        btn_tambah.setBorderPainted(false);
        btn_tambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/stokmasuk/Group 49.png"))); // NOI18N
        btn_tambah.setBorder(null);
        btn_tambah.setContentAreaFilled(false);
        btn_tambah.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/stokmasuk/Group 102.png"))); // NOI18N
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });
        page_stokmasuk.add(btn_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 210, 50));

        btn_hapus.setContentAreaFilled(false);
        btn_hapus.setBorderPainted(false);
        btn_hapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Hapus.png"))); // NOI18N
        btn_hapus.setBorder(null);
        btn_hapus.setContentAreaFilled(false);
        btn_hapus.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Hapus Select.png"))); // NOI18N
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });
        page_stokmasuk.add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 170, -1, 40));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/BG Button.png"))); // NOI18N
        page_stokmasuk.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 710, 65));

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_stokmasuk.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        label_username.setText("Username");
        page_stokmasuk.add(label_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        jTable_custom1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable_custom1);

        page_stokmasuk.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 710, 380));

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/stokmasuk/Stok Masuk.png"))); // NOI18N
        page_stokmasuk.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 320, 40));

        page_mainn.add(page_stokmasuk, "card2");

        page_tambahstokmasuk.setBackground(new java.awt.Color(255, 244, 232));
        page_tambahstokmasuk.setPreferredSize(new java.awt.Dimension(836, 666));
        page_tambahstokmasuk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/stokmasuk/Stok Masuk.png"))); // NOI18N
        page_tambahstokmasuk.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 312, 37));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_tambahstokmasuk.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        form_tambah.setBackground(new java.awt.Color(255, 244, 232));
        form_tambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_jumlahbarang.setBorder(null);
        form_tambah.add(txt_jumlahbarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 430, 20));

        txt_harga.setBorder(null);
        form_tambah.add(txt_harga, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, 430, 30));

        txt_pemasok.setBorder(null);
        form_tambah.add(txt_pemasok, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, 430, 30));

        cmb_pilihbarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmb_pilihbarang.setBorder(null);
        form_tambah.add(cmb_pilihbarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 430, 30));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/stokmasuk/Group 46 (3).png"))); // NOI18N
        form_tambah.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 490));

        page_tambahstokmasuk.add(form_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 560, 490));

        btn_simpan.setContentAreaFilled(false);

        btn_simpan.setBorderPainted(false);
        btn_simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/stokmasuk/Group 19.png"))); // NOI18N
        btn_simpan.setBorder(null);
        btn_simpan.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/stokmasuk/Group 51.png"))); // NOI18N
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });
        page_tambahstokmasuk.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 580, 120, 40));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_tambahstokmasuk.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        jLabel31.setText("Username");
        page_tambahstokmasuk.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        page_mainn.add(page_tambahstokmasuk, "card3");

        add(page_mainn, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
          loadBarangKeComboBox();
        page_mainn.removeAll();
        page_mainn.add(page_tambahstokmasuk);
        page_mainn.repaint();
        page_mainn.revalidate();
        
                loadDataStokMasuk();

    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
String selectedNamaBarang = (String) cmb_pilihbarang.getSelectedItem();

    if (selectedNamaBarang == null || selectedNamaBarang.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String idBarang = barangMap.get(selectedNamaBarang);
    if (idBarang == null) {
        JOptionPane.showMessageDialog(this, "Barang tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String jumlahStr = txt_jumlahbarang.getText().trim();
    String hargaStr = txt_harga.getText().trim();
    String pemasok = txt_pemasok.getText().trim();

    if (jumlahStr.isEmpty() || hargaStr.isEmpty() || pemasok.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        int qty = Integer.parseInt(jumlahStr);
        int harga = Integer.parseInt(hargaStr);
        String idStokMasuk = "SM" + System.currentTimeMillis();

        Koneksi.config();
        Connection conn = Koneksi.getConnection();
        String sql = "INSERT INTO stok_masuk (id_stok_masuk, id_barang, qty, harga, pemasok) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, idStokMasuk);
        pst.setString(2, idBarang);
        pst.setInt(3, qty);
        pst.setInt(4, harga);
        pst.setString(5, pemasok);
        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");

        // Reset form
        cmb_pilihbarang.setSelectedIndex(-1);
        txt_jumlahbarang.setText("");
        txt_harga.setText("");
        txt_pemasok.setText("");

        // Kembali ke halaman utama
        page_mainn.removeAll();
        page_mainn.add(page_stokmasuk);
        page_mainn.repaint();
        page_mainn.revalidate();
        
        loadDataStokMasuk();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Jumlah dan harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_btn_simpanActionPerformed

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
         cariBarang();
    }//GEN-LAST:event_txt_searchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JComboBox<String> cmb_pilihbarang;
    private javax.swing.JPanel form_tambah;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private custom.JTable_custom jTable_custom1;
    private javax.swing.JLabel label_username;
    private javax.swing.JPanel page_mainn;
    private javax.swing.JPanel page_stokmasuk;
    private javax.swing.JPanel page_tambahstokmasuk;
    private javax.swing.JTextField txt_harga;
    private javax.swing.JTextField txt_jumlahbarang;
    private javax.swing.JTextField txt_pemasok;
    private javax.swing.JTextField txt_search;
    // End of variables declaration//GEN-END:variables
}
