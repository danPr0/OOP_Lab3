package util;

import gui.FileTreePanel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileTreeEventManager {
    List<FileTreePanel> listeners;

    public FileTreeEventManager() {
        listeners = new ArrayList<>();
    }

    public void supervise() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            for (FileTreePanel listener : listeners) {
                Map<String, File[]> expandedPaths = listener.getExpandedPaths();
                for (Map.Entry<String, File[]> path : expandedPaths.entrySet()) {
                    List<File> treeFiles = List.of(path.getValue());
                    List<File> actualFiles = List.of(Objects.requireNonNull(new File(path.getKey()).listFiles()));

                    if (!treeFiles.equals(actualFiles)) {
                        notifyListener(listener);
                        break;
                    }
                }
            }
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
}
