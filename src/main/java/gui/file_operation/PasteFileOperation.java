package gui.file_operation;

import gui.FileTreePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class PasteFileOperation extends FileOperation {
    public PasteFileOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Map.Entry<String, byte[]> buffer : fileTree.getBuffer().entrySet()) {
            String endPath = fileTree.getCurrentFile().getAbsolutePath() + File.separator + buffer.getKey();
            boolean ifSuccess = pasteFile(endPath, buffer.getValue());

            if (!ifSuccess)
                JOptionPane.showMessageDialog(fileTree.getMyFrame(), "Unable to paste file", "ERROR", JOptionPane.ERROR_MESSAGE);
            fileTree.getReportBuilder().logPasteFileOperation(endPath, ifSuccess);
        }
        fileTree.getEventManager().notifyListeners();
    }

    private boolean pasteFile(String filepath, byte[] buffer) {
        boolean success = true;
        int noOfCopies = 0;
        while (new File(filepath).exists())
            filepath = convertExistingFilename(noOfCopies++, filepath);

        try {
            Files.write(Path.of(filepath), buffer);
        } catch (IOException ex) {
            success = false;
            ex.printStackTrace();
        }
        return success;
    }

}
