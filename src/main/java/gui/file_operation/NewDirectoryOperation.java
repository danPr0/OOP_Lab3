package gui.file_operation;

import gui.FileTreePanel;
import service.FileService;

import java.io.File;
import java.io.IOException;

public class NewDirectoryOperation extends NewFileOperation {
    public NewDirectoryOperation(String title, FileTreePanel fileTree) {
        super(title, fileTree);

        inputFrameTitle = "Enter directory name";
        errorMessage = "There's already a directory with the same name in this location";
        fileCreation = FileService::createNewDirectory;
    }
}
