package sample.table;

import java.io.File;
import java.util.ArrayList;

public class TablePanel
{
    ArrayList<TableRowCommander> panel;
    File current = new File("");

    public void scaner()
    {
        panel = new ArrayList<>();
        TableRowCommander parent = new TableRowCommander(current);
        if (current.getAbsoluteFile().getParentFile() != null) {
            parent.setName("...");
            parent.setSize("dir");
            parent.setType("");
            panel.add(parent);
        }

        FileScaner scaner = new FileScaner();
        ArrayList<File> result = scaner.scaner(current.getAbsolutePath());
        System.out.println("current path: "+current.getAbsolutePath());

        for (File file: result) {
            TableRowCommander row = new TableRowCommander(file);
            panel.add(row);
        }
    }

    public void setCurrent(String path)
    {
        current = new File(path);
        System.out.println(current);
    }

    public ArrayList<TableRowCommander> getPanel()
    {
        return panel;
    }

    public int getIndex(File file)
    {
        for (TableRowCommander row: panel) {
            if (row.file.getAbsolutePath().equals(file.getAbsolutePath())) {
                return panel.indexOf(row);
            }
        }

        return 0;
    }
}
