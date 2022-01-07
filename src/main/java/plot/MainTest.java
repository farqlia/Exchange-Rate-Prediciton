package plot;

import datagenerator.DataGenerator;
import datasciencealgorithms.utils.point.Point;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainTest extends JFrame {

    Plot<LocalDate> plotDemo;
    List<Point<LocalDate>> data;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainTest::new);
    }

    public MainTest() {

        setSize(1191, 800);
        setResizable(false);

        data = DataGenerator.getInstance().generateDataWithTrend(50, new BigDecimal("-1"), new BigDecimal("2"));
        plotDemo = new TimeSeriesPlot(new BigDecimal("-10"), new BigDecimal("250"),
                LocalDate.now().minusDays(60), LocalDate.now());

        debugData(data);

        this.getContentPane().add(plotDemo);


        JButton button = new JButton("Click to update plotDemo");
        button.addActionListener(e -> updatePlot());

        //this.getContentPane().add(button);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updatePlot();
        setVisible(true);
    }

    public void updatePlot(){
        SwingUtilities.invokeLater(() -> plotDemo.plotLine(data));
    }

    public void debugData(List<Point<LocalDate>> data){

        for (int i = 0; i < data.size(); i++){
            System.out.println(i + ": " + data.get(i).getY());
        }

    }

    public class CustomPanel extends JPanel{

        @Override
        public void paintComponent(Graphics g){

            plotDemo.paintComponent(g);

        }

    }

}
