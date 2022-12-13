package gui.file_operation;

import gui.FileTreePanel;

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
            String oldPath = fileTree.getCurrentFile().getAbsolutePath();
            boolean ifSuccess = renameFile(fileTree.getCurrentFile(), filenameField.getText());
            if (!ifSuccess) {
                JOptionPane.showMessageDialog(inputFrame, "There's already a file with the same name in this location", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else inputFrame.dispose();

            fileTree.getReportBuilder().logRenameFileOperation(oldPath, filenameField.getText(), ifSuccess);
            fileTree.getEventManager().notifyListeners();
        });

        panel.add(filenameField);
        panel.add(OKButton);

        inputFrame.add(panel);
        inputFrame.setVisible(true);
    }

    private boolean renameFile(File file, String newFilename) {
        return file.renameTo(new File(file.getParent() + File.separator + newFilename));
    }
}
