package gui;

import controller.ControllerCompagnia;
import unnamed.AlternativeCustomRenderer;
import unnamed.CustomRenderer;
import unnamed.DatePicker;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ModificaCorsaSpecifica {
    private JPanel contentPane;
    private JPanel panelDate;
    private JLabel labelData;
    private JTextField textData;
    private JButton bCal;
    private JLabel labelModificaCorsa;
    private JButton buttonCancella;
    private JSpinner spinnerMinuti;
    private SpinnerNumberModel modelMinuti = new SpinnerNumberModel(0, 0, null, 1);
    private JButton buttonRitardo;
    private JLabel labelMinutiRitardo;
    private JButton buttonChiudi;
    private JButton buttonVediCorse;
    private JScrollPane scrollPaneCorse;
    private JLabel label1;
    private JPanel panelLegenda;
    private JPanel panelBottom;
    private JPanel panelRitardo;
    private JPanel panelCorse;
    private JPanel panelChiudi;
    public JFrame frame;
    public JFrame frameChiamante;
    public ControllerCompagnia controllerCompagnia;
    private DateTimeFormatter formatter;
    public LocalDate dataCorsa;
    public ArrayList<Integer> idCorsa;
    public ArrayList<String> portoPartenza;
    public ArrayList<LocalTime> orarioPartenza;
    public ArrayList<String> portoArrivo;
    public ArrayList<LocalTime> orarioArrivo;
    public ArrayList<Integer> minutiRitardo;
    public ArrayList<Integer> postiDispPass;
    public ArrayList<Integer> postiDispVei;
    public ArrayList<Boolean> cancellata;
    public ArrayList<Boolean> booleanListGreen;
    public ArrayList<Boolean> booleanListBlue;
    private JTable tableCorse;

    public ModificaCorsaSpecifica(JFrame frameChiamante, ControllerCompagnia controllerCompagnia) {
        this.frameChiamante = frameChiamante;
        this.controllerCompagnia = controllerCompagnia;
        frame = new JFrame("Modifica Corsa");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.width / 1.2), (int) (screenSize.height / 1.2));
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("resources/icons/logo.png").getImage().getScaledInstance(400, 400, 1));
        frame.setVisible(true);
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        contentPane.setBackground(Color.white);
        panelBottom.setBackground(Color.white);
        panelChiudi.setBackground(Color.white);
        panelCorse.setBackground(Color.white);
        panelRitardo.setBackground(Color.white);
        panelDate.setBackground(Color.white);
        panelLegenda.setBackground(Color.white);

        textData.setEditable(false);
        textData.setText(String.valueOf(LocalDate.now().format(formatter)));

        CustomRenderer.aggiungiColoreLegenda(panelLegenda, new Color(255, 83, 83), "Corse cancellate");
        CustomRenderer.aggiungiColoreLegenda(panelLegenda, new Color(181, 255, 182), "Corse partite ma non ancora arrivate a destinazione");
        CustomRenderer.aggiungiColoreLegenda(panelLegenda, new Color(203, 203, 203), "Corse terminate");
        CustomRenderer.aggiungiColoreLegenda(panelLegenda, Color.white, "Corse non ancora partite");

        spinnerMinuti.setModel(modelMinuti);

        aggiornaTabellaCorse();

        bCal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textData.setText(new DatePicker(frame).setPickedDate());
            }
        });

        buttonVediCorse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textData.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserire una data");
                    return;
                }
                aggiornaTabellaCorse();
            }
        });

        buttonCancella.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableCorse.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona una corsa da cancellare");
                    return;
                }

                if (cancellata.get(selectedRow)) {
                    JOptionPane.showMessageDialog(null, "La corsa selezionata è già cancellata");
                } else if (booleanListGreen.get(selectedRow)) {
                    JOptionPane.showMessageDialog(null, "La corsa selezionata è già partita");
                } else if (booleanListBlue.get(selectedRow)) {
                    JOptionPane.showMessageDialog(null, "La corsa selezionata è già terminata");
                } else if (controllerCompagnia.cancellaCorsaSpecifica((int) tableCorse.getValueAt(selectedRow, 0), dataCorsa)) {
                    JOptionPane.showMessageDialog(null, "Corsa cancellata!");
                    aggiornaTabellaCorse();
                } else {
                    JOptionPane.showMessageDialog(null, "Non è stato possibile cancellare la corsa");
                }
            }
        });

        buttonRitardo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableCorse.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona una corsa");
                    return;
                }
                int minutiR = (int) spinnerMinuti.getValue();

                if (cancellata.get(selectedRow)) {
                    JOptionPane.showMessageDialog(null, "La corsa selezionata è già cancellata");
                } else if (booleanListGreen.get(selectedRow)) {
                    JOptionPane.showMessageDialog(null, "La corsa selezionata è già partita");
                } else if (booleanListBlue.get(selectedRow)) {
                    JOptionPane.showMessageDialog(null, "La corsa selezionata è già terminata");
                } else if (controllerCompagnia.segnalaRitardo((int) tableCorse.getValueAt(selectedRow, 0), dataCorsa, minutiR)) {
                    JOptionPane.showMessageDialog(null, "Ritardo segnalato!");
                    aggiornaTabellaCorse();
                } else {
                    JOptionPane.showMessageDialog(null, "Non è stato possibile segnalare il ritardo");
                }
            }
        });

        buttonChiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                frameChiamante.setVisible(true);
            }
        });
    }

    private void aggiornaTabellaCorse() {
        dataCorsa = LocalDate.parse(textData.getText(), formatter);

        idCorsa = new ArrayList<Integer>();
        portoPartenza = new ArrayList<String>();
        orarioPartenza = new ArrayList<LocalTime>();
        portoArrivo = new ArrayList<String>();
        orarioArrivo = new ArrayList<LocalTime>();
        minutiRitardo = new ArrayList<Integer>();
        postiDispPass = new ArrayList<Integer>();
        postiDispVei = new ArrayList<Integer>();
        cancellata = new ArrayList<Boolean>();

        booleanListGreen = new ArrayList<Boolean>();
        booleanListBlue = new ArrayList<Boolean>();

        controllerCompagnia.visualizzaCorseSpecifichePerData(dataCorsa, idCorsa, portoPartenza, orarioPartenza, portoArrivo, orarioArrivo, minutiRitardo, postiDispPass, postiDispVei, cancellata);

        String[] col = new String[]{"ID", "Da", "Ore", "A", "Ore", "Ritardo", "P. Passeggeri", "P. Veicoli", "Cancellata"};
        Object[][] data = new Object[idCorsa.size()][9];

        for (int i = 0; i < idCorsa.size(); i++) {
            data[i][0] = idCorsa.get(i);
            data[i][1] = portoPartenza.get(i);
            data[i][2] = orarioPartenza.get(i);
            data[i][3] = portoArrivo.get(i);
            data[i][4] = orarioArrivo.get(i);
            data[i][5] = minutiRitardo.get(i);
            data[i][6] = postiDispPass.get(i);
            data[i][7] = postiDispVei.get(i);
            data[i][8] = cancellata.get(i);

            if (dataCorsa.isEqual(LocalDate.now())) {
                if (!cancellata.get(i) && LocalTime.now().isAfter(orarioPartenza.get(i).plusMinutes(minutiRitardo.get(i))) && LocalTime.now().isBefore(orarioArrivo.get(i))) {
                    booleanListGreen.add(true);
                    booleanListBlue.add(false);
                } else if (!cancellata.get(i) && LocalTime.now().isAfter(orarioArrivo.get(i))) {
                    booleanListGreen.add(false);
                    booleanListBlue.add(true);
                } else {
                    booleanListGreen.add(false);
                    booleanListBlue.add(false);
                }
            } else if (LocalDate.now().isAfter(dataCorsa) && !cancellata.get(i)) {
                booleanListGreen.add(false);
                booleanListBlue.add(true);
            } else {
                booleanListGreen.add(false);
                booleanListBlue.add(false);
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, col) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCorse = new JTable(model);
        tableCorse.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        ListSelectionModel selectionModel = tableCorse.getSelectionModel();
        int columnIndexToHide = 8;
        TableColumnModel columnModel = tableCorse.getColumnModel();
        TableColumn column = columnModel.getColumn(columnIndexToHide);
        columnModel.removeColumn(column);
        tableCorse.setDefaultRenderer(Object.class, new AlternativeCustomRenderer(cancellata, booleanListGreen, booleanListBlue));
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneCorse.setViewportView(tableCorse);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelDate = new JPanel();
        panelDate.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panelDate, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelData = new JLabel();
        labelData.setText("Data:");
        panelDate.add(labelData, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textData = new JTextField();
        panelDate.add(textData, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        bCal = new JButton();
        bCal.setText("...");
        panelDate.add(bCal, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), new Dimension(30, -1), 0, false));
        buttonVediCorse = new JButton();
        buttonVediCorse.setText("Vedi Corse");
        panelDate.add(buttonVediCorse, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelBottom = new JPanel();
        panelBottom.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panelBottom, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonCancella = new JButton();
        buttonCancella.setText("Cancella Corsa");
        panelBottom.add(buttonCancella, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelRitardo = new JPanel();
        panelRitardo.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelBottom.add(panelRitardo, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        spinnerMinuti = new JSpinner();
        panelRitardo.add(spinnerMinuti, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRitardo = new JButton();
        buttonRitardo.setText("Segnala Ritardo");
        panelRitardo.add(buttonRitardo, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelMinutiRitardo = new JLabel();
        labelMinutiRitardo.setText("Minuti di Ritardo:");
        panelRitardo.add(labelMinutiRitardo, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonChiudi = new JButton();
        buttonChiudi.setText("Chiudi");
        panelBottom.add(buttonChiudi, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("               ");
        panelBottom.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelCorse = new JPanel();
        panelCorse.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panelCorse, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 400), new Dimension(-1, 400), 0, false));
        scrollPaneCorse = new JScrollPane();
        panelCorse.add(scrollPaneCorse, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(700, -1), new Dimension(1000, -1), new Dimension(1000, -1), 0, false));
        label1 = new JLabel();
        label1.setText("Tutte le corse nella data selezionata");
        panelCorse.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelModificaCorsa = new JLabel();
        Font labelModificaCorsaFont = this.$$$getFont$$$(null, -1, 22, labelModificaCorsa.getFont());
        if (labelModificaCorsaFont != null) labelModificaCorsa.setFont(labelModificaCorsaFont);
        labelModificaCorsa.setText("Modifica Corsa specifica");
        contentPane.add(labelModificaCorsa, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("");
        contentPane.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("");
        contentPane.add(label4, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("");
        contentPane.add(label5, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("");
        contentPane.add(label6, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        contentPane.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panelChiudi = new JPanel();
        panelChiudi.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panelChiudi, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panelChiudi.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panelChiudi.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        panelLegenda = new JPanel();
        panelLegenda.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        contentPane.add(panelLegenda, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

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

    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
