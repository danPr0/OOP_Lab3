package gui.file_operation;

import gui.FileTreePanel;
import org.apache.commons.io.FilenameUtils;
import service.FileService;

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
        boolean isSuccess = FileService.copyTxtFiles(fileTree.getCurrentFile(), fileTree.getBuffer());
        if (!isSuccess)
            JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to copy txt files", "ERROR", JOptionPane.ERROR_MESSAGE);

        fileTree.getReportBuilder().logCopyTxtFilesOperation(fileTree.getCurrentFile().getAbsolutePath(), isSuccess);
        fileTree.getEventManager().notifyListeners();
    }
}
