package util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public enum FileExtension {
    all("All files") {
        @Override
        public boolean isFileAccepted(File file) {
            return true;
        }
    },
    txt("TEXT files (*.txt)") {
        @Override
        public boolean isFileAccepted(File file) {
            return FilenameUtils.getExtension(file.getName()).equals("txt");
        }
    },
    html("HTML files (*.html)") {
        @Override
        public boolean isFileAccepted(File file) {
            return FilenameUtils.getExtension(file.getName()).equals("html");
        }
    };

    private String value;

    FileExtension(String value) {
        this.value = value;
    }

    public abstract boolean isFileAccepted(File file);

    public static FileExtension getByValue(String value) {
        for (FileExtension fileExtension : FileExtension.values())
            if (fileExtension.value.equals(value))
                return fileExtension;
        return null;
    }

    public String getValue() {
        return value;
    }
}
