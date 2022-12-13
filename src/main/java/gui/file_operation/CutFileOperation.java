package gui.file_operation;

import gui.FileTreePanel;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CutFileOperation extends FileOperation {
    public CutFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean ifSuccess = cutFile(fileTree.getCurrentFile(), fileTree.getBuffer());
        if (!ifSuccess)
            JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to cut the file", "ERROR", JOptionPane.ERROR_MESSAGE);

        fileTree.getReportBuilder().logCutFileOperation(fileTree.getCurrentFile().getAbsolutePath(), ifSuccess);
        fileTree.getEventManager().notifyListeners();
    }

    private boolean cutFile(File currentFile, Map<String, byte[]> buffer) {
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
}
