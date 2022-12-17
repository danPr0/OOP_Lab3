package gui.file_operation;

import gui.FileTreePanel;
import service.FileService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class RenameFileOperation extends FileOperation {
    public RenameFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createInputFrame();
    }

    private void createInputFrame() {
        JFrame inputFrame = new JFrame();
        inputFrame.setTitle("Enter new file name");
        inputFrame.setSize(new Dimension(300, 70));
        inputFrame.setLayout(new BorderLayout());
        inputFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        JTextField filenameField = new JTextField(fileTree.getCurrentFile().getName());
        filenameField.setPreferredSize(new Dimension(200, 20));

        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(okEvent -> {
            File currentFile = fileTree.getCurrentFile();
            String oldPath = currentFile.getAbsolutePath();
            String newPath = currentFile.getAbsolutePath().replace(currentFile.getName(), filenameField.getText());
            boolean isSuccess = FileService.renameFile(currentFile, newPath);
            if (!isSuccess) {
                JOptionPane.showMessageDialog(inputFrame, "There's already a file with the same name in this location", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else {
                if (new File(newPath).isDirectory())
                    fileTree.getEventManager().directoryRenamedEvent(oldPath, newPath);
                inputFrame.dispose();
            }

            fileTree.getReportBuilder().logRenameFileOperation(oldPath, newPath, isSuccess);
            fileTree.getEventManager().notifyListeners();
        });

        panel.add(filenameField);
        panel.add(OKButton);

        inputFrame.add(panel);
        inputFrame.setVisible(true);
    }
}
