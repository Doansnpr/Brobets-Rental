
package backend;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends javax.swing.JFrame {
    private String loggedInUser;
    private String loggedNamaUser;
    private String loggedAlamatUser;
    private String loggedNoTelpUser;
    private String loggedEmailUser;
    private String loggedUsernameUser;  
    
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    int xx,xy;
    
    public Login() {
        initComponents();
        Koneksi DB = new Koneksi();
        DB.config();
        con = DB.con;
    }

    public class Session {
      private static String idPengguna;
      private static String namaPengguna;
      private static String username;
      private static String role;

      public static void setSession(String id, String nama, String user, String userRole) {
          idPengguna = id;
          namaPengguna = nama;
          username = user;
          role = userRole;
      }

      public static String getIdPengguna() {
          return idPengguna;
      }

      public static String getNamaPengguna() {
          return namaPengguna;
      }

      public static String getUsername() {
          return username;
      }

      public static String getRole() {
          return role;
      }
  }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        form_login = new javax.swing.JPanel();
        txt_username = new javax.swing.JTextField();
        txt_password = new javax.swing.JPasswordField();
        btn_login = new javax.swing.JButton();
        link_register = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(930, 650));
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

        form_login.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_username.setBackground(new java.awt.Color(245, 245, 245));
        txt_username.setBorder(null);
        txt_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_usernameActionPerformed(evt);
            }
        });
        form_login.add(txt_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 380, 180, 18));

        txt_password.setBackground(new java.awt.Color(245, 245, 245));
        txt_password.setBorder(null);
        txt_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_passwordActionPerformed(evt);
            }
        });
        form_login.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 413, 180, 18));

        btn_login.setContentAreaFilled(false);

        btn_login.setBorderPainted(false);
        btn_login.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Button Login.png"))); // NOI18N
        btn_login.setBorder(null);
        btn_login.setContentAreaFilled(false);
        btn_login.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Button Select Login.png"))); // NOI18N
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });
        form_login.add(btn_login, new org.netbeans.lib.awtextra.AbsoluteConstraints(354, 440, 230, 30));

        link_register.setContentAreaFilled(false);

        link_register.setBorderPainted(false);
        link_register.setBackground(new java.awt.Color(242, 242, 242));
        link_register.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Link Register.png"))); // NOI18N
        link_register.setBorder(null);
        link_register.setContentAreaFilled(false);
        link_register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                link_registerActionPerformed(evt);
            }
        });
        form_login.add(link_register, new org.netbeans.lib.awtextra.AbsoluteConstraints(467, 477, 100, 20));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Login.png"))); // NOI18N
        form_login.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 650));

        getContentPane().add(form_login, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, 650));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_usernameActionPerformed

    private void txt_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_passwordActionPerformed

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
    
        String username = txt_username.getText();
        String password = new String(txt_password.getPassword());

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password tidak boleh kosong", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            con = Koneksi.getConnection();

            String query = "SELECT * FROM pengguna WHERE nama_pengguna = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, username);

            rs = pst.executeQuery();

            if (rs.next()) {
                String passDB = rs.getString("password");
                String id = rs.getString("id_pengguna");
                String nama = rs.getString("nama_lengkap");
                String alamat = rs.getString("alamat");
                String noTelp = rs.getString("no_hp");
                String namaUser = rs.getString("nama_pengguna");
                String email = rs.getString("email");
                String role = rs.getString("role");

                if (password.equals(passDB)) {
                    loggedInUser = id;
                    loggedNamaUser = nama;
                    loggedAlamatUser = alamat;
                    loggedNoTelpUser = noTelp;
                    loggedUsernameUser = namaUser;
                    loggedEmailUser = email;

                    Session.setSession(id, nama, namaUser, role);

                    // Tampilkan notifikasi login berhasil
                    JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + role, "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Buka halaman dashboard sesuai role setelah dialog tampil
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            if (role.equalsIgnoreCase("Admin")) {
                                new MenuDashPeg().setVisible(true);
                            } else {
                                new DashPeg().setVisible(true);
                            }
                            dispose();
                        }
                    });

                } else {
                    JOptionPane.showMessageDialog(this, "Password salah", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Username tidak ditemukan", "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pst.close();

            txt_password.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat login", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_loginActionPerformed

    private void link_registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_link_registerActionPerformed
        
        Register register = new Register();
        register.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_link_registerActionPerformed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
         xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:
        int x = evt.getXOnScreen();
       int y = evt.getYOnScreen();
       this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_formMouseDragged

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_login;
    private javax.swing.JPanel form_login;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton link_register;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_username;
    // End of variables declaration//GEN-END:variables
}
