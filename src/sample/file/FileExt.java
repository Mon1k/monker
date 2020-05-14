package sample.file;

import java.io.File;

public class FileExt
{
    public static String getExtension(File file)
    {
        String extenstion = "";
        if (file.getName().lastIndexOf(".") > 0) {
            extenstion = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        }

        return extenstion;
    }
}
