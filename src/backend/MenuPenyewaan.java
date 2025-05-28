
package backend;

import backend.Login.Session;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Paper;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class MenuPenyewaan extends javax.swing.JPanel {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
   boolean pelangganLama = false;
    String idPelangganLama = "";
    int poinSekarang = 0;


    
    public MenuPenyewaan() {
        initComponents();
        Koneksi DB = new Koneksi();
        DB.config();
        con = DB.con;
        
        load_table();
        label_username.setText(Login.Session.getUsername());
        label_username2.setText(Login.Session.getUsername());
        label_username3.setText(Login.Session.getUsername());
        
    }
    
    private String generateID(String tableName, String idColumn, String prefix) {
    String newID = prefix + "001";
    try {
        String sql = "SELECT " + idColumn + " FROM " + tableName + " ORDER BY " + idColumn + " DESC LIMIT 1";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();
        if (rs.next()) {
            String lastID = rs.getString(1);
            int num = Integer.parseInt(lastID.substring(prefix.length())) + 1;
            newID = prefix + String.format("%03d", num);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal generate ID: " + e.getMessage());
    }
    return newID;
}
    

    private void load_table() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Sewa");
    model.addColumn("Nama Penyewa");
    model.addColumn("Nama Pegawai");
    model.addColumn("Tanggal Sewa");
    model.addColumn("Tanggal Rencana Kembali");
    model.addColumn("Total Harga");
    model.addColumn("Bayar");
    model.addColumn("Kembalian");
    model.addColumn("Jaminan");
    model.addColumn("Status");

    try {
     String sql = "SELECT penyewaan.*, pengguna.nama_pengguna, pelanggan.nama_pelanggan FROM penyewaan JOIN pelanggan "
                + "ON penyewaan.id_pelanggan = pelanggan.id_pelanggan JOIN pengguna ON penyewaan.id_pengguna = pengguna.id_pengguna ORDER BY penyewaan.id_sewa DESC;";
     pst = con.prepareStatement(sql);
     rs = pst.executeQuery();

     while (rs.next()) {
         model.addRow(new Object[]{
             rs.getString("id_sewa"),             
             rs.getString("nama_pelanggan"),  
             rs.getString("nama_pengguna"),                    
             rs.getString("tgl_sewa"),                     
             rs.getString("tgl_rencana_kembali"),                     
             rs.getString("total_harga"),                     
             rs.getString("bayar"),                       
             rs.getString("kembalian"),                      
             rs.getString("jaminan"),                      
             rs.getString("status"),             
         });
     }

     table_sewa.setModel(model);

    table_sewa.getColumnModel().getColumn(0).setPreferredWidth(80);
    table_sewa.getColumnModel().getColumn(1).setPreferredWidth(120);
    table_sewa.getColumnModel().getColumn(2).setPreferredWidth(120);
    table_sewa.getColumnModel().getColumn(3).setPreferredWidth(100);
    table_sewa.getColumnModel().getColumn(4).setPreferredWidth(170);
    table_sewa.getColumnModel().getColumn(5).setPreferredWidth(100);
    table_sewa.getColumnModel().getColumn(6).setPreferredWidth(80);
    table_sewa.getColumnModel().getColumn(7).setPreferredWidth(90);
    table_sewa.getColumnModel().getColumn(8).setPreferredWidth(90);
    table_sewa.getColumnModel().getColumn(9).setPreferredWidth(100);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

    
    private void load_tableBrg() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Barang");       
        model.addColumn("Nama Barang");
        model.addColumn("Qty");
        model.addColumn("Harga");
        model.addColumn("Subtotal");

        table_barang.setModel(model);      
    }
    
     private void CekDanHitungKembalian() {
        try {
            String bayarText = txt_bayar.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");
            String totalText = txt_total.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");

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
            String totalText = txt_total.getText().replace("Rp ", "").replace(",", "").replaceAll("[^\\d]", "");
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
  
    private boolean isTendaSudahDitambahkan(DefaultTableModel model) {
        for (int i = 0; i < model.getRowCount(); i++) {
            String namaBarang = model.getValueAt(i, 1).toString().toLowerCase();
            double harga = Double.parseDouble(model.getValueAt(i, 3).toString());
            if (namaBarang.contains("tenda") && harga == 0.0) {
                return true;
            }
        }
        return false;
    }
     
    private void hitungTotalHarga() {
        double total = 0.0;
        DefaultTableModel model = (DefaultTableModel) table_barang.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            double subtotal = Double.parseDouble(model.getValueAt(i, 4).toString());
            total += subtotal;
        }
        txt_total.setText("Rp " + String.format("%,.0f", total));
    }

    public void cekPelanggan() {
    String nama = nama_penyewa.getText().trim();
    if (nama.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Isi nama pelanggan terlebih dahulu.");
        pelangganLama = false;
        return;
    }

    try {
        String query = "SELECT * FROM pelanggan WHERE nama_pelanggan = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, nama);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String status = rs.getString("status");

            if ("nonaktif".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(this, "Pelanggan ditemukan, namun statusnya NONAKTIF.\nAktifkan akun terlebih dahulu untuk melakukan sewa.");
                pelangganLama = false;
                return;
            }

            no_hp.setText(rs.getString("no_hp"));
            pelangganLama = true;
            idPelangganLama = rs.getString("id_pelanggan");
            poinSekarang = rs.getInt("poin");

            JOptionPane.showMessageDialog(this, "Pelanggan lama ditemukan dan aktif.\nPoin saat ini: " + poinSekarang);
        } else {
            pelangganLama = false;
            idPelangganLama = "";
            poinSekarang = 0;
            no_hp.setText("");
            JOptionPane.showMessageDialog(this, "Pelanggan baru ditemukan.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saat cek pelanggan: " + e.getMessage());
        pelangganLama = false;
    }
}

    private void hapusData() {
        int selectedRow = table_sewa.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Silakan pilih data yang ingin dihapus terlebih dahulu.",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus data ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String idSewa = table_sewa.getValueAt(selectedRow, 0).toString();

            try {
                con = Koneksi.getConnection(); 

                String sqlDetail = "DELETE FROM detail_sewa WHERE id_sewa = ?";
                PreparedStatement psDetail = con.prepareStatement(sqlDetail);
                psDetail.setString(1, idSewa);
                psDetail.executeUpdate();

                String sqlSewa = "DELETE FROM penyewaan WHERE id_sewa = ?";
                PreparedStatement psSewa = con.prepareStatement(sqlSewa);
                psSewa.setString(1, idSewa);
                psSewa.executeUpdate();

                DefaultTableModel model = (DefaultTableModel) table_sewa.getModel();
                model.removeRow(selectedRow);

                JOptionPane.showMessageDialog(
                    this,
                    "Data berhasil dihapus.",
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Gagal menghapus data: " + ex.getMessage(),
                    "Kesalahan",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
   
    private void tampilkanPreviewStruk(String isiStruk, String ucapan) {
      JFrame previewFrame = new JFrame("Preview Nota");
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

              // Font Calibri tetap
              Font font = new Font("Courier New", Font.PLAIN, 10);
              g2d.setFont(font);
              g2d.setColor(Color.BLACK);
              FontMetrics fm = g2d.getFontMetrics();

              int y = 130;
              int lineSpacing = 13; // Konsisten jarak antar baris
              int panelWidth = getWidth();

              // Header toko
              String[] headerLines = {
                  "Jl. Gajah Mada Gg. Buntu No. 2",
                  "(Barat Bank Danamon)Jember-Jawa Timur",
                  "WA Only (No Call/SMS) 0821 3191 2829",
                  "IG : brobet_jbr | Kode Pos. 68131"
              };

              for (String line : headerLines) {
                  int textWidth = fm.stringWidth(line);
                  int x = (panelWidth - textWidth) / 2;
                  g2d.drawString(line, x, y);
                  y += lineSpacing;
              }

              y += 5; // sedikit jarak sebelum isi struk

              // Isi struk dan ucapan
              for (String line : isiStruk.split("\n")) {
                  if (line.trim().equalsIgnoreCase(ucapan.trim())) {
                      int textWidth = fm.stringWidth(line);
                      int x = (panelWidth - textWidth) / 2;
                      g2d.drawString(line, x, y);
                  } else {
                      g2d.drawString(line, 10, y);
                  }
                  y += lineSpacing;
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
          cetakStruk(isiStruk, ucapan); // langsung cetak
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
            "(Barat Bank Danamon)Jember",
            "Telp. WA 082131912829",
            "IG : brobet_jbr"
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
                Font font = new Font("Courier New", Font.PLAIN, 9);
                g2d.setFont(font);
                FontMetrics fm = g2d.getFontMetrics();
                int lineSpacing = fm.getHeight() + 2;  // spasi antar baris

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
                    y += lineSpacing ;
                }

                for (String line : isiStruk.split("\n")) {
                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    String label = parts[0].trim();
                    String nilai = parts[1].trim();

                    int xLabel = 0;
                    int xTitik = 100;
                    int xValue = 110;

                    g2d.drawString(label, xLabel, y);
                    g2d.drawString(":", xTitik, y);
                    g2d.drawString(nilai, xValue, y);
                    y += lineSpacing;
                } else {
                    // buat baris lain yg panjang, pakai wrap
                    y = drawWrappedLine(g2d, line, 0, y, pageWidth, fm);
                }

                }


                // Ucapan (centered)
                y += 5;  // jarak manual kecil
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



    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser = new com.raven.datechooser.DateChooser();
        dateChooser2 = new com.raven.datechooser.DateChooser();
        page_main = new javax.swing.JPanel();
        page_penyewaan = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        btn_search = new javax.swing.JButton();
        txt_search = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btn_tambah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_detail = new javax.swing.JButton();
        btn_nota = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        label_username = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_sewa = new custom.JTable_custom();
        page_tambah = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        form_tambah = new javax.swing.JPanel();
        cek = new javax.swing.JButton();
        btn_calender = new javax.swing.JButton();
        btn_calender2 = new javax.swing.JButton();
        no_hp = new javax.swing.JTextField();
        tgl_kembali = new javax.swing.JTextField();
        tgl_pinjam = new javax.swing.JTextField();
        nama_penyewa = new javax.swing.JTextField();
        cek_ktm = new javax.swing.JRadioButton();
        cek_ktp = new javax.swing.JRadioButton();
        cek_sim = new javax.swing.JRadioButton();
        cek_fckk = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btn_back = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        label_username2 = new javax.swing.JLabel();
        btn_next = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        page_barang = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        btn_back9 = new javax.swing.JButton();
        form_table_tambah = new javax.swing.JPanel();
        txt_barcode = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_barang = new custom.JTable_customAutoresize();
        txt_searchBrg = new javax.swing.JTextField();
        btn_tambah_barang = new javax.swing.JButton();
        btn_hapus_barang = new javax.swing.JButton();
        btn_searchBrg = new javax.swing.JButton();
        txt_kembalian = new javax.swing.JTextField();
        txt_bayar = new javax.swing.JTextField();
        txt_nama = new javax.swing.JTextField();
        txt_qty = new javax.swing.JTextField();
        txt_total = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        label_username3 = new javax.swing.JLabel();

        dateChooser.setForeground(new java.awt.Color(195, 45, 45));
        dateChooser.setDateFormat("yyyy-MM-dd");
        dateChooser.setTextRefernce(tgl_pinjam);

        dateChooser2.setForeground(new java.awt.Color(195, 45, 45));
        dateChooser2.setDateFormat("yyyy-MM-dd");
        dateChooser2.setTextRefernce(tgl_kembali);

        setPreferredSize(new java.awt.Dimension(836, 666));
        setLayout(new java.awt.CardLayout());

        page_main.setBackground(new java.awt.Color(255, 244, 232));
        page_main.setPreferredSize(new java.awt.Dimension(836, 666));
        page_main.setLayout(new java.awt.CardLayout());

        page_penyewaan.setBackground(new java.awt.Color(255, 244, 232));
        page_penyewaan.setForeground(new java.awt.Color(230, 230, 230));
        page_penyewaan.setPreferredSize(new java.awt.Dimension(836, 666));
        page_penyewaan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Penyewaan.png"))); // NOI18N
        page_penyewaan.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 250, 37));

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_penyewaan.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

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
        page_penyewaan.add(btn_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 50, 40));

        txt_search.setBackground(new java.awt.Color(238, 236, 227));
        txt_search.setBorder(null);
        page_penyewaan.add(txt_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 181, 290, 20));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Search.png"))); // NOI18N
        page_penyewaan.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 410, -1));

        btn_tambah.setContentAreaFilled(false);

        btn_tambah.setBorderPainted(false);
        btn_tambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Tambah.png"))); // NOI18N
        btn_tambah.setBorder(null);
        btn_tambah.setContentAreaFilled(false);
        btn_tambah.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Tambah Select.png"))); // NOI18N
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });
        page_penyewaan.add(btn_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 210, 50));

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
        page_penyewaan.add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 170, -1, 40));

        btn_detail.setContentAreaFilled(false);

        btn_detail.setBorderPainted(false);
        btn_detail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Detail.png"))); // NOI18N
        btn_detail.setBorder(null);
        btn_detail.setContentAreaFilled(false);
        btn_detail.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Detail Select.png"))); // NOI18N
        btn_detail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detailActionPerformed(evt);
            }
        });
        page_penyewaan.add(btn_detail, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 170, -1, 40));

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
        page_penyewaan.add(btn_nota, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 170, -1, 40));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/BG Button.png"))); // NOI18N
        page_penyewaan.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 720, 65));

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_penyewaan.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        label_username.setText("Username");
        page_penyewaan.add(label_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        table_sewa.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(table_sewa);

        page_penyewaan.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 760, 380));

        page_main.add(page_penyewaan, "card2");

        page_tambah.setBackground(new java.awt.Color(255, 244, 232));
        page_tambah.setPreferredSize(new java.awt.Dimension(836, 666));
        page_tambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Data Penyewa.png"))); // NOI18N
        page_tambah.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 312, 37));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_tambah.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        form_tambah.setBackground(new java.awt.Color(255, 244, 232));
        form_tambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cek.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search Pelanggan.png"))); // NOI18N
        cek.setBorder(null);
        cek.setContentAreaFilled(false);
        cek.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search Select_1.png"))); // NOI18N
        cek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cekActionPerformed(evt);
            }
        });
        form_tambah.add(cek, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 183, 40, 30));

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
        form_tambah.add(btn_calender, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 238, 40, 40));

        btn_calender2.setContentAreaFilled(false);

        btn_calender2.setBorderPainted(false);
        btn_calender2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Calender.png"))); // NOI18N
        btn_calender2.setBorder(null);
        btn_calender2.setContentAreaFilled(false);
        btn_calender2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Calender Select.png"))); // NOI18N
        btn_calender2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_calender2ActionPerformed(evt);
            }
        });
        form_tambah.add(btn_calender2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 300, 40, 40));

        no_hp.setBackground(new java.awt.Color(255, 244, 232));
        no_hp.setToolTipText("");
        no_hp.setBorder(null);
        form_tambah.add(no_hp, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 368, 440, 30));

        tgl_kembali.setBackground(new java.awt.Color(255, 244, 232));
        tgl_kembali.setToolTipText("");
        tgl_kembali.setBorder(null);
        form_tambah.add(tgl_kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 306, 390, 30));

        tgl_pinjam.setBackground(new java.awt.Color(255, 244, 232));
        tgl_pinjam.setToolTipText("");
        tgl_pinjam.setBorder(null);
        form_tambah.add(tgl_pinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 244, 390, 30));

        nama_penyewa.setBackground(new java.awt.Color(255, 244, 232));
        nama_penyewa.setToolTipText("");
        nama_penyewa.setBorder(null);
        form_tambah.add(nama_penyewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 183, 390, 30));

        cek_ktm.setBorder(null);
        form_tambah.add(cek_ktm, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 430, 20, 20));

        cek_ktp.setBorder(null);
        form_tambah.add(cek_ktp, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 430, 20, 20));

        cek_sim.setBorder(null);
        form_tambah.add(cek_sim, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 430, 20, 20));
        form_tambah.add(cek_fckk, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 430, -1, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Form Tambah Penyewa (1).png"))); // NOI18N
        form_tambah.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 560, 490));

        page_tambah.add(form_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 580, 490));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/_Syarat dan Ketentuan berlaku..png"))); // NOI18N
        page_tambah.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 580, 230, 20));

        btn_back.setContentAreaFilled(false);

        btn_back.setBorderPainted(false);
        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Kembali.png"))); // NOI18N
        btn_back.setBorder(null);
        btn_back.setContentAreaFilled(false);
        btn_back.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Kembali Select.png"))); // NOI18N
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });
        page_tambah.add(btn_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(496, 581, 100, 40));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_tambah.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        label_username2.setText("Username");
        page_tambah.add(label_username2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        btn_back.setContentAreaFilled(false);

        btn_back.setBorderPainted(false);
        btn_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Buntton Lanjut.png"))); // NOI18N
        btn_next.setBorder(null);
        btn_next.setContentAreaFilled(false);
        btn_next.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Buntton Lanjut Select.png"))); // NOI18N
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });
        page_tambah.add(btn_next, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 580, 100, 40));
        page_tambah.add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, -1, -1));

        page_main.add(page_tambah, "card3");

        page_barang.setBackground(new java.awt.Color(255, 244, 232));
        page_barang.setPreferredSize(new java.awt.Dimension(836, 666));
        page_barang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Tambah Barang Sewa.png"))); // NOI18N
        page_barang.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 27, 440, 37));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 74.png"))); // NOI18N
        page_barang.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 27, 41, 37));

        btn_back.setContentAreaFilled(false);

        btn_back.setBorderPainted(false);
        btn_back9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Kembali.png"))); // NOI18N
        btn_back9.setBorder(null);
        btn_back9.setContentAreaFilled(false);
        btn_back9.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Kembali Select.png"))); // NOI18N
        btn_back9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_back9ActionPerformed(evt);
            }
        });
        page_barang.add(btn_back9, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 620, 100, 40));

        form_table_tambah.setBackground(new java.awt.Color(255, 244, 232));
        form_table_tambah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_barcode.setBackground(new java.awt.Color(238, 236, 227));
        txt_barcode.setBorder(null);
        form_table_tambah.add(txt_barcode, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 120, 20));

        table_barang.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(table_barang);

        form_table_tambah.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 560, 300));

        txt_searchBrg.setBackground(new java.awt.Color(238, 236, 227));
        txt_searchBrg.setBorder(null);
        txt_searchBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchBrgActionPerformed(evt);
            }
        });
        form_table_tambah.add(txt_searchBrg, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 43, 120, 20));

        btn_tambah_barang.setContentAreaFilled(false);

        btn_tambah_barang.setBorderPainted(false);
        btn_tambah_barang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Tambah Barang.png"))); // NOI18N
        btn_tambah_barang.setBorder(null);
        btn_tambah_barang.setContentAreaFilled(false);
        btn_tambah_barang.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Tambah Barang Select.png"))); // NOI18N
        btn_tambah_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_barangActionPerformed(evt);
            }
        });
        form_table_tambah.add(btn_tambah_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(448, 32, 80, 40));

        btn_hapus_barang.setContentAreaFilled(false);

        btn_hapus_barang.setBorderPainted(false);
        btn_hapus_barang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Hapus.png"))); // NOI18N
        btn_hapus_barang.setBorder(null);
        btn_hapus_barang.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Hapus Select.png"))); // NOI18N
        form_table_tambah.add(btn_hapus_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(529, 32, 80, 40));

        btn_search.setContentAreaFilled(false);

        btn_search.setBorderPainted(false);
        btn_searchBrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search.png"))); // NOI18N
        btn_searchBrg.setBorder(null);
        btn_searchBrg.setContentAreaFilled(false);
        btn_searchBrg.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Search Select.png"))); // NOI18N
        btn_searchBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchBrgActionPerformed(evt);
            }
        });
        form_table_tambah.add(btn_searchBrg, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 33, 40, 40));

        txt_kembalian.setBackground(new java.awt.Color(238, 236, 227));
        txt_kembalian.setBorder(null);
        form_table_tambah.add(txt_kembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 482, 110, 18));

        txt_bayar.setBackground(new java.awt.Color(238, 236, 227));
        txt_bayar.setBorder(null);
        txt_bayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_bayarKeyReleased(evt);
            }
        });
        form_table_tambah.add(txt_bayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 453, 110, 18));

        txt_nama.setBackground(new java.awt.Color(238, 236, 227));
        txt_nama.setBorder(null);
        form_table_tambah.add(txt_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 45, 90, 20));

        txt_qty.setBackground(new java.awt.Color(238, 236, 227));
        txt_qty.setBorder(null);
        form_table_tambah.add(txt_qty, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 46, 40, 20));

        txt_total.setEditable(false);
        txt_total.setBackground(new java.awt.Color(238, 236, 227));
        txt_total.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_total.setBorder(null);
        form_table_tambah.add(txt_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 463, 110, 24));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Form Tambah Barang Sewa Revisi.png"))); // NOI18N
        form_table_tambah.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, -10, 610, 540));

        page_barang.add(form_table_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 630, -1));

        btn_simpan.setContentAreaFilled(false);

        btn_simpan.setBorderPainted(false);
        btn_simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Simpan.png"))); // NOI18N
        btn_simpan.setBorder(null);
        btn_simpan.setContentAreaFilled(false);
        btn_simpan.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/penyewaan/Button Simpan Select.png"))); // NOI18N
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });
        page_barang.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 620, 110, 40));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashpeg/Group 28.png"))); // NOI18N
        page_barang.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, 69));

        label_username3.setText("Username");
        page_barang.add(label_username3, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 30, -1, 20));

        page_main.add(page_barang, "card3");

        add(page_main, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        page_main.removeAll();
        page_main.add(page_tambah);
        page_main.repaint();
        page_main.revalidate();
        
        
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        // TODO add your handling code here:
        page_main.removeAll();
        page_main.add(page_penyewaan);
        page_main.repaint();
        page_main.revalidate();
        
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_detailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detailActionPerformed
      int selectedRow = table_sewa.getSelectedRow();
        if (selectedRow != -1) {
            String idSewa = table_sewa.getValueAt(selectedRow, 0).toString();

            PanelDetailSewa panel = new PanelDetailSewa(idSewa);

            JDialog dialog = new JDialog();
            dialog.setTitle("Detail Sewa");
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.getContentPane().add(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(null); 
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data penyewaan terlebih dahulu.");
        }

    }//GEN-LAST:event_btn_detailActionPerformed

    private void btn_calenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_calenderActionPerformed
        // TODO add your handling code here:
      dateChooser.showPopup();
    }//GEN-LAST:event_btn_calenderActionPerformed

    private void btn_calender2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_calender2ActionPerformed
        // TODO add your handling code here:
        dateChooser2.showPopup();
    }//GEN-LAST:event_btn_calender2ActionPerformed

    private void btn_calenderPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btn_calenderPropertyChange
     
    }//GEN-LAST:event_btn_calenderPropertyChange

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
     try {
        if (nama_penyewa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi nama penyewa dulu dan cek pelanggan!");
            return;
        }

        String idPelanggan = pelangganLama ? idPelangganLama : generateID("pelanggan", "id_pelanggan", "PL");
        String idSewa = generateID("penyewaan", "id_sewa", "PN");


        String namaPenyewa = nama_penyewa.getText().trim();
        String tglPinjam = tgl_pinjam.getText().trim();
        String tglKembali = tgl_kembali.getText().trim();
        String noHp = no_hp.getText().trim();

        String bayarStr = txt_bayar.getText().replace("Rp", "").replace(".", "").replaceAll("\\s+", "");
        String kembalianStr = txt_kembalian.getText().replace("Rp", "").replace(".", "").replaceAll("\\s+", "");
        int bayar = Integer.parseInt(bayarStr);
        int kembalian = Integer.parseInt(kembalianStr);

        String jaminan = "";
        if (cek_sim.isSelected()) jaminan = "SIM";
        else if (cek_ktp.isSelected()) jaminan = "KTP";
        else if (cek_ktm.isSelected()) jaminan = "KTM";
        else if (cek_fckk.isSelected()) jaminan = "FC KK";

        
        DefaultTableModel model = (DefaultTableModel) table_barang.getModel();
        double totalHarga = 0;
        boolean rewardDipakai = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            int qty = Integer.parseInt(model.getValueAt(i, 2).toString());

            String hargaStr = model.getValueAt(i, 3).toString().replace("Rp", "").replace(".", "").replaceAll("\\s+", "");
            double harga = Double.parseDouble(hargaStr);

            totalHarga += qty * harga;

            String namaBarang = model.getValueAt(i, 1).toString().toLowerCase();
            if (namaBarang.contains("tenda") && harga == 0.0) {
                rewardDipakai = true;
            }
        }

        StringBuilder keterangan = new StringBuilder();
       
        boolean poinBaruDitambahkan = false;

        if (!pelangganLama) {
            int poin = 0;
            if (totalHarga >= 50000) {
                poin = 1;
                poinBaruDitambahkan = true;
            }
            String statusReward = (poin >= 5) ? "tersedia" : "tidak tersedia";

            String sqlPelanggan = "INSERT INTO pelanggan (id_pelanggan, nama_pelanggan, no_hp, poin, status_reward, status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psPelanggan = con.prepareStatement(sqlPelanggan);
            psPelanggan.setString(1, idPelanggan);
            psPelanggan.setString(2, namaPenyewa);
            psPelanggan.setString(3, noHp);
            psPelanggan.setInt(4, poin);
            psPelanggan.setString(5, statusReward);
            psPelanggan.setString(6, "aktif");
            psPelanggan.executeUpdate();

            pelangganLama = true;
            idPelangganLama = idPelanggan;
            poinSekarang = poin;

            keterangan.append("Pelanggan baru ditambahkan. Poin awal: ").append(poin).append(".\n");
        }


        if (pelangganLama && !rewardDipakai && totalHarga >= 50000 && !poinBaruDitambahkan) {
            poinSekarang += 1;
            String statusReward = (poinSekarang >= 5) ? "tersedia" : "tidak tersedia";

            String updatePoin = "UPDATE pelanggan SET poin = ?, status_reward = ? WHERE id_pelanggan = ?";
            PreparedStatement psUpdatePoin = con.prepareStatement(updatePoin);
            psUpdatePoin.setInt(1, poinSekarang);
            psUpdatePoin.setString(2, statusReward);
            psUpdatePoin.setString(3, idPelanggan);
            psUpdatePoin.executeUpdate();

            keterangan.append("Poin ditambahkan. Poin sekarang: ").append(poinSekarang).append(".\n");
        }

        String sqlSewa = "INSERT INTO penyewaan (id_sewa, id_pelanggan, id_pengguna, tgl_sewa, tgl_rencana_kembali, jaminan, total_harga, bayar, kembalian, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement psSewa = con.prepareStatement(sqlSewa);
        psSewa.setString(1, idSewa);
        psSewa.setString(2, idPelanggan);
        psSewa.setString(3, Session.getIdPengguna());
        psSewa.setString(4, tglPinjam);
        psSewa.setString(5, tglKembali);
        psSewa.setString(6, jaminan);
        psSewa.setDouble(7, totalHarga);
        psSewa.setInt(8, bayar);
        psSewa.setInt(9, kembalian);
        psSewa.setString(10, "Belum Kembali");
        
        psSewa.executeUpdate();

        for (int i = 0; i < table_barang.getRowCount(); i++) {
            String idBarang = table_barang.getValueAt(i, 0).toString();
            int qty = Integer.parseInt(table_barang.getValueAt(i, 2).toString());
            double harga = Double.parseDouble(table_barang.getValueAt(i, 3).toString());
            double subTotal = qty * harga;

            String idDetail = generateID("detail_sewa", "id_detail", "DTS");
            String sqlDetail = "INSERT INTO detail_sewa (id_detail, id_sewa, id_barang, qty, sub_total) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psDetail = con.prepareStatement(sqlDetail);
            psDetail.setString(1, idDetail);
            psDetail.setString(2, idSewa);
            psDetail.setString(3, idBarang);
            psDetail.setInt(4, qty);
            psDetail.setDouble(5, subTotal);
            psDetail.executeUpdate();
        }

        StringBuilder pesanAkhir = new StringBuilder("Transaksi penyewaan berhasil disimpan.\n");
        pesanAkhir.append(keterangan);

        JOptionPane.showMessageDialog(this, pesanAkhir.toString());


        nama_penyewa.setText("");
        no_hp.setText("");
        txt_total.setText("Rp 0");
        txt_bayar.setText("");
        txt_kembalian.setText("");

        DefaultTableModel modelClear = (DefaultTableModel) table_barang.getModel();
        modelClear.setRowCount(0);

        poinSekarang = 0;
        pelangganLama = false;
        idPelangganLama = "";
        
        page_main.removeAll();
        page_main.add(page_penyewaan);
        page_main.repaint();
        page_main.revalidate();
        load_table();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal simpan transaksi: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_searchActionPerformed

    private String idBarangTerpilih = ""; 
    private int hargaBarangTerpilih = 0; 

    
    private void btn_searchBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchBrgActionPerformed
        String keyword = txt_searchBrg.getText(); // TextField pencarian

    try {
        String sql = "SELECT * FROM barang WHERE nama_barang LIKE ?";
        pst = con.prepareStatement(sql);
        pst.setString(1, "%" + keyword + "%");
        rs = pst.executeQuery();

        if (rs.next()) {
            txt_nama.setText(rs.getString("nama_barang"));
            hargaBarangTerpilih = rs.getInt("harga_sewa");
            idBarangTerpilih = rs.getString("id_barang");
        } else {
            JOptionPane.showMessageDialog(null, "Barang tidak ditemukan!");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btn_searchBrgActionPerformed

    private void txt_searchBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchBrgActionPerformed
        String nama = txt_nama.getText();
        String qtyStr = txt_qty.getText();

        if (nama.isEmpty() || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Isi nama barang dan qty terlebih dahulu.");
            return;
        }

        int qty = Integer.parseInt(qtyStr);
       int hargaSewa = hargaBarangTerpilih;
       
        int subtotal = qty * hargaSewa;

        DefaultTableModel model = (DefaultTableModel) table_barang.getModel();
        model.addRow(new Object[]{nama, qty, hargaBarangTerpilih, subtotal});

        txt_nama.setText("");
        txt_qty.setText("");

        int total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += (int) model.getValueAt(i, 3);
        }
        txt_total.setText("Rp " + total);
    }//GEN-LAST:event_txt_searchBrgActionPerformed



    
    private void btn_tambah_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_barangActionPerformed
     try {
        String namaBarang = txt_nama.getText().trim();
        String qtyStr = txt_qty.getText().trim();

        if (namaBarang.isEmpty() || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Isi nama barang dan qty!");
            return;
        }

        int qtyInt = Integer.parseInt(qtyStr);
        if (qtyInt <= 0) {
            JOptionPane.showMessageDialog(null, "Qty harus lebih dari nol.");
            return;
        }

        String sql = "SELECT id_barang, harga_sewa, stok FROM barang WHERE nama_barang = ?";
        pst = con.prepareStatement(sql);
        pst.setString(1, namaBarang);
        rs = pst.executeQuery();

        if (rs.next()) {
            String idBarang = rs.getString("id_barang");
            int harga = rs.getInt("harga_sewa");  
            int stok = rs.getInt("stok");

            if (qtyInt > stok) {
                JOptionPane.showMessageDialog(null, "Stok tidak mencukupi! Stok tersedia: " + stok);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) table_barang.getModel();
            boolean barangSudahAda = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                String idTabel = model.getValueAt(i, 0).toString();
                if (idTabel.equals(idBarang)) {
                    int qtyLama = Integer.parseInt(model.getValueAt(i, 2).toString());
                    int qtyBaru = qtyLama + qtyInt;
                    int subTotalBaru = harga * qtyBaru;  

                    model.setValueAt(qtyBaru, i, 2);
                    model.setValueAt(subTotalBaru, i, 4);
                    barangSudahAda = true;
                    break;
                }
            }

            if (!barangSudahAda) {
                int subTotal = qtyInt * harga; 
                model.addRow(new Object[]{idBarang, namaBarang, qtyInt, harga, subTotal});
            }

            if (pelangganLama) {
                if (poinSekarang >= 5 && !isTendaSudahDitambahkan(model)) {
                    int jawab = JOptionPane.showConfirmDialog(null,
                        "Jumlah poin Anda: " + poinSekarang + "\n Mendapatkan 1x sewa tenda GRATIS!\nGunakan sekarang?",
                        "Reward Tersedia",
                        JOptionPane.YES_NO_OPTION);

                    if (jawab == JOptionPane.YES_OPTION) {
                        String sqlTenda = "SELECT id_barang, nama_barang FROM barang WHERE nama_barang LIKE '%tenda%'";
                        pst = con.prepareStatement(sqlTenda);
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            String idTenda = rs.getString("id_barang");
                            String namaTenda = rs.getString("nama_barang");
                            model.addRow(new Object[]{idTenda, namaTenda, 1, 0, 0});
                            JOptionPane.showMessageDialog(null, "Tenda gratis berhasil ditambahkan!");
                        }
                    }
                }
            }

            hitungTotalHarga();
            txt_nama.setText("");
            txt_qty.setText("");
            txt_searchBrg.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Barang tidak ditemukan!");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btn_tambah_barangActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
        hapusData();
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void cekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cekActionPerformed
        // TODO add your handling code here:
        cekPelanggan();
    }//GEN-LAST:event_cekActionPerformed

    private void txt_bayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_bayarKeyReleased
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

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        // TODO add your handling code here:
         page_main.removeAll();
        page_main.add(page_barang);
        page_main.repaint();
        page_main.revalidate();
        load_tableBrg();
    }//GEN-LAST:event_btn_nextActionPerformed

    private void btn_back9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_back9ActionPerformed
        // TODO add your handling code here:
        page_main.removeAll();
        page_main.add(page_tambah);
        page_main.repaint();
        page_main.revalidate();
    }//GEN-LAST:event_btn_back9ActionPerformed

    private void btn_notaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_notaActionPerformed
    
    int selectedRow = table_sewa.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data penyewaan yang ingin dicetak.");
        return;
    }

    try {
        String idSewa = table_sewa.getValueAt(selectedRow, 0).toString();

        // Ambil data utama penyewaan dan pelanggan
        String sqlSewa = "SELECT p.tgl_sewa, p.tgl_rencana_kembali, pl.nama_pelanggan, pl.no_hp, p.jaminan " +
                         "FROM penyewaan p JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan " +
                         "WHERE p.id_sewa = ?";
        PreparedStatement psSewa = con.prepareStatement(sqlSewa);
        psSewa.setString(1, idSewa);
        ResultSet rsSewa = psSewa.executeQuery();

        if (!rsSewa.next()) {
            JOptionPane.showMessageDialog(this, "Data penyewaan tidak ditemukan.");
            return;
        }

        String tglPinjam = rsSewa.getString("tgl_sewa");
        String tglKembali = rsSewa.getString("tgl_rencana_kembali");
        String nama = rsSewa.getString("nama_pelanggan");
        String noHp = rsSewa.getString("no_hp");
        String jaminan = rsSewa.getString("jaminan");
        

        // Ambil detail barang sewa
        String sqlDetail = "SELECT b.nama_barang, ds.qty, b.harga_sewa " +
                           "FROM detail_sewa ds JOIN barang b ON ds.id_barang = b.id_barang " +
                           "WHERE ds.id_sewa = ?";
        PreparedStatement psDetail = con.prepareStatement(sqlDetail);
        psDetail.setString(1, idSewa);
        ResultSet rsDetail = psDetail.executeQuery();
        
        int total = 0;
        StringBuilder detailBarang = new StringBuilder();
        detailBarang.append(String.format("%-15s %6s %12s\n", "Nama", "Jumlah", "Subtotal"));
        detailBarang.append("-------------------------------------------\n");

        while (rsDetail.next()) {
            String namaBarang = rsDetail.getString("nama_barang");
            int harga = rsDetail.getInt("harga_sewa");
            int qty = rsDetail.getInt("qty");
            int subTotal = qty * harga;
            total += subTotal;
            int sub = subTotal;

            String namaPendek = namaBarang.length() > 15 ? namaBarang.substring(0, 15) : namaBarang;
            detailBarang.append(String.format("%-13s %7d %12s\n", namaPendek, qty, String.format("Rp%,d", sub)));
        }



        // Ambil data bayar dan kembalian
        String sqlBayar = "SELECT bayar, kembalian FROM penyewaan WHERE id_sewa = ?";
        PreparedStatement psBayar = con.prepareStatement(sqlBayar);
        psBayar.setString(1, idSewa);
        ResultSet rsBayar = psBayar.executeQuery();

        String bayar = "Rp0", kembalian = "Rp0";
        if (rsBayar.next()) {
            bayar = String.format("Rp%,d", rsBayar.getInt("bayar"));
            kembalian = String.format("Rp%,d", rsBayar.getInt("kembalian"));
        }

        StringBuilder infoPelanggan = new StringBuilder();
        infoPelanggan.append("===========================================\n");
        infoPelanggan.append(String.format("%-13s : %s\n", "ID Penyewaan", idSewa));
        infoPelanggan.append(String.format("%-13s : %s\n", "Nama", nama));
        infoPelanggan.append(String.format("%-13s : %s\n", "No HP", noHp));
        infoPelanggan.append(String.format("%-13s : %s\n", "Tgl Pinjam", tglPinjam));
        infoPelanggan.append(String.format("%-13s : %s\n", "Tgl Kembali", tglKembali));
        infoPelanggan.append(String.format("%-13s : %s\n", "Jaminan", jaminan));

        infoPelanggan.append("-------------------------------------------\n");

        StringBuilder isiStruk = new StringBuilder();
        isiStruk.append("BARANG SEWA:\n");
        isiStruk.append(detailBarang);
        isiStruk.append("-------------------------------------------\n");
        isiStruk.append(String.format("%-13s : Rp%,d\n", "Total", total));
        isiStruk.append(String.format("%-13s : %s\n", "Bayar", bayar));
        isiStruk.append(String.format("%-13s : %s\n", "Kembalian", kembalian));
        isiStruk.append(String.format("%-13s : %s\n", "Kasir", Login.Session.getUsername()));
        isiStruk.append("===========================================\n\n");

        String ucapan = "TERIMAKASIH SUDAH MENYEWA!";
        StringBuilder previewStruk = new StringBuilder();
        previewStruk.append(infoPelanggan);
        previewStruk.append(isiStruk);

        // Tampilkan preview
        tampilkanPreviewStruk(previewStruk.toString(), ucapan);


    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menampilkan nota: " + e.getMessage());
    }

    }//GEN-LAST:event_btn_notaActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_back9;
    private javax.swing.JButton btn_calender;
    private javax.swing.JButton btn_calender2;
    private javax.swing.JButton btn_detail;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_hapus_barang;
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_nota;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_searchBrg;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_tambah_barang;
    private javax.swing.JButton cek;
    private javax.swing.JRadioButton cek_fckk;
    private javax.swing.JRadioButton cek_ktm;
    private javax.swing.JRadioButton cek_ktp;
    private javax.swing.JRadioButton cek_sim;
    private com.raven.datechooser.DateChooser dateChooser;
    private com.raven.datechooser.DateChooser dateChooser2;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JPanel form_table_tambah;
    private javax.swing.JPanel form_tambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label_username;
    private javax.swing.JLabel label_username2;
    private javax.swing.JLabel label_username3;
    private javax.swing.JTextField nama_penyewa;
    private javax.swing.JTextField no_hp;
    private javax.swing.JPanel page_barang;
    private javax.swing.JPanel page_main;
    private javax.swing.JPanel page_penyewaan;
    private javax.swing.JPanel page_tambah;
    private custom.JTable_customAutoresize table_barang;
    private custom.JTable_custom table_sewa;
    private javax.swing.JTextField tgl_kembali;
    private javax.swing.JTextField tgl_pinjam;
    private javax.swing.JTextField txt_barcode;
    private javax.swing.JTextField txt_bayar;
    private javax.swing.JTextField txt_kembalian;
    private javax.swing.JTextField txt_nama;
    private javax.swing.JTextField txt_qty;
    private javax.swing.JTextField txt_search;
    private javax.swing.JTextField txt_searchBrg;
    private javax.swing.JTextField txt_total;
    // End of variables declaration//GEN-END:variables
}
