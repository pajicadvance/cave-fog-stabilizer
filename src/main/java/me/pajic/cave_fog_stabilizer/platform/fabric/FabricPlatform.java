package me.pajic.cave_fog_stabilizer.platform.fabric;

//? if fabric {

import me.pajic.cave_fog_stabilizer.platform.Platform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricPlatform implements Platform {

	@Override
	public Path configDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}
}
//?}
