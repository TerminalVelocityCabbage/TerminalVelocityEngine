package com.terminalvelocitycabbage.engine.debug;

import com.terminalvelocitycabbage.engine.client.ClientBase;

public class Log {

	private static boolean testAndFallback(String toPrint) {
		boolean cont = true;
		if (ClientBase.getInstance() == null) {
			System.out.println("Printing with system out instead of with logger since clientbase instance is null");
			cont = false;
		}
		if (cont && ClientBase.getInstance().getLogger() == null) {
			System.out.println("Printing with system out instead of with logger since logger instance is null");
			cont = false;
		}
		if (!cont) {
			System.out.println(toPrint);
		}
		return cont;
	}

	public static void info(Object message) {
		if (testAndFallback(LogLevel.INFO.prefix + " " + message)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.INFO, String.valueOf(message));
		}
	}

	public static void info(Object message, String additionalInfo) {
		if (testAndFallback(LogLevel.INFO.prefix + " " + message + " " + additionalInfo)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.INFO, String.valueOf(message), additionalInfo);
		}
	}

	public static void warn(Object message) {
		if (testAndFallback(LogLevel.WARN.prefix + " " + message)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.WARN, String.valueOf(message));
		}
	}

	public static void warn(Object message, String additionalInfo) {
		if (testAndFallback(LogLevel.WARN.prefix + " " + message + " " + additionalInfo)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.WARN, String.valueOf(message), additionalInfo);
		}
	}

	public static void error(Object message) {
		if (testAndFallback(LogLevel.ERROR.prefix + " " + message)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.ERROR, String.valueOf(message));
		}
	}

	public static void error(Object message, String additionalInfo) {
		if (testAndFallback(LogLevel.ERROR.prefix + " " + message + " " + additionalInfo)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.ERROR, String.valueOf(message), additionalInfo);
		}
	}

	public static void debug(Object message) {
		if (testAndFallback(LogLevel.DEBUG.prefix + " " + message)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.DEBUG, String.valueOf(message));
		}
	}

	public static void debug(Object message, String additionalInfo) {
		if (testAndFallback(LogLevel.DEBUG.prefix + " " + message + " " + additionalInfo)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.DEBUG, String.valueOf(message), additionalInfo);
		}
	}

	public static void crash(Object message, Throwable throwable) {
		crash(throwable.getMessage(), message, throwable);
	}

	public static void crash(String title, Object message, Throwable throwable) {
		if (testAndFallback(LogLevel.DEBUG.prefix + " " + title + " " + message)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.CRASH, title, throwable);
			ClientBase.getInstance().getLogger().createLog(true);
		}
		throw new RuntimeException(String.valueOf(message), throwable);
	}

	public static void crash(String title, Object message, String additionalInfo, Throwable throwable) {
		if (testAndFallback(LogLevel.DEBUG.prefix + " " + title + " " + message + " " + additionalInfo)) {
			ClientBase.getInstance().getLogger().queueAndPrint(LogLevel.CRASH, title, additionalInfo, throwable);
			ClientBase.getInstance().getLogger().createLog(true);
		}
		throw new RuntimeException(String.valueOf(message), throwable);
	}

}
