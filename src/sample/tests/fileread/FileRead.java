package sample.tests.fileread;

import sample.file.FileUtils;
import sample.window.Popup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileRead
{
    public FileRead()
    {
        File file = new File("textBigfile.txt");
        FileUtils.generateBigFile(file.getName(), 10000000);

        String result = "File read speed test:\nFile size: " + file.length() + "\n";

        // BufferedReader
        try {
            long time = System.currentTimeMillis();
            StringBuilder text = new StringBuilder();
            BufferedReader reader = Files.newBufferedReader(file.toPath());
            while (reader.ready()) {
                text.append(reader.readLine()).append("\n");
            }
            reader.close();
            result += "BufferedReader: " + (System.currentTimeMillis() - time) + "ms\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        // InputStream
        try {
            long time = System.currentTimeMillis();
            String text = Arrays.toString(Files.readAllBytes(file.toPath()));
            result += "InputStream: " + (System.currentTimeMillis() - time) + "ms\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Scaner
        try {
            long time = System.currentTimeMillis();
            StringBuilder text = new StringBuilder();
            Scanner scaner = new Scanner(file);
            while (scaner.hasNext()) {
                text.append(scaner.nextLine());
            }
            scaner.close();
            result += "Scaner: " + (System.currentTimeMillis() - time) + "ms\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        // stream
        try {
            long time = System.currentTimeMillis();
            String text = Files.lines(file.toPath()).collect(Collectors.toList()).toString();
            result += "Stream: " + (System.currentTimeMillis() - time) + "ms\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        // channel
        try {
            long time = System.currentTimeMillis();
            StringBuilder text = new StringBuilder();
            SeekableByteChannel channel = Files.newByteChannel(file.toPath(), StandardOpenOption.READ);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int c;
            while ((c = channel.read(buffer)) != - 1) {
                buffer.rewind();
                for (int i = 0; i < c; i++)
                    text.append(buffer.get());
            }
            buffer.clear();
            channel.close();
            result += "Channel: " + (System.currentTimeMillis() - time) + "ms\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        file.delete();
        System.out.println("speed test:\n" + result);
        new Popup().show(result);
    }
}
