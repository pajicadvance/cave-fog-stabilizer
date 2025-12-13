package me.pajic.cave_fog_stabilizer.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;

public class ConfigHolder {
	private static final ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new, RegisterType.CLIENT);

	public static boolean shouldStabilizeFog() {
		return CONFIG.stabilizeCaveFog.get();
	}

	public static float getFogTransitionTime() {
		return CONFIG.fogTransitionTime.get() * 20;
	}

	public static int getStabilizedCaveFogColor() {
		return CONFIG.stabilizedCaveFogColor.get().toInt();
	}

	public static void init() {}
}
