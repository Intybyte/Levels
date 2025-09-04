package com.thexfactor117.levels.architectury.leveling;


import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.common.leveling.exp.ExperienceEditor;
import com.thexfactor117.levels.common.leveling.exp.LevelUpProcessor;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.common.nbt.NBTType;
import lombok.AllArgsConstructor;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

/**
 *
 * @author TheXFactor117
 *
 */
public class Experience implements ExperienceEditor {

    private final ItemStack stack;
    private final CompoundTag nbt;
    private final INBT inbt;

    public Experience(ItemStack stack) {
        this.stack = stack;
        this.nbt = stack.getOrCreateTag();
        this.inbt = NBTHelper.toCommon(nbt);
    }

    @Override
    public INBT getNBT() {
        return this.inbt;
    }

    @AllArgsConstructor
    public class LevelUp implements LevelUpProcessor {
        private final Player player;

        @Override
        public ExperienceEditor getExpEditor() {
            return Experience.this;
        }

        @Override
        public INBT getNBT() {
            return Experience.this.inbt;
        }

        @Override
        public void notifyLevelUp() {
            String message = stack.getDisplayName().getString() + ChatFormatting.GRAY + " has leveled up to level " + ChatFormatting.GOLD + Experience.this.getLevel() + ChatFormatting.GRAY + "!";
            player.sendSystemMessage(Component.literal(message));
        }

        @Override
        public boolean isWeapon() {
            return stack.getItem() instanceof SwordItem || stack.getItem() instanceof AxeItem;
        }

        @Override
        public void levelUpWeapon() {
            double multiplier = nbt.getDouble("Multiplier");
            ListTag taglist = nbt.getList("AttributeModifiers", NBTType.COMPOUND.ordinal()); // retrieves our custom Attribute Modifier implementation
            // update damage and attack speed values
            CompoundTag damageNbt = taglist.getCompound(0);
            CompoundTag speedNbt = taglist.getCompound(1);

            double damageAmount = damageNbt.getDouble("Amount");
            double speedAmount = speedNbt.getDouble("Amount");

            double newDamage = damageAmount + ((damageAmount * multiplier) / 2);
            double newSpeed = speedAmount - ((speedAmount * multiplier) / 2);

            damageNbt.putDouble("Amount", newDamage);
            speedNbt.putDouble("Amount", newSpeed);
        }

        @Override
        public boolean isArmor() {
            return stack.getItem() instanceof ArmorItem;
        }

        @Override
        public void levelUpArmor() {
            double multiplier = nbt.getDouble("Multiplier");
            ListTag taglist = nbt.getList("AttributeModifiers", NBTType.COMPOUND.ordinal()); // retrieves our custom Attribute Modifier implementation

            // update armor and armor toughness values
            CompoundTag armorNbt = taglist.getCompound(0);
            CompoundTag toughnessNbt = taglist.getCompound(1);

            double armorAmount = armorNbt.getDouble("Amount");
            double toughnessAmount = toughnessNbt.getDouble("Amount");

            double newArmor = armorAmount + ((armorAmount * multiplier) / 2);
            double newToughness = toughnessAmount - ((toughnessAmount * multiplier) / 2);
            armorNbt.putDouble("Amount", newArmor);
            toughnessNbt.putDouble("Amount", newToughness);
        }

        @Override
        public void saveEdits() {
            NBTHelper.saveStackNBT(stack, nbt);
        }
    }
}
