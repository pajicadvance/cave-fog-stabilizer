package me.pajic.cave_fog_stabilizer;

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
import java.util.Optional;

public class CaveFogHandler {

	private static float fogTransitionTimer = getFogTransitionTime();
	private static int currentFogColor = Integer.MAX_VALUE;
	private static final TagKey<Biome> CAVE_BIOMES = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_cave"));

	public static void handleCaveFog(Args args, ClientLevel level, Camera camera) {
		if (shouldStabilizeFog()) {
			BlockPos cameraPos = camera.blockPosition();
			int fogColorVariable = args.get(1);
			int skyColorVariable = args.get(2);
			float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks();
			int targetColor = getStabilizedCaveFogColor(tryGetBiomeFogColor(level.getBiome(cameraPos)).orElse(0));
			if (cameraPos.getY() < level.getSeaLevel() && level.getBrightness(LightLayer.SKY, cameraPos) == 0) {
				if (fogTransitionTimer > 0) {
					fogTransitionTimer = Math.max(0, fogTransitionTimer - partialTick);
					setLerpedCaveFogColor(args, targetColor, fogColorVariable, skyColorVariable);
				} else setDefaultCaveFogColor(args, targetColor);
			} else if (fogTransitionTimer < getFogTransitionTime()) {
				fogTransitionTimer = Math.min(getFogTransitionTime(), fogTransitionTimer + partialTick);
				setLerpedCaveFogColor(args, targetColor, fogColorVariable, skyColorVariable);
			}
		}
	}

	private static void setLerpedCaveFogColor(Args args, int targetColor, int fogColorVariable, int skyColorVariable) {
		args.set(1, ARGB.srgbLerp(fogTransitionTimer / getFogTransitionTime(), targetColor, fogColorVariable));
		args.set(2, ARGB.srgbLerp(fogTransitionTimer / getFogTransitionTime(), targetColor, skyColorVariable));
	}

	private static void setDefaultCaveFogColor(Args args, int targetColor) {
		args.set(1, targetColor);
		args.set(2, targetColor);
	}

	private static Optional<Integer> tryGetBiomeFogColor(Holder<Biome> biome) {
		EnvironmentAttributeMap.Entry<Integer, ?> attribute = biome.value().getAttributes().get(EnvironmentAttributes.FOG_COLOR);
		if (attribute != null && biome.is(CAVE_BIOMES)) return Optional.of((Integer) attribute.argument());
		return Optional.empty();
	}

	private static boolean shouldStabilizeFog() {
		return !CFS.CONFIG_AVAILABLE || ConfigHolder.shouldStabilizeFog();
	}

	private static float getFogTransitionTime() {
		return CFS.CONFIG_AVAILABLE ? ConfigHolder.getFogTransitionTime() : 100;
	}

	private static int getStabilizedCaveFogColor(int biomeFogColor) {
		Color baseColor = new Color(biomeFogColor);
		float[] hsb = Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), null);
		float caveFogBrightness;
		if (CFS.CONFIG_AVAILABLE) caveFogBrightness = biomeFogColor == 0 ?
				ConfigHolder.getStandardCaveFogBrightness() :
				ConfigHolder.getCustomCaveFogBrightness();
		else caveFogBrightness = 0.25F;
		return Color.HSBtoRGB(hsb[0], hsb[1], caveFogBrightness);
	}
}
