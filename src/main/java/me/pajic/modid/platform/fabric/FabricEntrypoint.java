package me.pajic.modid.platform.fabric;

//? fabric {

import me.pajic.modid.ModTemplate;
import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		ModTemplate.onInitialize();
	}
}
//?}
