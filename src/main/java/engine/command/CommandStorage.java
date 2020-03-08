package engine.command;

import java.util.ArrayList;

public class CommandStorage {

	private static ArrayList<Command> commands = new ArrayList<>();

	public void addCommand(Command command) {
		commands.add(command);
	}

	public static ArrayList<Command> getCommands() {
		return commands;
	}

}
