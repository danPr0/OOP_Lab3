package util;

import gui.FileTreePanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class FileTreeEventManager {
    List<FileTreePanel> listeners;

    public FileTreeEventManager() {
        listeners = new ArrayList<>();
    }

    public void supervise() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            EventQueue.invokeLater(() -> {
                for (FileTreePanel listener : listeners) {
                    Map<String, File[]> expandedPaths = listener.getExpandedPaths();
                    for (Map.Entry<String, File[]> path : expandedPaths.entrySet()) {
                        List<String> treeFiles = new ArrayList<>();
                        List<String> actualFiles = new ArrayList<>();

                        try {
                            for (File file : path.getValue())
                                treeFiles.add(file.getCanonicalPath());
                            for (File file : Objects.requireNonNull(new File(path.getKey()).listFiles()))
                                actualFiles.add(file.getCanonicalPath());
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (!treeFiles.equals(actualFiles)) {
                            notifyListener(listener);
                            break;
                        }
                    }
                }
            });
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void addListener(FileTreePanel listener) {
        listeners.add(listener);
    }

    public void removeListener(FileTreePanel listener) {
        listeners.remove(listener);
    }

    public void notifyListener(FileTreePanel listener) {
        listener.refreshTree();
    }

    public void notifyListeners() {
        for (FileTreePanel listener : listeners)
            listener.refreshTree();
    }

    public void directoryRenamedEvent(String oldFilepath, String newFilepath) {
        for (FileTreePanel listener : listeners)
            listener.directoryRenamed(oldFilepath, newFilepath);
    }
}
