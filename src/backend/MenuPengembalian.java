
package backend;

import backend.Login;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.print.*;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;

public class MenuPengembalian extends javax.swing.JPanel {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    public MenuPengembalian() {
        initComponents();
        label_username.setText(Login.Session.getUsername());
        label_username2.setText(Login.Session.getUsername());
        label_username3.setText(Login.Session.getUsername());
        label_username4.setText(Login.Session.getUsername());
        tampilDataPenyewaan(); 
        
    }

    public void tampilDataPenyewaan() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Sewa");
        model.addColumn("Nama Penyewa");
        model.addColumn("Tgl Sewa");
        model.addColumn("Tgl Rencana Kembali");
        model.addColumn("Jaminan");

        try {
            String sql = "SELECT p.id_sewa, pl.nama_pelanggan, p.tgl_sewa, p.tgl_rencana_kembali, p.jaminan "
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
                    rs.getDate("tgl_rencana_kembali"),
                    rs.getString("jaminan")
                });
            }

            
            table_kembali.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data: " + e.getMessage());
        }

    }
    
  
    private List<String> listIdKembali = new ArrayList<>();
   
    public void tampilDataRiwayatPengembalian() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Pengembalian");
    model.addColumn("Tgl Pengembalian");
    model.addColumn("Status");
    model.addColumn("Denda Keterlambatan");
    model.addColumn("Total Denda");

    
    listIdKembali.clear(); // clear list dulu setiap refresh

    try {
        String sql = "SELECT p.id_kembali, p.id_sewa, p.tgl_kembali, p.status, p.denda_keterlambatan, p.total_denda " +
                     "FROM pengembalian p";

        Koneksi.config();
        con = Koneksi.getConnection();
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();

        while (rs.next()) {
            listIdKembali.add(rs.getString("id_kembali")); // simpan id_kembali
            model.addRow(new Object[]{
                rs.getString("id_kembali"),
                rs.getDate("tgl_kembali"),
                rs.getString("status"),
                rs.getString("denda_keterlambatan"),
                rs.getString("total_denda")
            });
        }

        table_riwayat.setModel(model);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal menampilkan data: " + e.getMessage());
    }
}
    
    public void loadBarangKembali(String idSewa) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Kolom Jumlah Kembali dan Kondisi bisa diedit
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
                String idBarang = rs.getString("id_barang");
                String namaBarang = rs.getString("nama_barang");
                int qty = rs.getInt("qty");

               
                for (int i = 0; i < qty; i++) {
                    model.addRow(new Object[]{idBarang, namaBarang, 1, 1, "Baik"});
                }
            }

            table_barang_kembali.setModel(model);

            String[] kondisiEnum = {"Baik", "Rusak", "Hilang"};
            JComboBox<String> comboKondisi = new JComboBox<>(kondisiEnum);
            table_barang_kembali.getColumnModel().getColumn(4)
                .setCellEditor(new DefaultCellEditor(comboKondisi));

            model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();

                if (col == 4) {
                    String kondisi = (String) model.getValueAt(row, 4);

                    if ("Hilang".equals(kondisi)) {
                        model.setValueAt(0, row, 3);  
                    } else {
                        if (model.getValueAt(row, 3) == null || model.getValueAt(row, 3).toString().isEmpty() || 
                            model.getValueAt(row, 3).toString().equals("0")) {
                            model.setValueAt("", row, 3);  
                        }
                    }
                }

                try {
                    int dendaKeterlambatan = Integer.parseInt(denda_terlambat.getText());
                    hitungDendaBarangKembali(dendaKeterlambatan);
                } catch (NumberFormatException ex) {
                    total_denda.setText("0");
                }
            }
        });
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat barang: " + e.getMessage());
        }
    }

    public int parseRupiah(String rpText) {
        try {
            String clean = rpText.replace("Rp", "")
                                 .trim()
                                 .split(",")[0]
                                 .replace(".", "");
            return Integer.parseInt(clean);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void tampilkanPreviewStruk(String isiStruk, String ucapan) {
        JFrame previewFrame = new JFrame("Preview Struk Pengembalian");
        previewFrame.setSize(300, 500);
        previewFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                try {
                    BufferedImage logo = ImageIO.read(getClass().getResource("/assets/logo (2).png"));
                    g2d.drawImage(logo, 90, 10, 100, 100, null);
                } catch (IOException e) {
                    g2d.drawString("Logo tidak ditemukan", 10, 20);
                }

                g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
                g2d.setColor(Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();

                int y = 130;
                String[] headerLines = {
                    "Jl. Gajah Mada Gg. Buntu No. 2",
                    "(Barat Bank Danamon) Jember - Jawa Timur",
                    "WA Only (No Call/SMS) 0821 3191 2829",
                    "IG : brobet_jbr | Kode Pos. 68131",
                    ""
                };
                int panelWidth = getWidth();

                for (String line : headerLines) {
                    int textWidth = fm.stringWidth(line);
                    int x = (panelWidth - textWidth) / 2;
                    g2d.drawString(line, x, y);
                    y += fm.getHeight();
                }

                 // Gambar isi struk (kiri), kecuali baris ucapan
                    for (String line : isiStruk.split("\n")) {
                        if (line.trim().equalsIgnoreCase(ucapan.trim())) {
                            // Ucapan rata tengah
                            int textWidth = fm.stringWidth(line);
                            int x = (panelWidth - textWidth) / 2;
                            g2d.drawString(line, x, y);
                        } else {
                            g2d.drawString(line, 10, y);
                        }
                        y += fm.getHeight();
                    }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(280, 600);
            }
        };

        JScrollPane scrollPane = new JScrollPane(panel);
        previewFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel tombolPanel = new JPanel();
        JButton btnCetak = new JButton("Cetak");
        btnCetak.addActionListener(e -> {
            previewFrame.dispose();
            cetakStruk(isiStruk,ucapan);
        });
        tombolPanel.add(btnCetak);
        previewFrame.add(tombolPanel, BorderLayout.SOUTH);

        previewFrame.setVisible(true);
    }
     
    private void cetakStruk(String isiStruk, String ucapan) {
    try {
        BufferedImage logo = ImageIO.read(getClass().getResource("/assets/logo (2).png"));
        String[] headerLines = {
            "Jl. Gajah Mada Gg. Buntu No. 2",
            "(Barat Bank Danamon)Jember-Jawa Timur",
            "WA Only (No Call/SMS) 0821 3191 2829",
            "IG : brobet_jbr | Kode Pos. 68131",
            ""
        };

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        Paper paper = pf.getPaper();

        // Set ukuran kertas thermal 80mm = 72mm x 297mm (tinggi bebas)
        double width = 72 * 2.83;  // 1mm = 2.83 poin → 203dpi
        double height = 297 * 2.83; // tinggi kertas default A4
        double margin = 5; // kecilin margin

        paper.setSize(width, height);
        paper.setImageableArea(margin, margin, width - 2 * margin, height - 2 * margin);
        pf.setPaper(paper);
        pf.setOrientation(PageFormat.PORTRAIT);

        Printable printable = new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) return NO_SUCH_PAGE;

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
                FontMetrics fm = g2d.getFontMetrics();

                int y = 0;
                int pageWidth = (int) pageFormat.getImageableWidth();

                // Logo (centered)
                int logoWidth = 80, logoHeight = 80;
                int logoX = (pageWidth - logoWidth) / 2;
                g2d.drawImage(logo, logoX, y, logoWidth, logoHeight, null);
                y += logoHeight + 5;

                // Header (centered)
                for (String line : headerLines) {
                    int lineWidth = fm.stringWidth(line);
                    int x = (pageWidth - lineWidth) / 2;
                    g2d.drawString(line, x, y);
                    y += fm.getHeight();
                }

                // Isi struk (wrap jika terlalu panjang)
                for (String line : isiStruk.split("\n")) {
                    y = drawWrappedLine(g2d, line, 0, y, pageWidth, fm);
                }

                // Ucapan (centered)
                y += fm.getHeight();
                int ucapanWidth = fm.stringWidth(ucapan);
                int xUcapan = (pageWidth - ucapanWidth) / 2;
                g2d.drawString(ucapan, xUcapan, y);

                return PAGE_EXISTS;
            }

            // Fungsi bantu untuk potong baris panjang
            private int drawWrappedLine(Graphics2D g2d, String text, int x, int y, int maxWidth, FontMetrics fm) {
                String[] words = text.split(" ");
                StringBuilder lineBuilder = new StringBuilder();

                for (String word : words) {
                    if (fm.stringWidth(lineBuilder + word + " ") > maxWidth) {
                        g2d.drawString(lineBuilder.toString(), x, y);
                        y += fm.getHeight();
                        lineBuilder = new StringBuilder();
                    }
                    lineBuilder.append(word).append(" ");
                }
                if (!lineBuilder.toString().isEmpty()) {
                    g2d.drawString(lineBuilder.toString(), x, y);
                    y += fm.getHeight();
                }

                return y;
            }
        };

        job.setPrintable(printable, pf);

        if (job.printDialog()) {
            job.print();
            JOptionPane.showMessageDialog(null, "Nota berhasil dicetak!");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal mencetak: " + e.getMessage());
    }
}

    public void tampilkanPreviewStrukPengembalian(String idKembali) {
        try {
            // Ambil data pengembalian dan pelanggan
            String sqlPengembalian = "SELECT p.id_kembali, p.id_sewa, p.tgl_kembali, p.status, p.denda_keterlambatan, p.total_denda, " +
                                     "pg.nama_lengkap, pg.no_hp, s.tgl_sewa, s.jaminan " +
                                     "FROM pengembalian p " +
                                     "JOIN penyewaan s ON p.id_sewa = s.id_sewa " +
                                     "JOIN pengguna pg ON s.id_pengguna = pg.id_pengguna " +
                                     "WHERE p.id_kembali = ?";
            PreparedStatement psKembali = con.prepareStatement(sqlPengembalian);
            psKembali.setString(1, idKembali);
            ResultSet rsKembali = psKembali.executeQuery();

            if (!rsKembali.next()) {
                JOptionPane.showMessageDialog(this, "Data pengembalian tidak ditemukan.");
                return;
            }

            String idSewa = rsKembali.getString("id_sewa");
            String nama = rsKembali.getString("nama_lengkap");
            String noHp = rsKembali.getString("no_hp");
            String tglPinjam = rsKembali.getString("tgl_sewa");
            String tglKembali = rsKembali.getString("tgl_kembali");
            String status = rsKembali.getString("status");
            String jaminan = rsKembali.getString("jaminan");

            // Ambil detail barang
            String sqlDetail = "SELECT b.nama_barang, dp.kondisi, SUM(dp.jumlah) AS jumlah, SUM(dp.denda_barang) AS denda " +
                               "FROM detail_pengembalian dp " +
                               "JOIN barang b ON dp.id_barang = b.id_barang " +
                               "WHERE dp.id_kembali = ? " +
                               "GROUP BY b.nama_barang, dp.kondisi";
            PreparedStatement psDetail = con.prepareStatement(sqlDetail);
            psDetail.setString(1, idKembali);
            ResultSet rsDetail = psDetail.executeQuery();

            int totalDenda = 0;
            StringBuilder isiStruk = new StringBuilder();
            isiStruk.append("BARANG KEMBALI:\n");
            isiStruk.append(String.format("%-13s %3s %-4s %9s\n", "Nama", "Qty", "Kondisi", "Denda"));
            isiStruk.append("===========================================\n");

            while (rsDetail.next()) {
                String namaBarang = rsDetail.getString("nama_barang");
                int qty = rsDetail.getInt("jumlah");
                String kondisi = rsDetail.getString("kondisi");
                int denda = rsDetail.getInt("denda");
                totalDenda += denda;

                String namaPendek = namaBarang.length() > 13 ? namaBarang.substring(0, 13) : namaBarang;

                isiStruk.append(String.format("%-13s %3d %-6s %10s\n",
                    namaPendek,
                    qty,
                    kondisi.length() > 6 ? kondisi.substring(0, 6) : kondisi,
                    String.format("Rp %,d", denda)
                ));
            }

            // Ambil info pembayaran
            String sqlBayar = "SELECT bayar, kembalian FROM pengembalian WHERE id_kembali = ?";
            PreparedStatement psBayar = con.prepareStatement(sqlBayar);
            psBayar.setString(1, idKembali);
            ResultSet rsBayar = psBayar.executeQuery();

            int bayar = 0, kembalian = 0;
            if (rsBayar.next()) {
                bayar = rsBayar.getInt("bayar");
                kembalian = rsBayar.getInt("kembalian");
            }

            // Informasi pelanggan
            StringBuilder infoPelanggan = new StringBuilder();
            infoPelanggan.append("===========================================\n");
            infoPelanggan.append("ID Pengembalian : ").append(idKembali).append("\n");
            infoPelanggan.append("ID Penyewaan    : ").append(idSewa).append("\n");
            infoPelanggan.append("Nama            : ").append(nama).append("\n");
            infoPelanggan.append("No HP           : ").append(noHp).append("\n");
            infoPelanggan.append("Tgl Pinjam      : ").append(tglPinjam).append("\n");
            infoPelanggan.append("Tgl Kembali     : ").append(tglKembali).append("\n");
            infoPelanggan.append("Status          : ").append(status).append("\n");
            infoPelanggan.append("Jaminan         : ").append(jaminan).append("\n");
            infoPelanggan.append("-------------------------------------------\n");

            // Tambahkan total, bayar, kembalian, kasir
            isiStruk.append("-------------------------------------------\n");
            isiStruk.append(String.format("%-15s : Rp %,5d\n", "Total Denda", totalDenda));
            isiStruk.append(String.format("%-15s : Rp %,5d\n", "Bayar", bayar));
            isiStruk.append(String.format("%-15s : Rp %,5d\n", "Kembalian", kembalian));
            isiStruk.append("Kasir           : ").append(Login.Session.getUsername()).append("\n");
            isiStruk.append("===========================================\n\n");

            // Ucapan
            String ucapan = "TERIMA KASIH TELAH MENGEMBALIKAN!";

            // Gabung semua
            StringBuilder previewStruk = new StringBuilder();
            previewStruk.append(infoPelanggan);
            previewStruk.append(isiStruk);
            previewStruk.append(ucapan).append("\n");

            // Tampilkan preview struk
            tampilkanPreviewStruk(previewStruk.toString(), ucapan);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menampilkan nota pengembalian: " + e.getMessage());
        }
    }


   
    private void CekDanHitungKembalian() {
        try {
            String bayarText = txt_bayar.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");
            String totalText = total_denda.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");

            if (bayarText.isEmpty() || totalText.isEmpty() || bayarText.equals("0")) {
                txt_kembalian.setText("Rp 0");
                return;
            }

            int bayar = Integer.parseInt(bayarText);
            int totalHarga = Integer.parseInt(totalText);

            if (bayar < totalHarga) {
                txt_kembalian.setText("Rp 0"); 
                return;
            }

            Kembalian();
        } catch (NumberFormatException e) {
            txt_kembalian.setText("Rp 0");
        }
    }
    
    private void Kembalian() {
         try {
            String totalText = total_denda.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");
            String bayarText = txt_bayar.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");

            if (totalText.isEmpty() || bayarText.isEmpty()) {
                txt_kembalian.setText("Rp 0");
                return;
            }

            int totalHarga = Integer.parseInt(totalText);
            int bayar = Integer.parseInt(bayarText);

            int kembalian = bayar - totalHarga;

            if (kembalian < 0) {
                JOptionPane.showMessageDialog(null, "Jumlah bayar kurang!");
                txt_kembalian.setText("Rp 0");
                return;
            }

            txt_kembalian.setText("Rp " + String.format("%,d", kembalian));
        } catch (NumberFormatException e) {
            txt_kembalian.setText("Rp 0");
            JOptionPane.showMessageDialog(null, "Tolong masukkan angka yang valid.");
        }
    }



    
    
    public void hitungDendaBarangKembali(int dendaKeterlambatan) {
        int totalDendaKerusakan = 0;
        DefaultTableModel model = (DefaultTableModel) table_barang_kembali.getModel();

        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                String idBarang = model.getValueAt(i, 0).toString();
                String kondisi = model.getValueAt(i, 4).toString();

                // Ambil harga beli
                String sql = "SELECT harga_beli FROM barang WHERE id_barang = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, idBarang);
                rs = pst.executeQuery();

                if (rs.next()) {
                    int hargaBeli = rs.getInt("harga_beli");

                    if (kondisi.equalsIgnoreCase("Hilang")) {
                        // Jika hilang, denda = harga beli * 1 (karena hilang 1 barang)
                        totalDendaKerusakan += hargaBeli;
                    } else if (kondisi.equalsIgnoreCase("Rusak")) {
                        // Jika rusak, denda = harga beli * jumlah kembali
                        int jumlahKembali = Integer.parseInt(model.getValueAt(i, 3).toString());
                        totalDendaKerusakan += hargaBeli * jumlahKembali;
                    }
                }

                rs.close();
                pst.close();
            }

            int totalDenda = dendaKeterlambatan + totalDendaKerusakan;
            total_denda.setText("Rp " + String.format("%,.0f", (double) totalDenda));
            denda_kerusakan.setText("Rp " + String.format("%,.0f", (double) totalDendaKerusakan));


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
        btn_riwayat = new javax.swing.JButton();
        btn_retur = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        label_username = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btn_search = new javax.swing.JButton();
        txt_search = new javax.swing.JTextField();
        btn_search1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_kembali = new custom.JTable_customAutoresize();
        page_tambah = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        form_tambah = new javax.swing.JPanel();
        txt_jaminan = new javax.swing.JTextField();
        ID_transaksi = new javax.swing.JTextField();
        txt_nama_penyewa = new javax.swing.JTextField();
        tgl_kembali = new javax.swing.JTextField();
        txt_status = new javax.swing.JTextField();
        denda_terlambat = new javax.swing.JTextField();
        btn_calender = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        btn_next = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        label_username2 = new javax.swing.JLabel();
        btn_back = new javax.swing.JButton();
        daftar_barang = new javax.swing.JPanel();
        form_table_tambah = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_barang_kembali = new custom.JTable_customAutoresize();
        total_denda = new javax.swing.JTextField();
        denda_kerusakan = new javax.swing.JTextField();
        txt_kembalian = new javax.swing.JTextField();
        txt_bayar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        label_username3 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        btn_back1 = new javax.swing.JButton();
        btn_simpan = new javax.swing.JButton();
        riwayat_pengembalian = new javax.swing.JPanel();
        btn_nota = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        btn_search2 = new javax.swing.JButton();
        btn_detail = new javax.swing.JButton();
        txt_search1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        label_username4 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_riwayat = new custom.JTable_customAutoresize();
        btn_back2 = new javax.swing.JButton();

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
        btn_riwayat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Riwayat.png"))); // NOI18N
        btn_riwayat.setBorder(null);
        btn_riwayat.setContentAreaFilled(false);
        btn_riwayat.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Riwayat Select.png"))); // NOI18N
        btn_riwayat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_riwayatActionPerformed(evt);
            }
        });
        page_pengembalian.add(btn_riwayat, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 130, -1, 40));

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
        txt_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchActionPerformed(evt);
            }
        });
        page_pengembalian.add(txt_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 141, 300, 20));

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

        table_kembali.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(table_kembali);

        page_pengembalian.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 740, 420));

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

        txt_jaminan.setBackground(new java.awt.Color(255, 244, 232));
        txt_jaminan.setBorder(null);
        form_tambah.add(txt_jaminan, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 404, 430, 30));

        ID_transaksi.setBackground(new java.awt.Color(255, 244, 232));
        ID_transaksi.setBorder(null);
        form_tambah.add(ID_transaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 430, 30));

        txt_nama_penyewa.setBackground(new java.awt.Color(255, 244, 232));
        txt_nama_penyewa.setBorder(null);
        form_tambah.add(txt_nama_penyewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 142, 430, 30));

        tgl_kembali.setBackground(new java.awt.Color(255, 244, 232));
        tgl_kembali.setBorder(null);
        form_tambah.add(tgl_kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 206, 390, 30));

        txt_status.setBackground(new java.awt.Color(255, 244, 232));
        txt_status.setBorder(null);
        form_tambah.add(txt_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 274, 440, 30));

        denda_terlambat.setBackground(new java.awt.Color(255, 244, 232));
        denda_terlambat.setBorder(null);
        form_tambah.add(denda_terlambat, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 430, 30));

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
        form_tambah.add(btn_calender, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 200, 40, 40));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Form Pengembalian Revisi.png"))); // NOI18N
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

        label_username2.setText("Username");
        page_tambah.add(label_username2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

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

        table_barang_kembali.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(table_barang_kembali);

        form_table_tambah.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 560, 320));

        total_denda.setBackground(new java.awt.Color(255, 244, 232));
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
        form_table_tambah.add(total_denda, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 450, 200, 30));

        denda_kerusakan.setBackground(new java.awt.Color(255, 244, 232));
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
        form_table_tambah.add(denda_kerusakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 389, 200, 30));

        txt_kembalian.setBackground(new java.awt.Color(255, 244, 232));
        txt_kembalian.setBorder(null);
        form_table_tambah.add(txt_kembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 450, 210, 30));

        txt_bayar.setBackground(new java.awt.Color(255, 244, 232));
        txt_bayar.setBorder(null);
        txt_bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_bayarKeyReleased(evt);
            }
        });
        form_table_tambah.add(txt_bayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 387, 210, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Form Tambah Barang RevisiSewa.png"))); // NOI18N
        form_table_tambah.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 620, 520));

        daftar_barang.add(form_table_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 630, -1));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Daftar Barang Kembali.png"))); // NOI18N
        daftar_barang.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 460, 37));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        daftar_barang.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        label_username3.setText("Username");
        daftar_barang.add(label_username3, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        daftar_barang.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

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
        daftar_barang.add(btn_back1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 610, 100, 40));

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
        daftar_barang.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 610, 110, 40));

        page_main.add(daftar_barang, "card4");

        riwayat_pengembalian.setBackground(new java.awt.Color(255, 244, 232));
        riwayat_pengembalian.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_nota.setContentAreaFilled(false);

        btn_nota.setBorderPainted(false);
        btn_nota.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Nota.png"))); // NOI18N
        btn_nota.setBorder(null);
        btn_nota.setContentAreaFilled(false);
        btn_nota.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Nota Select.png"))); // NOI18N
        btn_nota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_notaActionPerformed(evt);
            }
        });
        riwayat_pengembalian.add(btn_nota, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 130, -1, 40));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Riwayat Pengembalian.png"))); // NOI18N
        riwayat_pengembalian.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 470, 37));

        btn_search.setContentAreaFilled(false);

        btn_search.setBorderPainted(false);
        btn_search2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Search.png"))); // NOI18N
        btn_search2.setBorder(null);
        btn_search2.setContentAreaFilled(false);
        btn_search2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Search Select.png"))); // NOI18N
        btn_search2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_search2ActionPerformed(evt);
            }
        });
        riwayat_pengembalian.add(btn_search2, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 133, 50, 40));

        btn_detail.setContentAreaFilled(false);

        btn_detail.setBorderPainted(false);
        btn_detail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Detail.png"))); // NOI18N
        btn_detail.setBorder(null);
        btn_detail.setContentAreaFilled(false);
        btn_detail.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Detail Select.png"))); // NOI18N
        btn_detail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detailActionPerformed(evt);
            }
        });
        riwayat_pengembalian.add(btn_detail, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 130, -1, 40));

        txt_search1.setBackground(new java.awt.Color(238, 236, 227));
        txt_search1.setBorder(null);
        txt_search1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_search1ActionPerformed(evt);
            }
        });
        riwayat_pengembalian.add(txt_search1, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 142, 270, 20));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Group 51.png"))); // NOI18N
        riwayat_pengembalian.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 720, 65));

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        riwayat_pengembalian.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        label_username4.setText("Username");
        riwayat_pengembalian.add(label_username4, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        riwayat_pengembalian.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        table_riwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(table_riwayat);

        riwayat_pengembalian.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 208, 740, 370));

        btn_back.setContentAreaFilled(false);

        btn_back.setBorderPainted(false);
        btn_back2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Kembali.png"))); // NOI18N
        btn_back2.setBorder(null);
        btn_back2.setContentAreaFilled(false);
        btn_back2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/pengembalian/Button Kembali Select.png"))); // NOI18N
        btn_back2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_back2ActionPerformed(evt);
            }
        });
        riwayat_pengembalian.add(btn_back2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 590, 100, 40));

        page_main.add(riwayat_pengembalian, "card5");

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
        String Jaminan = table_kembali.getValueAt(selectedRow, 4).toString();

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
        txt_jaminan.setText(Jaminan);
        
    
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

    private void btn_riwayatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_riwayatActionPerformed
        // TODO add your handling code here:
        page_main.removeAll();
        page_main.add(riwayat_pengembalian);
        page_main.repaint();
        page_main.revalidate();
        tampilDataRiwayatPengembalian();
    }//GEN-LAST:event_btn_riwayatActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed

        String idPengembalian = generateID("PM", "pengembalian", "id_kembali");
        String idSewa = ID_transaksi.getText();
        String tanggalKembali = tgl_kembali.getText();
        String status = txt_status.getText();
        int dendaKeterlambatan = parseRupiah(denda_terlambat.getText());
        int totalDenda = parseRupiah(total_denda.getText());
        String bayarStr = txt_bayar.getText().replace("Rp", "").replace(".", "").replaceAll("\\s+", "");
        String kembalianStr = txt_kembalian.getText().replace("Rp", "").replace(".", "").replaceAll("\\s+", "");
        int bayar = Integer.parseInt(bayarStr);
        int kembalian = Integer.parseInt(kembalianStr);

        try {
            // Simpan ke tabel pengembalian
            String sqlPengembalian = "INSERT INTO pengembalian (id_kembali, id_sewa, tgl_kembali, status, denda_keterlambatan, total_denda, bayar, kembalian) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sqlPengembalian);
            pst.setString(1, idPengembalian);
            pst.setString(2, idSewa);
            pst.setString(3, tanggalKembali);
            pst.setString(4, status);
            pst.setInt(5, dendaKeterlambatan);
            pst.setInt(6, totalDenda);
            pst.setInt(7, bayar);
            pst.setInt(8, kembalian);
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
                String sqlDetail = "INSERT INTO detail_pengembalian (id_detail_kembali, id_kembali, id_barang, jumlah, kondisi, denda_barang) VALUES (?, ?, ?, ?, ?, ?)";
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
               tampilkanPreviewStrukPengembalian(idPengembalian);
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

    private void denda_kerusakanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_denda_kerusakanFocusLost

    }//GEN-LAST:event_denda_kerusakanFocusLost

    private void denda_kerusakanFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_denda_kerusakanFocusGained

    }//GEN-LAST:event_denda_kerusakanFocusGained

    private void total_dendaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_total_dendaFocusLost

    }//GEN-LAST:event_total_dendaFocusLost

    private void total_dendaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_total_dendaFocusGained

    }//GEN-LAST:event_total_dendaFocusGained

    private void btn_back1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_back1ActionPerformed
        // TODO add your handling code here:
        page_main.removeAll();
        page_main.add(page_tambah);
        page_main.repaint();
        page_main.revalidate();
    }//GEN-LAST:event_btn_back1ActionPerformed

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

    private void btn_search2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_search2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_search2ActionPerformed

    private void txt_search1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_search1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_search1ActionPerformed

    private void btn_notaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_notaActionPerformed

    int selectedRow = table_riwayat.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data pengembalian yang ingin dicetak.");
        return;
    }

    try {
        String idKembali = listIdKembali.get(selectedRow);

        // Ambil data pengembalian dan pelanggan
        String sqlPengembalian = "SELECT p.id_kembali, p.id_sewa, p.tgl_kembali, p.status, p.denda_keterlambatan, p.total_denda, " +
                                 "pg.nama_lengkap, pg.no_hp, s.tgl_sewa, s.jaminan " +
                                 "FROM pengembalian p " +
                                 "JOIN penyewaan s ON p.id_sewa = s.id_sewa " +
                                 "JOIN pengguna pg ON s.id_pengguna = pg.id_pengguna " +
                                 "WHERE p.id_kembali = ?";
        PreparedStatement psKembali = con.prepareStatement(sqlPengembalian);
        psKembali.setString(1, idKembali);
        ResultSet rsKembali = psKembali.executeQuery();

        if (!rsKembali.next()) {
            JOptionPane.showMessageDialog(this, "Data pengembalian tidak ditemukan.");
            return;
        }

        String idSewa = rsKembali.getString("id_sewa");
        String nama = rsKembali.getString("nama_lengkap");
        String noHp = rsKembali.getString("no_hp");
        String tglPinjam = rsKembali.getString("tgl_sewa");
        String tglKembali = rsKembali.getString("tgl_kembali");
        String status = rsKembali.getString("status");
        String jaminan = rsKembali.getString("jaminan");

        // Ambil detail barang
        String sqlDetail = "SELECT b.nama_barang, dp.kondisi, SUM(dp.jumlah) AS jumlah, SUM(dp.denda_barang) AS denda " +
                   "FROM detail_pengembalian dp " +
                   "JOIN barang b ON dp.id_barang = b.id_barang " +
                   "WHERE dp.id_kembali = ? " +
                   "GROUP BY b.nama_barang, dp.kondisi";

        PreparedStatement psDetail = con.prepareStatement(sqlDetail);
        psDetail.setString(1, idKembali);
        ResultSet rsDetail = psDetail.executeQuery();

        int totalDenda = 0;
        StringBuilder isiStruk = new StringBuilder();
        isiStruk.append("BARANG KEMBALI:\n");
        isiStruk.append(String.format("%-13s %3s %-4s %9s\n", "Nama", "Jumlah", "Kondisi", "Denda"));
        isiStruk.append("===========================================\n");

        while (rsDetail.next()) {
        String namaBarang = rsDetail.getString("nama_barang");
        int qty = rsDetail.getInt("jumlah");
        String kondisi = rsDetail.getString("kondisi");
        int denda = rsDetail.getInt("denda");
        totalDenda += denda;

        String namaPendek = namaBarang.length() > 13 ? namaBarang.substring(0, 13) : namaBarang;

        isiStruk.append(String.format("%-13s %3d %-6s %10s\n",
            namaPendek,
            qty,
            kondisi.length() > 6 ? kondisi.substring(0, 6) : kondisi,
            String.format("Rp %,d", denda)
        ));
    }



        // Ambil info pembayaran
        String sqlBayar = "SELECT bayar, kembalian FROM pengembalian WHERE id_kembali = ?";
        PreparedStatement psBayar = con.prepareStatement(sqlBayar);
        psBayar.setString(1, idKembali);
        ResultSet rsBayar = psBayar.executeQuery();

        int bayar = 0, kembalian = 0;

        if (rsBayar.next()) {
            bayar = rsBayar.getInt("bayar");
            kembalian = rsBayar.getInt("kembalian");
        }


        // Informasi pelanggan
        StringBuilder infoPelanggan = new StringBuilder();
        infoPelanggan.append("===========================================\n");
        infoPelanggan.append("ID Pengembalian : ").append(idKembali).append("\n");
        infoPelanggan.append("ID Penyewaan    : ").append(idSewa).append("\n");
        infoPelanggan.append("Nama            : ").append(nama).append("\n");
        infoPelanggan.append("No HP           : ").append(noHp).append("\n");
        infoPelanggan.append("Tgl Pinjam      : ").append(tglPinjam).append("\n");
        infoPelanggan.append("Tgl Kembali     : ").append(tglKembali).append("\n");
        infoPelanggan.append("Status          : ").append(status).append("\n");
        infoPelanggan.append("Jaminan         : ").append(jaminan).append("\n");
        infoPelanggan.append("--------------------------------------------\n");

        // Tambahkan total, bayar, kembalian, kasir

        isiStruk.append("-------------------------------------------\n");
        isiStruk.append(String.format("%-15s : Rp %,5d\n", "Total Denda", totalDenda));
        isiStruk.append(String.format("%-15s : Rp %,5d\n", "Bayar", bayar));
        isiStruk.append(String.format("%-15s : Rp %,5d\n", "Kembalian", kembalian));
        isiStruk.append("Kasir           : ").append(Login.Session.getUsername()).append("\n");
        isiStruk.append("===========================================\n");
        isiStruk.append("\n");
        
        // Ucapan
        String ucapan = "TERIMA KASIH TELAH MENGEMBALIKAN!";

        // Gabung semua
        StringBuilder previewStruk = new StringBuilder();
        previewStruk.append(infoPelanggan);
        previewStruk.append(isiStruk);
        
        // Tampilkan preview
        tampilkanPreviewStruk(previewStruk.toString(), ucapan);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal mencetak nota pengembalian: " + e.getMessage());
    }

    }//GEN-LAST:event_btn_notaActionPerformed

    private void btn_back2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_back2ActionPerformed
        // TODO add your handling code here:
        page_main.removeAll();
        page_main.add(page_pengembalian);
        page_main.repaint();
        page_main.revalidate();
    }//GEN-LAST:event_btn_back2ActionPerformed

    private void txt_bayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_bayarKeyReleased
        // TODO add your handling code here:
          String input = txt_bayar.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");

        try {
            if (!input.isEmpty()) {
                int angka = Integer.parseInt(input); 
                txt_bayar.setText("Rp " + String.format("%,d", angka));
            } else {
                txt_bayar.setText("Rp 0");
            }
        } catch (NumberFormatException e) {
            txt_bayar.setText("Rp 0");
        }
        
        CekDanHitungKembalian();
    }//GEN-LAST:event_txt_bayarKeyReleased

    private void btn_detailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detailActionPerformed
        int selectedRow = table_riwayat.getSelectedRow();
        if (selectedRow != -1) {
            String idKembali = table_riwayat.getValueAt(selectedRow, 0).toString();

            PanelDetailKembali panel = new PanelDetailKembali(idKembali);

            JDialog dialog = new JDialog();
            dialog.setTitle("Detail Pengembalian");
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.getContentPane().add(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data pengembalian terlebih dahulu.");
        }
    }//GEN-LAST:event_btn_detailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ID_transaksi;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_back1;
    private javax.swing.JButton btn_back2;
    private javax.swing.JButton btn_calender;
    private javax.swing.JButton btn_detail;
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_nota;
    private javax.swing.JButton btn_retur;
    private javax.swing.JButton btn_riwayat;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_search1;
    private javax.swing.JButton btn_search2;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JPanel daftar_barang;
    private com.raven.datechooser.DateChooser dateChooser;
    private javax.swing.JTextField denda_kerusakan;
    private javax.swing.JTextField denda_terlambat;
    private javax.swing.JPanel form_table_tambah;
    private javax.swing.JPanel form_tambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel label_username;
    private javax.swing.JLabel label_username2;
    private javax.swing.JLabel label_username3;
    private javax.swing.JLabel label_username4;
    private javax.swing.JPanel page_main;
    private javax.swing.JPanel page_pengembalian;
    private javax.swing.JPanel page_tambah;
    private javax.swing.JPanel riwayat_pengembalian;
    private custom.JTable_customAutoresize table_barang_kembali;
    private custom.JTable_customAutoresize table_kembali;
    private custom.JTable_customAutoresize table_riwayat;
    private javax.swing.JTextField tgl_kembali;
    private javax.swing.JTextField total_denda;
    private javax.swing.JTextField txt_bayar;
    private javax.swing.JTextField txt_jaminan;
    private javax.swing.JTextField txt_kembalian;
    private javax.swing.JTextField txt_nama_penyewa;
    private javax.swing.JTextField txt_search;
    private javax.swing.JTextField txt_search1;
    private javax.swing.JTextField txt_status;
    // End of variables declaration//GEN-END:variables
}
