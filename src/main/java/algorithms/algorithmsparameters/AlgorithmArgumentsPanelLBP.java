package algorithms.algorithmsparameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;

public class AlgorithmArgumentsPanelLBP extends AlgorithmArgumentsPanel {

    private Map<Names, Number> params;
    private final JSlider lookBackPeriodSlider;
    int default_ = 5;

    @Override
    public Map<Names, Number> getMap() {
        params.computeIfAbsent(Names.LOOK_BACK_PERIOD, o -> default_);
        return params;
    }

    public AlgorithmArgumentsPanelLBP() {

        params = new EnumMap<>(Names.class);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(new JLabel("Lookback Period"));

        lookBackPeriodSlider = new JSlider(5, 15, 5);
        lookBackPeriodSlider.setPaintTicks(true);
        lookBackPeriodSlider.setPaintLabels(true);
        lookBackPeriodSlider.setMinorTickSpacing(1);
        lookBackPeriodSlider.setMajorTickSpacing(5);

        panel.add(lookBackPeriodSlider);

        okButton.addActionListener(new HandleParametersMap());

        add(panel, BorderLayout.CENTER);

    }

    private class HandleParametersMap implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            params.put(Names.LOOK_BACK_PERIOD, lookBackPeriodSlider.getValue());
            System.out.println(params);
        }
    }
}
