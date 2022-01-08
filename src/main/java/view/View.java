package view;

import algorithms.AlgorithmNames;
import exchangerateclass.CurrencyName;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class View extends AbstractView {

    Object[][] data;
    String[] columnNames = {"Date", "Actual", "Forecasted"};
    // Just for the time of testing
    public DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
    private final Calendar calendar = Calendar.getInstance();

    private JSpinner startDateSpinner, endDateSpinner;
    private JComboBox<AlgorithmNames> nameOfAlgorithmsComboBox;
    private java.util.List<CurrencyName> currencyNames;
    private JComboBox<CurrencyName> currencyNamesJComboBox;

    public View(){

        setSize(1000, 1000);

        setLayout(null);
        JPanel mainPanel = new JPanel();
        setContentPane(mainPanel);
        mainPanel.setLayout(new GridBagLayout());

        JPanel leftPanel = new JPanel(new GridBagLayout());


        // Goes back in time
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date startDate = calendar.getTime();
        Date todayDate = new Date();
        Date yesterdayDate = new Date(ChronoUnit.DAYS.addTo(todayDate.toInstant(), -1).toEpochMilli());

        int y = 0;

        startDateSpinner = new JSpinner(new SpinnerDateModel(yesterdayDate,
                startDate, yesterdayDate, Calendar.DAY_OF_MONTH));
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "dd-MM-yyyy"));
        addComp(leftPanel, new JLabel("Start Date"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, startDateSpinner, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        y++;

        endDateSpinner = new JSpinner(new SpinnerDateModel(todayDate,
                startDate, todayDate, Calendar.DAY_OF_MONTH));
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "dd-MM-yyyy"));
        addComp(leftPanel, new JLabel("End Date"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, endDateSpinner, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);


        nameOfAlgorithmsComboBox = new JComboBox<>(AlgorithmNames.values());
        addComp(leftPanel, new JLabel("Choose Algorithm"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, nameOfAlgorithmsComboBox, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        JPanel rightPanel = new JPanel();

        JTable table = new JTable(tableModel);

        JScrollPane scroller = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        rightPanel.add(scroller);

        //getContentPane().add(leftPanel, BorderLayout.WEST);
        //getContentPane().add(rightPanel, BorderLayout.EAST);
        addComp(mainPanel, leftPanel, 0, 0, 1,1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE);
        addComp(mainPanel, rightPanel, 1, 0, 1,1, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void updateTable(Object[][] data) {
        for (Object[] row : data){
            tableModel.addRow(row);
        }
    }

    @Override
    public void updateTable(Object[][] data, Object[] columnNames) {

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

    private class ComboBoxRenderer extends JLabel implements ListCellRenderer<CurrencyName>{

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
            return null;
        }
    }
}
