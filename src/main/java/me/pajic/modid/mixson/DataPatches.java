package me.pajic.modid.mixson;

import me.pajic.modid.ModTemplate;
import net.ramixin.mixson.Mixson;
import net.ramixin.mixson.enums.DebugOption;

public class DataPatches {

	private static boolean initialized = false;

	public static void init() {
		if (!initialized) {
			if (ModTemplate.xplat().isDebug()) {
				Mixson.enableDebugOption(DebugOption.BASIC_LOGGING);
				Mixson.enableDebugOption(DebugOption.EXTRA_LOGGING);
				Mixson.enableDebugOption(DebugOption.EXPORT_PATCHED_FILE);
			}

			initialized = true;
		}
	}
}
