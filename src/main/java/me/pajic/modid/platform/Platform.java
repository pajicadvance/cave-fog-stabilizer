package me.pajic.modid.platform;

public interface Platform {
	boolean isModLoaded(String modId);

	ModLoader loader();

	enum ModLoader {
		FABRIC, NEOFORGE, FORGE
	}
}
