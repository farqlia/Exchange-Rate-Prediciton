package plot;

import datagenerator.DataGenerator;

import javax.swing.*;
import datasciencealgorithms.utils.Point;
import studygraphics.PlottingBackground;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainFrame extends JFrame {

    public static void main(String[] args) {
        new MainFrame();
    }

    public MainFrame() {

        setSize(800, 600);

        JPanel thePanel = new JPanel();

        DataGenerator g = new DataGenerator();

        List<Point<LocalDate>> data = g.generateDataWithTrend(100, new BigDecimal("10"), new BigDecimal("2"), 5);

        JPanel comp = new PlottingBackground<>(data);

        add(comp);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

    }

}
