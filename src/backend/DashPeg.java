
package backend;

import custom.panel_custom;


public class DashPeg extends javax.swing.JFrame {

    int xx,xy;
   
    public DashPeg() {
        initComponents();
        
       activePanel = (panel_custom) btn_dash; 
       activePanel.setDynamicSize(200, btn_dash.getHeight()); 

        page.removeAll();
        page.add(new MenuDashPeg());
        page.repaint();
        page.revalidate();
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebar = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btn_dash = new custom.panel_custom();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_pemasok = new custom.panel_custom();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btn_barang = new custom.panel_custom();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btn_pelanggan = new custom.panel_custom();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btn_penyewaan = new custom.panel_custom();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btn_pengembalian = new custom.panel_custom();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btn_stok = new custom.panel_custom();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        content = new javax.swing.JPanel();
        content2 = new javax.swing.JPanel();
        page = new javax.swing.JPanel();

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

        sidebar.setBackground(new java.awt.Color(255, 244, 232));
        sidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logo (2).png"))); // NOI18N
        sidebar.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 150, 140));

        btn_dash.setBackground(new java.awt.Color(255, 244, 232));
        btn_dash.setRoundBottomLeft(10);
        btn_dash.setRoundBottomRight(10);
        btn_dash.setRoundTopLeft(10);
        btn_dash.setRoundTopRight(10);
        btn_dash.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_dashMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_dashMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_dashMouseExited(evt);
            }
        });
        btn_dash.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/DASHBOARD.png"))); // NOI18N
        btn_dash.add(jLabel3);
        jLabel3.setBounds(41, 10, 100, 20);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/HomeIcon.png"))); // NOI18N
        btn_dash.add(jLabel4);
        jLabel4.setBounds(8, 5, 27, 27);

        sidebar.add(btn_dash, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        btn_pemasok.setBackground(new java.awt.Color(255, 244, 232));
        btn_pemasok.setRoundBottomLeft(10);
        btn_pemasok.setRoundBottomRight(10);
        btn_pemasok.setRoundTopLeft(10);
        btn_pemasok.setRoundTopRight(10);
        btn_pemasok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_pemasokMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_pemasokMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_pemasokMouseExited(evt);
            }
        });
        btn_pemasok.setLayout(null);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/PEMASOK.png"))); // NOI18N
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
        });
        btn_pemasok.add(jLabel17);
        jLabel17.setBounds(41, 10, 100, 20);

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/Shopping_Cart_01.png"))); // NOI18N
        btn_pemasok.add(jLabel18);
        jLabel18.setBounds(8, 5, 30, 29);

        sidebar.add(btn_pemasok, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, -1, -1));

        btn_barang.setBackground(new java.awt.Color(255, 244, 232));
        btn_barang.setRoundBottomLeft(10);
        btn_barang.setRoundBottomRight(10);
        btn_barang.setRoundTopLeft(10);
        btn_barang.setRoundTopRight(10);
        btn_barang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_barangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_barangMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_barangMouseExited(evt);
            }
        });
        btn_barang.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/DAFTAR BARANG.png"))); // NOI18N
        btn_barang.add(jLabel5);
        jLabel5.setBounds(41, 10, 130, 20);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/List.png"))); // NOI18N
        btn_barang.add(jLabel6);
        jLabel6.setBounds(8, 5, 27, 27);

        sidebar.add(btn_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        btn_pelanggan.setBackground(new java.awt.Color(255, 244, 232));
        btn_pelanggan.setRoundBottomLeft(10);
        btn_pelanggan.setRoundBottomRight(10);
        btn_pelanggan.setRoundTopLeft(10);
        btn_pelanggan.setRoundTopRight(10);
        btn_pelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_pelangganMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_pelangganMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_pelangganMouseExited(evt);
            }
        });
        btn_pelanggan.setLayout(null);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/PELANGGAN.png"))); // NOI18N
        btn_pelanggan.add(jLabel7);
        jLabel7.setBounds(41, 10, 100, 20);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/User.png"))); // NOI18N
        btn_pelanggan.add(jLabel8);
        jLabel8.setBounds(8, 5, 27, 27);

        sidebar.add(btn_pelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        btn_penyewaan.setBackground(new java.awt.Color(255, 244, 232));
        btn_penyewaan.setRoundBottomLeft(10);
        btn_penyewaan.setRoundBottomRight(10);
        btn_penyewaan.setRoundTopLeft(10);
        btn_penyewaan.setRoundTopRight(10);
        btn_penyewaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_penyewaanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_penyewaanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_penyewaanMouseExited(evt);
            }
        });
        btn_penyewaan.setLayout(null);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/PENYEWAAN.png"))); // NOI18N
        btn_penyewaan.add(jLabel9);
        jLabel9.setBounds(41, 10, 100, 20);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/Additional Card.png"))); // NOI18N
        btn_penyewaan.add(jLabel10);
        jLabel10.setBounds(8, 5, 28, 28);

        sidebar.add(btn_penyewaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, -1, -1));

        btn_pengembalian.setBackground(new java.awt.Color(255, 244, 232));
        btn_pengembalian.setRoundBottomLeft(10);
        btn_pengembalian.setRoundBottomRight(10);
        btn_pengembalian.setRoundTopLeft(10);
        btn_pengembalian.setRoundTopRight(10);
        btn_pengembalian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_pengembalianMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_pengembalianMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_pengembalianMouseExited(evt);
            }
        });
        btn_pengembalian.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/PENGEMBALIAN.png"))); // NOI18N
        btn_pengembalian.add(jLabel11);
        jLabel11.setBounds(41, 10, 120, 20);

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/Transfer.png"))); // NOI18N
        btn_pengembalian.add(jLabel12);
        jLabel12.setBounds(8, 5, 28, 28);

        sidebar.add(btn_pengembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, -1, -1));

        btn_stok.setBackground(new java.awt.Color(255, 244, 232));
        btn_stok.setRoundBottomLeft(10);
        btn_stok.setRoundBottomRight(10);
        btn_stok.setRoundTopLeft(10);
        btn_stok.setRoundTopRight(10);
        btn_stok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_stokMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_stokMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_stokMouseExited(evt);
            }
        });
        btn_stok.setLayout(null);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/STOK MASUK.png"))); // NOI18N
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });
        btn_stok.add(jLabel13);
        jLabel13.setBounds(41, 10, 100, 20);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/Inbox.png"))); // NOI18N
        btn_stok.add(jLabel14);
        jLabel14.setBounds(8, 5, 28, 28);

        sidebar.add(btn_stok, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/Group 81.png"))); // NOI18N
        sidebar.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 200, 30));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/Group 80.png"))); // NOI18N
        sidebar.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 200, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/sidebar/Rectangle 6.png"))); // NOI18N
        sidebar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(sidebar, java.awt.BorderLayout.LINE_START);

        content.setBackground(new java.awt.Color(255, 255, 255));
        content.setLayout(new java.awt.BorderLayout());

        content2.setBackground(new java.awt.Color(255, 244, 232));

        page.setBackground(new java.awt.Color(255, 244, 232));
        page.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout content2Layout = new javax.swing.GroupLayout(content2);
        content2.setLayout(content2Layout);
        content2Layout.setHorizontalGroup(
            content2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(content2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(page, javax.swing.GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE)
                .addContainerGap())
        );
        content2Layout.setVerticalGroup(
            content2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, content2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(page, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        content.add(content2, java.awt.BorderLayout.CENTER);

        getContentPane().add(content, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private panel_custom activePanel = null;
    
    private void btn_dashMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_dashMouseExited
        if (activePanel != btn_dash) {
        ((panel_custom) btn_dash).setDynamicSize(180, btn_dash.getHeight());
    }
    }//GEN-LAST:event_btn_dashMouseExited

    private void btn_dashMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_dashMouseEntered
         if (activePanel != btn_dash) {
        ((panel_custom) btn_dash).setDynamicSize(200, btn_dash.getHeight());
    }
    }//GEN-LAST:event_btn_dashMouseEntered

    private void btn_barangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_barangMouseEntered
        ((panel_custom) btn_barang).setDynamicSize(200, btn_barang.getHeight());
    }//GEN-LAST:event_btn_barangMouseEntered

    private void btn_barangMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_barangMouseExited
        ((panel_custom) btn_barang).setDynamicSize(180, btn_barang.getHeight());
    }//GEN-LAST:event_btn_barangMouseExited

    private void btn_pelangganMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pelangganMouseEntered
         if (activePanel != btn_pelanggan) {
        ((panel_custom) btn_pelanggan).setDynamicSize(200, btn_pelanggan.getHeight());
         }
    }//GEN-LAST:event_btn_pelangganMouseEntered

    private void btn_pelangganMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pelangganMouseExited
         if (activePanel != btn_pelanggan) {
        ((panel_custom) btn_pelanggan).setDynamicSize(180, btn_pelanggan.getHeight());
         }
    }//GEN-LAST:event_btn_pelangganMouseExited

    private void btn_penyewaanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_penyewaanMouseEntered
         if (activePanel != btn_penyewaan) {
        ((panel_custom) btn_penyewaan).setDynamicSize(200, btn_penyewaan.getHeight());
         }
    }//GEN-LAST:event_btn_penyewaanMouseEntered

    private void btn_penyewaanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_penyewaanMouseExited
        if (activePanel != btn_penyewaan) {
        ((panel_custom) btn_penyewaan).setDynamicSize(180, btn_penyewaan.getHeight());
        }
    }//GEN-LAST:event_btn_penyewaanMouseExited

    private void btn_pengembalianMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pengembalianMouseEntered
         if (activePanel != btn_pengembalian) {
        ((panel_custom) btn_pengembalian).setDynamicSize(200, btn_pengembalian.getHeight());
    }
    }//GEN-LAST:event_btn_pengembalianMouseEntered

    private void btn_pengembalianMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pengembalianMouseExited
        if (activePanel != btn_pengembalian) {
        ((panel_custom) btn_pengembalian).setDynamicSize(180, btn_pengembalian.getHeight());
    }
    }//GEN-LAST:event_btn_pengembalianMouseExited

    private void btn_stokMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_stokMouseEntered
       if (activePanel != btn_stok) {
        ((panel_custom) btn_stok).setDynamicSize(200, btn_stok.getHeight());
       }
    }//GEN-LAST:event_btn_stokMouseEntered

    private void btn_stokMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_stokMouseExited
        if (activePanel != btn_stok) {
        ((panel_custom) btn_stok).setDynamicSize(180, btn_stok.getHeight());
        }
    }//GEN-LAST:event_btn_stokMouseExited

    private void btn_dashMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_dashMouseClicked
       if (activePanel != null && activePanel != btn_dash) {
            activePanel.setDynamicSize(180, activePanel.getHeight());
        }

        activePanel = (panel_custom) btn_dash;
        activePanel.setDynamicSize(200, btn_dash.getHeight());

        page.removeAll();
        page.add(new MenuDashPeg());
        page.repaint();
        page.revalidate();
    }//GEN-LAST:event_btn_dashMouseClicked

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
       int x = evt.getXOnScreen();
       int y = evt.getYOnScreen();
       this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_formMouseDragged

    private void btn_penyewaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_penyewaanMouseClicked
        if (activePanel != null && activePanel != btn_penyewaan) {
            activePanel.setDynamicSize(180, activePanel.getHeight());
        }

        activePanel = (panel_custom) btn_penyewaan;
        activePanel.setDynamicSize(200, btn_penyewaan.getHeight());

        page.removeAll();
        page.add(new MenuPenyewaan());
        page.repaint();
        page.revalidate();
    }//GEN-LAST:event_btn_penyewaanMouseClicked

    private void btn_pengembalianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pengembalianMouseClicked
        if (activePanel != null && activePanel != btn_pengembalian) {
            activePanel.setDynamicSize(180, activePanel.getHeight());
        }

        activePanel = (panel_custom) btn_pengembalian;
        activePanel.setDynamicSize(200, btn_pengembalian.getHeight());

        page.removeAll();
        page.add(new MenuPengembalian());
        page.repaint();
        page.revalidate();
    }//GEN-LAST:event_btn_pengembalianMouseClicked

    private void btn_pelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pelangganMouseClicked
        // TODO add your handling code here:
        if (activePanel != null && activePanel != btn_pelanggan) {
            activePanel.setDynamicSize(180, activePanel.getHeight());
        }

        activePanel = (panel_custom) btn_pelanggan;
        activePanel.setDynamicSize(200, btn_pelanggan.getHeight());

        page.removeAll();
        page.add(new MenuPelanggan());
        page.repaint();
        page.revalidate();
    }//GEN-LAST:event_btn_pelangganMouseClicked

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel13MouseClicked

    private void btn_stokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_stokMouseClicked
        // TODO add your handling code here:
          if (activePanel != null && activePanel != btn_stok) {
            activePanel.setDynamicSize(180, activePanel.getHeight());
        }

        activePanel = (panel_custom) btn_stok;
        activePanel.setDynamicSize(200, btn_stok.getHeight());

        page.removeAll();
        page.add(new StokMasuuk());
        page.repaint();
        page.revalidate();
        
    }//GEN-LAST:event_btn_stokMouseClicked

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel17MouseClicked

    private void btn_pemasokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pemasokMouseClicked
        
    }//GEN-LAST:event_btn_pemasokMouseClicked

    private void btn_pemasokMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pemasokMouseEntered
       if (activePanel != btn_pemasok) {
        ((panel_custom) btn_pemasok).setDynamicSize(200, btn_pemasok.getHeight());
       }
    }//GEN-LAST:event_btn_pemasokMouseEntered

    private void btn_pemasokMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pemasokMouseExited
        if (activePanel != btn_pemasok) {
        ((panel_custom) btn_pemasok).setDynamicSize(180, btn_pemasok.getHeight());
        }
    }//GEN-LAST:event_btn_pemasokMouseExited

    private void btn_barangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_barangMouseClicked
       
          if (activePanel != null && activePanel != btn_barang) {
            activePanel.setDynamicSize(180, activePanel.getHeight());
        }

        activePanel = (panel_custom) btn_barang;
        activePanel.setDynamicSize(200, btn_barang.getHeight());

        page.removeAll();
        page.add(new MenuBarang());
        page.repaint();
        page.revalidate();
    }//GEN-LAST:event_btn_barangMouseClicked

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
            java.util.logging.Logger.getLogger(DashPeg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashPeg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashPeg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashPeg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashPeg().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private custom.panel_custom btn_barang;
    private custom.panel_custom btn_dash;
    private custom.panel_custom btn_pelanggan;
    private custom.panel_custom btn_pemasok;
    private custom.panel_custom btn_pengembalian;
    private custom.panel_custom btn_penyewaan;
    private custom.panel_custom btn_stok;
    private javax.swing.JPanel content;
    private javax.swing.JPanel content2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel page;
    private javax.swing.JPanel sidebar;
    // End of variables declaration//GEN-END:variables
}
