package studygraphics;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import java.awt.*;
import java.text.AttributedCharacterIterator;

public class TestClassForTextDisplay extends JComponent {

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        g2D.drawString("A string", 0, 400);
        g2D.drawString("A 2nd string", 100, 400);
        g2D.drawString("A 2nd string", 200, 400);

    }

}
