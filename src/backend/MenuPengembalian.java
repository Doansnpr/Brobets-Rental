
package backend;

import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.print.*;
import javax.swing.*;

public class MenuPengembalian extends javax.swing.JPanel {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    public MenuPengembalian() {
        initComponents();
        label_username.setText(Login.Session.getUsername());
        tampilDataPenyewaan(); 
    }

    public void tampilDataPenyewaan() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Sewa");
        model.addColumn("Nama Penyewa");
        model.addColumn("Tgl Sewa");
        model.addColumn("Tgl Rencana Kembali");

        try {
            String sql = "SELECT p.id_sewa, pl.nama_pelanggan, p.tgl_sewa, p.tgl_rencana_kembali "
                        + "FROM penyewaan p "
                        + "JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan "
                        + "WHERE p.Status != 'Sudah Kembali'";


            Koneksi.config();
            con = Koneksi.getConnection();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_sewa"),
                    rs.getString("nama_pelanggan"),
                    rs.getDate("tgl_sewa"),
                    rs.getDate("tgl_rencana_kembali")
                });
            }

            table_kembali.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data: " + e.getMessage());
        }

    }
    
    public void loadBarangKembali(String idSewa) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };          
        
        
        model.addColumn("ID Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Jumlah Disewa");
        model.addColumn("Jumlah Kembali");
        model.addColumn("Kondisi");

        try {
            Koneksi.config();
            con = Koneksi.getConnection();
            String sql = "SELECT ds.id_barang, b.nama_barang, ds.qty " +
                         "FROM detail_sewa ds " +
                         "JOIN barang b ON ds.id_barang = b.id_barang " +
                         "WHERE ds.id_sewa = ?";

            pst = con.prepareStatement(sql);
            pst.setString(1, idSewa);
            rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("qty"),
                    "", // Kolom input jumlah kembali
                    ""  // Kolom input kondisi (Baik / Rusak / Hilang)
                });
            }

            table_barang_kembali.setModel(model);

     
            String[] kondisiEnum = {"Baik", "Rusak", "Hilang"};
            JComboBox<String> comboKondisi = new JComboBox<>(kondisiEnum);

            table_barang_kembali.getColumnModel().getColumn(4)
                .setCellEditor(new DefaultCellEditor(comboKondisi));

            
            model.addTableModelListener(e -> {
            try {
                int dendaKeterlambatan = Integer.parseInt(denda_terlambat.getText());
                hitungDendaBarangKembali(dendaKeterlambatan);
            } catch (NumberFormatException ex) {
                total_denda.setText("0");
            }
        });
        
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat barang: " + e.getMessage());
        }
    }                                                   
    
    public void cetakNota(String idPengembalian) {
    try {
        StringBuilder struk = new StringBuilder();
        
        // Ambil data pengembalian
        String sql = "SELECT p.id_kembali, p.id_sewa, p.tgl_kembali, p.status, p.denda_keterlambatan, p.total_denda, pg.nama_lengkap " +
                     "FROM pengembalian p JOIN penyewaan s ON p.id_sewa = s.id_sewa " +
                     "JOIN pengguna pg ON s.id_pengguna = pg.id_pengguna " +
                     "WHERE p.id_kembali = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, idPengembalian);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            struk.append("=========== STRUK PENGEMBALIAN ===========\n");
            struk.append("ID Pengembalian : ").append(rs.getString("id_kembali")).append("\n");
            struk.append("ID Sewa         : ").append(rs.getString("id_sewa")).append("\n");
            struk.append("Nama Penyewa    : ").append(rs.getString("nama_lengkap")).append("\n");
            struk.append("Tanggal Kembali : ").append(rs.getString("tgl_kembali")).append("\n");
            struk.append("Status          : ").append(rs.getString("status")).append("\n");
            struk.append("------------------------------------------\n");
            struk.append("Barang:\n");
        }

        rs.close();
        pst.close();

        // Ambil detail barang
        String sqlDetail = "SELECT dp.id_barang, b.nama_barang, dp.jumlah, dp.kondisi, dp.denda_barang " +
                           "FROM detail_pengembalian dp " +
                           "JOIN barang b ON dp.id_barang = b.id_barang " +
                           "WHERE dp.id_pengembalian = ?";
        pst = con.prepareStatement(sqlDetail);
        pst.setString(1, idPengembalian);
        rs = pst.executeQuery();

        while (rs.next()) {
            struk.append("- ").append(rs.getString("nama_barang")).append(" (x").append(rs.getInt("jumlah")).append(")\n");
            struk.append("  Kondisi: ").append(rs.getString("kondisi")).append("\n");
            struk.append("  Denda : Rp ").append(rs.getInt("denda_barang")).append("\n");
        }

        rs.close();
        pst.close();

        // Ambil total denda terakhir
        pst = con.prepareStatement("SELECT total_denda FROM pengembalian WHERE id_kembali = ?");
        pst.setString(1, idPengembalian);
        rs = pst.executeQuery();
        if (rs.next()) {
            struk.append("------------------------------------------\n");
            struk.append("Total Denda     : Rp ").append(rs.getInt("total_denda")).append("\n");
            struk.append("==========================================\n");
            struk.append("     TERIMA KASIH TELAH MENGEMBALIKAN     \n");
            struk.append("==========================================\n");
        }

        rs.close();
        pst.close();

        // Tampilkan dan cetak
        JTextArea textArea = new JTextArea(struk.toString());
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        boolean done = textArea.print();
        if (done) {
            JOptionPane.showMessageDialog(null, "Struk berhasil dicetak.");
        } else {
            JOptionPane.showMessageDialog(null, "Pencetakan dibatalkan.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal mencetak struk: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    
    public void hitungDendaBarangKembali(int dendaKeterlambatan) {
    int totalDendaKerusakan = 0;

    DefaultTableModel model = (DefaultTableModel) table_barang_kembali.getModel();

    try {
        for (int i = 0; i < model.getRowCount(); i++) {
            String idBarang = model.getValueAt(i, 0).toString();
            String kondisi = model.getValueAt(i, 4).toString();

            if (kondisi.equalsIgnoreCase("Rusak") || kondisi.equalsIgnoreCase("Hilang")) {
                String sql = "SELECT harga_beli FROM barang WHERE id_barang = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, idBarang);
                rs = pst.executeQuery();

                if (rs.next()) {
                    int jumlahKembali = Integer.parseInt(model.getValueAt(i, 3).toString());
                    int hargaBeli = rs.getInt("harga_beli");
                    totalDendaKerusakan += hargaBeli * jumlahKembali;
                }

                rs.close();
                pst.close();
            }
        }

        int totalDenda = dendaKeterlambatan + totalDendaKerusakan;
        denda_kerusakan.setText(String.valueOf(totalDendaKerusakan));
        total_denda.setText(String.valueOf(totalDenda));

    } catch (SQLException | NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Gagal menghitung denda: " + e.getMessage());
    }
}


public String generateID(String prefix, String table, String kolom) {
    String id = prefix + "001";
    try {
        String sql = "SELECT " + kolom + " FROM " + table + " ORDER BY " + kolom + " DESC LIMIT 1";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();

        if (rs.next()) {
            String lastID = rs.getString(1);
            int number = Integer.parseInt(lastID.replace(prefix, ""));
            number++;
            id = String.format("%s%03d", prefix, number);
        }

        rs.close();
        pst.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal generate ID: " + e.getMessage());
    }
    return id;
}


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser = new com.raven.datechooser.DateChooser();
        page_main = new javax.swing.JPanel();
        page_pengembalian = new javax.swing.JPanel();
        btn_retur = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        label_username = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btn_search = new javax.swing.JButton();
        txt_search = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_kembali = new custom.JTable_custom();
        btn_search1 = new javax.swing.JButton();
        txt_search1 = new javax.swing.JTextField();
        page_tambah = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        form_tambah = new javax.swing.JPanel();
        ID_transaksi = new javax.swing.JTextField();
        txt_nama_penyewa = new javax.swing.JTextField();
        tgl_kembali = new javax.swing.JTextField();
        txt_status = new javax.swing.JTextField();
        denda_terlambat = new javax.swing.JTextField();
        btn_calender = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        btn_next = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        btn_back = new javax.swing.JButton();
        daftar_barang = new javax.swing.JPanel();
        form_table_tambah = new javax.swing.JPanel();
        btn_back1 = new javax.swing.JButton();
        total_denda = new javax.swing.JTextField();
        denda_kerusakan = new javax.swing.JTextField();
        btn_simpan = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_barang_kembali = new custom.JTable_custom();
        jLabel1 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();

        dateChooser.setForeground(new java.awt.Color(195, 45, 45));
        dateChooser.setDateFormat("yyyy-MM-dd");
        dateChooser.setTextRefernce(tgl_kembali);

        setLayout(new java.awt.CardLayout());

        page_main.setBackground(new java.awt.Color(255, 244, 232));
        page_main.setLayout(new java.awt.CardLayout());

        page_pengembalian.setBackground(new java.awt.Color(255, 244, 232));
        page_pengembalian.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_retur.setContentAreaFilled(false);
        btn_retur.setBorderPainted(false);
        btn_retur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Retur.png"))); // NOI18N
        btn_retur.setBorder(null);
        btn_retur.setContentAreaFilled(false);
        btn_retur.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Retur Select.png"))); // NOI18N
        btn_retur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_returActionPerformed(evt);
            }
        });
        page_pengembalian.add(btn_retur, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 130, -1, 40));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_pengembalian.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Pengembalian.png"))); // NOI18N
        page_pengembalian.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 312, 37));

        label_username.setText("Username");
        page_pengembalian.add(label_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_pengembalian.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Search.png"))); // NOI18N
        page_pengembalian.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 410, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/BG Button.png"))); // NOI18N
        page_pengembalian.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 720, 65));

        btn_search.setContentAreaFilled(false);

        btn_search.setBorderPainted(false);
        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Search.png"))); // NOI18N
        btn_search.setBorder(null);
        btn_search.setContentAreaFilled(false);
        btn_search.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Search Select.png"))); // NOI18N
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });
        page_pengembalian.add(btn_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 50, 40));

        txt_search.setBackground(new java.awt.Color(238, 236, 227));
        txt_search.setBorder(null);
        page_pengembalian.add(txt_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 141, 300, 20));

        table_kembali.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(table_kembali);

        page_pengembalian.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 710, 410));

        btn_search.setContentAreaFilled(false);

        btn_search.setBorderPainted(false);
        btn_search1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search.png"))); // NOI18N
        btn_search1.setBorder(null);
        btn_search1.setContentAreaFilled(false);
        btn_search1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search Select.png"))); // NOI18N
        btn_search1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_search1ActionPerformed(evt);
            }
        });
        page_pengembalian.add(btn_search1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 50, 40));

        txt_search1.setBackground(new java.awt.Color(238, 236, 227));
        txt_search1.setBorder(null);
        page_pengembalian.add(txt_search1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 300, 20));

        page_main.add(page_pengembalian, "card2");

        page_tambah.setBackground(new java.awt.Color(255, 244, 232));
        page_tambah.setPreferredSize(new java.awt.Dimension(836, 666));
        page_tambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Pengembalian.png"))); // NOI18N
        page_tambah.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 312, 37));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_tambah.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        form_tambah.setBackground(new java.awt.Color(255, 244, 232));
        form_tambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ID_transaksi.setBackground(new java.awt.Color(255, 244, 232));
        ID_transaksi.setBorder(null);
        form_tambah.add(ID_transaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 430, 30));

        txt_nama_penyewa.setBackground(new java.awt.Color(255, 244, 232));
        txt_nama_penyewa.setBorder(null);
        txt_nama_penyewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nama_penyewaActionPerformed(evt);
            }
        });
        form_tambah.add(txt_nama_penyewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 223, 430, 30));

        tgl_kembali.setBackground(new java.awt.Color(255, 244, 232));
        tgl_kembali.setBorder(null);
        form_tambah.add(tgl_kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 287, 390, 30));

        txt_status.setBackground(new java.awt.Color(255, 244, 232));
        txt_status.setBorder(null);
        form_tambah.add(txt_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 355, 440, 30));

        denda_terlambat.setBackground(new java.awt.Color(255, 244, 232));
        denda_terlambat.setBorder(null);
        form_tambah.add(denda_terlambat, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 421, 430, 30));

        btn_calender.setContentAreaFilled(false);

        btn_calender.setBorderPainted(false);
        btn_calender.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Calender.png"))); // NOI18N
        btn_calender.setBorder(null);
        btn_calender.setContentAreaFilled(false);
        btn_calender.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Calender Select.png"))); // NOI18N
        btn_calender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_calenderActionPerformed(evt);
            }
        });
        btn_calender.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                btn_calenderPropertyChange(evt);
            }
        });
        form_tambah.add(btn_calender, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 280, 40, 40));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Form Pengembalian.png"))); // NOI18N
        form_tambah.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, -20, 560, 530));

        page_tambah.add(form_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 570, 490));

        btn_next.setContentAreaFilled(false);

        btn_next.setBorderPainted(false);
        btn_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Buntton Lanjut.png"))); // NOI18N
        btn_next.setBorder(null);
        btn_next.setContentAreaFilled(false);
        btn_next.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Buntton Lanjut Select.png"))); // NOI18N
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });
        page_tambah.add(btn_next, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 590, 100, 40));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_tambah.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        jLabel31.setText("Username");
        page_tambah.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        btn_back.setContentAreaFilled(false);

        btn_back.setBorderPainted(false);
        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Kembali.png"))); // NOI18N
        btn_back.setBorder(null);
        btn_back.setContentAreaFilled(false);
        btn_back.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Kembali Select.png"))); // NOI18N
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });
        page_tambah.add(btn_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 590, 100, 40));

        page_main.add(page_tambah, "card3");

        daftar_barang.setBackground(new java.awt.Color(255, 244, 232));
        daftar_barang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        form_table_tambah.setBackground(new java.awt.Color(255, 244, 232));
        form_table_tambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_back.setContentAreaFilled(false);

        btn_back.setBorderPainted(false);
        btn_back1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Kembali.png"))); // NOI18N
        btn_back1.setBorder(null);
        btn_back1.setContentAreaFilled(false);
        btn_back1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Kembali Select.png"))); // NOI18N
        btn_back1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_back1ActionPerformed(evt);
            }
        });
        form_table_tambah.add(btn_back1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 480, 100, 40));

        total_denda.setBackground(new java.awt.Color(255, 244, 232));
        total_denda.setForeground(new java.awt.Color(153, 153, 153));
        total_denda.setToolTipText("");
        total_denda.setBorder(null);
        total_denda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                total_dendaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                total_dendaFocusLost(evt);
            }
        });
        form_table_tambah.add(total_denda, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 478, 200, 30));

        denda_kerusakan.setBackground(new java.awt.Color(255, 244, 232));
        denda_kerusakan.setForeground(new java.awt.Color(153, 153, 153));
        denda_kerusakan.setToolTipText("");
        denda_kerusakan.setBorder(null);
        denda_kerusakan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                denda_kerusakanFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                denda_kerusakanFocusLost(evt);
            }
        });
        form_table_tambah.add(denda_kerusakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 419, 200, 30));

        btn_simpan.setContentAreaFilled(false);

        btn_simpan.setBorderPainted(false);
        btn_simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Simpan.png"))); // NOI18N
        btn_simpan.setBorder(null);
        btn_simpan.setContentAreaFilled(false);
        btn_simpan.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Simpan Select.png"))); // NOI18N
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });
        form_table_tambah.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 480, 110, 40));

        table_barang_kembali.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
        jScrollPane2.setViewportView(table_barang_kembali);

        form_table_tambah.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 560, 350));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Form Daftar Barang.png"))); // NOI18N
        form_table_tambah.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 620, 540));

        daftar_barang.add(form_table_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 630, -1));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Daftar Barang Kembali.png"))); // NOI18N
        daftar_barang.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 460, 37));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        daftar_barang.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        jLabel34.setText("Username");
        daftar_barang.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        daftar_barang.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        page_main.add(daftar_barang, "card4");

        add(page_main, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_searchActionPerformed

    private void btn_returActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_returActionPerformed
        int selectedRow = table_kembali.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Harap pilih data yang ingin diretur!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idSewa = table_kembali.getValueAt(selectedRow, 0).toString();
        String namaPelanggan = table_kembali.getValueAt(selectedRow, 1).toString();
        String tglRencanaKembali = table_kembali.getValueAt(selectedRow, 3).toString();

        java.time.LocalDate tglHariIni = java.time.LocalDate.now();
        java.time.LocalDate tglRencana = java.time.LocalDate.parse(tglRencanaKembali);
        
        long selisihHari = java.time.temporal.ChronoUnit.DAYS.between(tglRencana, tglHariIni);
        String status;
        int denda;

        if (selisihHari > 0) {
            status = "Terlambat";
            denda = (int) selisihHari * 10000;
        } else {
            status = "Tepat Waktu";
            denda = 0;
        }
        
        page_main.removeAll();
        page_main.add(page_tambah);
        page_main.repaint();
        page_main.revalidate();

        ID_transaksi.setText(idSewa);
        txt_nama_penyewa.setText(namaPelanggan);
        tgl_kembali.setText(tglHariIni.toString());
        txt_status.setText(status);
        denda_terlambat.setText(String.valueOf(denda));
        
    
    }//GEN-LAST:event_btn_returActionPerformed

    private void btn_calenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_calenderActionPerformed
        // TODO add your handling code here:
        dateChooser.showPopup();
    }//GEN-LAST:event_btn_calenderActionPerformed

    private void btn_calenderPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btn_calenderPropertyChange

    }//GEN-LAST:event_btn_calenderPropertyChange

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        // TODO add your handling code here:
        page_main.removeAll();
        page_main.add(daftar_barang);
        page_main.repaint();
        page_main.revalidate();
        
        String idSewa = ID_transaksi.getText(); // atau ambil dari form sebelumnya
        loadBarangKembali(idSewa);
    }//GEN-LAST:event_btn_nextActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed

    String idPengembalian = generateID("PM", "pengembalian", "id_kembali");
    String idSewa = ID_transaksi.getText();
    String tanggalKembali = tgl_kembali.getText();
    String status = txt_status.getText();
    int dendaKeterlambatan = Integer.parseInt(denda_terlambat.getText());
    int totalDenda = Integer.parseInt(total_denda.getText());

    try {
        // Simpan ke tabel pengembalian
        String sqlPengembalian = "INSERT INTO pengembalian (id_kembali, id_sewa, tgl_kembali, status, denda_keterlambatan, total_denda) VALUES (?, ?, ?, ?, ?, ?)";
        pst = con.prepareStatement(sqlPengembalian);
        pst.setString(1, idPengembalian);
        pst.setString(2, idSewa);
        pst.setString(3, tanggalKembali);
        pst.setString(4, status);
        pst.setInt(5, dendaKeterlambatan);
        pst.setInt(6, totalDenda);
        pst.executeUpdate();

        // Simpan detail pengembalian
        for (int i = 0; i < table_barang_kembali.getRowCount(); i++) {
            String idBarang = table_barang_kembali.getValueAt(i, 0).toString();
            int jumlahKembali = Integer.parseInt(table_barang_kembali.getValueAt(i, 3).toString());
            String kondisi = table_barang_kembali.getValueAt(i, 4).toString();

            int dendaBarang = 0;

            if (kondisi.equalsIgnoreCase("Rusak") || kondisi.equalsIgnoreCase("Hilang")) {
                String queryHarga = "SELECT harga_beli FROM barang WHERE id_barang = ?";
                PreparedStatement pstHarga = con.prepareStatement(queryHarga);
                pstHarga.setString(1, idBarang);
                ResultSet rsHarga = pstHarga.executeQuery();

                if (rsHarga.next()) {
                    int hargaBeli = rsHarga.getInt("harga_beli");
                    dendaBarang = hargaBeli * jumlahKembali;
                }

                rsHarga.close();
                pstHarga.close();
            }

            // Insert ke detail_pengembalian
            String idDetail = generateID("DTP", "detail_pengembalian", "id_detail_kembali");
            String sqlDetail = "INSERT INTO detail_pengembalian (id_detail_kembali, id_pengembalian, id_barang, jumlah, kondisi, denda_barang) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstDetail = con.prepareStatement(sqlDetail);
            pstDetail.setString(1, idDetail);
            pstDetail.setString(2, idPengembalian);
            pstDetail.setString(3, idBarang);
            pstDetail.setInt(4, jumlahKembali);
            pstDetail.setString(5, kondisi);
            pstDetail.setInt(6, dendaBarang);
            pstDetail.executeUpdate();
        }

        // Update status penyewaan
        String sqlUpdate = "UPDATE penyewaan SET status = 'Sudah Kembali' WHERE id_sewa = ?";
        PreparedStatement pstUpdate = con.prepareStatement(sqlUpdate);
        pstUpdate.setString(1, idSewa);
        pstUpdate.executeUpdate();

        // Sukses
        int pilihan = JOptionPane.showConfirmDialog(
            null,
            "Pengembalian berhasil disimpan!\nTotal Denda: Rp " + totalDenda +
            "\n\nApakah Anda ingin mencetak nota pengembalian sekarang?",
            "Pengembalian Berhasil",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );

        if (pilihan == JOptionPane.YES_OPTION) {
            // Panggil fungsi cetak nota
            cetakNota(idPengembalian);
        }

        // Refresh halaman
        page_main.removeAll();
        page_main.add(page_pengembalian);
        page_main.repaint();
        page_main.revalidate();
        tampilDataPenyewaan();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Gagal menyimpan pengembalian:\n" + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    }//GEN-LAST:event_btn_simpanActionPerformed

    private void denda_kerusakanFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_denda_kerusakanFocusGained
      
    }//GEN-LAST:event_denda_kerusakanFocusGained

    private void denda_kerusakanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_denda_kerusakanFocusLost
       
    }//GEN-LAST:event_denda_kerusakanFocusLost

    private void total_dendaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_total_dendaFocusGained
       
    }//GEN-LAST:event_total_dendaFocusGained

    private void total_dendaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_total_dendaFocusLost
        
    }//GEN-LAST:event_total_dendaFocusLost

    private void txt_nama_penyewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nama_penyewaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nama_penyewaActionPerformed

    private void btn_search1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_search1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_search1ActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        // TODO add your handling code here:
        page_main.removeAll();
        page_main.add(page_pengembalian);
        page_main.repaint();
        page_main.revalidate();

    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_back1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_back1ActionPerformed
        // TODO add your handling code here:
        page_main.removeAll();
        page_main.add(page_tambah);
        page_main.repaint();
        page_main.revalidate();
    }//GEN-LAST:event_btn_back1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ID_transaksi;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_back1;
    private javax.swing.JButton btn_calender;
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_retur;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_search1;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JPanel daftar_barang;
    private com.raven.datechooser.DateChooser dateChooser;
    private javax.swing.JTextField denda_kerusakan;
    private javax.swing.JTextField denda_terlambat;
    private javax.swing.JPanel form_table_tambah;
    private javax.swing.JPanel form_tambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel label_username;
    private javax.swing.JPanel page_main;
    private javax.swing.JPanel page_pengembalian;
    private javax.swing.JPanel page_tambah;
    private custom.JTable_custom table_barang_kembali;
    private custom.JTable_custom table_kembali;
    private javax.swing.JTextField tgl_kembali;
    private javax.swing.JTextField total_denda;
    private javax.swing.JTextField txt_nama_penyewa;
    private javax.swing.JTextField txt_search;
    private javax.swing.JTextField txt_search1;
    private javax.swing.JTextField txt_status;
    // End of variables declaration//GEN-END:variables
}
