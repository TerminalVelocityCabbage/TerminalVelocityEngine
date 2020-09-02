package engine.debug;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Log {

	public static void error(String message) {
		System.out.println( ansi().fg(RED).a("ERROR: ").reset().a(message).reset() );
	}

	public static void error(String message, String additionalInfo) {
		System.out.println( ansi().fg(RED).a("ERROR: ").reset().a(message).reset() );
		System.out.println( ansi().fg(RED).a(additionalInfo).reset() );
	}

	public static void warn(String message) {
		System.out.println( ansi().fg(YELLOW).a("WARNING: ").reset().a(message).reset() );
	}

	public static void warn(String message, String additionalInfo) {
		System.out.println( ansi().fg(YELLOW).a("WARNING: ").reset().a(message).reset() );
		System.out.println( ansi().fg(YELLOW).a(additionalInfo).reset() );
	}

	public static void info(String message) {
		System.out.println( ansi().fg(BLUE).a("INFO: ").reset().a(message).reset() );
	}

}
