package com.terminalvelocitycabbage.engine.debug;

import org.fusesource.jansi.AnsiConsole;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.DeflaterOutputStream;

import static org.fusesource.jansi.Ansi.ansi;

public class Logger {

    private final String nameSpace;
    private ArrayList<LogMessage> messageQueue;

    private Date date;

    private boolean showTimestamp = false;
    private boolean showAdditionalInfo = true;
    private boolean debugMode = false;
    private boolean showNamespace = false;

    public Logger(String nameSpace) {
        this.nameSpace = nameSpace;
        this.messageQueue = new ArrayList<>();
        this.date = new Date(System.currentTimeMillis());
    }

    protected void queueMessage(LogLevel logLevel, String message) {
        messageQueue.add(new LogMessage(logLevel, showNamespace ? "[" + nameSpace + "]" : "" + " " + message));
    }

    protected void queueAndPrint(LogLevel level, String message) {
        if (level == LogLevel.DEBUG && !debugMode) return;
        queueMessage(level, level.prefix + ": " + message);
        date.setTime(System.currentTimeMillis());
        AnsiConsole.out().println( ansi().fg(level.color).a(showTimestamp ? "[" + date.toString() + "]" : "").a(level.prefix + ": ").a(showNamespace ? "[" + nameSpace + "]" : "").reset().a(message).reset() );
    }

    protected void queueAndPrint(LogLevel level, String message, String additionalInfo) {
        if (level == LogLevel.DEBUG && !debugMode) return;
        date.setTime(System.currentTimeMillis());
        queueMessage(level, level.prefix + ": " + message);
        AnsiConsole.out().println( ansi().fg(level.color).a(showTimestamp ? "[" + date.toString() + "]" : "").a(level.prefix + ": ").a(showNamespace ? "[" + nameSpace + "]" : "").reset().a(message).reset() );
        if (showAdditionalInfo || debugMode) {
            queueMessage(level, additionalInfo);
            AnsiConsole.out().println( ansi().fg(level.color).a(showTimestamp ? "[" + date.toString() + "]" : "").a(showNamespace ? "[" + nameSpace + "]" : "").a(additionalInfo).reset() );
        }
    }

    protected void queueAndPrint(LogLevel level, String title, Throwable throwable) {
        Arrays.stream(throwable.getStackTrace()).sequential().forEach(stackTraceElement -> queueMessage(level, stackTraceElement.toString()));
        queueMessage(level, level.prefix + ": " + title);
        throwable.printStackTrace(AnsiConsole.err());
        date.setTime(System.currentTimeMillis());
        AnsiConsole.err().println();
        AnsiConsole.err().println( ansi().fg(level.color).a(showTimestamp ? "[" + date.toString() + "]" : "").a(level.prefix + ": ").a(showNamespace ? "[" + nameSpace + "]" : "").reset().a(title).reset() );
    }

    protected void queueAndPrint(LogLevel level, String title, String additionalInfo, Throwable throwable) {
        Arrays.stream(throwable.getStackTrace()).sequential().forEach(stackTraceElement -> queueMessage(level, stackTraceElement.toString()));
        queueMessage(level, level.prefix + ": " + title);
        throwable.printStackTrace(AnsiConsole.err());
        date.setTime(System.currentTimeMillis());
        AnsiConsole.err().println();
        AnsiConsole.err().println( ansi().fg(level.color).a(showTimestamp ? "[" + date.toString() + "]" : "").a(level.prefix + ": ").a(showNamespace ? "[" + nameSpace + "]" : "").reset().a(title).reset() );
        if (showAdditionalInfo || debugMode) {
            queueMessage(level, additionalInfo);
            AnsiConsole.err().println( ansi().fg(level.color).a(showTimestamp ? "[" + date.toString() + "]" : "").a(showNamespace ? "[" + nameSpace + "]" : "").a(additionalInfo).reset() );
        }
    }

    public Logger timestamp(boolean showTimestamp) {
        this.showTimestamp = showTimestamp;
        return this;
    }

    public Logger additionalInfo(boolean showAdditionalInfo) {
        this.showAdditionalInfo = showAdditionalInfo;
        return this;
    }

    public Logger debugMode(boolean debugMode) {
        this.debugMode = debugMode;
        return this;
    }

    public Logger showNamespace(boolean showNamespace) {
        this.showNamespace = showNamespace;
        return this;
    }

    public void createLog(boolean crash) {

        //The location of the latest log file
        Path latest = Paths.get("logs" + File.separator + "latest.log");

        //If this is a crash log we want to create a new file in a different directory
        if (crash) {
            return;
        }

        //Check to see if there already exists a latest.log and if so back it pu
        if (Files.exists(latest)) {

            //The old latest log
            File oldLog = latest.toFile();
            //Destination for the backpu log
            File backedUpLog = new File("logs" + File.separator + System.currentTimeMillis() + ".logbackup");

            try {
                FileInputStream fileToCompress = new FileInputStream(oldLog);
                FileOutputStream fileOutputStream = new FileOutputStream(backedUpLog);

                //The file deflater (to compress the file)
                DeflaterOutputStream deflater = new DeflaterOutputStream(fileOutputStream);

                //Write the old log data to the backup file
                int data;
                while ((data = fileToCompress.read()) != -1) {
                    deflater.write(data);
                }
                fileToCompress.close();
                deflater.close();

                //Delete the old latest.log file
                latest.toFile().delete();
            } catch (FileNotFoundException e) {
                Log.crash("Could not create compressed backup file", e);
            } catch (IOException e) {
                Log.crash("Could not read original file", e);
            }
        }

        //Create a new latest log file
        File logFile = Paths.get("logs" + File.separator + "latest.log").toFile();
        try {
            //Make sure that the logs directory exists
            logFile.getParentFile().mkdirs();
            //Create a new file
            boolean createSuccess = logFile.createNewFile();
            if (!createSuccess) {
                Log.crash("Could not create log file", new IOException());
            }
            //Write the new file nicely
            FileWriter fileWriter = new FileWriter(logFile);
            for (LogMessage message : messageQueue) {
                fileWriter.write(message.toString() + System.lineSeparator());
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            Log.crash("Could not write latest log file", e);
        }
    }
}
