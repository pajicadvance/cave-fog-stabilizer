package me.pajic.cave_fog_stabilizer.config;

//? >=1.21.11 {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.cave_fog_stabilizer.CFS;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.network.chat.Component;

//? neoforge {
/*import net.caffeinemc.mods.sodium.api.config.ConfigEntryPointForge;

@ConfigEntryPointForge(CFS.MOD_ID)
*///?}
@SuppressWarnings("unused")
@Entrypoint("sodium:config_api_user")
public class ModSodiumConfig implements ConfigEntryPoint {

	@Override
	public void registerConfigLate(ConfigBuilder builder) {
		builder.registerOwnModOptions()
				.setName("Cave Fog Stabilizer")
				.setColorTheme(builder.createColorTheme().setBaseThemeRGB(0xdaad93))
				.addPage(builder.createOptionPage()
						.setName(Component.translatable("cave_fog_stabilizer.config.settings"))
						.addOption(builder.createBooleanOption(CFS.id("stabilize_cave_fog"))
								.setName(Component.translatable("cave_fog_stabilizer.config.stabilizeCaveFog"))
								.setTooltip(Component.translatable("cave_fog_stabilizer.config.stabilizeCaveFog.desc"))
								.setDefaultValue(true)
								.setBinding(bl -> CFS.CONFIG.stabilizeCaveFog.validateAndSet(bl), () -> CFS.CONFIG.stabilizeCaveFog.get())
								.setStorageHandler(CFS.CONFIG::save))
						.addOption(builder.createIntegerOption(CFS.id("standard_cave_fog_brightness"))
								.setName(Component.translatable("cave_fog_stabilizer.config.standardCaveFogBrightness"))
								.setTooltip(Component.translatable("cave_fog_stabilizer.config.standardCaveFogBrightness.desc"))
								.setDefaultValue(0)
								.setValueFormatter(value -> Component.literal(value + "%"))
								.setRange(0, 100, 1)
								.setBinding(i -> CFS.CONFIG.standardCaveFogBrightness.validateAndSet(i), () -> CFS.CONFIG.standardCaveFogBrightness.get())
								.setStorageHandler(CFS.CONFIG::save))
						.addOption(builder.createIntegerOption(CFS.id("custom_cave_fog_brightness"))
								.setName(Component.translatable("cave_fog_stabilizer.config.customCaveFogBrightness"))
								.setTooltip(Component.translatable("cave_fog_stabilizer.config.customCaveFogBrightness.desc"))
								.setDefaultValue(20)
								.setValueFormatter(value -> Component.literal(value + "%"))
								.setRange(0, 100, 1)
								.setBinding(i -> CFS.CONFIG.customCaveFogBrightness.validateAndSet(i), () -> CFS.CONFIG.customCaveFogBrightness.get())
								.setStorageHandler(CFS.CONFIG::save))
						.addOption(builder.createIntegerOption(CFS.id("fog_transition_time"))
								.setName(Component.translatable("cave_fog_stabilizer.config.fogTransitionTime"))
								.setTooltip(Component.translatable("cave_fog_stabilizer.config.fogTransitionTime.desc"))
								.setDefaultValue(5)
								.setValueFormatter(value -> Component.translatable("cave_fog_stabilizer.config.seconds", value))
								.setRange(0, 10, 1)
								.setBinding(i -> CFS.CONFIG.fogTransitionTime.validateAndSet(i), () -> CFS.CONFIG.fogTransitionTime.get())
								.setStorageHandler(CFS.CONFIG::save))
				);
	}
}
//?}
