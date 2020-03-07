package engine.saves;

import engine.debug.Log;

import java.io.File;

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

	public static boolean checkForSaveDirectory(String saveName) {
		File file = new File("saves" + File.separator + saveName);
		return file.exists();
	}

}
