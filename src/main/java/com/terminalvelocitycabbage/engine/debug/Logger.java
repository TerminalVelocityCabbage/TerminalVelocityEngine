package com.terminalvelocitycabbage.engine.debug;

import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
}
