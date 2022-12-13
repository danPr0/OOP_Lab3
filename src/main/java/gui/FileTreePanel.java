package gui;

import gui.file_operation.*;
import org.apache.commons.io.FilenameUtils;
import report.ReportBuilder;
import util.FileExtension;
import util.FileTreeEventManager;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.html.HTMLDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class FileTreePanel extends JPanel {
    private MyFrame myFrame;
    private JScrollPane treeScrollPane;
    private JComboBox<String> diskSelect, fileFilter;
    private Map<String, File[]> expandedPaths;
    private JMenuItem newFileItem, openTxtFileItem, pasteFileItem, copyFileItem, copyTxtFilesItem, cutFileItem, deleteFileItem, renameFileItem;

    private Map<String, byte[]> buffer;
    private File currentFile = null;
    private FileTreeEventManager eventManager;
    private ReportBuilder reportBuilder;

    public FileTreePanel(MyFrame myFrame, int width, int height, Map<String, byte[]> buffer, FileTreeEventManager eventManager, ReportBuilder reportBuilder) {
        super();
        this.myFrame = myFrame;
        this.buffer = buffer;
        this.eventManager = eventManager;
        this.reportBuilder = reportBuilder;
        expandedPaths = new HashMap<>();

        setPreferredSize(new Dimension(width, height));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        diskSelect = new JComboBox<>(new String[]{"C:", "D:"});
        fileFilter = new JComboBox<>(new String[]{FileExtension.all.getValue(), FileExtension.txt.getValue(), FileExtension.html.getValue()});

        treeScrollPane = new JScrollPane(createFileTree(diskSelect.getSelectedItem() + File.separator, FileExtension.getByValue((String) fileFilter.getSelectedItem())));
        treeScrollPane.setPreferredSize(new Dimension(width, height - 200));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTree());

        initializeFileOperations();

        add(treeScrollPane);
        add(diskSelect);
        add(fileFilter);
        add(refreshButton);
    }

    private void initializeFileOperations() {
        newFileItem = new NewFileOperation("New", this);
        pasteFileItem = new PasteFileOperation("Paste", this);
        openTxtFileItem = new OpenTxtFileOperation("Open", this);
        copyFileItem = new CopyFileOperation("Copy", this);
        copyTxtFilesItem = new CopyTxtFilesOperation("Copy txt files", this);
        cutFileItem = new CutFileOperation("Cut", this);
        deleteFileItem = new DeleteFileOperation("Delete", this);
        renameFileItem = new RenameFileOperation("Rename", this);
    }

    private JTree createFileTree(String rootPath, FileExtension allowedExtension) {
        expandedPaths.putIfAbsent(createFilepath(new TreePath(rootPath)), new File(rootPath).listFiles());
        List<TreePath> pathsToExpand = new ArrayList<>();
        DefaultMutableTreeNode rootNode = getTreeNode(new TreeNode[]{}, new File(rootPath), rootPath, allowedExtension, pathsToExpand);

        JTree tree = new JTree(rootNode, true);
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                setIcon(FileSystemView.getFileSystemView().getSystemIcon(new File(createFilepath(new TreePath(node.getPath())))));
                return this;
            }
        });

        for (TreePath pathToExpand : pathsToExpand)
            tree.expandPath(pathToExpand);

        tree.addTreeSelectionListener(e -> {
            currentFile = new File(createFilepath(e.getPath()));

            JPopupMenu popupMenu = new JPopupMenu();
            if (((DefaultMutableTreeNode) e.getPath().getLastPathComponent()).getAllowsChildren()) {
                popupMenu.add(newFileItem);
                popupMenu.add(copyTxtFilesItem);
                popupMenu.add(pasteFileItem);
                popupMenu.add(renameFileItem);

                if (buffer == null)
                    pasteFileItem.setEnabled(false);
            } else {
                if (FilenameUtils.getExtension(currentFile.getName()).equals("txt"))
                    popupMenu.add(openTxtFileItem);
                popupMenu.add(copyFileItem);
                popupMenu.add(cutFileItem);
                popupMenu.add(deleteFileItem);
                popupMenu.add(renameFileItem);
            }
            popupMenu.show(myFrame, MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
        });

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                String filepath = createFilepath(event.getPath());
                expandedPaths.put(filepath, new File(filepath).listFiles());
                reportBuilder.logEnterDirectoryOperation(createFilepath(event.getPath()));
                refreshTree();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                expandedPaths.remove(createFilepath(event.getPath()));
                reportBuilder.logLeaveDirectoryOperation(createFilepath(event.getPath()));
            }
        });

        return tree;
    }

    private DefaultMutableTreeNode getTreeNode(TreeNode[] parentPath, File directory, String dirName, FileExtension allowedExtension, List<TreePath> pathsToExpand) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(dirName);
        List<TreeNode> treeNodes = new ArrayList<>(List.of(parentPath));
        treeNodes.add(node);

        TreePath currentPath = new TreePath(treeNodes.toArray(TreeNode[]::new));
        if (directory.listFiles() != null && expandedPaths.keySet().stream().anyMatch(path -> path.equals(createFilepath(currentPath)))) {
            pathsToExpand.add(currentPath);
            expandedPaths.replace(createFilepath(currentPath), directory.listFiles());

            for (File file : Objects.requireNonNull(directory.listFiles())) {
                try {
                    if (file.isDirectory())
                        node.add(getTreeNode(treeNodes.toArray(TreeNode[]::new), file, file.getName(), allowedExtension, pathsToExpand));
                    else if (allowedExtension.isFileAccepted(file))
                        node.add(new DefaultMutableTreeNode(file.getName(), false));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        return node;
    }

    private String createFilepath(TreePath path) {
        StringBuilder result = new StringBuilder();
        for (Object branch : path.getPath()) {
            result.append(branch.toString()).append(File.separator);
        }
        return result.toString();
    }

    public synchronized void refreshTree() {
        treeScrollPane.setViewportView(createFileTree(diskSelect.getSelectedItem() + File.separator, FileExtension.getByValue((String) fileFilter.getSelectedItem())));
        treeScrollPane.repaint();
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public Map<String, byte[]> getBuffer() {
        return buffer;
    }

    public ReportBuilder getReportBuilder() {
        return reportBuilder;
    }

    public Map<String, File[]> getExpandedPaths() {
        return expandedPaths;
    }

    public MyFrame getMyFrame() {
        return myFrame;
    }

    public FileTreeEventManager getEventManager() {
        return eventManager;
    }
}