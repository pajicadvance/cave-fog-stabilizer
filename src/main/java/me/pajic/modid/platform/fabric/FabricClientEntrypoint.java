package me.pajic.modid.platform.fabric;

//? fabric {

import me.pajic.modid.ModTemplate;
import net.fabricmc.api.ClientModInitializer;

public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModTemplate.onInitializeClient();
	}

}
//?}
