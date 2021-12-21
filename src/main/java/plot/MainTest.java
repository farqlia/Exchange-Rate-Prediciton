package plot;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.Point;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainTest extends JFrame {

    Plot<LocalDate> plot;
    List<Point<LocalDate>> data;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainTest::new);
    }

    public MainTest() {

        setSize(1191, 800);
        setResizable(false);

        JPanel thePanel = new JPanel();

        DataGenerator g = new DataGenerator();

        data = g.generateDataWithTrend(100, new BigDecimal("-1"), new BigDecimal("2"));

        plot = new Plot<>();

        debugData(data);

        add(plot);

        updatePlot();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

    }

    public void updatePlot(){
        SwingUtilities.invokeLater(() -> plot.plotData(data));
    }

    public void debugData(List<Point<LocalDate>> data){

        for (int i = 0; i < data.size(); i++){
            System.out.println(i + ": " + data.get(i).getY());
        }

    }

}
