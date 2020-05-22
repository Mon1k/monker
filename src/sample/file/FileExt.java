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

    public static String humanSize(File file)
    {
        String result;
        long size = file.length();
        int index = 0;
        while (true) {
            if (size > 1024) {
                index++;
            } else {
                break;
            }
            size /= 1024;
        }

        result = size + " ";
        switch (index) {
            default -> result += "b";
            case 1 -> result += "Kb";
            case 2 -> result += "Mb";
            case 3 -> result += "Gb";
            case 4 -> result += "Tb";
            case 5 -> result += "Pb";
        }

        return result;
    }
}
