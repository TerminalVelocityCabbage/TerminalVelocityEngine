package engine.io;

import engine.configs.Config;
import engine.debug.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveManager {

	public static boolean createSaveDirectory(String saveName) {
		File file = new File("saves" + File.separator + saveName);
		boolean success = file.mkdirs();
		if (success) {
			Log.info("Save Directory Created with great success!");
			return true;
		} else {
			Log.error("The save directory encountered some error");
			return false;
		}
	}

	public static boolean createConfigFile(Config config, String saveName) {
		Path file = Paths.get("saves" + File.separator + saveName + File.separator + config.getFileName() + ".json");
		try {
			Files.write(file, config.asWritableArray(), StandardCharsets.UTF_8);
			Log.info("Created default config");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean checkForSaveDirectory(String saveName) {
		File file = new File("saves" + File.separator + saveName);
		return file.exists();
	}

}
