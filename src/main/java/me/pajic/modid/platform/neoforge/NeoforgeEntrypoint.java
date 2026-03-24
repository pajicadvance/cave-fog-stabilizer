package me.pajic.modid.platform.neoforge;

//? neoforge {

/*import me.pajic.modid.ModTemplate;
import me.pajic.modid.mixson.DataPatches;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ModTemplate.MOD_ID)
@EventBusSubscriber(modid = ModTemplate.MOD_ID)
public class NeoforgeEntrypoint {

	@SubscribeEvent
	private static void onCommonSetup(FMLCommonSetupEvent event) {
		ModTemplate.onInitialize();
	}

	@SubscribeEvent
	private static void initDataPatches(RegisterEvent event) {
		DataPatches.init();
	}
}
*///?}
