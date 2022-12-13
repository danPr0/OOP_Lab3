package gui.file_operation;

import gui.FileTreePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class DeleteFileOperation extends FileOperation{
    public DeleteFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean ifSuccess = deleteFile(fileTree.getCurrentFile());
        if (!ifSuccess)
            JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to delete file", "ERROR", JOptionPane.ERROR_MESSAGE);

        fileTree.getReportBuilder().logDeleteFileOperation(fileTree.getCurrentFile().getAbsolutePath(), ifSuccess);
        fileTree.getEventManager().notifyListeners();
    }

    private boolean deleteFile(File file) {
        return file.delete();
    }
}
