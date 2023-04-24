package devs.mrp.springturkey.services.oauth.impl;

public enum EmailCommand {
	UPDATE_PASSWORD("UPDATE_PASSWORD");

	private String command;

	EmailCommand(String command) {
		this.command = command;
	}

	public String getCommand() {
		return this.command;
	}
}
