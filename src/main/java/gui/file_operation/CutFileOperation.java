package gui.file_operation;

import gui.FileTreePanel;
import org.apache.commons.io.FileUtils;
import service.FileService;

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
        boolean isSuccess = FileService.cutFile(fileTree.getCurrentFile(), fileTree.getBuffer());
        if (!isSuccess)
            JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to cut the file", "ERROR", JOptionPane.ERROR_MESSAGE);

        fileTree.getReportBuilder().logCutFileOperation(fileTree.getCurrentFile().getAbsolutePath(), isSuccess);
        fileTree.getEventManager().notifyListeners();
    }
}
