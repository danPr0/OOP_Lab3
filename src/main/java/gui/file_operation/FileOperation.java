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
}
