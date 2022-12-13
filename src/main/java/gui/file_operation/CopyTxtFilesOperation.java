package gui.file_operation;

import gui.FileTreePanel;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CopyTxtFilesOperation extends FileOperation {
    public CopyTxtFilesOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean ifSuccess = copyTxtFiles(fileTree.getCurrentFile(), fileTree.getBuffer());
        if (!ifSuccess)
            JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to copy txt files", "ERROR", JOptionPane.ERROR_MESSAGE);

        fileTree.getReportBuilder().logCopyTxtFilesOperation(fileTree.getCurrentFile().getAbsolutePath(), ifSuccess);
        fileTree.getEventManager().notifyListeners();
    }

    private boolean copyTxtFiles(File directory, Map<String, byte[]> buffer) {
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
}
