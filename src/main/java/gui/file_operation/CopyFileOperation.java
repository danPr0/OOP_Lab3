package gui.file_operation;

import gui.FileTreePanel;
import service.FileService;

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
        boolean isSuccess = FileService.copyFile(fileTree.getCurrentFile(), fileTree.getBuffer());
        if (!isSuccess)
            JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to copy the file", "ERROR", JOptionPane.ERROR_MESSAGE);

        fileTree.getReportBuilder().logCopyFileOperation(fileTree.getCurrentFile().getAbsolutePath(), isSuccess);
        fileTree.getEventManager().notifyListeners();
    }
}
