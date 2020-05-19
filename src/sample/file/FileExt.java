package sample.file;

import java.io.*;

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

    public static String getName(File file)
    {
        String name = "";
        if (file.getName().lastIndexOf(".") > 0) {
            name = file.getName().substring(0, file.getName().lastIndexOf("."));
        }

        return name;
    }

    public static void copy(File src, File dest)
    {
        try (FileInputStream in = new FileInputStream(src); FileOutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
