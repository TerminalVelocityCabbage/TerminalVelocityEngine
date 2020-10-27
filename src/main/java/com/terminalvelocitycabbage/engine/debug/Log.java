package com.terminalvelocitycabbage.debug;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Log {

	public static void error(Object message) {
		System.out.println( ansi().fg(RED).a("ERROR: ").reset().a(String.valueOf(message)).reset() );
	}

	public static void error(Object message, String additionalInfo) {
		System.out.println( ansi().fg(RED).a("ERROR: ").reset().a(String.valueOf(message)).reset() );
		System.out.println( ansi().fg(RED).a(additionalInfo).reset() );
	}

	public static void warn(Object message) {
		System.out.println( ansi().fg(YELLOW).a("WARNING: ").reset().a(String.valueOf(message)).reset() );
	}

	public static void warn(Object message, String additionalInfo) {
		System.out.println( ansi().fg(YELLOW).a("WARNING: ").reset().a(String.valueOf(message)).reset() );
		System.out.println( ansi().fg(YELLOW).a(additionalInfo).reset() );
	}

	public static void info(Object message) {
		System.out.println( ansi().fg(BLUE).a("INFO: ").reset().a(String.valueOf(message)).reset() );
	}

	public static void info(Object message, String additionalInfo) {
		System.out.println( ansi().fg(BLUE).a("INFO: ").reset().a(String.valueOf(message)).reset() );
		System.out.println( ansi().fg(BLUE).a(additionalInfo).reset() );
	}

}
