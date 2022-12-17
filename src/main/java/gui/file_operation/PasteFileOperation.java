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

public class PasteFileOperation extends FileOperation {
    public PasteFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Map.Entry<String, byte[]> buffer : fileTree.getBuffer().entrySet()) {
            String endPath = fileTree.getCurrentFile().getAbsolutePath() + File.separator + buffer.getKey();
            boolean isSuccess = FileService.pasteFile(endPath, buffer.getValue());

            if (!isSuccess)
                JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to paste file", "ERROR", JOptionPane.ERROR_MESSAGE);
            fileTree.getReportBuilder().logPasteFileOperation(endPath, isSuccess);
        }
        fileTree.getEventManager().notifyListeners();
    }
}
