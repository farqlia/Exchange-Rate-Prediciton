package view.view;

import algorithms.AlgorithmInitializer;
import exchangerateclass.CurrencyName;
import model.CustomTableModel;
import model.ResultsTableModel;
import model.StatisticsTableModel;
import view.other.Menu;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class View extends AbstractView {

    private final JSpinner startDateSpinner;
    private final JSpinner endDateSpinner;
    private final JComboBox<AlgorithmInitializer> nameOfAlgorithmsComboBox;
    private final CustomComboBox customComboBox;
    private final JButton customizeAlgorithmButton;
    private CustomTableModel<ResultsTableModel.Row> modelA;
    private CustomTableModel<StatisticsTableModel.Row> modelS;
    JTable tableA;
    private JMenuBar menuBar;
    private JButton predictButton;
    Font font = new Font("Roboto", Font.BOLD, 15);

    public View(java.util.List<CurrencyName> currencyNames, CustomTableModel<ResultsTableModel.Row> modelA,
                CustomTableModel<StatisticsTableModel.Row> modelS){

        setSize(900, 600);

        this.modelA = modelA;
        this.modelS = modelS;

        menuBar = new Menu();
        setJMenuBar(menuBar);

        setLayout(null);
        JPanel mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(new GridBagLayout());

        // ----------- CREATE LEFT PANEL ------------------

        JPanel leftPanel = new JPanel(new GridBagLayout());

        // Goes back in time
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        Date startDate = calendar.getTime();
        Date todayDate = new Date();
        Date tenDaysEarlier = new Date(ChronoUnit.DAYS.addTo(todayDate.toInstant(), -10).toEpochMilli());

        int y = 0;

        customComboBox = new CustomComboBox(currencyNames);
        addComp(leftPanel, new JLabel("Choose Currency"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, customComboBox, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        startDateSpinner = new JSpinner(new SpinnerDateModel(tenDaysEarlier,
                startDate, tenDaysEarlier, Calendar.DAY_OF_MONTH));
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "dd-MM-yyyy"));
        addComp(leftPanel, new JLabel("Start Date"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, startDateSpinner, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        y++;

        endDateSpinner = new JSpinner(new SpinnerDateModel(todayDate,
                startDate, todayDate, Calendar.DAY_OF_MONTH));
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "dd-MM-yyyy"));
        addComp(leftPanel, new JLabel("End Date"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, endDateSpinner, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);


        nameOfAlgorithmsComboBox = new JComboBox<>(AlgorithmInitializer.values());
        addComp(leftPanel, new JLabel("Choose Algorithm"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, nameOfAlgorithmsComboBox, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        y++;
        customizeAlgorithmButton = new JButton("Customize Algorithm");
        addComp(leftPanel, customizeAlgorithmButton, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        customizeAlgorithmButton
                .addActionListener(ev -> ((AlgorithmInitializer) nameOfAlgorithmsComboBox.getSelectedItem()).getAlgorithmArguments().collectArguments());

        predictButton = new JButton("Predict");
        addComp(leftPanel, predictButton, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        predictButton.addActionListener(new HandleButtonListener());



        // ----------- CREATE RIGHT PANEL ------------------


        JTabbedPane tabbedPane = new JTabbedPane();

        tableA = new JTable(modelA);

        // When user clicks on this menu option, we set rendering for table
        menuBar.addPropertyChangeListener(Menu.SET_RENDERING, ev ->{
                    tableA.setDefaultRenderer(BigDecimal.class, new NumberRenderer((BigDecimal)
                            modelS.getValueAt(2, 1))); // Pass in MAPE from statistics table
                    tableA.repaint();
                }
        );

        predictButton.addActionListener(ev -> tableA.setDefaultRenderer(BigDecimal.class, null));

        tableA.setFont(font);
        tableA.setRowHeight(20);
        JScrollPane scroller = new JScrollPane(tableA,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab(modelA.toString(), scroller);

        JTable tableS = new JTable(modelS);
        tableS.setFont(font);
        tableS.setRowHeight(20);
        JScrollPane scroller2 = new JScrollPane(tableS,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab(modelS.toString(), scroller2);


        addComp(mainPanel, leftPanel, 0, 0, 1,1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
        addComp(mainPanel, tabbedPane, 1, 0, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void addComp(JPanel thePanel, JComponent comp, int xPos, int yPos, int compWidth, int compHeight, int place, int stretch){

        GridBagConstraints gridConstraints = new GridBagConstraints();

        gridConstraints.gridx = xPos;
        gridConstraints.gridy = yPos;
        gridConstraints.gridwidth = compWidth;
        gridConstraints.gridheight = compHeight;
        gridConstraints.weightx = 100;
        gridConstraints.weighty = 100;
        gridConstraints.insets = new Insets(5, 5, 5, 5);
        gridConstraints.anchor = place;
        gridConstraints.fill = stretch;

        thePanel.add(comp, gridConstraints);
    }

    @Override
    public void disableActions() {
        predictButton.setEnabled(false);
        customizeAlgorithmButton.setEnabled(false);
    }

    @Override
    public void enableActions() {
        predictButton.setEnabled(true);
        customizeAlgorithmButton.setEnabled(true);
    }

    public class HandleButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            Date sD = (Date) startDateSpinner.getValue();
            Date eD = (Date) endDateSpinner.getValue();
            CurrencyName cN = (CurrencyName)customComboBox.currencyNamesComboBox.getSelectedItem();
            AlgorithmInitializer algorithmInitializer = (AlgorithmInitializer) nameOfAlgorithmsComboBox.getSelectedItem();

            ViewEvent event = new ViewEvent(LocalDate.ofInstant(sD.toInstant(), ZoneId.of("CET")),
                    LocalDate.ofInstant(eD.toInstant(), ZoneId.of("CET")),
                    algorithmInitializer,
                    cN.getCode());

            tableA.setDefaultRenderer(BigDecimal.class, null);
            modelA.deleteRows();
            modelS.deleteRows();

            notifyObservers(event);
        }
    }

    private static class CustomComboBox extends JPanel{
        JComboBox<CurrencyName> currencyNamesComboBox;

        CustomComboBox(List<CurrencyName> currencyNamesList){
            currencyNamesComboBox = new JComboBox<>(new Vector<>(currencyNamesList));
            ComboBoxRenderer renderer = new ComboBoxRenderer();
            currencyNamesComboBox.setRenderer(renderer);
            add(currencyNamesComboBox);

        }
    }

    private static class ComboBoxRenderer extends JLabel implements ListCellRenderer<CurrencyName>{

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends CurrencyName> list, CurrencyName value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected){
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setText(String.format("%s, (%s)", value.getCode(), value.getCurrency()));
            return this;
        }
    }

    public class NumberRenderer extends JLabel implements TableCellRenderer {

        int RGB = 255;
        BigDecimal a = new BigDecimal(100);
        BigDecimal averagePercError;
        BigDecimal rangeOfValues;
        Color[] colors;

        public NumberRenderer(BigDecimal averagePercError){
            rangeOfValues = findMax(); // For now, it is equal to the biggest value
            this.averagePercError = averagePercError;
            colors = new Color[modelA.getRowCount()];
            // Computes color for a row based on column representing percentage error
            computeColors();
            setOpaque(true);

        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setBackground(colors[row]);
            setFont(font);
            setText(value.toString());
            return this;
        }


        private void computeColors(){
            for (int row = 0; row < modelA.getRowCount(); row++){

                BigDecimal percentageError = modelA.getRow(row).getPercentageError();
                BigDecimal offset = percentageError.subtract(averagePercError).divide(rangeOfValues,
                        RoundingMode.HALF_UP).abs();
                int x = RGB - offset.multiply(a).intValue();
                Color c = Color.WHITE;
                if (percentageError.compareTo(averagePercError) > 0){
                    c = new Color(RGB, x, x);
                } else if (percentageError.compareTo(averagePercError) < 0){
                    c = new Color(x, RGB, x);
                }

                colors[row] = c;
            }
        }

        private BigDecimal findMax(){

            return modelA.getListOfRows().stream()
                    .map(ResultsTableModel.Row::getPercentageError)
                    .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        }



    }

}
