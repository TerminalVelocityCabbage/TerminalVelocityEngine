package com.terminalvelocitycabbage.engine.debug;

import java.sql.Timestamp;

public class LogMessage {

    private String timestamp;
    private LogLevel level;
    private String message;
    private boolean clean;

    public LogMessage(LogLevel logLevel, String message, boolean clean) {
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        this.level = logLevel;
        this.message = message;
        this.clean = clean;
    }

    public LogMessage(LogLevel logLevel, String message) {
        this(logLevel, message, false);
    }

    @Override
    public String toString() {
        return clean ? message : "[" + timestamp + "][" + level + "]: " + message;
    }
}
