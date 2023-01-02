package com.terminalvelocitycabbage.engine.debug;

import com.terminalvelocitycabbage.engine.client.ClientBase;

public class Log {

	public static void info(Object message) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.INFO, String.valueOf(message));
	}

	public static void info(Object message, String additionalInfo) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.INFO, String.valueOf(message), additionalInfo);
	}

	public static void warn(Object message) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.WARN, String.valueOf(message));
	}

	public static void warn(Object message, String additionalInfo) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.WARN, String.valueOf(message), additionalInfo);
	}

	public static void error(Object message) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.ERROR, String.valueOf(message));
	}

	public static void error(Object message, String additionalInfo) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.ERROR, String.valueOf(message), additionalInfo);
	}

	public static void debug(Object message) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.DEBUG, String.valueOf(message));
	}

	public static void debug(Object message, String additionalInfo) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.DEBUG, String.valueOf(message), additionalInfo);
	}

	public static void crash(Object message, Throwable throwable) {
		crash(throwable.getMessage(), message, throwable);
	}

	public static void crash(String title, Object message, Throwable throwable) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.CRASH, title, throwable);
		ClientBase.getInstance().getLogger().createLog(true);
		throw new RuntimeException(String.valueOf(message), throwable);
	}

	public static void crash(String title, Object message, String additionalInfo, Throwable throwable) {
		ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.CRASH, title, additionalInfo, throwable);
		ClientBase.getInstance().getLogger().createLog(true);
		throw new RuntimeException(String.valueOf(message), throwable);
	}

}
