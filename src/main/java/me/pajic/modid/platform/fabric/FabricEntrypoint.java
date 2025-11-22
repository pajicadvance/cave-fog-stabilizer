package me.pajic.modid.platform.fabric;

//? fabric {

import me.pajic.modid.ModTemplate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

@SuppressWarnings("unused")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		ModTemplate.onInitialize();
		initConditionalCommonResources();
	}

	private void initConditionalCommonResources() {
		FabricLoader.getInstance().getModContainer(ModTemplate.MOD_ID).ifPresent(modContainer ->
				ResourceManagerHelper.registerBuiltinResourcePack(
						ModTemplate.id(ModTemplate.PACK_VERSION + "/dp"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				)
		);
	}
}
//?}
