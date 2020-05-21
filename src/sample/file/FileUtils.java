package sample.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils
{
    public static void generateBigFile(String name, long size)
    {
        File file = new File(name);
        int index = 1;
        while (true) {
            if (!file.exists()) {
                break;
            }
            file = new File(name);
            String newName = FileExt.getName(file) + " copy";
            if (index > 1) {
                newName += " " + index;
            }
            newName += "." + FileExt.getExtension(file);
            file = new File(newName);
            index++;
        }

        try {
            FileWriter writer = new FileWriter(file.getAbsolutePath());
            for (long i = 0; i < size; i++) {
                writer.write(String.valueOf(" "));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
