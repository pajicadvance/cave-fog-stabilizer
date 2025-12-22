package me.pajic.cave_fog_stabilizer;

import me.pajic.cave_fog_stabilizer.config.ConfigHolder;
import me.pajic.cave_fog_stabilizer.platform.Platform;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? fabric {
import me.pajic.cave_fog_stabilizer.platform.fabric.FabricPlatform;
//?} neoforge {
/*import me.pajic.cave_fog_stabilizer.platform.neoforge.NeoforgePlatform;
 *///?}

@SuppressWarnings("LoggingSimilarMessage")
public class CFS {

	public static final String MOD_ID = /*$ mod_id*/ "cave_fog_stabilizer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Platform PLATFORM = createPlatformInstance();
	public static final boolean CONFIG_AVAILABLE = PLATFORM.isModLoaded("sodium");

	public static void onInitializeClient() {
		if (CONFIG_AVAILABLE) ConfigHolder.init();
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		 *///?}
	}

	public static Platform xplat() {
		return PLATFORM;
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
