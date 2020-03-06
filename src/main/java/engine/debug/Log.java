package engine.debug;

public class Log {

	public static void error(String message) {
		System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "ERROR:" + ConsoleColors.RESET + ConsoleColors.RED + message + ConsoleColors.RESET);
	}

	public static void error(String message, String additionalInfo) {
		System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "ERROR: " + ConsoleColors.RESET + ConsoleColors.RED + message + ConsoleColors.RESET);
		System.out.println(ConsoleColors.RED + " " + additionalInfo + ConsoleColors.RESET);
	}

	public static void warn(String message) {
		System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "WARNING:" + ConsoleColors.RESET + ConsoleColors.YELLOW + message + ConsoleColors.RESET);
	}

	public static void warn(String message, String additionalInfo) {
		System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "WARNING: " + ConsoleColors.RESET + ConsoleColors.YELLOW + message + ConsoleColors.RESET);
		System.out.println(ConsoleColors.RED + " " + additionalInfo + ConsoleColors.RESET);
	}

	public static void info(String message) {
		System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "INFO:" + ConsoleColors.RESET + ConsoleColors.WHITE + message + ConsoleColors.RESET);
	}

}
