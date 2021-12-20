package studygraphics;

import javax.swing.*;
import java.awt.*;

public class MainTestClassForTestDisplay extends JFrame {

    public static void main(String[] args) {
        new MainTestClassForTestDisplay();
    }

    public MainTestClassForTestDisplay(){

        setSize(500, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new TestClassForTextDisplay());

        setVisible(true);

    }

}
