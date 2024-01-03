package gui;

import controller.ControllerCliente;
import unnamed.CustomRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;

/**
 * The type Tuoi biglietti.
 */
public class TuoiBiglietti {
    private JPanel panel1;
    private JButton bChiudi;
    private JLabel label;
    private JScrollPane scrollPaneBiglietti;
    private JPanel panelLegenda;
    private JTable tableBiglietti;
    public JFrame frame;
    public JFrame frameChiamante;
    public ControllerCliente controllerCliente;


    public TuoiBiglietti(JFrame frameChiamante, ControllerCliente controllerCliente) {
        this.frameChiamante = frameChiamante;
        this.controllerCliente = controllerCliente;
        frame = new JFrame("bigliettiAttivi");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.8));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        aggiungiColoreLegenda(panelLegenda, new Color(255, 70, 70, 255), "Biglietti non più attivi");
        aggiungiColoreLegenda(panelLegenda, Color.white, "Biglietti ancora attivi");

        ArrayList<Integer> idBiglietti = new ArrayList<Integer>();
        ArrayList<Integer> idCorse = new ArrayList<Integer>();
        ArrayList<LocalDate> dataCor = new ArrayList<LocalDate>();
        ArrayList<LocalTime> orePart = new ArrayList<LocalTime>();
        ArrayList<String> portoPar = new ArrayList<String>();
        ArrayList<String> portoDest = new ArrayList<String>();
        ArrayList<Boolean> bagagli = new ArrayList<Boolean>();
        ArrayList<String> targaVeicolo = new ArrayList<String>();
        ArrayList<Integer> etaPass = new ArrayList<Integer>();
        ArrayList<LocalDate> dataAcquisto = new ArrayList<LocalDate>();
        ArrayList<Float> prezzi = new ArrayList<Float>();
        ArrayList<Integer> minutiRitardo = new ArrayList<Integer>();

        controllerCliente.visualizzaBigliettiAcquistati(idBiglietti, idCorse, dataCor, orePart, minutiRitardo, portoPar, portoDest, bagagli, targaVeicolo, etaPass, dataAcquisto, prezzi);

        String[] col = new String[]{"N° Biglietto", "ID Corsa", "Data", "Ore", "Da", "A", "Bagaglio", "Veicolo", "Età Passeggero", "Data Acquisto", "Prezzo"};
        Object[][] data = new Object[idCorse.size()][11];
        for (int i = 0; i < idBiglietti.size(); i++) {
            data[i][0] = idBiglietti.get(i);
            data[i][1] = idCorse.get(i);
            data[i][2] = dataCor.get(i);
            data[i][3] = orePart.get(i);
            data[i][4] = portoPar.get(i);
            data[i][5] = portoDest.get(i);
            data[i][6] = bagagli.get(i);
            data[i][7] = targaVeicolo.get(i);
            data[i][8] = etaPass.get(i);
            data[i][9] = dataAcquisto.get(i);
            data[i][10] = prezzi.get(i);
        }
        DefaultTableModel model = new DefaultTableModel(data, col) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArrayList<Boolean> booleanList = new ArrayList<Boolean>();
        for (int i = 0; i < idBiglietti.size(); i++) {
            if (dataCor.get(i).isBefore(LocalDate.now()) || (dataCor.get(i).isEqual(LocalDate.now()) && orePart.get(i).plusMinutes(minutiRitardo.get(i)).isBefore(LocalTime.now()))) {
                booleanList.add(true);
            } else {
                booleanList.add(false);
            }
        }

        tableBiglietti = new JTable(model);
        tableBiglietti.getTableHeader().setReorderingAllowed(false);
        tableBiglietti.setDefaultRenderer(Object.class, new CustomRenderer(booleanList));
        ListSelectionModel selectionModel = tableBiglietti.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneBiglietti.setViewportView(tableBiglietti);
        bChiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });
    }

    private void aggiungiColoreLegenda(JPanel panelLegenda, Color colore, String descrizione) {
        JPanel colorSquare = new JPanel();
        colorSquare.setPreferredSize(new Dimension(20, 20));
        colorSquare.setBackground(colore);
        Border bordo = BorderFactory.createLineBorder(Color.BLACK);
        colorSquare.setBorder(bordo);

        JLabel labelDescrizione = new JLabel(descrizione);

        JPanel bulletPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bulletPanel.add(colorSquare);
        bulletPanel.add(labelDescrizione);

        panelLegenda.add(bulletPanel);
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
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        label = new JLabel();
        Font labelFont = this.$$$getFont$$$(null, -1, 20, label.getFont());
        if (labelFont != null) label.setFont(labelFont);
        label.setText("I tuoi Biglietti");
        panel1.add(label, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPaneBiglietti = new JScrollPane();
        panel1.add(scrollPaneBiglietti, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 200), new Dimension(-1, 200), 0, false));
        panelLegenda = new JPanel();
        panelLegenda.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panelLegenda, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        bChiudi = new JButton();
        bChiudi.setText("Chiudi");
        panel1.add(bChiudi, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
