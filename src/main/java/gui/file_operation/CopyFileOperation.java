package gui.file_operation;

import gui.FileTreePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class CopyFileOperation extends FileOperation {
    public CopyFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean ifSuccess = copyFile(fileTree.getCurrentFile(), fileTree.getBuffer());
        if (!ifSuccess)
            JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to copy the file", "ERROR", JOptionPane.ERROR_MESSAGE);

        fileTree.getReportBuilder().logCopyFileOperation(fileTree.getCurrentFile().getAbsolutePath(), ifSuccess);
        fileTree.getEventManager().notifyListeners();
    }

    private boolean copyFile(File file, Map<String, byte[]> buffer) {
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
}
