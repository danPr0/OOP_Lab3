package gui.file_operation;

import gui.FileTreePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class NewFileOperation extends FileOperation {
    public NewFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createInputFrame();
    }

    private void createInputFrame() {
        JFrame inputFrame = new JFrame();
        inputFrame.setTitle("Enter file name");
        inputFrame.setSize(new Dimension(300, 70));
        inputFrame.setLayout(new BorderLayout());
        inputFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        JTextField filenameField = new JTextField();
        filenameField.setPreferredSize(new Dimension(200, 20));

        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(okEvent -> {
            String endPath = fileTree.getCurrentFile().getAbsolutePath() + File.separator + filenameField.getText();
            boolean ifSuccess = createNewFile(endPath);
            if (!ifSuccess) {
                JOptionPane.showMessageDialog(inputFrame, "There's already a file with the same name in this location", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else inputFrame.dispose();

            fileTree.getReportBuilder().logNewFileOperation(endPath, ifSuccess);
            fileTree.getEventManager().notifyListeners();
        });

        panel.add(filenameField);
        panel.add(OKButton);

        inputFrame.add(panel);
        inputFrame.setVisible(true);
    }

    private boolean createNewFile(String endPath) {
        boolean success = true;
        try {
            if (!new File(endPath).createNewFile())
                success = false;
        } catch (IOException ex) {
            success = false;
            ex.printStackTrace();
        }
        return success;
    }
}
