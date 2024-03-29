package br.com.leandromoreira.chip16.ui;

import br.com.leandromoreira.chip16.Chip16Machine;
import br.com.leandromoreira.chip16.gpu.Java2DRender;
import br.com.leandromoreira.chip16.util.ConfigManager;
import java.awt.HeadlessException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author leandro-rm
 */
public class JChip16BR extends javax.swing.JFrame {

    private Chip16Machine machine;

    public JChip16BR() {
        initLookAndFeel(ConfigManager.getConfig().getLAF());
        initComponents();
        setLocationRelativeTo(null);
        createBufferStrategy(2);
        setIgnoreRepaint(true);
        setTitle(ConfigManager.getConfig().getTitle());
    }

    private void initLookAndFeel(final String lookfeel) {
        String lookAndFeel = null;
        if (lookfeel != null) {
            if (lookfeel.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            } else if (lookfeel.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            } else if (lookfeel.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } else if (lookfeel.equals("GTK")) {
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            } else if (lookfeel.equals("Nimbus")) {
                lookAndFeel = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
            } else {
                try {
                    UIManager.setLookAndFeel(lookAndFeel);
                } catch (ClassNotFoundException ex) {
                    lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                } catch (InstantiationException ex) {
                    lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                } catch (IllegalAccessException ex) {
                    lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                } catch (UnsupportedLookAndFeelException ex) {
                    lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                }
            }
            try {
                UIManager.setLookAndFeel(lookAndFeel);
            } catch (ClassNotFoundException e) {
                System.err.println("Couldn't find class for specified look and feel:" + lookAndFeel);
                System.err.println("Did you include the L&F library in the class path?");
                System.err.println("Using the default look and feel.");
            } catch (UnsupportedLookAndFeelException e) {
                System.err.println("Can't use the specified look and feel (" + lookAndFeel + ") on this platform.");
                System.err.println("Using the default look and feel.");
            } catch (Exception e) {
                System.err.println("Couldn't get specified look and feel (" + lookAndFeel + "), for some reason.");
                System.err.println("Using the default look and feel.");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPnScreen = new javax.swing.JPanel();
        jTool = new javax.swing.JToolBar();
        jBtnLoad = new javax.swing.JButton();
        jBtnPause = new javax.swing.JButton();
        jBtnStop = new javax.swing.JButton();
        jBtnVideoSize = new javax.swing.JButton();
        jBtnAbout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        jPnScreen.setBackground(new java.awt.Color(9, 5, 0));
        jPnScreen.setPreferredSize(new java.awt.Dimension(320, 240));

        javax.swing.GroupLayout jPnScreenLayout = new javax.swing.GroupLayout(jPnScreen);
        jPnScreen.setLayout(jPnScreenLayout);
        jPnScreenLayout.setHorizontalGroup(
            jPnScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );
        jPnScreenLayout.setVerticalGroup(
            jPnScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        jTool.setFloatable(false);
        jTool.setBorderPainted(false);

        jBtnLoad.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("open-project-btn.png"))); // NOI18N
        jBtnLoad.setToolTipText("Load rom");
        jBtnLoad.setFocusable(false);
        jBtnLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLoadActionPerformed(evt);
            }
        });
        jTool.add(jBtnLoad);

        jBtnPause.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("pause-button.png"))); // NOI18N
        jBtnPause.setToolTipText("Pause and resume");
        jBtnPause.setFocusable(false);
        jBtnPause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnPause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPauseActionPerformed(evt);
            }
        });
        jTool.add(jBtnPause);

        jBtnStop.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("finish-session-btn.png"))); // NOI18N
        jBtnStop.setToolTipText("Stop");
        jBtnStop.setFocusable(false);
        jBtnStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnStopActionPerformed(evt);
            }
        });
        jTool.add(jBtnStop);

        jBtnVideoSize.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("2.gif"))); // NOI18N
        jBtnVideoSize.setToolTipText("Video size");
        jBtnVideoSize.setFocusable(false);
        jBtnVideoSize.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnVideoSize.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnVideoSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnVideoSizeActionPerformed(evt);
            }
        });
        jTool.add(jBtnVideoSize);

        jBtnAbout.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("about.png"))); // NOI18N
        jBtnAbout.setToolTipText("About");
        jBtnAbout.setFocusable(false);
        jBtnAbout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnAbout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTool.add(jBtnAbout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTool, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPnScreen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTool, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPnScreen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
    }//GEN-LAST:event_formKeyTyped

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (machine != null) {
            machine.addCommand(evt.getKeyCode());
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        if (machine != null) {
            machine.removeCommand(evt.getKeyCode());
        }
    }//GEN-LAST:event_formKeyReleased

    private void jBtnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLoadActionPerformed
        loadFile();
    }//GEN-LAST:event_jBtnLoadActionPerformed

    private void jBtnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnPauseActionPerformed
        if (machine != null) {
            if (machine.isPaused()) {
                machine.resume();
            } else {
                machine.pause();
            }
        }
    }//GEN-LAST:event_jBtnPauseActionPerformed
    private boolean flipFlop2X = true;

    private void jBtnVideoSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnVideoSizeActionPerformed
        if (flipFlop2X) {
            setSize(getWidth() * 2, getHeight() * 2);
        } else {
            setSize(getWidth() / 2, getHeight() / 2);
        }
        flipFlop2X = !flipFlop2X;
    }//GEN-LAST:event_jBtnVideoSizeActionPerformed

    private void jBtnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnStopActionPerformed
        if (machine != null) {
            machine.stop();
        }
    }//GEN-LAST:event_jBtnStopActionPerformed

    public void loadFile() throws HeadlessException {
        final JFileChooser chooseARom = new JFileChooser(new File("./rom/ROMs"));

        int whatUserDid = chooseARom.showOpenDialog(this);
        if (whatUserDid == JFileChooser.APPROVE_OPTION) {
            File romFile = chooseARom.getSelectedFile();
            startMachine(romFile);
        }
    }

    private void startMachine(final File romFile) {
        if (machine != null) {
            machine.stop();
        }
        machine = new Chip16Machine(romFile);
        setTitle(ConfigManager.getConfig().getTitle() + " " + machine.getRom().getTitleName());
        machine.start(new Java2DRender(jPnScreen.getGraphics().create()));
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new JChip16BR().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAbout;
    private javax.swing.JButton jBtnLoad;
    private javax.swing.JButton jBtnPause;
    private javax.swing.JButton jBtnStop;
    private javax.swing.JButton jBtnVideoSize;
    private javax.swing.JPanel jPnScreen;
    private javax.swing.JToolBar jTool;
    // End of variables declaration//GEN-END:variables
}
