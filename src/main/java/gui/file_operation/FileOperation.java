package gui.file_operation;

import gui.FileTreePanel;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class FileOperation extends JMenuItem implements ActionListener {
    protected FileTreePanel fileTree;

    public FileOperation(String title, FileTreePanel fileTree) {
        super(title);
        this.fileTree = fileTree;
        addActionListener(this);
    }

    protected String convertExistingFilename(int noOfCopies, String filename) {
        String extension = FilenameUtils.getExtension(filename);
        if (noOfCopies == 0)
            return filename.substring(0, filename.lastIndexOf("." + extension)) +  "(" + (noOfCopies + 1) + ")." + extension;
        else return filename.substring(0, filename.lastIndexOf("(" + noOfCopies + ")")) + "(" + (noOfCopies + 1) + ")." + extension;
    }
}
