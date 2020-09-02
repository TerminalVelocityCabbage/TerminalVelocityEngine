package engine.command;

import engine.entity.Player;

public interface CommandCallable {

	/**
	 * @param player that executed the command
	 * @param arguments the optional arguments present in the command
	 * @return command result with success and optional feedback
	 */
	CommandResult execute(Player player, String arguments);

}
