package com.thexfactor117.levels.bukkit.leveling;

import com.cryptomorin.xseries.XAttribute;
import com.thexfactor117.levels.bukkit.nbt.NBTHelper;
import com.thexfactor117.levels.bukkit.util.AttributeUtil;
import com.thexfactor117.levels.bukkit.util.ItemUtil;
import com.thexfactor117.levels.bukkit.util.StackUtil;
import com.thexfactor117.levels.bukkit.util.WeaponHelper;
import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.MinecraftAttributes;
import com.thexfactor117.levels.common.leveling.exp.ExperienceEditor;
import com.thexfactor117.levels.common.leveling.exp.LevelUpProcessor;
import com.thexfactor117.levels.common.nbt.INBT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@Getter
public class Experience implements ExperienceEditor {

    private final ItemStack stack;
    private final ItemMeta meta;
    private final PersistentDataContainer pdc;
    private final INBT inbt;

    public Experience(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) {
            throw new RuntimeException("Invalid itemStack passed to experience");
        }

        this.stack = stack;
        this.meta = stack.getItemMeta();
        this.pdc = meta.getPersistentDataContainer();
        this.inbt = NBTHelper.toCommon(pdc);
    }

    public Experience(ItemStack stack, ItemMeta meta) {
        if (stack == null || meta == null) {
            throw new RuntimeException("Invalid itemStack passed to experience");
        }

        this.stack = stack;
        this.meta = meta;
        this.pdc = meta.getPersistentDataContainer();
        this.inbt = NBTHelper.toCommon(pdc);
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
            player.sendMessage(
                    StackUtil.getName(stack) + LegacyTextColor.GRAY + " has leveled up to level " + LegacyTextColor.GOLD + Experience.this.getLevel() + LegacyTextColor.GRAY + "!"
            );
        }

        @Override
        public boolean isWeapon() {
            return ItemUtil.isWeapon(stack.getType());
        }

        @Override
        public void levelUpWeapon() {
            Double multiplier = pdc.get(WeaponHelper.MULTIPLIER, PersistentDataType.DOUBLE);

            if (multiplier == null) {
                System.out.println("Multiplier is null");
                return;
            }

            AttributeUtil.editAttribute(meta, XAttribute.ATTACK_DAMAGE.get(), MinecraftAttributes.ATTACK_DAMAGE_UUID, (old) -> {
                double damageAmount = old.getAmount();
                double newDamage = damageAmount + ((damageAmount * multiplier) / 2);
                return new AttributeModifier(
                        MinecraftAttributes.ATTACK_DAMAGE_UUID,
                        "attackDamage",
                        newDamage,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HAND
                );
            });

            AttributeUtil.editAttribute(meta, XAttribute.ATTACK_SPEED.get(), MinecraftAttributes.ATTACK_SPEED_UUID, (old) -> {
                double speedAmount = old.getAmount();
                double newSpeedDamage = speedAmount - ((speedAmount * multiplier) / 2);
                return new AttributeModifier(
                        MinecraftAttributes.ATTACK_SPEED_UUID,
                        "attackSpeed",
                        newSpeedDamage,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HAND
                );
            });
        }

        @Override
        public boolean isArmor() {
            return ItemUtil.isArmor(stack.getType());
        }

        @Override
        public void levelUpArmor() {
            Double multiplier = pdc.get(WeaponHelper.MULTIPLIER, PersistentDataType.DOUBLE);

            if (multiplier == null) {
                System.out.println("Multiplier is null");
                return;
            }

            EquipmentSlot slot = WeaponHelper.getSlotOfArmor(stack.getType());
            AttributeUtil.editAttribute(meta, XAttribute.ARMOR.get(), MinecraftAttributes.ARMOR_UUID, (old) -> {
                double armorAmount = old.getAmount();
                double newArmor = armorAmount + ((armorAmount * multiplier) / 2);
                return new AttributeModifier(
                        MinecraftAttributes.ARMOR_UUID,
                        "armor",
                        newArmor,
                        AttributeModifier.Operation.ADD_NUMBER,
                        slot
                );
            });

            AttributeUtil.editAttribute(meta, XAttribute.ARMOR_TOUGHNESS.get(), MinecraftAttributes.ARMOR_TOUGHNESS_UUID, (old) -> {
                double toughnessAmount = old.getAmount();
                double newToughness = toughnessAmount + ((toughnessAmount * multiplier) / 2);
                return new AttributeModifier(
                        MinecraftAttributes.ARMOR_TOUGHNESS_UUID,
                        "armorToughness",
                        newToughness,
                        AttributeModifier.Operation.ADD_NUMBER,
                        slot
                );
            });
        }

        @Override
        public void saveEdits() {
            Experience.this.stack.setItemMeta(Experience.this.meta);
        }
    }
}
