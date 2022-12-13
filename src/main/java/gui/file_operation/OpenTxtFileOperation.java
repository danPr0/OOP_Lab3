package gui.file_operation;

import gui.FileTreePanel;
import gui.TxtFileEditor;

import java.awt.event.ActionEvent;

public class OpenTxtFileOperation extends FileOperation {
    public OpenTxtFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new TxtFileEditor(fileTree.getCurrentFile());
        fileTree.getReportBuilder().logOpenTxtFileOperation(fileTree.getCurrentFile().getAbsolutePath());
    }
}
