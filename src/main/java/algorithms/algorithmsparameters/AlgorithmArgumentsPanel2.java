package algorithms.algorithmsparameters;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import view.view.View;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlgorithmArgumentsPanel2 extends AlgorithmArgumentsPanel{

    private Map<Names, Number> params;
    private JTextField field;

    public AlgorithmArgumentsPanel2(){

        params = new EnumMap<>(Names.class);

        field = new JTextField();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(new JLabel("Alpha Parameter"));
        panel.add(field);

        okButton.addActionListener(new HandleParametersMap());

        add(panel, BorderLayout.CENTER);

    }

    @Override
    public Map<Names, Number> getMap() {
        return params;
    }

    private class HandleParametersMap implements ActionListener {

        private final Pattern decimalPattern = Pattern.compile("0.[56789]\\d*");
        private final BigDecimal default_ = new BigDecimal("0.7");

        @Override
        public void actionPerformed(ActionEvent e) {
            Matcher matcher = decimalPattern.matcher(field.getText().trim());
            if (matcher.matches()){
                params.put(Names.ALPHA, new BigDecimal(matcher.group()));
            }
            else {
                params.put(Names.ALPHA, default_);
                JOptionPane.showMessageDialog(AlgorithmArgumentsPanel2.this,
                        "Enter values from range (0.7, 1.0)", "Invalid value", JOptionPane.ERROR_MESSAGE);
            }
            field.setText("");
            System.out.println(params);
        }
    }
}
