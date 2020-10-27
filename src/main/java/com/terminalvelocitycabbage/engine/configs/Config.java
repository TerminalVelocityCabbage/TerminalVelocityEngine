package com.terminalvelocitycabbage.engine.configs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

	private final String fileName;
	private final String directory;
	private HashMap<String, String> options;

	private Config(Builder builder) {
		this.fileName = builder.filename;
		this.directory = builder.directory;
		this.options = builder.options;
	}

	public static Config.Builder builder() {
		return new Config.Builder();
	}

	public boolean save() {
		try {
			Path file;
			if (this.getDirectory() != null) {
				file = Paths.get(this.getDirectory() + File.separator + this.getFileName() + ".config");
			} else {
				file = Paths.get(this.getFileName() + ".config");
			}
			Files.write(file, this.asWritableArray(), StandardCharsets.UTF_8);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static Config buildFromFile(Path path) throws IOException {
		List<String> realContents = Files.readAllLines(path);
		Config.Builder builder = Config.builder();
		for (String line : realContents) {

			//Ignore Tabs
			line = line.replace("\t", "");

			//If the line is not a comment
			if (!line.startsWith("#")) {
				String[] actualLine = line.split(":");
				builder.addKey(actualLine[0].trim(), actualLine[1].trim());
			}
		}
		return builder.build();
	}

	public static Config load(String path, String fileName) throws IOException {
		return buildFromFile(Paths.get(path + File.separator + fileName + ".config"));
	}

	public static Config load(String fileName) throws IOException {
		return buildFromFile(Paths.get(fileName + ".config"));
	}

	public static class Builder {

		private Builder() {}

		private String filename;
		private String directory;
		private HashMap<String, String> options = new HashMap<>();

		public Config.Builder setFileName(String fileName) {
			this.filename = fileName;
			return this;
		}

		public Config.Builder setPath(String path) {
			this.directory = path;
			return this;
		}

		//TODO make this check for duplicate keys
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

	public String getDirectory() {
		return directory;
	}

	public HashMap<String, String> getOptions() {
		return options;
	}

	public List<String> asWritableArray() {
		List<String> writeableArray = new ArrayList<>();

		//TODO make a #addComment option to builder instead of this
		writeableArray.add("#this is the beginning of the " + this.fileName + " configuration file.");

		for (Map.Entry element : this.options.entrySet()) {
			writeableArray.add("\t" + element.getKey() + ": " + element.getValue());
		}

		return writeableArray;
	}
}
