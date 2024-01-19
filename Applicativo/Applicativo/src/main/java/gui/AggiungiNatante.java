package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import controller.ControllerCompagnia;

/**
 * The type Aggiungi natante.
 */
public class AggiungiNatante {
    private JPanel contentPane;
    private JButton bAggiungiNatante;
    private JComboBox cbTipoNatante;
    private JTextField tfCapPasseggeri;
    private JTextField tfCapVeicoli;
    private JTextField tfNomeNatante;
    private JButton bIndietro;

    public ControllerCompagnia controllerCompagnia;
    public JFrame frame;
    public JFrame frameChiamante;


    public AggiungiNatante(JFrame frameChiamante, ControllerCompagnia controllerCompagnia) {

        this.frameChiamante = frameChiamante;
        this.controllerCompagnia = controllerCompagnia;
        frame = new JFrame("aggiungiNatante");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.8));
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("resources/icons/logo.png").getImage().getScaledInstance(400, 400, 1));
        frame.setVisible(true);

        contentPane.setBackground(Color.white);

        for (String s : Arrays.asList("traghetto", "motonave", "aliscafo", "altro")) {
            cbTipoNatante.addItem(s);
        }

        bAggiungiNatante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int capVeicoli;
                String nome = tfNomeNatante.getText();
                int capPasseggeri;
                String temp1 = tfCapPasseggeri.getText();
                String tipoNatante = (String) cbTipoNatante.getSelectedItem();

                try {
                    capPasseggeri = Integer.parseInt(temp1);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "inserisci un valore numerico in Capienza Passeggeri");
                    return;
                }


                if (tipoNatante.equals("traghetto") || tipoNatante.equals("altro")) {
                    String temp = tfCapVeicoli.getText();

                    try {
                        capVeicoli = Integer.parseInt(temp);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Inserisci un valore numerico in Capienza Veicoli");
                        return;
                    }
                } else {
                    capVeicoli = 0;
                    JOptionPane.showMessageDialog(null, "siccome il tipo selezionato non ha disponibilità di veicoli, la capienza è 0");
                }


                if (nome.equals("Nome Natante") || nome.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "inserisci il nome correttamente.");
                    return;
                } else {
                    boolean check = controllerCompagnia.aggiungiNatante(nome, tipoNatante, capPasseggeri, capVeicoli);
                    if (!check) {
                        JOptionPane.showMessageDialog(null, "non è stato possibile aggiungere il natante.");
                    } else {
                        JOptionPane.showMessageDialog(null, "natante aggiunto.");
                    }
                    frameChiamante.setVisible(true);
                    frame.dispose();
                }
            }
        });
        bIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 8, new Insets(0, 0, 0, 0), -1, -1));
        bAggiungiNatante = new JButton();
        bAggiungiNatante.setText("Aggiungi");
        contentPane.add(bAggiungiNatante, new com.intellij.uiDesigner.core.GridConstraints(2, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        contentPane.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(3, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cbTipoNatante = new JComboBox();
        cbTipoNatante.setToolTipText("");
        contentPane.add(cbTipoNatante, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tfCapPasseggeri = new JTextField();
        tfCapPasseggeri.setText("Capienza Passeggeri");
        contentPane.add(tfCapPasseggeri, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tfCapVeicoli = new JTextField();
        tfCapVeicoli.setText("Capienza Veicoli");
        contentPane.add(tfCapVeicoli, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tfNomeNatante = new JTextField();
        tfNomeNatante.setText("Nome Natante");
        contentPane.add(tfNomeNatante, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("tipo natante");
        contentPane.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bIndietro = new JButton();
        bIndietro.setText("Chiudi");
        contentPane.add(bIndietro, new com.intellij.uiDesigner.core.GridConstraints(2, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("");
        contentPane.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("");
        contentPane.add(label3, new com.intellij.uiDesigner.core.GridConstraints(1, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("");
        contentPane.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
