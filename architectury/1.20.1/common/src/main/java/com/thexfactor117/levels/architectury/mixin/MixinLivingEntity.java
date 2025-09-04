package com.thexfactor117.levels.architectury.mixin;

import com.thexfactor117.levels.architectury.events.custom.AttackCallback;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Unique
    private static final ThreadLocal<DamageSource> levels_Main$currentDamageSource = new ThreadLocal<>();

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    private void onActuallyHurtHead(DamageSource source, float damage, CallbackInfo ci) {
        levels_Main$currentDamageSource.set(source);
    }

    @ModifyVariable(
        method = "actuallyHurt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"
        ),
        ordinal = 0,
        argsOnly = true
    )
    private float modifyFinalDamage(float f) {
        DamageSource source = levels_Main$currentDamageSource.get();
        LivingEntity self = (LivingEntity)(Object)this;

        AttackCallback.AttackInstance instance = new AttackCallback.AttackInstance(source, self, f);
        AttackCallback.EVENT.invoker().attack(instance);

        return instance.getAmount();
    }


    @Inject(method = "actuallyHurt", at = @At("RETURN"))
    private void onActuallyHurtEnd(DamageSource source, float damage, CallbackInfo ci) {
        levels_Main$currentDamageSource.remove();
    }
}
