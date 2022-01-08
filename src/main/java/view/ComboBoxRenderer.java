package view;

import exchangerateclass.CurrencyName;

import javax.swing.*;
import java.awt.*;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer<CurrencyName>{

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
