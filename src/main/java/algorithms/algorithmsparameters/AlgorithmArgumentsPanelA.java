package algorithms.algorithmsparameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlgorithmArgumentsPanelA extends AlgorithmArgumentsPanel{

    private final Map<Names, Number> params;
    private final JTextField field;
    private final BigDecimal default_ = new BigDecimal("0.7");

    @Override
    public Map<Names, Number> getMap() {
        params.computeIfAbsent(Names.ALPHA, o -> default_);
        return params;
    }

    public AlgorithmArgumentsPanelA(){

        params = new EnumMap<>(Names.class);

        field = new JTextField();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(new JLabel("Alpha Parameter"));
        panel.add(field);

        okButton.addActionListener(new HandleParametersMap());

        add(panel, BorderLayout.CENTER);

    }



    private class HandleParametersMap implements ActionListener {

        private final Pattern decimalPattern = Pattern.compile("0.[56789]\\d*");

        @Override
        public void actionPerformed(ActionEvent e) {
            Matcher matcher = decimalPattern.matcher(field.getText().trim());
            if (matcher.matches()){
                params.put(Names.ALPHA, new BigDecimal(matcher.group()));
            }
            else {
                params.put(Names.ALPHA, default_);
                JOptionPane.showMessageDialog(AlgorithmArgumentsPanelA.this,
                        "Enter values from range (0.7, 1.0)", "Invalid value", JOptionPane.ERROR_MESSAGE);
            }
            field.setText("");
            System.out.println(params);
        }
    }
}
