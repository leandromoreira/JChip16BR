package br.com.leandromoreira.chip16.ui;

import br.com.leandromoreira.chip16.Chip16Machine;
import br.com.leandromoreira.chip16.gpu.Java2DRender;
import br.com.leandromoreira.chip16.util.ConfigManager;
import java.io.File;
import javax.swing.JFileChooser;

/**
 * @author leandro-rm
 */
public class JChip16BR extends javax.swing.JFrame {

    private Chip16Machine machine;
    

    public JChip16BR() {
        initComponents();
        setLocationRelativeTo(null);
        createBufferStrategy(2);
        setIgnoreRepaint(true);
        setTitle(ConfigManager.getConfig().getTitle());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPnScreen = new javax.swing.JPanel();
        jMnuBarMain = new javax.swing.JMenuBar();
        jMnuFile = new javax.swing.JMenu();
        jMnuLoad = new javax.swing.JMenuItem();
        jMnuEmulation = new javax.swing.JMenu();
        jMnuRun = new javax.swing.JMenuItem();
        jMnuPause = new javax.swing.JMenuItem();
        jMnuSetup = new javax.swing.JMenu();
        jMnuVideo = new javax.swing.JMenuItem();
        jMnuAudio = new javax.swing.JMenuItem();
        jMnuJoystick = new javax.swing.JMenuItem();
        jMnuSetupEmulation = new javax.swing.JMenuItem();
        jMnuAbout = new javax.swing.JMenu();
        jMnuJChip16BR = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
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

        jMnuFile.setText("File");

        jMnuLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/leandromoreira/chip16/resource/open-project-btn.png"))); // NOI18N
        jMnuLoad.setText("Load");
        jMnuLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnuLoadActionPerformed(evt);
            }
        });
        jMnuFile.add(jMnuLoad);

        jMnuBarMain.add(jMnuFile);

        jMnuEmulation.setText("Emulation");

        jMnuRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/leandromoreira/chip16/resource/run-project-btn.png"))); // NOI18N
        jMnuRun.setText("Run");
        jMnuEmulation.add(jMnuRun);

        jMnuPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/leandromoreira/chip16/resource/breakpoint-badge.png"))); // NOI18N
        jMnuPause.setText("Pause");
        jMnuEmulation.add(jMnuPause);

        jMnuBarMain.add(jMnuEmulation);

        jMnuSetup.setText("Setup");

        jMnuVideo.setText("Video");
        jMnuSetup.add(jMnuVideo);

        jMnuAudio.setText("Audio");
        jMnuSetup.add(jMnuAudio);

        jMnuJoystick.setText("Joystick");
        jMnuSetup.add(jMnuJoystick);

        jMnuSetupEmulation.setText("Emulation");
        jMnuSetup.add(jMnuSetupEmulation);

        jMnuBarMain.add(jMnuSetup);

        jMnuAbout.setText("About");

        jMnuJChip16BR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/leandromoreira/chip16/resource/step-over-btn.png"))); // NOI18N
        jMnuJChip16BR.setText("JChip16BR");
        jMnuAbout.add(jMnuJChip16BR);

        jMnuBarMain.add(jMnuAbout);

        setJMenuBar(jMnuBarMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPnScreen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPnScreen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

        System.out.println("typed: " + evt.getKeyChar());

    }//GEN-LAST:event_formKeyTyped

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

        System.out.println("pressed: " + evt.getKeyChar());

    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased

        System.out.println("released: " + evt.getKeyChar());

    }//GEN-LAST:event_formKeyReleased

    private void jMnuLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnuLoadActionPerformed
        final JFileChooser chooseARom = new JFileChooser();
        int whatUserDid = chooseARom.showOpenDialog(this);
        if (whatUserDid == JFileChooser.APPROVE_OPTION) {
            File romFile = chooseARom.getSelectedFile();
            startMachine(romFile);
        }
    }//GEN-LAST:event_jMnuLoadActionPerformed

    private void startMachine(final File romFile) {
        if (machine!=null){
            machine.stop();
        }
        machine = new Chip16Machine(romFile);
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
    private javax.swing.JMenu jMnuAbout;
    private javax.swing.JMenuItem jMnuAudio;
    private javax.swing.JMenuBar jMnuBarMain;
    private javax.swing.JMenu jMnuEmulation;
    private javax.swing.JMenu jMnuFile;
    private javax.swing.JMenuItem jMnuJChip16BR;
    private javax.swing.JMenuItem jMnuJoystick;
    private javax.swing.JMenuItem jMnuLoad;
    private javax.swing.JMenuItem jMnuPause;
    private javax.swing.JMenuItem jMnuRun;
    private javax.swing.JMenu jMnuSetup;
    private javax.swing.JMenuItem jMnuSetupEmulation;
    private javax.swing.JMenuItem jMnuVideo;
    private javax.swing.JPanel jPnScreen;
    // End of variables declaration//GEN-END:variables
}
