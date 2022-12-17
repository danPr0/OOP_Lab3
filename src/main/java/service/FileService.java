package service;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class FileService {
    public static boolean copyFile(File file, Map<String, byte[]> buffer) {
        boolean success = true;
        try {
            buffer.clear();
            buffer.put(file.getName(), Files.readAllBytes(Path.of(file.getAbsolutePath())));
        } catch (IOException ex) {
            success = false;
            ex.printStackTrace();
        }
        return success;
    }

    public static boolean copyTxtFiles(File directory, Map<String, byte[]> buffer) {
        boolean success = true;
        try {
            buffer.clear();
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isFile() && FilenameUtils.getExtension(file.getName()).equals("txt"))
                    buffer.put(file.getName(), Files.readAllBytes(Path.of(file.getAbsolutePath())));
            }
        } catch (IOException ex) {
            success = false;
            ex.printStackTrace();
        }
        return success;
    }

    public static boolean cutFile(File currentFile, Map<String, byte[]> buffer) {
        boolean success = true;
        try {
            buffer.clear();
            buffer.put(currentFile.getName(), Files.readAllBytes(Path.of(currentFile.getAbsolutePath())));
        } catch (IOException ex) {
            success = false;
            ex.printStackTrace();
        }

        if (!currentFile.delete())
            success = false;

        return success;
    }

    public static boolean deleteFile(File file) {
        return file.delete();
    }

    public static boolean createNewFile(String path) {
        boolean success;
        try {
            success = new File(path).createNewFile();
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    public static boolean createNewDirectory(String path) {
        return new File(path).mkdirs();
    }

    public static boolean pasteFile(String filepath, byte[] buffer) {
        boolean success = true;
        int noOfCopies = 0;
        while (new File(filepath).exists())
            filepath = convertExistingFilename(noOfCopies++, filepath);

        try {
            Files.write(Path.of(filepath), buffer);
        } catch (IOException ex) {
            success = false;
            ex.printStackTrace();
        }
        return success;
    }

    public static boolean renameFile(File file, String newFilepath) {
        return file.renameTo(new File(newFilepath));
    }

    public static void writeContentToFile(String path, String content) {
        try {
            Files.writeString(Path.of(path), content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String convertExistingFilename(int noOfCopies, String filename) {
        String extension = FilenameUtils.getExtension(filename);
        if (noOfCopies == 0)
            return filename.substring(0, filename.lastIndexOf("." + extension)) +  "(" + (noOfCopies + 1) + ")." + extension;
        else return filename.substring(0, filename.lastIndexOf("(" + noOfCopies + ")")) + "(" + (noOfCopies + 1) + ")." + extension;
    }
}
