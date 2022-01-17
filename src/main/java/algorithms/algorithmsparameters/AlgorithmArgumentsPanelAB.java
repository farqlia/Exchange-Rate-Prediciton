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

public class AlgorithmArgumentsPanelAB extends AlgorithmArgumentsPanel{

    private final Map<Names, Number> params;
    private final JTextField fieldAlpha;
    private final JTextField fieldBeta;
    private final BigDecimal default_ = new BigDecimal("0.4");

    @Override
    public Map<Names, Number> getMap() {
        params.computeIfAbsent(Names.BETA, o -> default_);
        params.computeIfAbsent(Names.ALPHA, o -> default_);
        return params;
    }

    public AlgorithmArgumentsPanelAB(){

        params = new EnumMap<>(Names.class);

        fieldAlpha = new JTextField();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        panel.add(new JLabel("Alpha Parameter"));
        panel.add(fieldAlpha);

        fieldBeta = new JTextField();
        panel.add(new JLabel("Beta Parameter"));
        panel.add(fieldBeta);

        okButton.addActionListener(new AlgorithmArgumentsPanelAB.HandleParametersMap());

        add(panel, BorderLayout.CENTER);

    }

    private class HandleParametersMap implements ActionListener{

        private final Pattern decimalPattern = Pattern.compile("0.\\d+");

        @Override
        public void actionPerformed(ActionEvent e) {
            mapParam(Names.ALPHA, fieldAlpha);
            mapParam(Names.BETA, fieldBeta);
            System.out.println(params);
        }
        private void mapParam(Names param, JTextField text){
            Matcher matcher = decimalPattern.matcher(text.getText().trim());
            if (matcher.matches()){
                params.put(param, new BigDecimal(matcher.group()));
            }
            else {
                params.put(param, default_);
                JOptionPane.showMessageDialog(AlgorithmArgumentsPanelAB.this,
                        "Enter values from range (0.0, 1.0)", "Invalid value", JOptionPane.ERROR_MESSAGE);
            }
            text.setText("");
        }
    }
}
