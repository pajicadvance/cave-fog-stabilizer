package me.pajic.cave_fog_stabilizer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.pajic.cave_fog_stabilizer.CFS;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;

public class CFSOptions implements StorageEventHandler {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
	public boolean shouldStabilizeFog = true;
	public int standardCaveFogBrightness = 0;
	public int customCaveFogBrightness = 20;
	public int fogTransitionTime = 5;
	private File file;

	public static CFSOptions load(File file) {
		CFSOptions config;

		if (file.exists()) {
			try (FileReader reader = new FileReader(file)) {
				config = GSON.fromJson(reader, CFSOptions.class);
			} catch (Exception e) {
				CFS.LOGGER.error("Could not parse config, falling back to defaults!", e);
				config = new CFSOptions();
			}
		} else {
			config = new CFSOptions();
		}

		config.file = file;
		config.writeChanges();

		return config;
	}

	public void writeChanges() {
		File dir = file.getParentFile();

		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException("Could not create parent directories");
			}
		} else if (!dir.isDirectory()) {
			throw new RuntimeException("The parent file is not a directory");
		}

		try (FileWriter writer = new FileWriter(file)) {
			GSON.toJson(this, writer);
		} catch (IOException e) {
			throw new RuntimeException("Could not save configuration file", e);
		}
	}

	@Override
	public void afterSave() {
		writeChanges();
	}

	public boolean getShouldStabilizeFog() {
		return shouldStabilizeFog;
	}

	public void setShouldStabilizeFog(boolean shouldStabilizeFog) {
		this.shouldStabilizeFog = shouldStabilizeFog;
	}

	public int getStandardCaveFogBrightness() {
		return standardCaveFogBrightness;
	}

	public void setStandardCaveFogBrightness(int standardCaveFogBrightness) {
		this.standardCaveFogBrightness = standardCaveFogBrightness;
	}

	public int getCustomCaveFogBrightness() {
		return customCaveFogBrightness;
	}

	public void setCustomCaveFogBrightness(int customCaveFogBrightness) {
		this.customCaveFogBrightness = customCaveFogBrightness;
	}

	public int getFogTransitionTime() {
		return fogTransitionTime;
	}

	public void setFogTransitionTime(int fogTransitionTime) {
		this.fogTransitionTime = fogTransitionTime;
	}
}
