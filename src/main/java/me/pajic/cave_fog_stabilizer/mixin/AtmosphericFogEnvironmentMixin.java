package me.pajic.cave_fog_stabilizer.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.cave_fog_stabilizer.CaveFogHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(AtmosphericFogEnvironment.class)
public class AtmosphericFogEnvironmentMixin {

    @ModifyArgs(
            method = "getBaseColor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/ARGB;srgbLerp(FII)I",
                    ordinal = 1
            )
    )
    private void handleCaveFog(Args args, @Local(argsOnly = true) ClientLevel level, @Local(argsOnly = true) Camera camera) {
		CaveFogHandler.handleCaveFog(args, level, camera);
    }
}
