package com.terminalvelocitycabbage.engine.command;

import java.util.ArrayList;

public class CommandStorage {

	private static ArrayList<Command> commands = new ArrayList<>();

	public void addCommand(Command command) {
		commands.add(command);
	}

	public static ArrayList<Command> getCommands() {
		return commands;
	}

	public static ArrayList<String> getCommandTree(Command command, String currentText) {
		var possibilities = new ArrayList<String>();
		for (Command possible : command.getSubCommands()) {
			for (String alias : possible.getAliases()) {
				if (alias.startsWith(currentText)) {
					possibilities.add(alias);
				}
			}
		}
		return possibilities;
	}

}
