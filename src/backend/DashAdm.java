
package backend;

import custom.panel_custom;

public class DashAdm extends javax.swing.JFrame {

    int xx,xy;
    
    public DashAdm() {
        initComponents();
        
        activePanel = (panel_custom) btn_dashadm; // Atur panel aktif
       activePanel.setDynamicSize(200, btn_dashadm.getHeight()); // Ukuran diperbesar untuk menunjukkan aktif

        // Load dashboard panel ke dalam panel utama
        page2.removeAll();
        page2.add(new MenuDashAdm());
        page2.repaint();
        page2.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebar2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btn_dashadm = new custom.panel_custom();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_user = new custom.panel_custom();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btn_laporan = new custom.panel_custom();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        content2 = new javax.swing.JPanel();
        contentADM = new javax.swing.JPanel();
        page2 = new javax.swing.JPanel();

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

        sidebar2.setBackground(new java.awt.Color(255, 244, 232));
        sidebar2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logo (2).png"))); // NOI18N
        sidebar2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 150, 140));

        btn_dashadm.setBackground(new java.awt.Color(255, 244, 232));
        btn_dashadm.setRoundBottomLeft(10);
        btn_dashadm.setRoundBottomRight(10);
        btn_dashadm.setRoundTopLeft(10);
        btn_dashadm.setRoundTopRight(10);
        btn_dashadm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_dashadmMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_dashadmMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_dashadmMouseExited(evt);
            }
        });
        btn_dashadm.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/DASHBOARD.png"))); // NOI18N
        btn_dashadm.add(jLabel3);
        jLabel3.setBounds(41, 10, 100, 20);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/HomeIcon.png"))); // NOI18N
        btn_dashadm.add(jLabel4);
        jLabel4.setBounds(8, 5, 27, 27);

        sidebar2.add(btn_dashadm, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        btn_user.setBackground(new java.awt.Color(255, 244, 232));
        btn_user.setRoundBottomLeft(10);
        btn_user.setRoundBottomRight(10);
        btn_user.setRoundTopLeft(10);
        btn_user.setRoundTopRight(10);
        btn_user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_userMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_userMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_userMouseExited(evt);
            }
        });
        btn_user.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashadm/DATA USER.png"))); // NOI18N
        btn_user.add(jLabel5);
        jLabel5.setBounds(41, 10, 130, 20);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashadm/User.png"))); // NOI18N
        btn_user.add(jLabel6);
        jLabel6.setBounds(8, 5, 27, 27);

        sidebar2.add(btn_user, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        btn_laporan.setBackground(new java.awt.Color(255, 244, 232));
        btn_laporan.setRoundBottomLeft(10);
        btn_laporan.setRoundBottomRight(10);
        btn_laporan.setRoundTopLeft(10);
        btn_laporan.setRoundTopRight(10);
        btn_laporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_laporanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_laporanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_laporanMouseExited(evt);
            }
        });
        btn_laporan.setLayout(null);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashadm/LAPORAN.png"))); // NOI18N
        btn_laporan.add(jLabel9);
        jLabel9.setBounds(41, 10, 100, 20);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashadm/Description.png"))); // NOI18N
        btn_laporan.add(jLabel10);
        jLabel10.setBounds(8, 5, 28, 29);

        sidebar2.add(btn_laporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/dashadm/LAPORAN PUTIH.png"))); // NOI18N
        sidebar2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 200, 30));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/Group 80.png"))); // NOI18N
        sidebar2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 200, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/Rectangle 6.png"))); // NOI18N
        sidebar2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(sidebar2, java.awt.BorderLayout.LINE_START);

        content2.setBackground(new java.awt.Color(255, 255, 255));
        content2.setLayout(new java.awt.BorderLayout());

        contentADM.setBackground(new java.awt.Color(255, 244, 232));

        page2.setBackground(new java.awt.Color(255, 244, 232));
        page2.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout contentADMLayout = new javax.swing.GroupLayout(contentADM);
        contentADM.setLayout(contentADMLayout);
        contentADMLayout.setHorizontalGroup(
            contentADMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentADMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(page2, javax.swing.GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE)
                .addContainerGap())
        );
        contentADMLayout.setVerticalGroup(
            contentADMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentADMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(page2, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
                .addContainerGap())
        );

        content2.add(contentADM, java.awt.BorderLayout.CENTER);

        getContentPane().add(content2, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private panel_custom activePanel = null;
    
    private void btn_dashadmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_dashadmMouseClicked
        if (activePanel != null && activePanel != btn_dashadm) {
            activePanel.setDynamicSize(180, activePanel.getHeight());
        }

        activePanel = (panel_custom) btn_dashadm;
        activePanel.setDynamicSize(200, btn_dashadm.getHeight());

        page2.removeAll();
        page2.add(new MenuDashAdm());
        page2.repaint();
        page2.revalidate();
    }//GEN-LAST:event_btn_dashadmMouseClicked

    private void btn_dashadmMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_dashadmMouseEntered
        if (activePanel != btn_dashadm) {
            ((panel_custom) btn_dashadm).setDynamicSize(200, btn_dashadm.getHeight());
        }
    }//GEN-LAST:event_btn_dashadmMouseEntered

    private void btn_dashadmMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_dashadmMouseExited
        if (activePanel != btn_dashadm) {
            ((panel_custom) btn_dashadm).setDynamicSize(180, btn_dashadm.getHeight());
        }
    }//GEN-LAST:event_btn_dashadmMouseExited

    private void btn_userMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_userMouseEntered
        if (activePanel != btn_user){
        ((panel_custom) btn_user).setDynamicSize(200, btn_user.getHeight());
        }
    }//GEN-LAST:event_btn_userMouseEntered

    private void btn_userMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_userMouseExited
        if (activePanel != btn_user){
        ((panel_custom) btn_user).setDynamicSize(180, btn_user.getHeight());
        }
    }//GEN-LAST:event_btn_userMouseExited

    private void btn_laporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_laporanMouseClicked
        if (activePanel != null && activePanel != btn_laporan) {
            activePanel.setDynamicSize(180, activePanel.getHeight());
        }

        activePanel = (panel_custom) btn_laporan;
        activePanel.setDynamicSize(200, btn_laporan.getHeight());

        page2.removeAll();
        page2.add(new MenuPenyewaan());
        page2.repaint();
        page2.revalidate();
    }//GEN-LAST:event_btn_laporanMouseClicked

    private void btn_laporanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_laporanMouseEntered
        if (activePanel != btn_laporan) {
            ((panel_custom) btn_laporan).setDynamicSize(200, btn_laporan.getHeight());
        }
    }//GEN-LAST:event_btn_laporanMouseEntered

    private void btn_laporanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_laporanMouseExited
        if (activePanel != btn_laporan) {
            ((panel_custom) btn_laporan).setDynamicSize(180, btn_laporan.getHeight());
        }
    }//GEN-LAST:event_btn_laporanMouseExited

    private void btn_userMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_userMouseClicked
        // TODO add your handling code here:
         if (activePanel != null && activePanel != btn_user) {
            activePanel.setDynamicSize(180, activePanel.getHeight());
        }

        activePanel = (panel_custom) btn_user;
        activePanel.setDynamicSize(200, btn_user.getHeight());

        page2.removeAll();
        page2.add(new MenuUser());
        page2.repaint();
        page2.revalidate();
    }//GEN-LAST:event_btn_userMouseClicked

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

   boolean panjang = false;
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
            java.util.logging.Logger.getLogger(DashAdm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashAdm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashAdm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashAdm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashAdm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private custom.panel_custom btn_dashadm;
    private custom.panel_custom btn_laporan;
    private custom.panel_custom btn_user;
    private javax.swing.JPanel content2;
    private javax.swing.JPanel contentADM;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel page2;
    private javax.swing.JPanel sidebar2;
    // End of variables declaration//GEN-END:variables
}
