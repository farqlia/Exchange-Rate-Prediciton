package viewtest;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import view.view.View;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

public class ViewTest {

    @Mock
    DefaultTableModel modelA, modelS;
    @Mock
    ActionEvent e;
    @Test
    void shouldNotifyObserversWithCorrectArguments(){

        View v = new View(Collections.emptyList(), modelS, modelA);
        ActionListener a = v.new HandleButtonListener();
        a.actionPerformed(e);

    }

}
