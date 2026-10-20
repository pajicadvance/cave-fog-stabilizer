package me.pajic.cave_fog_stabilizer.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pajic.cave_fog_stabilizer.CaveFogHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if <26.3
/*import net.minecraft.util.ARGB;*/

@Mixin(FogRenderer.class)
public class FogRendererMixin {

	//? if >=26.3 {
	@WrapOperation(
			method = "computeFogColor",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;getBaseColor(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/Camera;IF)Lorg/joml/Vector3fc;"
			)
	)
	private Vector3fc stabilizeCaveFog(
			FogEnvironment environment,
			ClientLevel level,
			Camera camera,
			int renderDistance,
			float partialTick,
			Operation<Vector3fc> original
	) {
		Vector3fc color = original.call(environment, level, camera, renderDistance, partialTick);
		Vector3f modifiedColor = CaveFogHandler.modifyBaseFogColor(environment, color, level, camera);
		return modifiedColor != null ? modifiedColor : color;
	}
	//?} else {
	/*@WrapOperation(
			method = "computeFogColor",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;getBaseColor(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/Camera;IF)I"
			)
	)
	private int stabilizeCaveFog(
			FogEnvironment environment,
			ClientLevel level,
			Camera camera,
			int renderDistance,
			float partialTick,
			Operation<Integer> original
	) {
		int color = original.call(environment, level, camera, renderDistance, partialTick);
		Vector3fc vanillaColor = new Vector3f(ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color));
		Vector3f modifiedColor = CaveFogHandler.modifyBaseFogColor(environment, vanillaColor, level, camera);
		return modifiedColor != null ? ARGB.colorFromFloat(1.0F, modifiedColor.x, modifiedColor.y, modifiedColor.z) : color;
	}
	*///?}

	@WrapOperation(
			method = "computeFogColor",
			at = @At(
					value = "INVOKE",
					//? if >=26.2 {
					target = "Lnet/minecraft/client/renderer/GameRenderer;nightVisionScale(Lnet/minecraft/world/entity/LivingEntity;F)F"
					//?} else {
					/*target = "Lnet/minecraft/client/renderer/GameRenderer;getNightVisionScale(Lnet/minecraft/world/entity/LivingEntity;F)F"
					*///?}
			)
	)
	private float applyNightVision(LivingEntity entity, float partialTick, Operation<Float> original) {
		return CaveFogHandler.shouldSuppressVanillaNightVision() ? 0.0F : original.call(entity, partialTick);
	}
}
