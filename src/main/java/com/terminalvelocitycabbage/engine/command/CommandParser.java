package com.terminalvelocitycabbage.engine.command;

import com.terminalvelocitycabbage.engine.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandParser {

	public static CommandResult parse(String input, Player player) {

		//Get rid of the command prefix
		input = input.trim().substring(1);
		List<String> arguments = Arrays.asList(input.split(" "));

		//Test if command root exists
		Command currentCommand = getCommandFromAlias(arguments.get(0), CommandStorage.getCommands());
		if (currentCommand != null) {
			Command endTreeSubCommand;
			int argPos = 1;
			//Loop through arguments to find subcommands
			for (String argument : arguments.subList(1, arguments.size())) {
				endTreeSubCommand = getCommandFromAlias(argument, currentCommand.getSubCommands());
				if (endTreeSubCommand == null) {
					break;
				} else {
					currentCommand = endTreeSubCommand;
					argPos++;
				}
			}
			//execute the command and return the result.
			return currentCommand.getExecutable().execute(player, String.join(" ", arguments.subList(argPos, arguments.size())));
		} else {
			return CommandResult.failure("That command doesn't exist.");
		}
	}

	private static Command getCommandFromAlias(String alias, List<Command> commands) {
		if (commands == null) {
			return null;
		}
		for (Command command : commands) {
			for (String currentAlias : command.getAliases()) {
				if (alias.startsWith(currentAlias)) {
					return command;
				}
			}
		}
		return null;
	}

}
