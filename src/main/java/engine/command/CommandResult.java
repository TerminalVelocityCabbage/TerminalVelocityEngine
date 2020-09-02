package engine.command;

public class CommandResult {

	private boolean success;
	private String feedback;

	private CommandResult(boolean success, String feedback) {
		this.success = success;
		this.feedback = feedback;
	}

	public static CommandResult success() {
		return new CommandResult(true, "");
	}

	public static CommandResult success(String feedback) {
		return new CommandResult(true, feedback);
	}

	public static CommandResult failure() {
		return new CommandResult(false, "");
	}

	public static CommandResult failure(String feedback) {
		return new CommandResult(false, feedback);
	}

	public String getFeedback() {
		return feedback;
	}

	public boolean wasSuccessful() {
		return success;
	}
}
