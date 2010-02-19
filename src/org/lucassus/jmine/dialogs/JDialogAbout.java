/*
 * JDialogAbout.java
 *
 * Created on 29 sierpie� 2005, 19:15
 */

package org.lucassus.jmine.dialogs;

import java.awt.Rectangle;

/**
 *
 * @author  lucassus
 */
public class JDialogAbout extends javax.swing.JDialog {
	
	/** Creates new form JDialogAbout */
	public JDialogAbout(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		Rectangle parentBounds = parent.getBounds();
		setLocation(parentBounds.x + 20, parentBounds.y + 20);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jToggleButtonOk = new javax.swing.JToggleButton();
    jTextArea = new javax.swing.JTextArea();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("About");
    setResizable(false);
    getContentPane().setLayout(new java.awt.GridBagLayout());

    jToggleButtonOk.setText("Ok");
    jToggleButtonOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jToggleButtonOkActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    getContentPane().add(jToggleButtonOk, gridBagConstraints);

    jTextArea.setBackground(new java.awt.Color(238, 238, 238));
    jTextArea.setEditable(false);
    jTextArea.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
    jTextArea.setText("Author:\tŁukasz Bandzarewicz\nEmail:\tlukasz.bandzarewicz@gmail.com\n\t(C) 2005 - 2010");
    getContentPane().add(jTextArea, new java.awt.GridBagConstraints());

    pack();
  }// </editor-fold>//GEN-END:initComponents

	private void jToggleButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonOkActionPerformed
		setVisible(false);
		dispose();
	}//GEN-LAST:event_jToggleButtonOkActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextArea jTextArea;
  private javax.swing.JToggleButton jToggleButtonOk;
  // End of variables declaration//GEN-END:variables
	
}
