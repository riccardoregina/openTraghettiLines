package gui;

import controller.ControllerCompagnia;
import unnamed.DatePicker;
import unnamed.Pair;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Locale;

public class CreaCorse {
    private JPanel panel1;
    private JButton bCrea;
    private JComboBox cbPortoArrivo;
    private JComboBox cbPortoPartenza;
    private JComboBox cbNatante;
    private JPanel panel1_1;
    private JCheckBox checkLun;
    private JCheckBox checkMer;
    private JCheckBox checkGio;
    private JCheckBox checkVen;
    private JCheckBox checkSab;
    private JCheckBox checkMar;
    private JCheckBox checkDom;
    private JTextField tfCosto;
    private JTextField tfSconto;
    private JTextField tfCostoPrevendita;
    private JTextField tfCostoVeicolo;
    private JTextField tfCostoBagaglio;
    private SpinnerNumberModel modelOre = new SpinnerNumberModel(0, 0, 23, 1);
    private SpinnerNumberModel modelMinuti = new SpinnerNumberModel(0, 0, 59, 1);
    private JPanel panel1_2;
    private JSpinner spinnerOraP;
    private JSpinner spinnerOraA;
    private JSpinner spinnerMinP;
    private JSpinner spinnerMinA;
    private JTextField tfDataPart;
    private JTextField tfDataArrivo;
    private JPanel panelPeriodo;
    private JButton buttonCreaPeriodo;
    private JLabel dip;
    private JLabel dfp;
    private JTextField tfDataFinePeriodo;
    private JButton bScalo;
    private JTextField tfDataInizioPeriodo;
    private JButton buttonChiudi;
    private JLabel labelCreaCorsa;
    private JButton bCal1;
    private JButton bCal2;
    private JButton bSelPeriodo;
    public JFrame frame;

    public JFrame frameChiamante;

    public ControllerCompagnia controllerCompagnia;

    public CreaCorse(JFrame frameChiamante, ControllerCompagnia controllerCompagnia) {
        this.frameChiamante = frameChiamante;
        this.controllerCompagnia = controllerCompagnia;
        frame = new JFrame("crea Corsa");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.8));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        tfDataInizioPeriodo.setEditable(false);
        tfDataInizioPeriodo.setText(String.valueOf(LocalDate.now().format(formatter)));
        tfDataFinePeriodo.setEditable(false);
        tfDataFinePeriodo.setText(String.valueOf(LocalDate.now().format(formatter)));

        spinnerMinA.setModel(modelMinuti);
        spinnerMinP.setModel(modelMinuti);
        spinnerOraP.setModel(modelOre);
        spinnerOraA.setModel(modelOre);

        ArrayList<LocalDate> inzioPer = new ArrayList<LocalDate>();
        ArrayList<LocalDate> finePer = new ArrayList<LocalDate>();

        ArrayList<Pair> porti = new ArrayList<Pair>();
        ArrayList<String> natanti = new ArrayList<String>();
        ArrayList<Integer> idPortiScalo = new ArrayList<Integer>();
        ArrayList<LocalTime> oraAttracco = new ArrayList<LocalTime>();
        ArrayList<LocalTime> oraRipartenza = new ArrayList<LocalTime>();

        controllerCompagnia.visualizzaPorti(porti);
        for (Pair porto : porti) {
            cbPortoPartenza.addItem(porto.last);
            cbPortoArrivo.addItem(porto.last);
        }

        controllerCompagnia.visualizzaNatanti(natanti);
        for (String natante : natanti) {
            cbNatante.addItem(natante);
        }

        bCal1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfDataInizioPeriodo.setText(new DatePicker(frame).setPickedDate());
            }
        });

        bCal2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfDataFinePeriodo.setText(new DatePicker(frame).setPickedDate());
            }
        });

        bCrea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idxPortoPartenza = cbPortoPartenza.getSelectedIndex();
                int idPortoPartenza = (int) porti.get(idxPortoPartenza).first;

                int idxPortoArrivo = cbPortoArrivo.getSelectedIndex();
                int idPortoArrivo = (int) porti.get(idxPortoArrivo).first;

                String giorniAttivi;
                BitSet giorniPeriodo = new BitSet(7);
                String costo = tfCosto.getText();
                float costoIntero;
                String valoreSconto = tfSconto.getText();
                float sconto;
                String prevendita = tfCostoPrevendita.getText();
                float costoPrevendita;
                String bagaglio = tfCostoBagaglio.getText();
                float costoBagaglio;
                String veicolo = tfCostoVeicolo.getText();
                float costoVeicolo;

                String nomeNatante = (String) cbNatante.getSelectedItem();

                int oraPart = (int) spinnerOraP.getValue();
                int oraArrivo = (int) spinnerOraA.getValue();
                int minPart = (int) spinnerMinP.getValue();
                int minArrivo = (int) spinnerMinA.getValue();
                String dataP = tfDataPart.getText();
                String dataA = tfDataArrivo.getText();
                LocalDate dataPartenza;
                LocalDate dataArrivo;
                LocalTime orarioPartenza;
                LocalTime orarioArrivo;

                int idCorsa = 0;

                try {
                    dataPartenza = LocalDate.parse(dataP);
                    dataArrivo = LocalDate.parse(dataA);
                    orarioPartenza = LocalTime.of(oraPart, minPart);
                    orarioArrivo = LocalTime.of(oraArrivo, minArrivo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "inserisci le date in maniera corretta (d-mm-yyyy)");
                    return;
                }
                if (dataPartenza.isAfter(dataArrivo)) {
                    JOptionPane.showMessageDialog(null, "La partenza non può essere dopo dell'arrivo");
                    return;
                } else if (dataPartenza.equals(dataArrivo) && orarioPartenza.isAfter(orarioArrivo)) {
                    JOptionPane.showMessageDialog(null, "se le date coincidono l'arrivo deve avvenire dopo la partenza. Inserisci correttamente gli orari");
                }

                //creo il bitset giusto per i giorni di attivita
                if (checkDom.isSelected())
                    giorniPeriodo.set(0);
                if (checkLun.isSelected())
                    giorniPeriodo.set(1);
                if (checkMar.isSelected())
                    giorniPeriodo.set(2);
                if (checkMer.isSelected())
                    giorniPeriodo.set(3);
                if (checkGio.isSelected())
                    giorniPeriodo.set(4);
                if (checkVen.isSelected())
                    giorniPeriodo.set(5);
                if (checkSab.isSelected())
                    giorniPeriodo.set(6);

                giorniAttivi = giorniPeriodo.toString();

                try {
                    costoIntero = Float.parseFloat(costo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "inserisci un valore numerico in Costo Intero");
                    return;
                }

                try {
                    sconto = Float.parseFloat(valoreSconto);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "inserisci un valore numerico in Sconto");
                    return;
                }
                if (sconto < 0 || sconto > 100) {
                    JOptionPane.showMessageDialog(null, "inserisci in sconto un valore tra 0 e 100");
                    return;
                }

                try {
                    costoPrevendita = Float.parseFloat(prevendita);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "inserisci un valore numerico in prevendita");
                    return;
                }
                if (costoPrevendita < 0) {
                    JOptionPane.showMessageDialog(null, "inserisci in sconto un valore positivo in prevendita");
                    return;
                }

                try {
                    costoVeicolo = Float.parseFloat(veicolo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "inserisci un valore numerico in veicolo");
                    return;
                }
                if (costoVeicolo < 0) {
                    JOptionPane.showMessageDialog(null, "inserisci un valore positivo in veicolo");
                    return;
                }

                try {
                    costoBagaglio = Float.parseFloat(bagaglio);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "inserisci un valore numerico in bagaglio");
                    return;
                }
                if (costoPrevendita < 0) {
                    JOptionPane.showMessageDialog(null, "inserisci un valore positivo in prevendita");
                    return;
                }

                if (controllerCompagnia.creaCorsa(idPortoPartenza, idPortoArrivo, giorniAttivi, inzioPer, finePer, orarioPartenza, orarioArrivo,
                        costoIntero, sconto, costoBagaglio, costoPrevendita, costoVeicolo, nomeNatante, idCorsa)) {
                    controllerCompagnia.aggiungiScali(idCorsa, idPortiScalo, oraAttracco, oraRipartenza);
                } else {
                    JOptionPane.showMessageDialog(null, "non è stato possibile creare la corsa");
                    return;
                }

            }
        });


        buttonCreaPeriodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dataIP = tfDataInizioPeriodo.getText();
                String dataFP = tfDataFinePeriodo.getText();
                try {
                    LocalDate dataInizioP = LocalDate.parse(dataIP);
                    LocalDate dataFineP = LocalDate.parse(dataFP);
                    inzioPer.add(dataFineP);
                    finePer.add(dataFineP);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "inserisci le date del periodo in maniera corretta (d-mm-yyyy)");
                    return;
                }
            }
        });

        buttonChiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                frame.dispose();
            }
        });


        bScalo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AggiungiScalo aggiungiScalo = new AggiungiScalo(frame, controllerCompagnia, idPortiScalo, oraAttracco, oraRipartenza);
                frame.setVisible(false);
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
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 9, new Insets(0, 0, 0, 0), -1, -1));
        cbPortoArrivo = new JComboBox();
        panel1.add(cbPortoArrivo, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbNatante = new JComboBox();
        panel1.add(cbNatante, new com.intellij.uiDesigner.core.GridConstraints(1, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1_1 = new JPanel();
        panel1_1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel1_1, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 7, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        checkLun = new JCheckBox();
        checkLun.setText("Lun");
        panel1_1.add(checkLun, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1_1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        checkMar = new JCheckBox();
        checkMar.setText("Mar");
        panel1_1.add(checkMar, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkMer = new JCheckBox();
        checkMer.setSelected(false);
        checkMer.setText("Mer");
        panel1_1.add(checkMer, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkGio = new JCheckBox();
        checkGio.setText("Gio");
        panel1_1.add(checkGio, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkVen = new JCheckBox();
        checkVen.setText("Ven");
        panel1_1.add(checkVen, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkSab = new JCheckBox();
        checkSab.setText("Sab");
        panel1_1.add(checkSab, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkDom = new JCheckBox();
        checkDom.setText("Dom");
        panel1_1.add(checkDom, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tfCosto = new JTextField();
        tfCosto.setText("Costo Intero");
        panel1.add(tfCosto, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tfSconto = new JTextField();
        tfSconto.setText("percentuale sconto (0-100)");
        panel1.add(tfSconto, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tfCostoPrevendita = new JTextField();
        tfCostoPrevendita.setText("costo Prevendita");
        panel1.add(tfCostoPrevendita, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tfCostoVeicolo = new JTextField();
        tfCostoVeicolo.setText("costo Veicolo");
        panel1.add(tfCostoVeicolo, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tfCostoBagaglio = new JTextField();
        tfCostoBagaglio.setText("Costo Bagaglio");
        panel1.add(tfCostoBagaglio, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        panel1_2 = new JPanel();
        panel1_2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel1_2, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        spinnerOraP = new JSpinner();
        spinnerOraP.setEnabled(true);
        panel1_2.add(spinnerOraP, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel1_2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        spinnerOraA = new JSpinner();
        panel1_2.add(spinnerOraA, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerMinP = new JSpinner();
        panel1_2.add(spinnerMinP, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spinnerMinA = new JSpinner();
        panel1_2.add(spinnerMinA, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Orario Partenza");
        panel1_2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Orario Arrivo");
        panel1_2.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbPortoPartenza = new JComboBox();
        panel1.add(cbPortoPartenza, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelPeriodo = new JPanel();
        panelPeriodo.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panelPeriodo, new com.intellij.uiDesigner.core.GridConstraints(5, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tfDataInizioPeriodo = new JTextField();
        tfDataInizioPeriodo.setText("yyyy-mm-dd");
        panelPeriodo.add(tfDataInizioPeriodo, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panelPeriodo.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tfDataFinePeriodo = new JTextField();
        tfDataFinePeriodo.setText("yyyy-mm-dd");
        panelPeriodo.add(tfDataFinePeriodo, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        dip = new JLabel();
        dip.setText("data inizio periodo");
        panelPeriodo.add(dip, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dfp = new JLabel();
        dfp.setText("data fine periodo");
        panelPeriodo.add(dfp, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCreaPeriodo = new JButton();
        buttonCreaPeriodo.setText("Aggiungi Periodo");
        panelPeriodo.add(buttonCreaPeriodo, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bCal1 = new JButton();
        bCal1.setText("...");
        panelPeriodo.add(bCal1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bCal2 = new JButton();
        bCal2.setText("...");
        panelPeriodo.add(bCal2, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bSelPeriodo = new JButton();
        bSelPeriodo.setText("Seleziona Periodo");
        panelPeriodo.add(bSelPeriodo, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bCrea = new JButton();
        bCrea.setText("CREA");
        panel1.add(bCrea, new com.intellij.uiDesigner.core.GridConstraints(1, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(6, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        bScalo = new JButton();
        bScalo.setText("Aggiungi Scalo");
        panel1.add(bScalo, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(1, 8, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonChiudi = new JButton();
        buttonChiudi.setText("CHIUDI");
        panel1.add(buttonChiudi, new com.intellij.uiDesigner.core.GridConstraints(2, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelCreaCorsa = new JLabel();
        Font labelCreaCorsaFont = this.$$$getFont$$$(null, -1, 20, labelCreaCorsa.getFont());
        if (labelCreaCorsaFont != null) labelCreaCorsa.setFont(labelCreaCorsaFont);
        labelCreaCorsa.setText("Crea Corsa");
        panel1.add(labelCreaCorsa, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
