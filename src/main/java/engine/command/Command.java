package engine.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command {

	private List<String> aliases;
	private List<Command> subCommands;
	private final CommandCallable executable;

	private Command(Builder builder) {
		this.aliases = builder.aliases;
		this.subCommands = builder.subCommands;
		this.executable = builder.executable;
	}

	public static Command.Builder builder() {
		return new Command.Builder();
	}

	public static class Builder {

		private Builder() {}

		private List<String> aliases = new ArrayList<>();
		private List<Command> subCommands = new ArrayList<>();
		private CommandCallable executable;

		public Command.Builder alias(String... aliases) {
			this.aliases = Arrays.asList(aliases);
			return this;
		}

		public Command.Builder addSubCommand(Command command) {
			this.subCommands.add(command);
			return this;
		}

		public Command.Builder executes(CommandCallable executable) {
			this.executable = executable;
			return this;
		}

		public Command build() {
			return new Command(this);
		}
	}

	public List<String> getAliases() {
		return aliases;
	}

	public List<Command> getSubCommands() {
		return subCommands;
	}

	public CommandCallable getExecutable() {
		return executable;
	}

}
