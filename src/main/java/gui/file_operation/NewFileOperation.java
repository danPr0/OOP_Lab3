package gui.file_operation;

import gui.FileTreePanel;
import service.FileService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

public class NewFileOperation extends FileOperation {
    protected String inputFrameTitle, errorMessage;
    protected Predicate<String> fileCreation;

    public NewFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);

        inputFrameTitle = "Enter file name";
        errorMessage = "There's already a file with the same name in this location";
        fileCreation = FileService::createNewFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createInputFrame();
    }

    private void createInputFrame() {
        JFrame inputFrame = new JFrame();
        inputFrame.setTitle(inputFrameTitle);
        inputFrame.setSize(new Dimension(300, 70));
        inputFrame.setLayout(new BorderLayout());
        inputFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        JTextField filenameField = new JTextField();
        filenameField.setPreferredSize(new Dimension(200, 20));

        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(okEvent -> {
            String endPath = fileTree.getCurrentFile().getAbsolutePath() + File.separator + filenameField.getText();
            boolean isSuccess = fileCreation.test(endPath);
            if (!isSuccess) {
                JOptionPane.showMessageDialog(inputFrame, errorMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else inputFrame.dispose();

            fileTree.getReportBuilder().logNewFileOperation(endPath, isSuccess);
            fileTree.getEventManager().notifyListeners();
        });

        panel.add(filenameField);
        panel.add(OKButton);

        inputFrame.add(panel);
        inputFrame.setVisible(true);
    }
}
