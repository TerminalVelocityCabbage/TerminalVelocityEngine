package engine.server;

import com.github.simplenet.Server;
import engine.configs.Config;
import engine.debug.Log;
import engine.events.EventDispatcher;
import engine.events.server.ServerClientConnectionEvent;
import engine.saves.SaveManager;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.IOException;

public abstract class ServerBase extends EventDispatcher {

	String id;
	Server server;

	public ServerBase(String id) {
		this.id = id;
	}

	public void init() {
		preInit();
		//Enable Console colors
		AnsiConsole.systemInstall();

		//Start up the network listeners
		server = new Server();
	}

	//Basically set the server files up before starting any connections
	private void preInit() {
		//Check for and load or create new server configuration
		Log.info("Server Starting with ID: " + id);
		if (!SaveManager.checkForSaveDirectory(id)) {
			Log.info("No save Directory found for this save, assuming its a new save.");
			if (SaveManager.createSaveDirectory(id)) {
				Config config = Config.builder()
						.setFileName("server")
						.setPath("saves" + File.separator + id)
						.addKey("ip", "localhost")
						.addKey("port", "49056")
						.addKey("motd", "Message of the Day!")
						.build();
				if (config.save()) {
					return;
				}
			}
		} else {
			Log.info("Found existing save with name " + id + " loading that save.");
			return;
		}
		Log.error("Something went wrong during server initialization, the server will not start.");
	}

	//Binds the server and begins allowing connections
	public void start() {

		//dispatch events
		server.onConnect(client -> {
			dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.CONNECT, server, client));
		});

		//Load the server's config file into a usable object
		try {
			Config config = Config.load("saves" + File.separator + this.id , "server");
			//Bind the server to the configured port and IP
			server.bind(config.getOptions().get("ip"),
					Integer.parseInt(config.getOptions().get("port")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Server getServer() {
		return this.server;
	}

}
