package me.pajic.cave_fog_stabilizer;

import me.pajic.cave_fog_stabilizer.config.ConfigHolder;
//? if fabric
import net.fabricmc.loader.api.FabricLoader;
//? if neoforge
//import net.neoforged.fml.ModList;

@SuppressWarnings("LoggingSimilarMessage")
public class CFS {

	public static final String MOD_ID = /*$ mod_id*/ "cave_fog_stabilizer";
	public static final boolean CONFIG_AVAILABLE =
			//? if fabric
			FabricLoader.getInstance().isModLoaded("fzzy_config");
			//? if neoforge
			//ModList.get().isLoaded("fzzy_config");

	public static void onInitializeClient() {
		if (CONFIG_AVAILABLE) ConfigHolder.init();
	}
}
