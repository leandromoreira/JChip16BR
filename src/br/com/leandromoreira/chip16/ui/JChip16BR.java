package br.com.leandromoreira.chip16.ui;

import br.com.leandromoreira.chip16.Chip16Machine;
import br.com.leandromoreira.chip16.gpu.Java2DRender;
import br.com.leandromoreira.chip16.util.ConfigManager;
import java.awt.HeadlessException;
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
        jToolBar1 = new javax.swing.JToolBar();
        jBtnLoad = new javax.swing.JButton();
        jBtnPause = new javax.swing.JButton();

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

        jToolBar1.setRollover(true);

        jBtnLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/leandromoreira/chip16/resource/open-project-btn.png"))); // NOI18N
        jBtnLoad.setToolTipText("Load rom");
        jBtnLoad.setFocusable(false);
        jBtnLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLoadActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnLoad);

        jBtnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/leandromoreira/chip16/resource/pause-button.png"))); // NOI18N
        jBtnPause.setToolTipText("Pause execution");
        jBtnPause.setFocusable(false);
        jBtnPause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnPause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnPauseActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnPause);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPnScreen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPnScreen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
    }//GEN-LAST:event_formKeyTyped

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (machine != null) {
            machine.sendCommand(evt.getKeyCode());
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        if (machine != null) {
            machine.sendCommand(evt.getKeyCode());
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
    private javax.swing.JButton jBtnLoad;
    private javax.swing.JButton jBtnPause;
    private javax.swing.JPanel jPnScreen;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
