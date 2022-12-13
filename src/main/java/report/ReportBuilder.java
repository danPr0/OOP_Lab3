package report;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class ReportBuilder {
    private Report report;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public ReportBuilder(String format) {
        report = new Report(format);
        report.setGreeting(String.format("Звіт про роботу файлового менеджера. %s", formatter.format(Date.from(Instant.now()))));
    }

    public ReportBuilder logOpenTxtFileOperation(String filepath) {
        String log = "%s Зміни. Відкрито текстовий файл %s";
        report.getMessages().add(String.format(log, formatter.format(Date.from(Instant.now())), filepath));
        return this;
    }

    public ReportBuilder logCopyFileOperation(String filepath, boolean ifSuccess) {
        String log = ifSuccess ? "%s Зміни. Скопійовано файл %s до буферу обміну." :
                                 "%s Помилка при копіюванні файлу %s до буферу обміну.";
        report.getMessages().add(String.format(log, formatter.format(Date.from(Instant.now())), filepath));
        return this;
    }

    public ReportBuilder logCopyTxtFilesOperation(String directory, boolean ifSuccess) {
        String log = ifSuccess ? "%s Зміни. Скопійовано текстові файлі з директорії %s до буферу обміну." :
                                 "%s Помилка при копіюванні текстових файлів з директорії %s до буферу обміну.";
        report.getMessages().add(String.format(log, formatter.format(Date.from(Instant.now())), directory));
        return this;
    }

    public ReportBuilder logCutFileOperation(String filepath, boolean ifSuccess) {
        String log = ifSuccess ? "%s Зміни. Вилучено файл %s" :
                                 "%s Помилка при вилученні файлу %s";
        report.getMessages().add(String.format(log, formatter.format(Date.from(Instant.now())), filepath));
        return this;
    }

    public ReportBuilder logDeleteFileOperation(String filepath, boolean ifSuccess) {
        String log = ifSuccess ? "%s Зміни. Видалено файл %s" :
                                 "%s Помилка при видаленні файлу %s";
        report.getMessages().add(String.format(log, formatter.format(Date.from(Instant.now())), filepath));
        return this;
    }

    public ReportBuilder logNewFileOperation(String filepath, boolean ifSuccess) {
        String log = ifSuccess ? "%s Зміни. Створено файл %s" :
                                 "%s Помилка при створенні файлу %s";
        report.getMessages().add(String.format(log, formatter.format(Date.from(Instant.now())), filepath));
        return this;
    }

    public ReportBuilder logPasteFileOperation(String filepath, boolean ifSuccess) {
        String log = ifSuccess ? "%s Зміни. Вставлено файл %s" :
                                 "%s Помилка при вставленні файлу.";
        report.getMessages().add(String.format(log, formatter.format(Date.from(Instant.now())), filepath));
        return this;
    }

    public ReportBuilder logRenameFileOperation(String oldFilepath, String newFilename, boolean ifSuccess) {
        String log = ifSuccess ? "%s Зміни. Перейменовано файл %s у %s" :
                                 "%s Помилка при перейменуванні файлу %s у %s";
        report.getMessages().add(String.format(log, formatter.format(Date.from(Instant.now())), oldFilepath, newFilename));
        return this;
    }

    public ReportBuilder logEnterDirectoryOperation(String filepath) {
        report.getMessages().add(String.format("%s Перехід. Відкрито директорію %s", formatter.format(Date.from(Instant.now())), filepath));
        return this;
    }

    public ReportBuilder logLeaveDirectoryOperation(String filepath) {
        report.getMessages().add(String.format("%s Перехід. Закрито директорію %s", formatter.format(Date.from(Instant.now())), filepath));
        return this;
    }

    public Report build() {
        report.setEnding(String.format("Дякуємо, що були з нами! На все добре! %s", formatter.format(Date.from(Instant.now()))));
        return report;
    }
}
