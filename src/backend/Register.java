
package backend;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends javax.swing.JFrame {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    int xx,xy;
    
    public Register() {
        initComponents();
        Koneksi DB = new Koneksi();
        DB.config();
        con = DB.con;
    }
    
    private void reset() {
        txt_nama.setText("");
        txt_username.setText("");
        txt_email.setText("");
        txt_password.setText("");
        txt_telp.setText("");
        txt_alamat.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_custom2 = new custom.panel_custom();
        txt_role = new javax.swing.JTextField();
        txt_nama = new javax.swing.JTextField();
        txt_username = new javax.swing.JTextField();
        txt_email = new javax.swing.JTextField();
        txt_telp = new javax.swing.JTextField();
        txt_password = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_alamat = new javax.swing.JTextArea();
        btn_register = new javax.swing.JButton();
        link_register = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_custom2.setBackground(new java.awt.Color(204, 204, 255));
        panel_custom2.setRoundBottomLeft(30);
        panel_custom2.setRoundBottomRight(30);
        panel_custom2.setRoundTopLeft(30);
        panel_custom2.setRoundTopRight(30);
        panel_custom2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_role.setEditable(false);
        txt_role.setBackground(new java.awt.Color(245, 245, 245));
        txt_role.setText("Pegawai");
        txt_role.setToolTipText("");
        txt_role.setBorder(null);
        panel_custom2.add(txt_role, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 447, 180, -1));

        txt_nama.setBackground(new java.awt.Color(245, 245, 245));
        txt_nama.setBorder(null);
        panel_custom2.add(txt_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 251, 180, -1));

        txt_username.setBackground(new java.awt.Color(245, 245, 245));
        txt_username.setBorder(null);
        panel_custom2.add(txt_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 281, 180, -1));

        txt_email.setBackground(new java.awt.Color(245, 245, 245));
        txt_email.setBorder(null);
        panel_custom2.add(txt_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 311, 180, -1));

        txt_telp.setEditable(false);
        txt_telp.setBackground(new java.awt.Color(245, 245, 245));
        txt_telp.setBorder(null);
        panel_custom2.add(txt_telp, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 371, 180, -1));

        txt_password.setBackground(new java.awt.Color(245, 245, 245));
        txt_password.setBorder(null);
        panel_custom2.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 341, 180, -1));

        jScrollPane1.setBorder(null);

        txt_alamat.setBackground(new java.awt.Color(245, 245, 245));
        txt_alamat.setColumns(20);
        txt_alamat.setRows(5);
        jScrollPane1.setViewportView(txt_alamat);

        panel_custom2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 402, 180, 30));

        btn_register.setContentAreaFilled(false);

        btn_register.setBorderPainted(false);
        btn_register.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Button Register.png"))); // NOI18N
        btn_register.setContentAreaFilled(false);
        btn_register.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Button Register Select.png"))); // NOI18N
        btn_register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registerActionPerformed(evt);
            }
        });
        panel_custom2.add(btn_register, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 470, 230, 30));

        link_register.setContentAreaFilled(false);

        link_register.setBorderPainted(false);
        link_register.setBackground(new java.awt.Color(242, 242, 242));
        link_register.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Link Login.png"))); // NOI18N
        link_register.setBorder(null);
        link_register.setContentAreaFilled(false);
        link_register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                link_registerActionPerformed(evt);
            }
        });
        panel_custom2.add(link_register, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 504, -1, 20));

        jLabel2.setBackground(new java.awt.Color(252, 252, 252));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Form Register.png"))); // NOI18N
        panel_custom2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 650));

        getContentPane().add(panel_custom2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 650));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
       int x = evt.getXOnScreen();
       int y = evt.getYOnScreen();
       this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_formMouseDragged

    private void btn_registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registerActionPerformed
        // TODO add your handling code here:
        try {
        if (txt_nama.getText().isEmpty() || txt_username.getText().isEmpty() ||
            txt_email.getText().isEmpty() || txt_password.getText().isEmpty() ||
            txt_telp.getText().isEmpty() || txt_alamat.getText().isEmpty() ||
            txt_role.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Tidak ada data yang diinput!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sqlCheck = "SELECT COUNT(*) AS count FROM pengguna WHERE username = ? AND role = ?";
        pst = con.prepareStatement(sqlCheck);
        pst.setString(1, txt_username.getText());
        pst.setString(2, txt_role.getText());
        rs = pst.executeQuery();

        if (rs.next() && rs.getInt("count") > 0) {
            JOptionPane.showMessageDialog(null, "Username telah digunakan!");
        } else {
            String sqlID = "SELECT id_user FROM user ORDER BY id_user DESC LIMIT 1";
            pst = con.prepareStatement(sqlID);
            rs = pst.executeQuery();

            String newID = "UR001";
            if (rs.next()) {
                String IDterakhir = rs.getString("id_user");
                int angka = Integer.parseInt(IDterakhir.substring(1)) + 1;
                newID = String.format("UR%03d", angka);
            }

            rs.close();
            pst.close();

            String sql = "INSERT INTO pengguna (id_pengguna, nama, username, email, password, alamat, no_telp, role) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sql);

    //        String hashedPassword = BCrypt.hashpw(new String(txt_password.getPassword()), BCrypt.gensalt());

            pst.setString(1, newID);
            pst.setString(2, txt_nama.getText());
            pst.setString(3, txt_username.getText());
            pst.setString(4, txt_email.getText());
            pst.setString(5, txt_password.getText()); // atau hashedPassword jika menggunakan enkripsi
            pst.setString(6, txt_alamat.getText());
            pst.setString(7, txt_telp.getText());
            pst.setString(8, txt_role.getText());

            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Register berhasil!");
                reset();
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

      
    }//GEN-LAST:event_btn_registerActionPerformed

    private void link_registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_link_registerActionPerformed
        // TODO add your handling code here:
        Login login = new Login();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_link_registerActionPerformed


    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Register().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_register;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton link_register;
    private custom.panel_custom panel_custom2;
    private javax.swing.JTextArea txt_alamat;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_nama;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_role;
    private javax.swing.JTextField txt_telp;
    private javax.swing.JTextField txt_username;
    // End of variables declaration//GEN-END:variables
}
