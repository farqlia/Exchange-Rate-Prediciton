package viewtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import view.other.Plot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;

public class PlotTest {

    Plot plot;
    @BeforeEach
    void setUp(){
        plot = new Plot();
    }

    @Mock
    ActionEvent e;

    @Test
    void shouldCreateCorrectFilePath(){
        plot.setTitle("EUR, 2021-12-01, 2022-01-01");
        Path p = Path.of("plots" , plot.getTitle().replaceAll("\\W+", "_") + "." + "png");
        Assertions.assertEquals("plots\\EUR_2021_12_01_2022_01_01.png",
                p.toString());
    }

    @Test
    void shouldSaveAsPNGFile(){
        plot.setTitle("EUR, 2021-12-01, 2022-01-01");
        ActionListener listener = plot.new HandlePlotSave("png");

        listener.actionPerformed(e);
        File f = Path.of("plots\\EUR_2021_12_01_2022_01_01.png").toFile();
        Assertions.assertTrue(f.exists());
    }

    @Test
    void shouldSaveAsJPEGFile(){
        plot.setTitle("EUR, 2021-12-01, 2022-01-01");
        ActionListener listener = plot.new HandlePlotSave("jpeg");

        listener.actionPerformed(e);
        File f = Path.of("plots\\EUR_2021_12_01_2022_01_01.jpeg").toFile();
        Assertions.assertTrue(f.exists());
    }

}
