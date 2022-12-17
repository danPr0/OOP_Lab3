package gui;

import org.apache.commons.io.FileUtils;
import service.FileService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TxtFileEditor extends JFrame {
    private File file;
    private JTextArea fileContent;

    public TxtFileEditor(File file) {
        super();
        this.file = file;
        setSize(new Dimension(800, 600));

        fileContent = createInputPanel();
        setJMenuBar(new TextEditorMenu(this));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JTextArea createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));

        String text = "";
        try {
            text = new String(Files.readAllBytes(Path.of(file.getAbsolutePath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JTextArea fileContent = new JTextArea(text);
        fileContent.setFont(new Font("Arial", Font.PLAIN, 15));
        inputPanel.add(fileContent);
        getContentPane().add(new JScrollPane(inputPanel));
        return fileContent;
    }

    private class TextEditorMenu extends JMenuBar {
        private JFrame frameOwner;

        public TextEditorMenu(JFrame frameOwner) {
            super();
            this.frameOwner = frameOwner;

            JMenu file = new JMenu("File");
            JMenu help = new JMenu("Help");

            file.add(createSaveFileItem());
            file.add(createFindSingleWordsItem());
            file.add(createLightCopyToAnotherFileItem());
            help.add(createHelpInfoItem());

            add(file);
            add(help);
        }

        private JMenuItem createSaveFileItem() {
            JMenuItem saveItem = new JMenuItem("Save");
            saveItem.addActionListener(e -> FileService.writeContentToFile(file.getAbsolutePath(), fileContent.getText()));
            return saveItem;
        }

        private JMenuItem createFindSingleWordsItem() {
            JMenuItem findSingleWordsItem = new JMenuItem("Find Single Words");
            findSingleWordsItem.addActionListener(e -> {
                JDialog dialog = new JDialog(frameOwner);
                dialog.setTitle("Single words");
                dialog.setSize(new Dimension(300, 200));

                JPanel textPanel = new JPanel();
                textPanel.add(new JLabel(getSingleWordsFromText(fileContent.getText())));

                dialog.getContentPane().add(new JScrollPane(textPanel));
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            });

            return findSingleWordsItem;
        }

        private JMenuItem createLightCopyToAnotherFileItem() {
            JMenuItem lightCopyItem = new JMenuItem("Light Copy");
            lightCopyItem.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify a file to copy");
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));

                int userSelection = fileChooser.showSaveDialog(this);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String filepath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!filepath.endsWith(".txt"))
                        filepath += ".txt";
                    saveLightCopy(fileContent.getText(), filepath);
                }
            });
            return lightCopyItem;
        }

        private JMenuItem createHelpInfoItem() {
            JMenuItem helpInfoItem = new JMenuItem("View Help");
            helpInfoItem.addActionListener(e -> {
                JDialog dialog = new JDialog(frameOwner);
                dialog.setTitle("Help");
                dialog.setSize(new Dimension(400, 200));

                JPanel helpPanel = new JPanel();
                helpPanel.add(new JLabel("<html><p>Операція Save - зберігання змін у файлі</p>" +
                        "<p>Операція Find Single Words - знаходить слова,<br> які зустрічаються в тексті один раз</p>" +
                        "<p>Операція Light Copy - копіює даний файл в інший таким чином,<br>" +
                        "що з послідовних однакових рядків виводиться лише один</p><html>"));

                dialog.getContentPane().add(new JScrollPane(helpPanel));
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            });
            return helpInfoItem;
        }

        private String getSingleWordsFromText(String text) {
            List<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));
            List<String> singleWords = new ArrayList<>();
            for (String word : words) {
                int count = 0;
                for (String wordFound : words)
                    if (wordFound.equals(word))
                        count++;
                if (count == 1)
                    singleWords.add(word);
            }
            return singleWords.stream().reduce((r, w) -> r + ", " + w).get();
        }

        private void saveLightCopy(String text, String filepath) {
            List<String> lines = new ArrayList<>(Arrays.asList(text.split("\n")));

            for (int i = 1; i < lines.size(); ) {
                if (lines.get(i).equals(lines.get(i - 1)))
                    lines.remove(lines.get(i));
                else i++;
            }

            FileService.writeContentToFile(filepath, lines.stream().reduce((r, w) -> r + "\n" + w).get());
        }
    }
}
