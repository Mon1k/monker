package sample.table;

import java.io.File;
import java.util.ArrayList;

public class FileScaner
{
    public ArrayList<File> scaner(String path)
    {
        ArrayList<File> result = new ArrayList<>();

        File filePath = new File(path);
        System.out.println("found files:");
        for (File file: filePath.listFiles()) {
            System.out.println(file);
            result.add(file);
        }

        return result;
    }
}
