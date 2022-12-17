package gui.file_operation;

import gui.FileTreePanel;
import service.FileService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class DeleteFileOperation extends FileOperation{
    public DeleteFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean isSuccess = FileService.deleteFile(fileTree.getCurrentFile());
        if (!isSuccess)
            JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to delete file", "ERROR", JOptionPane.ERROR_MESSAGE);

        fileTree.getReportBuilder().logDeleteFileOperation(fileTree.getCurrentFile().getAbsolutePath(), isSuccess);
        fileTree.getEventManager().notifyListeners();
    }
}
