package me.pajic.modid.platform.fabric;

//? fabric {

import me.pajic.modid.ModTemplate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

@SuppressWarnings("unused")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModTemplate.onInitializeClient();
		initConditionalClientResources();
	}

	private void initConditionalClientResources() {
		FabricLoader.getInstance().getModContainer(ModTemplate.MOD_ID).ifPresent(modContainer ->
				ResourceManagerHelper.registerBuiltinResourcePack(
						ModTemplate.id(ModTemplate.PACK_VERSION + "/rp"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				)
		);
	}
}
//?}
