package algorithms.algorithmsparameters;

import javax.swing.*;
import java.awt.*;
public abstract class AlgorithmArgumentsPanel extends JPanel implements AlgorithmArguments {

    private JDialog dialog;

    protected JButton okButton;

    public AlgorithmArgumentsPanel(){

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        okButton = new JButton("Save");
        okButton.addActionListener(ev -> dialog.setVisible(false));
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    public void collectArguments(){

        if (dialog == null){
            dialog = new JDialog();

            // Here we add panel to dialog window
            dialog.add(this);
            // Default button is automatically pushed when user
            // clicks trigger key (usually Enter)
            dialog.pack();
        }
        dialog.setTitle("Customize Algorithm");
        dialog.setVisible(true);
    }
}
