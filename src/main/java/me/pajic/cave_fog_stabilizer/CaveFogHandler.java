package me.pajic.cave_fog_stabilizer;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import me.pajic.cave_fog_stabilizer.config.ConfigHolder;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import java.awt.Color;

public class CaveFogHandler {

	private static float fogTransitionTimer = getFogTransitionTime();
	private static float fogBrightnessTimer = getFogTransitionTime();
	private static float skyBrightnessTimer = getFogTransitionTime();
	private static int prevFogColor = Integer.MAX_VALUE;
	private static int prevSkyColor = Integer.MAX_VALUE;
	private static final TagKey<Biome> CAVE_BIOMES = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_cave"));

	public static void handleCaveFog(Args args, ClientLevel level, Camera camera) {
		if (shouldStabilizeFog()) {
			BlockPos cameraPos = camera.blockPosition();
			int fogColorVariable = args.get(1);
			int skyColorVariable = args.get(2);
			float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
			Holder<Biome> biome = level.getBiome(cameraPos);
			IntIntImmutablePair targetColors = getStabilizedColors(biome, partialTick);
			int targetFogColor = targetColors.leftInt();
			int targetSkyColor = targetColors.rightInt();
			if (cameraPos.getY() < level.getSeaLevel() && level.getBrightness(LightLayer.SKY, cameraPos) == 0) {
				if (fogTransitionTimer > 0) {
					fogTransitionTimer = Math.max(0, fogTransitionTimer - partialTick);
					setLerpedCaveFogColor(args, targetFogColor, targetSkyColor, fogColorVariable, skyColorVariable);
				} else setDefaultCaveFogColor(args, targetFogColor, targetSkyColor);
			} else if (fogTransitionTimer < getFogTransitionTime()) {
				fogTransitionTimer = Math.min(getFogTransitionTime(), fogTransitionTimer + partialTick);
				setLerpedCaveFogColor(args, targetFogColor, targetSkyColor, fogColorVariable, skyColorVariable);
			}
		}
	}

	private static IntIntImmutablePair getStabilizedColors(Holder<Biome> biome, float partialTick) {
		Color fogBaseColor = Color.BLACK;
		Color skyBaseColor = Color.BLACK;
		EnvironmentAttributeMap.Entry<Integer, ?> fogEntry = biome.value().getAttributes().get(EnvironmentAttributes.FOG_COLOR);
		EnvironmentAttributeMap.Entry<Integer, ?> skyEntry = biome.value().getAttributes().get(EnvironmentAttributes.SKY_COLOR);
		if (biome.is(CAVE_BIOMES)) {
			if (fogEntry != null) fogBaseColor = new Color((Integer) fogEntry.argument());
			if (skyEntry != null) skyBaseColor = new Color((Integer) skyEntry.argument());
		}
		float[] hsbFog = Color.RGBtoHSB(fogBaseColor.getRed(), fogBaseColor.getGreen(), fogBaseColor.getBlue(), null);
		float[] hsbSky = Color.RGBtoHSB(skyBaseColor.getRed(), skyBaseColor.getGreen(), skyBaseColor.getBlue(), null);
		float fogBrightness;
		float skyBrightness;
		if (CFS.CONFIG_AVAILABLE) {
			fogBrightness = fogBaseColor.equals(Color.BLACK) ?
					ConfigHolder.getStandardCaveFogBrightness() :
					ConfigHolder.getCustomCaveFogBrightness();
			skyBrightness = skyBaseColor.equals(Color.BLACK) ?
					ConfigHolder.getStandardCaveFogBrightness() :
					ConfigHolder.getCustomCaveFogBrightness();
		}
		else fogBrightness = skyBrightness = fogBaseColor.equals(Color.BLACK) ? 0 : 0.2F;
		int fogColor = Color.HSBtoRGB(hsbFog[0], hsbFog[1], fogBrightness);
		int skyColor = Color.HSBtoRGB(hsbSky[0], hsbSky[1], skyBrightness);
		if (prevFogColor == Integer.MAX_VALUE) prevFogColor = fogColor;
		if (prevSkyColor == Integer.MAX_VALUE) prevSkyColor = skyColor;
		if (prevFogColor != fogColor && fogBrightnessTimer > 0) {
			fogBrightnessTimer = Math.max(0, fogBrightnessTimer - partialTick);
			fogColor = ARGB.srgbLerp(fogBrightnessTimer / getFogTransitionTime(), fogColor, prevFogColor);
		} else {
			fogBrightnessTimer = getFogTransitionTime();
			prevFogColor = fogColor;
		}
		if (prevSkyColor != skyColor && skyBrightnessTimer > 0) {
			skyBrightnessTimer = Math.max(0, skyBrightnessTimer - partialTick);
			skyColor = ARGB.srgbLerp(skyBrightnessTimer / getFogTransitionTime(), skyColor, prevSkyColor);
		} else {
			skyBrightnessTimer = getFogTransitionTime();
			prevSkyColor = skyColor;
		}
		return new IntIntImmutablePair(fogColor, skyColor);
	}

	private static void setLerpedCaveFogColor(Args args, int targetFogColor, int targetSkyColor, int fogColorVariable, int skyColorVariable) {
		args.set(1, ARGB.srgbLerp(fogTransitionTimer / getFogTransitionTime(), targetFogColor, fogColorVariable));
		args.set(2, ARGB.srgbLerp(fogTransitionTimer / getFogTransitionTime(), targetSkyColor, skyColorVariable));
	}

	private static void setDefaultCaveFogColor(Args args, int targetFogColor, int targetSkyColor) {
		args.set(1, targetFogColor);
		args.set(2, targetSkyColor);
	}

	private static boolean shouldStabilizeFog() {
		return !CFS.CONFIG_AVAILABLE || ConfigHolder.shouldStabilizeFog();
	}

	private static float getFogTransitionTime() {
		return CFS.CONFIG_AVAILABLE ? ConfigHolder.getFogTransitionTime() : 100;
	}
}
