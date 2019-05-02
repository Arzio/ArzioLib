package com.arzio.arziolib;

import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Works only for GitHub.
 */
public enum UpdateChecker {

	INSTANCE;

	public static final String API_URL = "https://api.github.com/repos/Arzio/ArzioLib/releases";
	private String latestVersionTag;
	private UpdateState state = UpdateState.UP_TO_DATE;

	public UpdateState getState() {
		return this.state;
	}

	/**
	 * Gets the latest version previously retrieved from GitHub. In case of
	 * connection errors, it returns the current plugin version.
	 * 
	 * @return String with the latest plugin version
	 */
	public String getLatestVersionTag() {
		return this.state == UpdateState.FAILED_TO_CHECK ? ArzioLib.getInstance().getDescription().getVersion()
				: this.latestVersionTag;
	}

	public void checkUpdates(CheckMethod method) {
		Logger logger = ArzioLib.getInstance().getLogger();
		logger.info("Checking for ArzioLib updates...");

		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				Plugin plugin = ArzioLib.getInstance();

				try {
					JSONArray updates = (JSONArray) new JSONParser().parse(IOUtils.toString(new URL(API_URL)));
					JSONObject release = (JSONObject) updates.get(0); // Gets the first element of the array (most
																		// updated version)

					// Updates the latest version tag obtained from GitHub
					UpdateChecker.INSTANCE.latestVersionTag = (String) release.get("tag_name");

					boolean isUpToDate = plugin.getDescription().getVersion().equalsIgnoreCase(latestVersionTag);
					if (isUpToDate) {
						UpdateChecker.INSTANCE.state = UpdateState.UP_TO_DATE;
						plugin.getLogger().info("ArzioLib is up-to-date! :3");
					} else {
						UpdateChecker.INSTANCE.state = UpdateState.NEEDS_UPDATE;
						plugin.getLogger().info(
								"There is a new ArzioLib version to download! Download it at https://github.com/Arzio/ArzioLib/releases");
					}
				} catch (Exception e) {
					UpdateChecker.INSTANCE.state = UpdateState.FAILED_TO_CHECK;
					plugin.getLogger().warning(
							"ArzioLib could not get information about the latest updates. Check your firewall or internet connection!");
					e.printStackTrace();
				}
			}
		};

		switch (method) {
		case ASYNC:
			Bukkit.getScheduler().runTaskAsynchronously(ArzioLib.getInstance(), runnable);
			break;
		case SYNC:
			runnable.run();
			break;
		}
	}

	public static enum UpdateState {
		UP_TO_DATE, NEEDS_UPDATE, FAILED_TO_CHECK;
	}

	public static enum CheckMethod {
		/** It will use the current thread to check for updates */
		SYNC,
		/** It will run in a async way, from Bukkit's scheduler */
		ASYNC;
	}
}
