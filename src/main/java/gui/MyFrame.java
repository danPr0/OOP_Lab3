package gui;

import report.Report;
import report.ReportBuilder;
import util.FileTreeEventManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MyFrame extends JFrame implements WindowListener {
    private ReportBuilder reportBuilder;

    public MyFrame() {
        super();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);

        JMenuBar menuBar = new FrameMenu(this);
        setJMenuBar(menuBar);

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        FileTreeEventManager eventManager = new FileTreeEventManager();
        reportBuilder = Report.builder(askForReportFormat());
        Map<String, byte[]> buffer = new HashMap<>();

        FileTreePanel leftPanel = new FileTreePanel(this, this.getWidth() / 2 - 100, 600, buffer, eventManager, reportBuilder);
        leftPanel.setBackground(Color.gray);

        FileTreePanel rightPanel = new FileTreePanel(this, this.getWidth() / 2 - 100, 600, buffer, eventManager, reportBuilder);
        rightPanel.setBackground(Color.gray);

        eventManager.addListener(leftPanel);
        eventManager.addListener(rightPanel);
        eventManager.supervise();

        layout.putConstraint(SpringLayout.WEST, menuBar, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, menuBar, 0, SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, leftPanel, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, leftPanel, 10, SpringLayout.SOUTH, menuBar);

        layout.putConstraint(SpringLayout.WEST, rightPanel, 100, SpringLayout.EAST, leftPanel);
        layout.putConstraint(SpringLayout.NORTH, rightPanel, 10, SpringLayout.SOUTH, menuBar);

        add(leftPanel);
        add(rightPanel);

        addWindowListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private String askForReportFormat() {
        String userInput;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Please choose report format [txt/html]");
            userInput = scanner.nextLine();
        }
        while (!(userInput.equals("txt") || userInput.equals("html")));

        return userInput;
    }

    static class FrameMenu extends JMenuBar {
        private JFrame frameOwner;
        public FrameMenu(JFrame frameOwner) {
            super();
            this.frameOwner = frameOwner;

            JMenu reference = new JMenu("Reference");
            JMenu help = new JMenu("Help");

            reference.add(createAboutProgramItem());
            help.add(createHelpItem());

            add(reference);
            add(help);
        }

        private JMenuItem createAboutProgramItem() {
            JMenuItem aboutProgram = new JMenuItem("About Program");
            aboutProgram.addActionListener(e -> {
                JDialog helpWindow = new JDialog(frameOwner);
                helpWindow.setTitle("About Program");
                helpWindow.setSize(new Dimension(400, 200));

                JPanel helpPanel = new JPanel();
                helpPanel.add(new JLabel("<html><p>Програма представляє собою файловий менеджер,<br>" +
                        "    який може оброблювати файли та редагувати тексти</p><br><br>" +
                        "<p>Автор : Прошин Данило</p><html>"));

                helpWindow.getContentPane().add(new JScrollPane(helpPanel));
                helpWindow.setLocationRelativeTo(null);
                helpWindow.setVisible(true);
            });
            return aboutProgram;
        }

        private JMenuItem createHelpItem() {
            JMenuItem viewHelp = new JMenuItem("View Help");
            viewHelp.addActionListener(e -> {
                JDialog helpWindow = new JDialog(frameOwner);
                helpWindow.setTitle("Help");
                helpWindow.setSize(new Dimension(500, 275));

                JPanel helpPanel = new JPanel();
                helpPanel.add(new JLabel("<html>" +
                        "<p>1.Внизу панелі ви можете обрати диск комп'ютера, фільтр файлів<br> та перезавантажити дерево файлів.</p>" +
                        "<p>2.Для папок доступні наступні операції :<br> " +
                        "- Операція New (Створює новий файл в цій директорії)<br>" +
                        "- Операція Copy txt files (Копіює усі наявні текстові файли до буфер обміну)<br>" +
                        "- Операція Paste (Вставляє в директорію останній файл з буфер обміну)<br>" +
                        "- Операція Rename (Перейменовує директорію)</p>" +
                        "<p>3.Для файлів доступні наступні операції :<br>" +
                        "- Операція Open (Доступна тільки для текстових файлів. Відкриває текстовий редактор для заданого файлу)<br>" +
                        "- Операція Copy (Копіює файл до буфер обміну)<br>" +
                        "- Операція Cut (Копіює файл до буфер обміну та видаляє його)<br>" +
                        "- Операція Delete (Видаляє файл)<br>" +
                        "- Операція Rename (Перейменовує файл)<br></p><html>"));

                helpWindow.getContentPane().add(new JScrollPane(helpPanel));
                helpWindow.setLocationRelativeTo(null);
                helpWindow.setVisible(true);
            });
            return viewHelp;
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Report report = reportBuilder.build();
        try {
            report.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
