package sample.table;

import javafx.beans.property.SimpleStringProperty;
import sample.file.FileExt;

import java.io.File;
import java.text.SimpleDateFormat;

public class TableRowCommander
{
    public File file;

    private SimpleStringProperty name;
    private SimpleStringProperty size;
    private SimpleStringProperty type;
    private SimpleStringProperty date;

    public TableRowCommander(File file)
    {
        this.file = file;
        name = new SimpleStringProperty(file.getName());
        size = new SimpleStringProperty(file.isDirectory() ? "dir" : Long.toString(file.length()));
        type = new SimpleStringProperty(file.isDirectory() ? "" : FileExt.getExtension(file));
        date = new SimpleStringProperty(getDate());
    }

    public String getName()
    {
        return name.get();
    }

    public void setName(String name)
    {
        this.name.set(name);
    }

    public String getSize()
    {
        return FileExt.humanSize(file);
    }

    public void setSize(String size)
    {
        this.size.set(size);
    }

    public String getType()
    {
        return type.get();
    }

    public void setType(String type)
    {
        this.type.set(type);
    }

    public String getDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

        return sdf.format(file.lastModified());
    }

    public File getFile()
    {
        return file;
    }
}
