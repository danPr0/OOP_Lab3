package report;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Report {
    private String format;
    private String txtFilepath = "report.txt";
    private String htmlFilepath = "report.html";
    private String greeting, ending;
    private List<String> messages = new ArrayList<>();

    public Report(String format) {
        this.format = format;
    }

    public static ReportBuilder builder(String format) {
        return new ReportBuilder(format);
    }

    public void save() throws IOException {
        if (format.equals("html"))
            saveHtml();
        else saveTxt();
    }

    private void saveTxt() throws IOException {
        File outputFile = new File(txtFilepath);
        if (!outputFile.exists())
            outputFile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(greeting);
        writer.newLine();

        for (String message : messages) {
            writer.write(message);
            writer.newLine();
        }

        writer.write(ending);
        writer.newLine();

        writer.close();
    }

    private void saveHtml() throws IOException {
        File outputFile = new File(htmlFilepath);
        if (!outputFile.exists())
            outputFile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write("<html><body>");
        writer.newLine();
        writer.write("<h1>" + greeting + "</h1>");
        writer.newLine();

        for (String message : messages) {
            writer.write("<p>" + message + "</p>");
            writer.newLine();
        }

        writer.write("<p><i>" + ending + "</i></p>");
        writer.newLine();
        writer.write("</body></html>");
        writer.newLine();

        writer.close();
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public void setEnding(String ending) {
        this.ending = ending;
    }
}
