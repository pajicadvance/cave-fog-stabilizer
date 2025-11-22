package me.pajic.modid.platform.neoforge;

//? neoforge {
/*
import me.pajic.modid.ModTemplate;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@EventBusSubscriber(modid = ModTemplate.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {

	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		ModTemplate.onInitializeClient();
	}

	@SubscribeEvent
	private static void initClientResources(AddPackFindersEvent event) {
		event.addPackFinders(
				ModTemplate.id("resourcepacks/" + ModTemplate.PACK_VERSION + "/rp"),
				PackType.CLIENT_RESOURCES,
				Component.literal("Mod " + ModTemplate.PACK_VERSION + " Resource Pack"),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		);
	}
}
*///?}
