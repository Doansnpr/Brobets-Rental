
package backend;

import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

/**
 *
 * @author Khafila Maulidiyah W
 */
public class MenuBarang extends javax.swing.JPanel {

    // Di bagian deklarasi variabel
    private final ButtonGroup statusGroup = new ButtonGroup();
    private String selectedStatus = "Tersedia"; // Default value

    public MenuBarang() {
        initComponents();
        populateStatusComboBox();
        EnumComboBoxLoader.loadEnumFromDatabase(cmb_status);

        // Buat combobox transparan
        cmb_status.setOpaque(false);
        cmb_status.setBackground(new Color(0, 0, 0, 0));
        cmb_status.setForeground(Color.WHITE);
        cmb_status.setBorder(null);

        //cmb_status1 transparan
        cmb_status1.setOpaque(false);
        cmb_status1.setBackground(new Color(0, 0, 0, 0));
        cmb_status1.setForeground(Color.WHITE);
        cmb_status1.setBorder(null);
    
    //buat dropdown transparan
    cmb_status.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(false);
            }
            return c;
        }
    });
    
    //buat dropdown cmb_status1 transparan
    cmb_status1.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(false);
            }
            return c;
        }
    });
    
        try {
            loadDataFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cariData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Harga Sewa");
        model.addColumn("Harga Beli");
        model.addColumn("Kategori");
        model.addColumn("Stok");
        model.addColumn("Status");

        String keyword = txt_search.getText(); // ambil teks dari field pencarian

        try {
            Koneksi koneksi = new Koneksi();
            koneksi.config();
            Connection conn = koneksi.getConnection();

            String query = "SELECT * FROM barang WHERE id_barang LIKE ? OR nama_barang LIKE ? OR harga_sewa LIKE ? OR harga_beli LIKE ? OR kategori LIKE ? OR stok LIKE ? OR status LIKE ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ps.setString(4, "%" + keyword + "%");
            ps.setString(5, "%" + keyword + "%");
            ps.setString(6, "%" + keyword + "%");
            ps.setString(7, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("harga_sewa"),
                    rs.getInt("harga_beli"),
                    rs.getString("kategori"),
                    rs.getInt("Stok"),
                    rs.getString("Status")
                });
            }

            tbl_barang.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencari data: " + e.getMessage());
        }
    }

    public void populateStatusComboBox() {
    try {
        // Koneksi ke database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/brobets", "root", "");

        // Query untuk mengambil status unik dari tabel barang
        String query = "SELECT DISTINCT status FROM barang";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        // Set untuk menyimpan status agar tidak duplikat
        Set<String> statusSet = new HashSet<>();

        // Menghapus item lama
        cmb_status1.removeAllItems();

        // Tambah status dari database
        while (rs.next()) {
            String status = rs.getString("status");
            if (status != null) {
                status = status.trim();
                if (!status.isEmpty() && !statusSet.contains(status)) {
                    statusSet.add(status);
                    cmb_status1.addItem(status);
                }
            }
        }

        // Tambahkan status manual sebagai cadangan
        String[] manualStatuses = { "Rusak", "Hilang", "Maintenance"};
        for (String manualStatus : manualStatuses) {
            if (!statusSet.contains(manualStatus)) {
                statusSet.add(manualStatus);
                cmb_status1.addItem(manualStatus);
            }
        }

        // Tutup koneksi
        rs.close();
        stmt.close();
        con.close();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Kesalahan database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
public class EnumComboBoxLoader {

    // Ganti sesuai koneksi database kamu
    private static final String DB_URL = "jdbc:mysql://localhost:3306/brobets";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void loadEnumFromDatabase(JComboBox<String> comboBox) {
        String query = "SHOW COLUMNS FROM barang LIKE 'status'";  // Query ke kolom status

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                // Ambil tipe data kolom (ENUM)
                String type = rs.getString("Type");  // Misalnya enum('Tersedia', 'Disewa', 'Rusak', 'Hilang', 'Maintance')
                System.out.println("Tipe enum: " + type);  // Debug: Tampilkan tipe enum

                // Ambil nilai-nilai enum dari tipe tersebut
                String enumValues = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
                String[] values = enumValues.replace("'", "").split(",");

                // Kosongkan comboBox dan tambahkan nilai enum
                comboBox.removeAllItems();
                for (String value : values) {
                    String formatted = value.substring(0, 1).toUpperCase() + value.substring(1);  // Capitalize huruf pertama
                    System.out.println("Menambahkan: " + formatted);  // Debug: Tampilkan nilai yang ditambahkan
                    comboBox.addItem(formatted);
                }
            } else {
                System.out.println("Kolom 'status' tidak ditemukan.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal memuat enum: " + e.getMessage());
        }
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

        page_main = new javax.swing.JPanel();
        page_barang = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        btn_search = new javax.swing.JButton();
        txt_search = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_barang = new custom.JTable_custom();
        page_tambah = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        form_tambah = new javax.swing.JPanel();
        txt_kategori = new javax.swing.JTextField();
        txt_nama = new javax.swing.JTextField();
        txt_beli = new javax.swing.JTextField();
        txt_harga = new javax.swing.JTextField();
        txt_stok = new javax.swing.JTextField();
        cmb_status = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        page_ubah = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        btn_update = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        form_ubah = new javax.swing.JPanel();
        txt_kategori1 = new javax.swing.JTextField();
        txt_nama1 = new javax.swing.JTextField();
        txt_beli1 = new javax.swing.JTextField();
        txt_harga1 = new javax.swing.JTextField();
        txt_stok1 = new javax.swing.JTextField();
        cmb_status1 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        page_main.setBackground(new java.awt.Color(255, 244, 232));
        page_main.setLayout(new java.awt.CardLayout());

        page_barang.setBackground(new java.awt.Color(255, 244, 232));
        page_barang.setForeground(new java.awt.Color(230, 230, 230));
        page_barang.setPreferredSize(new java.awt.Dimension(836, 666));
        page_barang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/Daftar Barang.png"))); // NOI18N
        page_barang.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 300, 37));

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_barang.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        btn_search.setContentAreaFilled(false);

        btn_search.setBorderPainted(false);
        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search.png"))); // NOI18N
        btn_search.setBorder(null);
        btn_search.setContentAreaFilled(false);
        btn_search.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search Select.png"))); // NOI18N
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });
        page_barang.add(btn_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 50, 40));

        txt_search.setBackground(new java.awt.Color(238, 236, 227));
        txt_search.setBorder(null);
        page_barang.add(txt_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 181, 290, 20));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Search.png"))); // NOI18N
        page_barang.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 410, -1));

        btn_tambah.setContentAreaFilled(false);

        btn_tambah.setBorderPainted(false);
        btn_tambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/Group 97.png"))); // NOI18N
        btn_tambah.setBorder(null);
        btn_tambah.setContentAreaFilled(false);
        btn_tambah.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/Group 98.png"))); // NOI18N
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });
        page_barang.add(btn_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 210, 50));

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
        page_barang.add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 170, -1, 40));

        btn_ubah.setContentAreaFilled(false);

        btn_ubah.setBorderPainted(false);
        btn_ubah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/Group 93.png"))); // NOI18N
        btn_ubah.setBorder(null);
        btn_ubah.setContentAreaFilled(false);
        btn_ubah.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/Group 95 (1).png"))); // NOI18N
        btn_ubah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ubahActionPerformed(evt);
            }
        });
        page_barang.add(btn_ubah, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 170, -1, 40));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/BG Button.png"))); // NOI18N
        page_barang.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 720, 65));

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_barang.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        jLabel27.setText("Username");
        page_barang.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        tbl_barang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Barang", "Nama Barang", "Harga Sewa", "Harga Beli", "Kategori", "Stok", "Status"
            }
        ));
        jScrollPane1.setViewportView(tbl_barang);

        page_barang.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 730, 360));

        page_main.add(page_barang, "card2");

        page_tambah.setBackground(new java.awt.Color(255, 244, 232));
        page_tambah.setPreferredSize(new java.awt.Dimension(836, 666));
        page_tambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/Tambah Barang.png"))); // NOI18N
        page_tambah.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 330, 37));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_tambah.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        form_tambah.setBackground(new java.awt.Color(255, 244, 232));
        form_tambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_kategori.setBackground(new java.awt.Color(255, 244, 232));
        txt_kategori.setForeground(new java.awt.Color(153, 153, 153));
        txt_kategori.setBorder(null);
        txt_kategori.setText("Kategori"); txt_kategori.setForeground(Color.GRAY);
        txt_kategori.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_kategoriFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_kategoriFocusLost(evt);
            }
        });
        txt_kategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kategoriActionPerformed(evt);
            }
        });
        form_tambah.add(txt_kategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 338, 440, 25));

        txt_nama.setText("Nama Barang"); txt_nama.setForeground(Color.GRAY);
        txt_nama.setBackground(new java.awt.Color(255, 244, 232));
        txt_nama.setForeground(new java.awt.Color(153, 153, 153));
        txt_nama.setBorder(null);
        txt_nama.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_namaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_namaFocusLost(evt);
            }
        });
        form_tambah.add(txt_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 216, 440, 25));

        txt_beli.setText("Harga Beli"); txt_beli.setForeground(Color.GRAY);
        txt_beli.setBackground(new java.awt.Color(255, 244, 232));
        txt_beli.setForeground(new java.awt.Color(153, 153, 153));
        txt_beli.setToolTipText("");
        txt_beli.setBorder(null);
        txt_beli.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_beliFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_beliFocusLost(evt);
            }
        });
        txt_beli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_beliActionPerformed(evt);
            }
        });
        form_tambah.add(txt_beli, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 298, 440, 25));

        txt_harga.setText("Harga Sewa"); txt_harga.setForeground(Color.GRAY);
        txt_harga.setBackground(new java.awt.Color(255, 244, 232));
        txt_harga.setForeground(new java.awt.Color(153, 153, 153));
        txt_harga.setToolTipText("");
        txt_harga.setBorder(null);
        txt_harga.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_hargaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_hargaFocusLost(evt);
            }
        });
        txt_harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_hargaActionPerformed(evt);
            }
        });
        form_tambah.add(txt_harga, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 256, 440, 25));

        txt_stok.setText("Stok"); txt_stok.setForeground(Color.GRAY);
        txt_stok.setBackground(new java.awt.Color(255, 244, 232));
        txt_stok.setForeground(new java.awt.Color(153, 153, 153));
        txt_stok.setToolTipText("");
        txt_stok.setBorder(null);
        txt_stok.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_stokFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_stokFocusLost(evt);
            }
        });
        txt_stok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_stokActionPerformed(evt);
            }
        });
        form_tambah.add(txt_stok, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 378, 440, 25));

        cmb_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmb_status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_statusActionPerformed(evt);
            }
        });
        form_tambah.add(cmb_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 419, 440, 25));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/FORM FIKS.png"))); // NOI18N
        form_tambah.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 560, 490));

        page_tambah.add(form_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 580, 490));

        btn_simpan.setContentAreaFilled(false);

        btn_simpan.setBorderPainted(false);
        btn_simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/Group 96.png"))); // NOI18N
        btn_simpan.setContentAreaFilled(false);
        btn_simpan.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/Group 99.png"))); // NOI18N
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });
        page_tambah.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 580, 100, 40));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_tambah.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        jLabel31.setText("Username");
        page_tambah.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        page_main.add(page_tambah, "card3");

        page_ubah.setBackground(new java.awt.Color(255, 244, 232));
        page_ubah.setPreferredSize(new java.awt.Dimension(836, 666));
        page_ubah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/Tambah Barang.png"))); // NOI18N
        page_ubah.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 330, 37));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_ubah.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        btn_simpan.setContentAreaFilled(false);

        btn_simpan.setBorderPainted(false);
        btn_update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/ubah.png"))); // NOI18N
        btn_update.setBorder(null);
        btn_update.setContentAreaFilled(false);
        btn_update.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/ubah gelap.png"))); // NOI18N
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        page_ubah.add(btn_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 580, 100, 40));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_ubah.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        jLabel35.setText("Username");
        page_ubah.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        form_ubah.setBackground(new java.awt.Color(255, 244, 232));
        form_ubah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_kategori1.setBackground(new java.awt.Color(255, 244, 232));
        txt_kategori1.setForeground(new java.awt.Color(153, 153, 153));
        txt_kategori1.setBorder(null);
        txt_kategori.setText("Kategori"); txt_kategori.setForeground(Color.GRAY);
        txt_kategori1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_kategori1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_kategori1FocusLost(evt);
            }
        });
        txt_kategori1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kategori1ActionPerformed(evt);
            }
        });
        form_ubah.add(txt_kategori1, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 338, 440, 25));

        txt_nama.setText("Nama Barang"); txt_nama.setForeground(Color.GRAY);
        txt_nama1.setBackground(new java.awt.Color(255, 244, 232));
        txt_nama1.setForeground(new java.awt.Color(153, 153, 153));
        txt_nama1.setBorder(null);
        txt_nama1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_nama1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_nama1FocusLost(evt);
            }
        });
        form_ubah.add(txt_nama1, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 216, 440, 25));

        txt_beli.setText("Harga Beli"); txt_beli.setForeground(Color.GRAY);
        txt_beli1.setBackground(new java.awt.Color(255, 244, 232));
        txt_beli1.setForeground(new java.awt.Color(153, 153, 153));
        txt_beli1.setToolTipText("");
        txt_beli1.setBorder(null);
        txt_beli1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_beli1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_beli1FocusLost(evt);
            }
        });
        txt_beli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_beli1ActionPerformed(evt);
            }
        });
        form_ubah.add(txt_beli1, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 298, 440, 25));

        txt_harga.setText("Harga"); txt_harga.setForeground(Color.GRAY);
        txt_harga1.setBackground(new java.awt.Color(255, 244, 232));
        txt_harga1.setForeground(new java.awt.Color(153, 153, 153));
        txt_harga1.setToolTipText("");
        txt_harga1.setBorder(null);
        txt_harga1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_harga1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_harga1FocusLost(evt);
            }
        });
        txt_harga1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_harga1ActionPerformed(evt);
            }
        });
        form_ubah.add(txt_harga1, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 256, 440, 25));

        txt_stok.setText("Stok"); txt_stok.setForeground(Color.GRAY);
        txt_stok1.setBackground(new java.awt.Color(255, 244, 232));
        txt_stok1.setForeground(new java.awt.Color(153, 153, 153));
        txt_stok1.setToolTipText("");
        txt_stok1.setBorder(null);
        txt_stok1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_stok1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_stok1FocusLost(evt);
            }
        });
        txt_stok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_stok1ActionPerformed(evt);
            }
        });
        form_ubah.add(txt_stok1, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 378, 440, 25));

        cmb_status1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmb_status1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_status1ActionPerformed(evt);
            }
        });
        form_ubah.add(cmb_status1, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 419, 440, 25));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/barang/FORM FIKS.png"))); // NOI18N
        form_ubah.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 560, 490));

        page_ubah.add(form_ubah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 580, 490));

        page_main.add(page_ubah, "card3");

        add(page_main, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        page_main.removeAll();
        page_main.add(page_tambah);
        page_main.repaint();
        page_main.revalidate();
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        int selectedRow = tbl_barang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tbl_barang.getModel();
        String idBarang = (String) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/brobets", "root", "");
            String sql = "DELETE FROM barang WHERE id_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, idBarang);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Hapus baris dari tabel
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.", "Sukses", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_ubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ubahActionPerformed
        // TODO add your handling code here:
        String id_barang, nama_barang, kategori, status;
        int stok, harga_sewa, harga_beli;

        int PilihBaris = tbl_barang.getSelectedRow();
        if (PilihBaris == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tbl_barang.getModel();
        String selectedidBarang = (String) model.getValueAt(PilihBaris, 0); // Disimpan, tapi tidak diubah
        nama_barang = (String) model.getValueAt(PilihBaris, 1);
        harga_sewa = (int) model.getValueAt(PilihBaris, 2);
        harga_beli = (int) model.getValueAt(PilihBaris, 3);
        kategori = (String) model.getValueAt(PilihBaris, 4);
        stok = (int) model.getValueAt(PilihBaris, 5);
        status = (String) model.getValueAt(PilihBaris, 6);

        page_main.removeAll();
        page_main.add(page_ubah);
        page_main.repaint();
        page_main.revalidate();
        

        txt_nama1.setText(nama_barang);
        txt_nama1.setForeground(Color.BLACK);
        txt_harga1.setText(String.valueOf(harga_sewa));
        txt_harga1.setForeground(Color.BLACK);
        txt_beli1.setText(String.valueOf(harga_beli));
        txt_beli1.setForeground(Color.BLACK);
        txt_kategori1.setText(kategori);
        txt_kategori1.setForeground(Color.BLACK);
        txt_stok1.setText(String.valueOf(stok));
        txt_stok1.setForeground(Color.BLACK);
    }//GEN-LAST:event_btn_ubahActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        // TODO add your handling code here:
        String nama_barang, kategori, status, save;
        int harga_sewa, stok, harga_beli;

        String id_barang = generateIdOtomatis();
        nama_barang = txt_nama.getText();
        txt_nama.setForeground(Color.BLACK);
        String hargaStr = txt_harga.getText();
        String beliStr = txt_beli.getText();
        kategori = txt_kategori.getText();
        String stokStr = txt_stok.getText();
        status = cmb_status.getSelectedItem().toString().toUpperCase();


        // Validasi kosong
        if (nama_barang.isEmpty() || nama_barang.equals("Masukkan Nama Barang")
                || hargaStr.isEmpty() || hargaStr.equals("Masukkan Harga Sewa")
                || beliStr.isEmpty() || beliStr.equals("Masukkan Harga Beli")
                || kategori.isEmpty() || kategori.equals("Masukkan Kategori")
                || stokStr.isEmpty() || stokStr.equals("Masukkan Stok")
                || cmb_status.getSelectedItem() == null || cmb_status.getSelectedItem().toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validasi angka
        try {
            harga_sewa = Integer.parseInt(hargaStr);
            harga_beli = Integer.parseInt(beliStr);
            stok = Integer.parseInt(stokStr);

            if (harga_sewa <= 0 || harga_beli <= 0 || stok <= 0) {
                JOptionPane.showMessageDialog(this, "Harga, Denda Rusak, Denda Hilang, dan Stok harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            save = "INSERT INTO barang (id_barang, nama_barang, harga_sewa, harga_beli, kategori, stok, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/brobets", "root", "");
            PreparedStatement simpan = con.prepareStatement(save);
            simpan.setString(1, id_barang);
            simpan.setString(2, nama_barang);
            simpan.setInt(3, harga_sewa);
            simpan.setInt(4, harga_beli);
            simpan.setString(5, kategori);
            simpan.setInt(6, stok);
            simpan.setString(7, status);

            int result = simpan.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                page_main.removeAll();
                page_main.add(page_barang);
                page_main.repaint();
                page_main.revalidate();
                clearTambahBarang();
                loadDataFromDatabase();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan barang!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            con.close();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga Sewa, Harga Beli,  dan Stok harus berupa angka valid!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void txt_stokFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_stokFocusGained
        if (txt_stok.getText().equals("Stok")) {
            txt_stok.setText("");
            txt_stok.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txt_stokFocusGained

    private void txt_stokFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_stokFocusLost
        if (txt_stok.getText().isEmpty()) {
            txt_stok.setText("Stok");
            txt_stok.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_txt_stokFocusLost

    private void txt_stokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_stokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_stokActionPerformed

    private void txt_hargaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_hargaFocusGained
        if (txt_harga.getText().equals("Harga Sewa")) {
            txt_harga.setText("");
            txt_harga.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txt_hargaFocusGained

    private void txt_hargaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_hargaFocusLost
        if (txt_harga.getText().isEmpty()) {
            txt_harga.setText("Harga Sewa");
            txt_harga.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_txt_hargaFocusLost

    private void txt_hargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_hargaActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txt_hargaActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        // TODO add your handling code here:
        cariData();
    }//GEN-LAST:event_btn_searchActionPerformed

    private void txt_beliFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_beliFocusGained
        if (txt_beli.getText().equals("Harga Beli")) {
            txt_beli.setText("");
            txt_beli.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txt_beliFocusGained

    private void txt_beliFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_beliFocusLost
        if (txt_beli.getText().isEmpty()) {
            txt_beli.setText("Harga Beli");
            txt_beli.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_txt_beliFocusLost

    private void txt_beliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_beliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_beliActionPerformed
        
    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
     String newNamaBarang = txt_nama1.getText().trim();
        String newKategori = txt_kategori1.getText().trim();
        String newStatus = cmb_status1.getSelectedItem().toString(); // Diambil dari JComboBox

        String hargaStr = txt_harga1.getText().trim();
        String beliStr = txt_beli1.getText().trim();
        String stokStr = txt_stok1.getText().trim();

        // Validasi inputan
        if (newNamaBarang.isEmpty() || hargaStr.isEmpty() || beliStr.isEmpty() || newKategori.isEmpty() || stokStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Konversi harga, beli, stok ke angka
            int newHargaSewa = Integer.parseInt(hargaStr);
            int newBeli = Integer.parseInt(beliStr);
            int newStok = Integer.parseInt(stokStr);

            // Validasi angka yang dimasukkan
            if (newHargaSewa <= 0 || newBeli <= 0 || newStok <= 0) {
                JOptionPane.showMessageDialog(this, "Harga, Denda, dan Stok harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ambil ID barang yang dipilih dari tabel
            int row = tbl_barang.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data barang yang ingin diupdate!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedIdBarang = tbl_barang.getValueAt(row, 0).toString();

            // Koneksi ke database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/brobets", "root", "");

            // Cek apakah nama barang sudah digunakan (menghindari duplikasi)
            String cekQuery = "SELECT * FROM barang WHERE nama_barang = ? AND id_barang != ?";
            PreparedStatement cekStmt = con.prepareStatement(cekQuery);
            cekStmt.setString(1, newNamaBarang);
            cekStmt.setString(2, selectedIdBarang);
            ResultSet rs = cekStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Nama barang sudah digunakan, silakan pilih yang lain", "Error", JOptionPane.ERROR_MESSAGE);
                con.close();
                return;
            }

            // Lakukan update data barang
            String updateQuery = "UPDATE barang SET nama_barang = ?, harga_sewa = ?, harga_beli = ?, kategori = ?, stok = ?, status = ? WHERE id_barang = ?";
            PreparedStatement ubah = con.prepareStatement(updateQuery);
            ubah.setString(1, newNamaBarang);
            ubah.setInt(2, newHargaSewa);
            ubah.setInt(3, newBeli);
            ubah.setString(4, newKategori);
            ubah.setInt(5, newStok);
            ubah.setString(6, newStatus); // Menggunakan status yang dipilih dari JComboBox
            ubah.setString(7, selectedIdBarang);

            int result = ubah.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Data barang berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                  loadDataFromDatabase();
                page_main.removeAll();
                page_main.add(page_barang);
                page_main.repaint();
                page_main.revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui data barang.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            con.close();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga Sewa, Harga Beli, dan Stok harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Kesalahan database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        
    }//GEN-LAST:event_btn_updateActionPerformed



    private void txt_namaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_namaFocusGained
        // TODO add your handling code here:
        if (txt_nama.getText().equals("Nama Barang")) {
            txt_nama.setText("");
            txt_nama.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txt_namaFocusGained

    private void txt_namaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_namaFocusLost
        // TODO add your handling code here:
        if (txt_nama1.getText().isEmpty()) {
            txt_nama1.setForeground(Color.GRAY);
            txt_nama1.setText("Nama Barang");
        }
    }//GEN-LAST:event_txt_namaFocusLost

    private void txt_kategoriFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_kategoriFocusGained
        // TODO add your handling code here:
        if (txt_kategori.getText().equals("Kategori")) {
            txt_kategori.setText("");
            txt_kategori.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txt_kategoriFocusGained

    private void txt_kategoriFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_kategoriFocusLost
        // TODO add your handling code here:
        if (txt_kategori.getText().isEmpty()) {
            txt_kategori.setForeground(Color.GRAY);
            txt_kategori.setText("Kategori");
        }
    }//GEN-LAST:event_txt_kategoriFocusLost

    private void txt_kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kategoriActionPerformed

    private void cmb_statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_statusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_statusActionPerformed

    private void txt_kategori1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_kategori1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kategori1FocusGained

    private void txt_kategori1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_kategori1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kategori1FocusLost

    private void txt_kategori1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kategori1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kategori1ActionPerformed

    private void txt_nama1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_nama1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nama1FocusGained

    private void txt_nama1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_nama1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nama1FocusLost

    private void txt_beli1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_beli1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_beli1FocusGained

    private void txt_beli1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_beli1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_beli1FocusLost

    private void txt_beli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_beli1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_beli1ActionPerformed

    private void txt_harga1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_harga1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_harga1FocusGained

    private void txt_harga1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_harga1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_harga1FocusLost

    private void txt_harga1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_harga1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_harga1ActionPerformed

    private void txt_stok1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_stok1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_stok1FocusGained

    private void txt_stok1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_stok1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_stok1FocusLost

    private void txt_stok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_stok1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_stok1ActionPerformed

    private void cmb_status1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_status1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmb_status1ActionPerformed

    String url = "jdbc:mysql://localhost:3306/brobets";
    String user = "root";
    String DBpassword = "";

    public String generateIdOtomatis() {
        String idBaru = "BRG001";
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/brobets", "root", "");
            String sql = "SELECT id_barang FROM barang ORDER BY id_barang DESC LIMIT 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String lastId = rs.getString("id_barang"); // contoh: BRG007
                int angka = Integer.parseInt(lastId.substring(3)); // ambil 007 → jadi int 7
                angka++;
                idBaru = String.format("BRG%03d", angka); // jadi BRG008
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace(); // biar keliatan errornya
            JOptionPane.showMessageDialog(null, "Gagal generate ID: " + e.getMessage());
        }

        return idBaru;
    }

    private void loadDataFromDatabase() throws java.sql.SQLException {
        String query = "SELECT * FROM barang";
        try (Connection con = DriverManager.getConnection(url, user, DBpassword); java.sql.Statement stmt = con.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(query)) {
            DefaultTableModel model = (DefaultTableModel) tbl_barang.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("harga_sewa"),
                    rs.getInt("harga_beli"),
                    rs.getString("kategori"),
                    rs.getInt("Stok"),
                    rs.getString("Status"),};
                model.addRow(row);
            }
        }
    }

    private void clearTambahBarang() {
        txt_nama.setText("");
        txt_harga.setText("");
        txt_beli.setText("");
        txt_kategori.setText("");
        txt_stok.setText("");
        
        // Jika ada komponen lain seperti combo box atau date picker, tambahkan juga resetnya di sini
        // contoh: combo_kategori.setSelectedIndex(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> cmb_status;
    private javax.swing.JComboBox<String> cmb_status1;
    private javax.swing.JPanel form_tambah;
    private javax.swing.JPanel form_ubah;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel page_barang;
    private javax.swing.JPanel page_main;
    private javax.swing.JPanel page_tambah;
    private javax.swing.JPanel page_ubah;
    private custom.JTable_custom tbl_barang;
    private javax.swing.JTextField txt_beli;
    private javax.swing.JTextField txt_beli1;
    private javax.swing.JTextField txt_harga;
    private javax.swing.JTextField txt_harga1;
    private javax.swing.JTextField txt_kategori;
    private javax.swing.JTextField txt_kategori1;
    private javax.swing.JTextField txt_nama;
    private javax.swing.JTextField txt_nama1;
    private javax.swing.JTextField txt_search;
    private javax.swing.JTextField txt_stok;
    private javax.swing.JTextField txt_stok1;
    // End of variables declaration//GEN-END:variables
}
