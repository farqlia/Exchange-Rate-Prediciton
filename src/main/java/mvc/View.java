package mvc;

import algorithms.algorithmsinitializer.AlgorithmInitializer;
import algorithms.algorithmsinitializer.AlgorithmInitializerExAnte;
import algorithms.algorithmsinitializer.AlgorithmInitializerExPost;
import exchangerateclass.CurrencyName;
import view.view.other.Menu;
import view.view.AbstractView;
import view.view.ViewEvent;
import view.view.ViewEventType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private final JComboBox<AlgorithmInitializer> nameOfAlgorithmsExPostComboBox;
    private final JComboBox<AlgorithmInitializer> nameOfAlgorithmsExAnteComboBox;
    private final CustomComboBox customComboBox;
    private final JButton customizeAlgorithmButton;
    private JMenuBar menuBar;
    private JButton predictExPostButton;
    private JButton predictExAnteButton;
    private HandleButtonListenerExPost buttonListener;
    private Font font = new Font("Roboto", Font.BOLD, 15);

    public View(java.util.List<CurrencyName> currencyNames, JTable ... tables){

        setSize(900, 600);

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
        calendar.add(Calendar.MONTH, -6);
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

        ChangeListener correctDateValues = new HandleCorrectDateValues();
        // Make sure that the upper date (end date) is always bigger than lower date (start date)
        endDateSpinner.addChangeListener(correctDateValues);
        startDateSpinner.addChangeListener(correctDateValues);

        nameOfAlgorithmsExPostComboBox = new JComboBox<>(AlgorithmInitializerExPost.values());
        addComp(leftPanel, new JLabel("Choose Algorithm"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, nameOfAlgorithmsExPostComboBox, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        y++;
        customizeAlgorithmButton = new JButton("Customize Algorithm");
        addComp(leftPanel, customizeAlgorithmButton, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        customizeAlgorithmButton
                .addActionListener(ev -> ((AlgorithmInitializer) nameOfAlgorithmsExPostComboBox.getSelectedItem()).getAlgorithmArguments().collectArguments());

        predictExPostButton = new JButton("Predict Past");
        addComp(leftPanel, predictExPostButton, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        buttonListener = new HandleButtonListenerExPost();
        predictExPostButton.addActionListener(buttonListener);

        nameOfAlgorithmsExAnteComboBox = new JComboBox<>(AlgorithmInitializerExAnte.values());
        addComp(leftPanel, new JLabel("Choose Algorithm"), 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        addComp(leftPanel, nameOfAlgorithmsExAnteComboBox, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        predictExAnteButton = new JButton("Predict Future");
        predictExAnteButton.addActionListener(new HandleButtonListenerExAnte());

        addComp(leftPanel, predictExAnteButton, 0, y++, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);

        // ----------- CREATE RIGHT PANEL ------------------


        JTabbedPane tabbedPane = new JTabbedPane();

        for (JTable table : tables){
            table.setFont(font);
            table.setRowHeight(20);
            JScrollPane scroller = new JScrollPane(table,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            tabbedPane.addTab(table.toString(), scroller);
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
        predictExPostButton.setEnabled(false);
        predictExAnteButton.setEnabled(false);
        customizeAlgorithmButton.setEnabled(false);
        for (MenuElement el : menuBar.getSubElements()){
            el.getComponent().setEnabled(false);
        }
    }

    @Override
    public void enableActions() {
        predictExPostButton.setEnabled(true);
        predictExAnteButton.setEnabled(true);
        customizeAlgorithmButton.setEnabled(true);
        for (MenuElement el : menuBar.getSubElements()){
            el.getComponent().setEnabled(true);
        }
    }

    private class HandleCorrectDateValues implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e) {
            // Make sure that the upper date (end date) is always bigger than lower date (start date)
            SpinnerDateModel model = (SpinnerDateModel)endDateSpinner.getModel();
            Date currLow = (Date)startDateSpinner.getValue();
            model.setStart(currLow);
            if (currLow.compareTo((Date)endDateSpinner.getValue()) >= 0){
                model.setValue(currLow);
            }
            model.setStart(currLow);
        }
    }

    public class HandleButtonListenerExAnte implements ActionListener{

        private ViewEvent event;

        @Override
        public void actionPerformed(ActionEvent e) {

            Date sD = (Date) endDateSpinner.getValue();
            CurrencyName cN = (CurrencyName)customComboBox.currencyNamesComboBox.getSelectedItem();
            AlgorithmInitializer algorithmInitializer = (AlgorithmInitializer) nameOfAlgorithmsExAnteComboBox.getSelectedItem();

            event = new ViewEvent(LocalDate.ofInstant(sD.toInstant(), ZoneId.of("CET")),
                    LocalDate.now(),
                    algorithmInitializer,
                    cN.getCode());

            notifyObservers(ViewEventType.EX_ANTE, event);
        }
    }

    public class HandleButtonListenerExPost implements ActionListener{

        private ViewEvent event;

        @Override
        public void actionPerformed(ActionEvent e) {
            Date sD = (Date) startDateSpinner.getValue();
            Date eD = (Date) endDateSpinner.getValue();
            CurrencyName cN = (CurrencyName)customComboBox.currencyNamesComboBox.getSelectedItem();
            AlgorithmInitializer algorithmInitializer = (AlgorithmInitializer) nameOfAlgorithmsExPostComboBox.getSelectedItem();

            event = new ViewEvent(LocalDate.ofInstant(sD.toInstant(), ZoneId.of("CET")),
                    LocalDate.ofInstant(eD.toInstant(), ZoneId.of("CET")),
                    algorithmInitializer,
                    cN.getCode());

            notifyObservers(ViewEventType.EX_POST, event);
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
