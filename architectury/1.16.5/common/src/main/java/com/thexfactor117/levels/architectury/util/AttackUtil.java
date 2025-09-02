package com.thexfactor117.levels.architectury.util;

import com.thexfactor117.levels.architectury.leveling.Experience;
import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.nbt.INBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;

public class AttackUtil {
    public static void processDeath(CompoundTag nbt, ItemStack stack, LivingEntity enemy, Player player) {
        addExperience(stack, enemy);
        useRarity(nbt, stack, true);
        attemptLevel(nbt, stack, player);
    }

    /**
     * Adds experience to the stack's NBT.
     * @param stack
     * @param enemy
     */
    public static void addExperience(ItemStack stack, LivingEntity enemy) {
        Experience exp = new Experience(stack);
        if (exp.isMaxLevel()) {
            return;
        }

        // DEV
        /* todo either figure out or just abandon this
        boolean isDev = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        if (isDev) {
            exp.addExperience(200);
        }*/

        // WEAPONS AND BOW
        if (stack.getItem() instanceof SwordItem || stack.getItem() instanceof BowItem) {
            int xp = (int) (enemy.getMaxHealth() * 0.2);
            exp.addExperience(xp);
        }

        // ARMOR AND SHIELD
        if (stack.getItem() instanceof ArmorItem || stack.getItem() instanceof ShieldItem) {
            int xp = 0;

            // Default to use Attack Damage if available; uses Health if Attack Damage doesn't exist.
            if (enemy.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                xp = (int) (enemy.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5);
            } else {
                xp = (int) (enemy.getMaxHealth() * 0.5);
            }

            exp.addExperience(xp);
        }
    }

    /**
     * Uses rarity bonuses, such as bonus experience or durability bonuses.
     * @param nbt
     * @param stack
     */
    public static void useRarity(CompoundTag nbt, ItemStack stack, boolean death) {
        INBT inbt = NBTHelper.toCommon(nbt);
        Rarity rarity = Rarity.getRarity(inbt);

        if (rarity == Rarity.DEFAULT) {
            return;
        }

        boolean isUnlimitedDurability = Configs.getInstance().main.getBoolean("unlimitedDurability");
        Experience exp = new Experience(stack);
        ItemType type = ItemUtil.type(stack.getItem());

        if (type != null && death) {
            int addedXp = rarity.generateExperience();
            exp.addExperience(addedXp);
        }

        if (!isUnlimitedDurability && !death) {
            int repairDurability = rarity.generateRarityRepair();
            stack.setDamageValue(
                Math.max(stack.getDamageValue() - repairDurability, 0)
            );
        }
    }

    /**
     * Attempts to level up the current stack.
     * @param stack
     * @param player
     */
    public static void attemptLevel(CompoundTag nbt, ItemStack stack, Player player) {
        new Experience(stack).new LevelUp(player).levelUp();
        NBTHelper.saveStackNBT(stack, nbt);
    }
}
