package engine.command;

import engine.entity.Player;

import java.util.List;

public class CommandParser {

	public static CommandResult parse(String input, Player player) {
		//Get rid of the command prefix
		input = input.substring(1);
		for (Command command : CommandStorage.getCommands()) {
			for (String alias : command.getAliases()) {
				return command.getExecutable().execute(player, input);
			}
		}
		return CommandResult.failure("");
	}

}
