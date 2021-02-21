package com.terminalvelocitycabbage.engine.debug;

import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Log {

	public static void error(Object message) {
		AnsiConsole.out.println( ansi().fg(RED).a("[ERROR]: ").reset().a(String.valueOf(message)).reset() );
	}

	public static void error(Object message, String additionalInfo) {
		AnsiConsole.out.println( ansi().fg(RED).a("[ERROR]: ").reset().a(String.valueOf(message)).reset() );
		AnsiConsole.out.println( ansi().fg(RED).a(additionalInfo).reset() );
	}

	public static void warn(Object message) {
		AnsiConsole.out.println( ansi().fg(YELLOW).a("[WARNING]: ").reset().a(String.valueOf(message)).reset() );
	}

	public static void warn(Object message, String additionalInfo) {
		AnsiConsole.out.println( ansi().fg(YELLOW).a("[WARNING]: ").reset().a(String.valueOf(message)).reset() );
		AnsiConsole.out.println( ansi().fg(YELLOW).a(additionalInfo).reset() );
	}

	public static void info(Object message) {
		AnsiConsole.out.println(ansi().fg(BLUE).a("[INFO]: ").reset().a(String.valueOf(message)).reset());
	}

	public static void info(Object message, String additionalInfo) {
		AnsiConsole.out.println( ansi().fg(BLUE).a("[INFO]: ").reset().a(String.valueOf(message)).reset() );
		AnsiConsole.out.println( ansi().fg(BLUE).a(additionalInfo).reset() );
	}

}
