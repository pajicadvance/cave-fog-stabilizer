package me.pajic.modid.platform.fabric;

//? fabric {

import me.pajic.modid.ModTemplate;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.modid.mixson.DataPatches;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		DataPatches.init();
		ModTemplate.onInitialize();
	}
}
//?}
