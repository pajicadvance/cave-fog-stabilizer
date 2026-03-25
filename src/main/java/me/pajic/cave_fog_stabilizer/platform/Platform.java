package me.pajic.cave_fog_stabilizer.platform;

import java.nio.file.Path;

public interface Platform {

	boolean isModLoaded(String modId);

	Path configDir();
}
