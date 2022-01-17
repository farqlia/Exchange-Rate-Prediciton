package algorithms.algorithmsparameters;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Map;

public class AlgorithmArgumentsPanelDefault extends AlgorithmArgumentsPanel{

    public AlgorithmArgumentsPanelDefault(){

        JPanel thePanel = new JPanel(new BorderLayout());
        thePanel.add(new JLabel("No Customization Needed"), BorderLayout.CENTER);

        okButton.setText("OK");

        add(thePanel, BorderLayout.CENTER);

    }

    @Override
    public Map<Names, Number> getMap() {
        return Collections.emptyMap();
    }
}
