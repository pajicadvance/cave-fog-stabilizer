package me.pajic.cave_fog_stabilizer.config;

import me.pajic.cave_fog_stabilizer.CFS;

public class ConfigHolder {
	private static CFSOptions CONFIG;

	public static CFSOptions options() {
		if (CONFIG == null) init();
		return CONFIG;
	}

	public static void init() {
		CONFIG = CFSOptions.load(CFS.xplat().configDir().resolve("cave-fog-stabilizer-options.json").toFile());
	}
}
