package mvc;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class JTableView extends JTable {

    private String name;
    public JTableView(AbstractTableModel model,
                      String name){
        super(model);
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

}
