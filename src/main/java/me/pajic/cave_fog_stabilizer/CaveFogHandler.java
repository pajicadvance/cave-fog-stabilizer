package me.pajic.cave_fog_stabilizer;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.lang.ref.WeakReference;

//? if >=1.21.11 {
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
//?}

public class CaveFogHandler {

	public static final TagKey<Biome> CONVENTIONAL_CAVE_BIOMES = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_cave"));
	public static final TagKey<Biome> CONVENTIONAL_UNDERGROUND_BIOMES = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_underground"));
	private static final int LEGACY_DEFAULT_FOG_COLOR = 0xC0D8FF;
	private static final float[] GAUSSIAN_KERNEL = {0.0F, 1.0F, 4.0F, 6.0F, 4.0F, 1.0F, 0.0F};
	private static final float BLACK_THRESHOLD = 1.0E-4F;

	private static float stabilization;
	private static WeakReference<ClientLevel> lastLevel = new WeakReference<>(null);
	private static boolean suppressVanillaNightVision;

	public static Vector3f modifyBaseFogColor(FogEnvironment environment, Vector3fc vanillaColor, ClientLevel level, Camera camera) {
		suppressVanillaNightVision = false;
		if (!(environment instanceof AtmosphericFogEnvironment)) return null;

		DeltaTracker deltaTracker = Minecraft.getInstance().getDeltaTracker();
		float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
		updateStabilization(level, shouldStabilize(level, cameraBlockPosition(camera)), deltaTracker.getGameTimeDeltaTicks());
		if (stabilization <= 0.0F) return null;

		Vector3f fogColor = new Vector3f();
		Vector3f nightVisionFogColor = new Vector3f();
		sampleStabilizedFogColor(level, cameraPosition(camera), fogColor, nightVisionFogColor);

		float nightVision = getNightVisionScale(cameraEntity(camera), partialTick);
		fogColor.lerp(nightVisionFogColor, nightVision);
		suppressVanillaNightVision = true;
		if (stabilization >= 1.0F) return fogColor;
		return applyVanillaNightVision(new Vector3f(vanillaColor), nightVision).lerp(fogColor, stabilization);
	}

	public static boolean shouldSuppressVanillaNightVision() {
		return suppressVanillaNightVision;
	}

	private static void updateStabilization(ClientLevel level, boolean shouldStabilize, float deltaTicks) {
		float target = shouldStabilize ? 1.0F : 0.0F;
		int transitionTime = CFS.CONFIG.fogTransitionTime.get();
		if (lastLevel.get() != level || transitionTime <= 0) {
			lastLevel = new WeakReference<>(level);
			stabilization = target;
			return;
		}
		float step = deltaTicks / (transitionTime * 20.0F);
		stabilization = target > stabilization ? Math.min(target, stabilization + step) : Math.max(target, stabilization - step);
	}

	private static boolean shouldStabilize(ClientLevel level, BlockPos cameraPos) {
		if (!CFS.CONFIG.stabilizeCaveFog.get() || !level.dimensionType().hasSkyLight()) return false;
		return isCaveBiome(level.getBiome(cameraPos)) || isUnderground(level, cameraPos);
	}

	public static boolean isUnderground(ClientLevel level, BlockPos cameraPos) {
		return cameraPos.getY() < level.getSeaLevel() && level.getBrightness(LightLayer.SKY, cameraPos) == 0;
	}

	public static boolean isCaveBiome(Holder<Biome> biome) {
		return biome.is(Biomes.LUSH_CAVES) || biome.is(Biomes.DRIPSTONE_CAVES) || biome.is(Biomes.DEEP_DARK) ||
                biome.is(CONVENTIONAL_CAVE_BIOMES) || biome.is(CONVENTIONAL_UNDERGROUND_BIOMES);
	}

	private static void sampleStabilizedFogColor(ClientLevel level, Vec3 pos, Vector3f fogColor, Vector3f nightVisionFogColor) {
		float standardBrightness = CFS.CONFIG.standardCaveFogBrightness.get() / 100.0F;
		float customBrightness = CFS.CONFIG.customCaveFogBrightness.get() / 100.0F;
		BiomeManager biomeManager = level.getBiomeManager();
		Vector3f biomeFogColor = new Vector3f();
		float totalWeight = 0.0F;

		double quartX = (pos.x - 2.0) * 0.25;
		double quartY = (pos.y - 2.0) * 0.25;
		double quartZ = (pos.z - 2.0) * 0.25;
		int baseX = Mth.floor(quartX);
		int baseY = Mth.floor(quartY);
		int baseZ = Mth.floor(quartZ);
		float deltaX = (float) (quartX - baseX);
		float deltaY = (float) (quartY - baseY);
		float deltaZ = (float) (quartZ - baseZ);

		for (int x = 0; x < 6; x++) {
			float weightX = Mth.lerp(deltaX, GAUSSIAN_KERNEL[x + 1], GAUSSIAN_KERNEL[x]);
			if (weightX <= 0.0F) continue;
			for (int y = 0; y < 6; y++) {
				float weightY = Mth.lerp(deltaY, GAUSSIAN_KERNEL[y + 1], GAUSSIAN_KERNEL[y]);
				if (weightY <= 0.0F) continue;
				for (int z = 0; z < 6; z++) {
					float weightZ = Mth.lerp(deltaZ, GAUSSIAN_KERNEL[z + 1], GAUSSIAN_KERNEL[z]);
					if (weightZ <= 0.0F) continue;
					float weight = weightX * weightY * weightZ;
					Holder<Biome> biome = biomeManager.getNoiseBiomeAtQuart(baseX - 2 + x, baseY - 2 + y, baseZ - 2 + z);
					if (getCustomFogColor(level, biome, biomeFogColor)) {
						float maxChannel = biomeFogColor.get(biomeFogColor.maxComponent());
						fogColor.fma(customBrightness * weight, biomeFogColor);
						if (maxChannel > BLACK_THRESHOLD) nightVisionFogColor.fma(weight / maxChannel, biomeFogColor);
						else nightVisionFogColor.add(weight, weight, weight);
					} else {
						fogColor.add(standardBrightness * weight, standardBrightness * weight, standardBrightness * weight);
						nightVisionFogColor.add(weight, weight, weight);
					}
					totalWeight += weight;
				}
			}
		}

		fogColor.div(totalWeight);
		nightVisionFogColor.div(totalWeight);
	}

	private static boolean getCustomFogColor(ClientLevel level, Holder<Biome> biome, Vector3f dest) {
		if (!isCaveBiome(biome)) return false;
		//? if >=1.21.11 {
		EnvironmentAttributeMap attributes = biome.value().getAttributes();
		if (!attributes.contains(EnvironmentAttributes.FOG_COLOR)) return false;
		var dimensionFogColor = level.dimensionType().attributes().applyModifier(
				EnvironmentAttributes.FOG_COLOR,
				EnvironmentAttributes.FOG_COLOR.defaultValue()
		);
		setColor(dest, attributes.applyModifier(EnvironmentAttributes.FOG_COLOR, dimensionFogColor));
		//?} else {
		/*int fogColor = biome.value().getFogColor();
		if ((fogColor & 0xFFFFFF) == LEGACY_DEFAULT_FOG_COLOR) return false;
		setColor(dest, fogColor);
		*///?}
		return true;
	}

	private static Vector3f applyVanillaNightVision(Vector3f color, float nightVision) {
		if (nightVision <= 0.0F || color.x == 0.0F || color.y == 0.0F || color.z == 0.0F) return color;
		float maxChannel = color.get(color.maxComponent());
		//? if >=26.2 {
		float scale = 1.0F / Math.clamp(maxChannel, 0.07F, 1.0F);
		float maxScale = 1.0F / maxChannel;
		color.x = Mth.lerp(nightVision, color.x, color.x * (color.x == maxChannel ? maxScale : scale));
		color.y = Mth.lerp(nightVision, color.y, color.y * (color.y == maxChannel ? maxScale : scale));
		color.z = Mth.lerp(nightVision, color.z, color.z * (color.z == maxChannel ? maxScale : scale));
		//?} else {
		/*float scale = 1.0F / maxChannel;
		color.x = Mth.lerp(nightVision, color.x, color.x * scale);
		color.y = Mth.lerp(nightVision, color.y, color.y * scale);
		color.z = Mth.lerp(nightVision, color.z, color.z * scale);
		*///?}
		return color;
	}

	private static float getNightVisionScale(Entity entity, float partialTick) {
		if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(MobEffects.NIGHT_VISION) && !livingEntity.hasEffect(MobEffects.DARKNESS)) {
			//? if >=26.2 {
			return GameRenderer.nightVisionScale(livingEntity, partialTick);
			//?} else {
			/*return GameRenderer.getNightVisionScale(livingEntity, partialTick);
			*///?}
		}
		return 0.0F;
	}

	private static void setColor(Vector3f dest, int rgb) {
		dest.set((rgb >> 16 & 0xFF) / 255.0F, (rgb >> 8 & 0xFF) / 255.0F, (rgb & 0xFF) / 255.0F);
	}

	private static void setColor(Vector3f dest, Vector3fc rgb) {
		dest.set(rgb);
	}

	private static Vec3 cameraPosition(Camera camera) {
		//? if >=1.21.11 {
		return camera.position();
		//?} else {
		/*return camera.getPosition();
		*///?}
	}

	private static BlockPos cameraBlockPosition(Camera camera) {
		//? if >=1.21.11 {
		return camera.blockPosition();
		//?} else {
		/*return camera.getBlockPosition();
		*///?}
	}

	private static Entity cameraEntity(Camera camera) {
		//? if >=1.21.11 {
		return camera.entity();
		//?} else {
		/*return camera.getEntity();
		*///?}
	}
}
