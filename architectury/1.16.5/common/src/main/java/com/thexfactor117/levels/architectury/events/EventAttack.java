package com.thexfactor117.levels.architectury.events;

import com.thexfactor117.levels.architectury.events.custom.AttackCallback;
import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.SwordAttribute;
import com.thexfactor117.levels.common.leveling.attributes.WeaponAttributes;
import com.thexfactor117.levels.common.nbt.INBT;
import me.shedaniel.architectury.event.events.EntityEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.thexfactor117.levels.architectury.util.AttackUtil.addExperience;
import static com.thexfactor117.levels.architectury.util.AttackUtil.attemptLevel;
import static com.thexfactor117.levels.architectury.util.AttackUtil.processDeath;
import static com.thexfactor117.levels.architectury.util.AttackUtil.useRarity;
import static net.minecraft.world.InteractionResult.SUCCESS;

public class EventAttack {
    public static void register() {
        AttackCallback.EVENT.register((event) -> {
            Entity source = event.getSource().getDirectEntity();
            LivingEntity victim = event.getVictim();

            if (source == null) {
                return;
            }

            if (source instanceof Player) {
                Player player = (Player) source;
                LivingEntity enemy = victim;
                ItemStack stack = player.getMainHandItem();
                CompoundTag nbt = stack.getOrCreateTag();

                if (nbt != null && stack.getItem() instanceof SwordItem) {
                    processHit(event, nbt, stack, enemy, player);
                }
            } else if (source instanceof LivingEntity && victim instanceof Player) {
                Player player = (Player) victim;
                LivingEntity enemy = (LivingEntity) source;

                processArmorHit(event, player, enemy);
            }
            else if (source instanceof Arrow) {
                Arrow arrow = (Arrow) source;

                Entity shooter = arrow.getOwner();
                if (shooter == null) {
                    return;
                }

                if (shooter instanceof Player) {
                    Player player = (Player) shooter;
                    LivingEntity enemy = victim;
                    ItemStack stack = player.getMainHandItem();
                    CompoundTag nbt = stack.getOrCreateTag();

                    if (enemy != null && nbt != null && stack.getItem() instanceof BowItem) {
                        processHit(event, nbt, stack, enemy, player);
                    }
                } else if (shooter instanceof LivingEntity) {
                    Player player = (Player) victim;
                    LivingEntity enemy = (LivingEntity) shooter;

                    if (player != null) {
                        processArmorHit(event, player, enemy);
                    }
                }
            }
        });

        EntityEvent.LIVING_DEATH.register((living, dmgSource) -> {

            Entity source = dmgSource.getDirectEntity();
            if (source == null) {
                return SUCCESS;
            }

            if (source instanceof Player) {
                Player player = (Player) source;
                LivingEntity enemy = living;
                ItemStack stack = player.getMainHandItem();
                CompoundTag nbt = stack.getOrCreateTag();

                if (enemy != null && nbt != null && stack.getItem() instanceof SwordItem) {
                    processDeath(nbt, stack, enemy, player);
                }
            } else if (source instanceof Arrow) {
                Arrow arrow = (Arrow) source;

                Entity shooter = arrow.getOwner();
                if (shooter == null) {
                    return SUCCESS;
                }

                if (shooter instanceof Player) {
                    Player player = (Player) shooter;
                    LivingEntity enemy = living;
                    ItemStack stack = player.getMainHandItem();
                    CompoundTag nbt = stack.getOrCreateTag();

                    if (enemy != null && nbt != null && stack.getItem() instanceof BowItem) {
                        processDeath(nbt, stack, enemy, player);

                        INBT inbt = NBTHelper.toCommon(nbt);
                        if (BowAttribute.RECOVER.hasAttribute(inbt)) {
                            enemy.spawnAtLocation(Items.ARROW, (int) (Math.random() * 2));
                        }
                    }
                }
            }

            return SUCCESS;
        });
    }

    private static void processArmorHit(AttackCallback.AttackInstance event, Player player, LivingEntity enemy) {
        for (ItemStack stack : player.inventory.armor) {
            CompoundTag nbt = stack.getOrCreateTag();

            if (nbt != null && stack.getItem() instanceof ArmorItem) {
                processHit(event, nbt, stack, enemy, player);
            }
        }

        ItemStack shield = player.inventory.offhand.get(0);
        if (shield.getItem() instanceof ShieldItem) {
            CompoundTag nbt = shield.getOrCreateTag();

            if (nbt != null && player.getUsedItemHand() == InteractionHand.OFF_HAND) {
                processHit(event, nbt, shield, enemy, player);
            }
        }
    }

    private static void processHit(AttackCallback.AttackInstance event, CompoundTag nbt, ItemStack stack, LivingEntity enemy, Player player) {
        addExperience(stack, enemy);
        useRarity(nbt, stack, false);
        useAttributes(nbt, event, stack, player, enemy);
        attemptLevel(nbt, stack, player);
    }

    private static void useAttributes(CompoundTag baseNbt, AttackCallback.AttackInstance event, ItemStack stack, Player player, LivingEntity enemy) {
        // WEAPONS
        INBT nbt = NBTHelper.toCommon(baseNbt);

        //region Any attributes
        if (AnyAttributes.FIRE.hasAttribute(nbt) && AnyAttributes.FIRE.rollChance())
            enemy.setSecondsOnFire((int) AnyAttributes.FIRE.getCalculatedValue(nbt));

        if (AnyAttributes.FROST.hasAttribute(nbt) && AnyAttributes.FROST.rollChance())
            enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) AnyAttributes.FROST.getCalculatedValue(nbt), 10));

        if (AnyAttributes.POISON.hasAttribute(nbt) && AnyAttributes.POISON.rollChance())
            enemy.addEffect(new MobEffectInstance(MobEffects.POISON, (int) AnyAttributes.POISON.getCalculatedValue(nbt), AnyAttributes.POISON.getAttributeTier(nbt)));

        if (AnyAttributes.DURABLE.hasAttribute(nbt) && AnyAttributes.DURABLE.rollChance())
            stack.setDamageValue(stack.getDamageValue() - (int) AnyAttributes.DURABLE.getCalculatedValue(nbt));
        //endregion

        //region Weapon attributes
        float dmgAmount = event.getAmount();
        if (WeaponAttributes.ABSORB.hasAttribute(nbt) && WeaponAttributes.ABSORB.rollChance()) {
            float regenAmount = (float) (dmgAmount * WeaponAttributes.ABSORB.getCalculatedValue(nbt) / 100.0);
            player.setHealth(player.getHealth() + regenAmount);
        }

        // tiers: (6% chance, 8% chance, 1125%% chance); sets enemies health to something small, so damage kills enemy in one hit
        if (WeaponAttributes.VOID.hasAttribute(nbt)) {
            double chance = WeaponAttributes.VOID.getCalculatedValue(nbt);
            if (Math.random() * 100.0 <= chance)
                enemy.setHealth(0.001F);
        }

        if (WeaponAttributes.CRITICAL.hasAttribute(nbt) && WeaponAttributes.CRITICAL.rollChance()) {
            float bonus = (float) (dmgAmount * WeaponAttributes.CRITICAL.getCalculatedValue(nbt) / 100.0); // 20% chance; tiers: (20%, 30%, 45%)
            event.setAmount(dmgAmount + bonus);
        }
        //endregion

        if (stack.getItem() instanceof SwordItem) {
            if (SwordAttribute.CHAINED.hasAttribute(nbt) && SwordAttribute.CHAINED.rollChance()) {
                double radius = SwordAttribute.CHAINED.getCalculatedValue(nbt);
                Vec3 pos = player.position();
                List<LivingEntity> entityList = enemy.level.getEntitiesOfClass(LivingEntity.class, new AABB(pos.x - radius, pos.y - radius, pos.z - radius, pos.x + radius, pos.y + radius, pos.z + radius));

                for (LivingEntity entity : entityList) {
                    if (entity instanceof Player || entity instanceof Animal) {
                        continue;
                    }

                    entity.hurt(DamageSource.playerAttack(player), dmgAmount / 2);
                }
            }
        }

        // ARMOR
        if (stack.getItem() instanceof ArmorItem) {
            if (ArmorAttribute.MAGICAL.hasAttribute(nbt) && event.getSource().equals(DamageSource.MAGIC))
                event.setAmount((float) (dmgAmount * ArmorAttribute.MAGICAL.getCalculatedValue(nbt) / 100.0)); // tiers: (20%, 30%, 45%)
        }
    }
}
