package view.view;

import algorithms.AlgorithmName;
import exchangerateclass.CurrencyName;
import view.other.Menu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class View extends AbstractView {

    private final JSpinner startDateSpinner;
    private final JSpinner endDateSpinner;
    private final JComboBox<AlgorithmName> nameOfAlgorithmsComboBox;
    private final CustomComboBox customComboBox;
    private final JButton customizeAlgorithmButton;
    private DefaultTableModel[] tableModels;
    private JMenuBar menuBar;
    private JButton predictButton;

    public View(java.util.List<CurrencyName> currencyNames, DefaultTableModel ... tableModels){

        setSize(900, 600);

        this.tableModels = tableModels;

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


        nameOfAlgorithmsComboBox = new JComboBox<>(AlgorithmName.values());
        addComp(leftPanel, new JLabel("Choose Algorithm"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, nameOfAlgorithmsComboBox, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        y++;
        customizeAlgorithmButton = new JButton("Customize Algorithm");
        addComp(leftPanel, customizeAlgorithmButton, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        customizeAlgorithmButton
                .addActionListener(ev -> ((AlgorithmName) nameOfAlgorithmsComboBox.getSelectedItem()).getAlgorithmArguments().collectArguments());

        predictButton = new JButton("Predict");
        addComp(leftPanel, predictButton, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        predictButton.addActionListener(new HandleButtonListener());

        // ----------- CREATE RIGHT PANEL ------------------

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(new Color(104, 103, 103));

        for (DefaultTableModel tableModel : tableModels){
            JTable table = new JTable(tableModel);
            table.setFont(new Font("Roboto", Font.BOLD, 15));
            table.setRowHeight(20);
            JScrollPane scroller = new JScrollPane(table,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            tabbedPane.addTab(tableModel.toString(), scroller);
        }

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
            AlgorithmName algorithmName = (AlgorithmName) nameOfAlgorithmsComboBox.getSelectedItem();

            ViewEvent event = new ViewEvent(LocalDate.ofInstant(sD.toInstant(), ZoneId.of("CET")),
                    LocalDate.ofInstant(eD.toInstant(), ZoneId.of("CET")),
                    algorithmName,
                    cN.getCode());

            for (DefaultTableModel tM : tableModels){
               deleteRows(tM);
            }

            notifyObservers(event);
        }
    }



    private void deleteRows(DefaultTableModel model){
        for (int i = model.getRowCount() - 1; i >= 0; i--){
            model.removeRow(i);
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

}
