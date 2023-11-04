package com.miejiang.freeandpowerful.tools;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * GBK编码格式文件转换为UTF-8格式文件
 */
public class GBKToUTF8Converter {

    public static void convertPath(String folderPath) {

        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        if (isGBKFile(filePath.toString())) {
                            convertAndReplace(filePath.toString());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("批量文件转换和替换完成！");
    }

    private static void convertAndReplace(String filePath) {
        try {
            // 创建GBK编码的输入流
            FileInputStream inputStream = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(inputStream, "GBK");

            // 创建UTF-8编码的输出流
            File tempFile = new File(filePath + ".tmp");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");

            // 逐行读取并写入文件
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }

            // 关闭流
            reader.close();
            writer.close();

            // 删除原始文件
            File originalFile = new File(filePath);
            if (originalFile.delete()) {
                System.out.println("原始文件已删除: " + filePath);
            } else {
                System.out.println("无法删除原始文件: " + filePath);
                return;
            }

            // 重命名临时文件为原始文件名
            if (tempFile.renameTo(originalFile)) {
                System.out.println("文件转换和替换完成: " + filePath);
            } else {
                System.out.println("无法重命名文件: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isGBKFile(String filePath) {
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("GBK"));
            int readChar = reader.read();
            reader.close();
            return readChar != -1; // 如果能读取到字符，说明是GBK编码文件
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * @param inputFilePath  输入文件路径
     * @param outputFilePath 输出文件路径
     */
    public static void convert(String inputFilePath, String outputFilePath) {
        try {
            // 创建GBK编码的输入流
            FileInputStream inputStream = new FileInputStream(inputFilePath);
            InputStreamReader reader = new InputStreamReader(inputStream, "GBK");

            // 创建UTF-8编码的输出流
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");

            // 逐行读取并写入文件
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }

            // 关闭流
            reader.close();
            writer.close();

            System.out.println("文件转换完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void convertAndReplace(String inputFilePath, String outputFilePath) {

        try {
            // 创建GBK编码的输入流
            FileInputStream inputStream = new FileInputStream(inputFilePath);
            InputStreamReader reader = new InputStreamReader(inputStream, "GBK");

            // 创建UTF-8编码的输出流
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");

            // 逐行读取并写入文件
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }

            // 关闭流
            reader.close();
            writer.close();

            // 删除原始文件
            File originalFile = new File(inputFilePath);
            if (originalFile.delete()) {
                System.out.println("原始文件已删除.");
            } else {
                System.out.println("无法删除原始文件.");
            }

            // 重命名输出文件为原始文件名
            File convertedFile = new File(outputFilePath);
            if (convertedFile.renameTo(originalFile)) {
                System.out.println("文件转换并替换完成！");
            } else {
                System.out.println("无法重命名文件。请手动替换原始文件。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
