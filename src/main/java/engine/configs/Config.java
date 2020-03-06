package engine.configs;

import java.util.*;

public class Config {

	private final String fileName;
	private HashMap<String, String> options;

	private Config(Builder builder) {
		this.fileName = builder.filename;
		this.options = builder.options;
	}

	public static Config.Builder builder() {
		return new Config.Builder();
	}

	public static class Builder {

		private Builder() {}

		private String filename;
		private HashMap<String, String> options = new HashMap<>();

		public Config.Builder setFileName(String fileName) {
			this.filename = fileName;
			return this;
		}

		public Config.Builder addKey(String key, String defaultValue) {
			this.options.put(key, defaultValue);
			return this;
		}

		public Config build() {
			return new Config(this);
		}

	}

	public String getFileName() {
		return fileName;
	}

	public List<String> asWritableArray() {
		List<String> writeableArray = new ArrayList<>();

		writeableArray.add("#this is the beginning of the " + this.fileName + " configuration file.");

		for (Map.Entry element : this.options.entrySet()) {
			writeableArray.add("\t" + element.getKey() + ": " + element.getValue());
		}

		return writeableArray;
	}
}
