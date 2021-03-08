package com.terminalvelocitycabbage.engine.debug;

import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Log {

	public static void error(Object message) {
		AnsiConsole.out().println( ansi().fg(RED).a("[ERROR]: ").reset().a(String.valueOf(message)).reset() );
	}

	public static void error(Object message, String additionalInfo) {
		AnsiConsole.out().println( ansi().fg(RED).a("[ERROR]: ").reset().a(String.valueOf(message)).reset() );
		AnsiConsole.out().println( ansi().fg(RED).a(additionalInfo).reset() );
	}

	public static void warn(Object message) {
		AnsiConsole.out().println( ansi().fg(YELLOW).a("[WARNING]: ").reset().a(String.valueOf(message)).reset() );
	}

	public static void warn(Object message, String additionalInfo) {
		AnsiConsole.out().println( ansi().fg(YELLOW).a("[WARNING]: ").reset().a(String.valueOf(message)).reset() );
		AnsiConsole.out().println( ansi().fg(YELLOW).a(additionalInfo).reset() );
	}

	public static void info(Object message) {
		AnsiConsole.out().println(ansi().fg(BLUE).a("[INFO]: ").reset().a(String.valueOf(message)).reset());
	}

	public static void info(Object message, String additionalInfo) {
		AnsiConsole.out().println( ansi().fg(BLUE).a("[INFO]: ").reset().a(String.valueOf(message)).reset() );
		AnsiConsole.out().println( ansi().fg(BLUE).a(additionalInfo).reset() );
	}

	public static void crash(Object message, Throwable throwable) {
		crash(throwable.getMessage(), message, throwable);
	}

	public static void crash(String title, Object message, Throwable throwable) {
		throwable.printStackTrace(AnsiConsole.err());
		AnsiConsole.err().println();
		AnsiConsole.err().println( ansi().fg(RED).a("[CRASH]: ").reset().a(title).reset() );
		throw new RuntimeException(String.valueOf(message), throwable);
	}

	public static void crash(String title, Object message, String additionalInfo, Throwable throwable) {
		throwable.printStackTrace(AnsiConsole.err());
		AnsiConsole.err().println();
		AnsiConsole.err().println( ansi().fg(RED).a("[CRASH]: ").reset().a(title).reset() );
		AnsiConsole.err().println( ansi().fg(RED).a(additionalInfo).reset() );
		throw new RuntimeException(String.valueOf(message), throwable);
	}

}
