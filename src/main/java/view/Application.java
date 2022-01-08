package view;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {

        View view = new View();

        SwingUtilities.invokeLater(() -> view.setVisible(true));

    }

}
