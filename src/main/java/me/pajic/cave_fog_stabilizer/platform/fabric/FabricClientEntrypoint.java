package me.pajic.cave_fog_stabilizer.platform.fabric;

//? fabric {

import me.pajic.cave_fog_stabilizer.CFS;
import net.fabricmc.api.ClientModInitializer;

@SuppressWarnings("unused")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		CFS.onInitializeClient();
	}
}
//?}
