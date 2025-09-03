package com.thexfactor117.levels.architectury.mixin;

import com.thexfactor117.levels.architectury.events.custom.ArrowLooseCallback;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public abstract class MixinBowItem {
    @Shadow
    public abstract int getUseDuration(ItemStack stack);

    @Inject(
        method = "releaseUsing",
        at = @At("HEAD")
    )
    private void onReleaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int timeLeft, CallbackInfo ci) {
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            int chargeLeft = this.getUseDuration(itemStack) - timeLeft;
            ArrowLooseCallback.EVENT.invoker().onLoose(player, player.getMainHandItem(), chargeLeft);
        }
    }
}
