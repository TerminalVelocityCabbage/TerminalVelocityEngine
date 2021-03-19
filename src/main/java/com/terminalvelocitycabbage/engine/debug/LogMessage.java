package com.terminalvelocitycabbage.engine.debug;

import java.sql.Timestamp;

public class LogMessage {

    private String timestamp;
    private LogLevel level;
    private String message;

    public LogMessage(LogLevel logLevel, String message) {
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        this.level = logLevel;
        this.message = message;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "][" + level + "]: " + message;
    }
}
