package me.pajic.cave_fog_stabilizer.config;

import me.pajic.cave_fog_stabilizer.CFS;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.option.Range;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.network.chat.Component;
//? if neoforge
//import net.caffeinemc.mods.sodium.api.config.ConfigEntryPointForge;

//? if neoforge
//@ConfigEntryPointForge(CFS.MOD_ID)
public class CFSConfig implements ConfigEntryPoint {

	@Override
	public void registerConfigLate(ConfigBuilder builder) {
		builder.registerOwnModOptions()
				.setColorTheme(builder.createColorTheme().setBaseThemeRGB(0xdaad93))
				.addPage(builder.createOptionPage()
						.setName(Component.translatable("cave_fog_stabilizer.config"))
						.addOption(builder.createBooleanOption(CFS.id("stabilize_cave_fog"))
								.setName(Component.translatable("cave_fog_stabilizer.config.stabilizeCaveFog"))
								.setTooltip(Component.translatable("cave_fog_stabilizer.config.stabilizeCaveFog.desc"))
								.setStorageHandler(ConfigHolder.options())
								.setBinding(ConfigHolder.options()::setShouldStabilizeFog, ConfigHolder.options()::getShouldStabilizeFog)
								.setDefaultValue(true)
						)
						.addOption(builder.createIntegerOption(CFS.id("standard_cave_fog_brightness"))
								.setName(Component.translatable("cave_fog_stabilizer.config.standardCaveFogBrightness"))
								.setTooltip(Component.translatable("cave_fog_stabilizer.config.standardCaveFogBrightness.desc"))
								.setStorageHandler(ConfigHolder.options())
								.setBinding(ConfigHolder.options()::setStandardCaveFogBrightness, ConfigHolder.options()::getStandardCaveFogBrightness)
								.setValidator(new Range(0, 100, 1))
								.setValueFormatter(value -> Component.literal(value + "%"))
								.setDefaultValue(0)
						)
						.addOption(builder.createIntegerOption(CFS.id("custom_cave_fog_brightness"))
								.setName(Component.translatable("cave_fog_stabilizer.config.customCaveFogBrightness"))
								.setTooltip(Component.translatable("cave_fog_stabilizer.config.customCaveFogBrightness.desc"))
								.setStorageHandler(ConfigHolder.options())
								.setBinding(ConfigHolder.options()::setCustomCaveFogBrightness, ConfigHolder.options()::getCustomCaveFogBrightness)
								.setValidator(new Range(0, 100, 1))
								.setValueFormatter(value -> Component.literal(value + "%"))
								.setDefaultValue(0)
						)
						.addOption(builder.createIntegerOption(CFS.id("fog_transition_time"))
								.setName(Component.translatable("cave_fog_stabilizer.config.fogTransitionTime"))
								.setTooltip(Component.translatable("cave_fog_stabilizer.config.fogTransitionTime.desc"))
								.setStorageHandler(ConfigHolder.options())
								.setBinding(ConfigHolder.options()::setFogTransitionTime, ConfigHolder.options()::getFogTransitionTime)
								.setValidator(new Range(0, 10, 1))
								.setValueFormatter(value -> Component.translatable("cave_fog_stabilizer.config.seconds", value))
								.setDefaultValue(5)
						)
				);
	}
}
