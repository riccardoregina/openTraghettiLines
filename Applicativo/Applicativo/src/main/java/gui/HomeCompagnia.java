package gui;

import controller.ControllerCompagnia;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeCompagnia {
    private JPanel contentPane;
    private JPanel panel1;
    private JButton bCreaCorsa;
    private JButton bModificaCorsa;
    private JButton bAggiungiNatante;
    private JButton bRimuoviNatante;
    private JPanel panel2;
    private JScrollPane scrollPaneCorse;
    private JTable tableCorse;
    private JScrollPane scrollPaneNatanti;
    private JLabel corseErogateLabel;
    private JLabel natantiDisponibiliLabel;
    private JButton buttonEliminaCorsa;
    private JButton buttonLogout;
    private JButton buttonModificaCorsaRegolare;
    private JButton buttonInfoNatante;
    private JButton buttonIncassi;
    private JButton buttonContatti;
    private JLabel labelHome;
    private JPanel panelCorse;
    private JPanel panelInfoCorse;
    private JPanel panelNatanti;
    private JPanel panelInfoNatanti;
    private JPanel panelContatti;
    private JPanel panelLogout;
    private JLabel labelLogout;
    private JLabel labelButtonLogout;
    private JPanel panelL;
    private JLabel labelContatti;
    private JTable tableNatanti;
    public ControllerCompagnia controllerCompagnia;
    public JFrame frameChiamante;
    public JFrame frame;

    public HomeCompagnia(JFrame frameChiamante, ControllerCompagnia controllerCompagnia) {

        this.frameChiamante = frameChiamante;
        this.controllerCompagnia = controllerCompagnia;
        frame = new JFrame("homeCompagnia");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.width / 1.2), (int) (screenSize.height / 1.2));
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("resources/icons/logo.png").getImage().getScaledInstance(400, 400, 1));
        frame.setVisible(true);

        Color white = Color.white;
        Color gray = new Color(216, 232, 240);
        Color accent = new Color(81, 115, 248);
        Color accentHovered = new Color(182, 146, 248);
        Color secondary = new Color(145, 209, 241);

        contentPane.setBackground(Color.white);
        panelContatti.setBackground(Color.white);
        panelCorse.setBackground(Color.white);
        panel1.setBackground(Color.white);
        panelInfoCorse.setBackground(Color.white);
        panelNatanti.setBackground(Color.white);
        panel2.setBackground(Color.white);
        panelInfoNatanti.setBackground(Color.white);
        panelLogout.setBackground(Color.white);
        panelL.setBackground(Color.white);

        ImageIcon imageAncora = new ImageIcon("resources/icons/icons8-anchor-48.png");
        ImageIcon imageNave = new ImageIcon("resources/icons/icons8-nave-48.png");
        ImageIcon imageLogout = new ImageIcon("resources/icons/icons8-ritorno-48.png");
        ImageIcon imageLogoutHovered = new ImageIcon("resources/icons/icons8-ritorno-48-hovered.png");
        ImageIcon imageContatti = new ImageIcon("resources/icons/icons8-informazioni-48.png");
        ImageIcon imageContattiHovered = new ImageIcon("resources/icons/icons8-informazioni-48-hovered.png");

        corseErogateLabel.setIcon(imageAncora);
        corseErogateLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        natantiDisponibiliLabel.setIcon(imageNave);
        natantiDisponibiliLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        labelButtonLogout.setIcon(imageLogout);
        labelContatti.setIcon(imageContatti);
        labelContatti.setHorizontalTextPosition(SwingConstants.LEFT);

        bRimuoviNatante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idxNatante = tableNatanti.getSelectedRow();
                if (idxNatante == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona il natante da eliminare");
                    return;
                }

                String nat = (String) tableNatanti.getValueAt(idxNatante, 0);
                controllerCompagnia.rimuoviNatante(nat);
                aggiornaTabellaNatanti();
                aggiornaTabellaCorse();
            }
        });
        bAggiungiNatante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AggiungiNatante aggiungiNatante = new AggiungiNatante(frame, controllerCompagnia);
                frame.setVisible(false);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                aggiornaTabellaNatanti();
                aggiornaTabellaCorse();
            }
        });
        bCreaCorsa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreaCorse creaCorse = new CreaCorse(frame, controllerCompagnia);
                frame.setVisible(false);
            }
        });
        bModificaCorsa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModificaCorsaSpecifica modificaCorsaSpecifica = new ModificaCorsaSpecifica(frame, controllerCompagnia);
                frame.setVisible(false);
            }
        });

        buttonEliminaCorsa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableCorse.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona la corsa da eliminare");
                    return;
                }

                int idCorsaDaEliminare = (int) tableCorse.getValueAt(selectedRow, 0);
                controllerCompagnia.eliminaCorsaRegolare(idCorsaDaEliminare);
                aggiornaTabellaCorse();
            }
        });

        buttonModificaCorsaRegolare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableCorse.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona una corsa");
                    return;
                }

                int idCorsa = (int) tableCorse.getValueAt(selectedRow, 0);

                ModificaCorsaRegolare modificaCorsaRegolare = new ModificaCorsaRegolare(frame, controllerCompagnia, idCorsa);
                frame.setVisible(false);
            }
        });

        buttonIncassi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableCorse.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona una corsa");
                    return;
                }

                int idCorsa = (int) tableCorse.getValueAt(selectedRow, 0);
                IncassiCorsa incassiCorsa = new IncassiCorsa(frame, controllerCompagnia, idCorsa);
                frame.setVisible(false);
            }
        });

        buttonInfoNatante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableNatanti.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona un natante");
                    return;
                }

                String nomeNatante = (String) tableNatanti.getValueAt(selectedRow, 0);
                InfoNatante infoNatante = new InfoNatante(frame, controllerCompagnia, nomeNatante);
                frame.setVisible(false);
            }
        });

        labelContatti.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                GestisciContatti gestisciContatti = new GestisciContatti(frame, controllerCompagnia);
                frame.setVisible(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelContatti.setIcon(imageContattiHovered);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                labelContatti.setIcon(imageContatti);
            }
        });

        labelButtonLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                HomeMain homeMain = new HomeMain();
                frame.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelButtonLogout.setIcon(imageLogoutHovered);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                labelButtonLogout.setIcon(imageLogout);
            }
        });
    }

    private void aggiornaTabellaCorse() {
        DefaultTableModel modelCorse;
        String[] col;
        Object[][] data;

        ArrayList<Integer> idCorsa = new ArrayList<Integer>();
        ArrayList<String> portoPartenza = new ArrayList<String>();
        ArrayList<String> portoArrivo = new ArrayList<String>();
        ArrayList<String> natante = new ArrayList<String>();
        ArrayList<LocalTime> orarioPartenza = new ArrayList<LocalTime>();
        ArrayList<LocalTime> orarioArrivo = new ArrayList<LocalTime>();

        controllerCompagnia.visualizzaCorseRegolari(idCorsa, portoPartenza, portoArrivo, natante, orarioPartenza, orarioArrivo);

        col = new String[]{"ID", "Partenza", "Orario", "Arrivo", "Orario", "Natante"};
        data = new Object[idCorsa.size()][6];
        for (int i = 0; i < idCorsa.size(); i++) {
            data[i][0] = idCorsa.get(i);
            data[i][1] = portoPartenza.get(i);
            data[i][2] = orarioPartenza.get(i);
            data[i][3] = portoArrivo.get(i);
            data[i][4] = orarioArrivo.get(i);
            data[i][5] = natante.get(i);
        }
        modelCorse = new DefaultTableModel(data, col) {
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        TableRowSorter<DefaultTableModel> sorterCorse = new TableRowSorter<>(modelCorse);

        tableCorse = new JTable(modelCorse);
        tableCorse.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableCorse.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableCorse.setDefaultRenderer(Object.class, centerRenderer);
        tableCorse.setRowSorter(sorterCorse);
        sorterCorse.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        ListSelectionModel selectionModel = tableCorse.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneCorse.setViewportView(tableCorse);
    }

    private void aggiornaTabellaNatanti() {
        DefaultTableModel modelNatanti;
        String[] col;
        Object[][] data;

        ArrayList<String> nome = new ArrayList<String>();
        ArrayList<Integer> capPasseggeri = new ArrayList<Integer>();
        ArrayList<Integer> capVeicoli = new ArrayList<Integer>();
        ArrayList<String> tipo = new ArrayList<String>();

        controllerCompagnia.visualizzaNatanti(nome, capPasseggeri, capVeicoli, tipo);

        col = new String[]{"Nome", "Tipo", "Passeggeri", "Veicoli"};
        data = new Object[nome.size()][4];
        for (int i = 0; i < nome.size(); i++) {
            data[i][0] = nome.get(i);
            data[i][1] = tipo.get(i);
            data[i][2] = capPasseggeri.get(i);
            data[i][3] = capVeicoli.get(i);
        }

        modelNatanti = new DefaultTableModel(data, col) {
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2 || columnIndex == 3) {
                    return Integer.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        TableRowSorter<DefaultTableModel> sorterNatanti = new TableRowSorter<>(modelNatanti);

        tableNatanti = new JTable(modelNatanti);
        tableNatanti.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableNatanti.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableNatanti.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableNatanti.setDefaultRenderer(Object.class, centerRenderer);
        tableNatanti.setRowSorter(sorterNatanti);
        sorterNatanti.setSortKeys(List.of(new RowSorter.SortKey(1, SortOrder.ASCENDING))); //ordina per tipo
        ListSelectionModel selectionModel = tableNatanti.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneNatanti.setViewportView(tableNatanti);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("");
        contentPane.add(label1, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("");
        contentPane.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("");
        contentPane.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("");
        contentPane.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelCorse = new JPanel();
        panelCorse.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panelCorse, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scrollPaneCorse = new JScrollPane();
        panelCorse.add(scrollPaneCorse, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelCorse.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bCreaCorsa = new JButton();
        bCreaCorsa.setText("Crea Corsa ");
        panel1.add(bCreaCorsa, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonEliminaCorsa = new JButton();
        buttonEliminaCorsa.setText("Elimina Corsa");
        panel1.add(buttonEliminaCorsa, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        corseErogateLabel = new JLabel();
        Font corseErogateLabelFont = this.$$$getFont$$$(null, -1, 20, corseErogateLabel.getFont());
        if (corseErogateLabelFont != null) corseErogateLabel.setFont(corseErogateLabelFont);
        corseErogateLabel.setText("Corse Erogate");
        panelCorse.add(corseErogateLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelInfoCorse = new JPanel();
        panelInfoCorse.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelCorse.add(panelInfoCorse, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bModificaCorsa = new JButton();
        bModificaCorsa.setText("Modifica Corsa specifica");
        panelInfoCorse.add(bModificaCorsa, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(180, -1), new Dimension(180, -1), null, 0, false));
        buttonModificaCorsaRegolare = new JButton();
        buttonModificaCorsaRegolare.setText("Modifica Corsa regolare");
        panelInfoCorse.add(buttonModificaCorsaRegolare, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(180, -1), new Dimension(180, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panelInfoCorse.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        buttonIncassi = new JButton();
        buttonIncassi.setText("Incassi Corsa");
        panelInfoCorse.add(buttonIncassi, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(180, -1), new Dimension(180, -1), null, 0, false));
        panelNatanti = new JPanel();
        panelNatanti.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panelNatanti, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scrollPaneNatanti = new JScrollPane();
        panelNatanti.add(scrollPaneNatanti, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelNatanti.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bAggiungiNatante = new JButton();
        bAggiungiNatante.setText("Aggiungi Natante");
        panel2.add(bAggiungiNatante, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bRimuoviNatante = new JButton();
        bRimuoviNatante.setText("Rimuovi Natante");
        panel2.add(bRimuoviNatante, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        natantiDisponibiliLabel = new JLabel();
        Font natantiDisponibiliLabelFont = this.$$$getFont$$$(null, -1, 20, natantiDisponibiliLabel.getFont());
        if (natantiDisponibiliLabelFont != null) natantiDisponibiliLabel.setFont(natantiDisponibiliLabelFont);
        natantiDisponibiliLabel.setText("Natanti Disponibili");
        panelNatanti.add(natantiDisponibiliLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelInfoNatanti = new JPanel();
        panelInfoNatanti.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelNatanti.add(panelInfoNatanti, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonInfoNatante = new JButton();
        buttonInfoNatante.setText("Info Natante");
        panelInfoNatanti.add(buttonInfoNatante, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(180, -1), new Dimension(180, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panelInfoNatanti.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        panelContatti = new JPanel();
        panelContatti.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panelContatti, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panelContatti.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        labelContatti = new JLabel();
        labelContatti.setText("Contatti");
        panelContatti.add(labelContatti, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelHome = new JLabel();
        Font labelHomeFont = this.$$$getFont$$$(null, -1, 22, labelHome.getFont());
        if (labelHomeFont != null) labelHome.setFont(labelHomeFont);
        labelHome.setText("Home");
        contentPane.add(labelHome, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelLogout = new JPanel();
        panelLogout.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panelLogout, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelL = new JPanel();
        panelL.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelLogout.add(panelL, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelButtonLogout = new JLabel();
        labelButtonLogout.setText("");
        panelL.add(labelButtonLogout, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelLogout = new JLabel();
        labelLogout.setText("Esci");
        panelL.add(labelLogout, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 17), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panelLogout.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
