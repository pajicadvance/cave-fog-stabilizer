package me.pajic.cave_fog_stabilizer.platform.fabric;

//? fabric {

import me.pajic.cave_fog_stabilizer.CFS;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		CFS.onInitializeClient();
	}
}
//?}
