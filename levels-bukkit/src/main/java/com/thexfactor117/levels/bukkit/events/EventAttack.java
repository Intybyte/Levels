package com.thexfactor117.levels.bukkit.events;

import com.cryptomorin.xseries.XAttribute;
import com.cryptomorin.xseries.XPotion;
import com.thexfactor117.levels.bukkit.leveling.Experience;
import com.thexfactor117.levels.bukkit.nbt.NBTHelper;
import com.thexfactor117.levels.bukkit.util.ItemUtil;
import com.thexfactor117.levels.bukkit.util.StackUtil;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.SwordAttribute;
import com.thexfactor117.levels.common.leveling.attributes.WeaponAttributes;
import com.thexfactor117.levels.common.nbt.INBT;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;

/**
 *
 * @author TheXFactor117
 *
 */
public class EventAttack implements Listener {
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        Entity source = event.getDamager();
        Entity victim = event.getEntity();
        boolean dead = victim.isDead();

        if (!dead) {
            if (source instanceof Player player && victim instanceof LivingEntity enemy) {
                ItemStack stack = player.getInventory().getItemInMainHand();

                if (ItemUtil.isSword(stack.getType())) {
                    processHit(event, stack, enemy, player);
                }

            } else if (source instanceof LivingEntity && victim instanceof Player) {
                Player player = (Player) victim;
                LivingEntity enemy = (LivingEntity) source;

                processArmorHit(event, player, enemy);
            } else if (source instanceof Arrow && victim instanceof LivingEntity) {
                Arrow arrow = (Arrow) source;

                ProjectileSource shooter = arrow.getShooter();
                if (shooter == null) {
                    return;
                }

                if (shooter instanceof Player) {
                    Player player = (Player) shooter;
                    LivingEntity enemy = (LivingEntity) victim;
                    ItemStack stack = player.getInventory().getItemInMainHand();

                    if (stack.getType() == Material.BOW) {
                        processHit(event, stack, enemy, player);
                    }
                } else if (shooter instanceof LivingEntity && victim instanceof Player) {
                    Player player = (Player) victim;
                    LivingEntity enemy = (LivingEntity) shooter;

                    processArmorHit(event, player, enemy);
                }
            }
        } else {
            if (source instanceof Player && victim instanceof LivingEntity) {
                Player player = (Player) source;
                LivingEntity enemy = (LivingEntity) victim;
                ItemStack stack = player.getInventory().getItemInMainHand();
                ItemMeta meta = stack.getItemMeta();

                if (meta != null && ItemUtil.isSword(stack.getType())) {
                    processDeath(stack, meta, enemy, player);

                    stack.setItemMeta(meta);
                }
            } else if (source instanceof Arrow) {
                Arrow arrow = (Arrow) source;
                ProjectileSource shooter = arrow.getShooter();
                if (shooter == null) {
                    return;
                }

                if (shooter instanceof Player && victim instanceof LivingEntity) {
                    Player player = (Player) shooter;
                    LivingEntity enemy = (LivingEntity) victim;
                    ItemStack stack = player.getInventory().getItemInMainHand();
                    ItemMeta meta = stack.getItemMeta();

                    if (meta == null || stack.getType() != Material.BOW) {
                        return;
                    }

                    processDeath(stack, meta, enemy, player);

                    INBT inbt = NBTHelper.toCommon(meta.getPersistentDataContainer());
                    if (BowAttribute.RECOVER.hasAttribute(inbt)) {
                        enemy.getLocation().getWorld().dropItem(enemy.getLocation(), new ItemStack(Material.ARROW, (int) (Math.random() * 2)) );
                    }

                    stack.setItemMeta(meta);
                }
            }
        }
    }

    private void processDeath(ItemStack stack, ItemMeta meta, LivingEntity enemy, Player player) {
        addExperience(stack, meta, enemy);
        useRarity(stack, meta, true);
        attemptLevel(stack, meta, player);
    }

    private void processArmorHit(EntityDamageByEntityEvent event, Player player, LivingEntity enemy) {
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            if (stack != null) {
                processHit(event, stack, enemy, player);
            }
        }

        ItemStack shield = player.getInventory().getItemInOffHand();
        if (shield.getType() == Material.SHIELD) {
            if (player.isHandRaised()) {
                processHit(event, shield, enemy, player);
            }
        }
    }

    private void processHit(EntityDamageByEntityEvent event, ItemStack stack, LivingEntity enemy, Player player) {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return;

        addExperience(stack, meta, enemy);
        useRarity(stack, meta, false);
        useAttributes(event, stack, meta, player, enemy);
        attemptLevel(stack, meta, player);

        stack.setItemMeta(meta);
    }

    /**
     * Adds experience to the stack's NBT.
     * @param stack
     * @param enemy
     */
    private void addExperience(ItemStack stack, ItemMeta meta, LivingEntity enemy) {
        Experience exp = new Experience(stack, meta);
        if (exp.isMaxLevel()) {
            return;
        }

        Material type = stack.getType();
        // WEAPONS AND BOW
        if (type.toString().contains("SWORD") || type == Material.BOW) {
            int xp = (int) (enemy.getMaxHealth() * 0.2);
            exp.addExperience(xp);
        }

        // ARMOR AND SHIELD
        if (ItemUtil.isArmor(type) || type == Material.SHIELD) {
            int xp = 0;

            // Default to use Attack Damage if available; uses Health if Attack Damage doesn't exist.
            if (enemy.getAttribute(XAttribute.ATTACK_DAMAGE.get()) != null) {
                xp = (int) (enemy.getAttribute(XAttribute.ATTACK_DAMAGE.get()).getValue() * 0.5);
            } else {
                xp = (int) (enemy.getAttribute(XAttribute.MAX_HEALTH.get()).getValue() * 0.5);
            }

            exp.addExperience(xp);
        }
    }

    /**
     * Uses rarity bonuses, such as bonus experience or durability bonuses.
     * @param stack
     */
    private void useRarity(ItemStack stack, ItemMeta meta, boolean death) {
        INBT inbt = NBTHelper.toCommon(meta.getPersistentDataContainer());
        Rarity rarity = Rarity.getRarity(inbt);

        if (rarity == Rarity.DEFAULT) {
            return;
        }

        boolean isUnlimitedDurability = Configs.getInstance().main.getBoolean("unlimitedDurability");
        Experience exp = new Experience(stack, meta);
        ItemType type = ItemUtil.type(stack.getType());

        if (type != null && death) {
            int addedXp = rarity.generateExperience();
            exp.addExperience(addedXp);
        }

        if (!isUnlimitedDurability && !death) {
            int repairDurability = rarity.generateRarityRepair();
            StackUtil.safeRepair(meta, repairDurability);
        }
    }


    /**
     * Uses any attributes the stack currently has.
     * @param stack
     * @param meta
     * @param player
     * @param enemy
     */
    private void useAttributes(EntityDamageByEntityEvent event, ItemStack stack, ItemMeta meta, Player player, LivingEntity enemy) {
        // WEAPONS

        INBT nbt = NBTHelper.toCommon(meta.getPersistentDataContainer());

        //region Any attributes
        if (AnyAttributes.FIRE.hasAttribute(nbt) && AnyAttributes.FIRE.rollChance())
            enemy.setFireTicks((int) AnyAttributes.FIRE.getCalculatedValue(nbt) * 20);

        if (AnyAttributes.FROST.hasAttribute(nbt) && AnyAttributes.FROST.rollChance()) {
            int calculated = (int) AnyAttributes.FROST.getCalculatedValue(nbt);
            enemy.addPotionEffect(new PotionEffect(XPotion.SLOWNESS.getPotionEffectType(), calculated, 10));
            enemy.setFreezeTicks(calculated * 4);
        }

        if (AnyAttributes.POISON.hasAttribute(nbt) && AnyAttributes.POISON.rollChance())
            enemy.addPotionEffect(new PotionEffect(XPotion.POISON.getPotionEffectType(), (int) AnyAttributes.POISON.getCalculatedValue(nbt), AnyAttributes.POISON.getAttributeTier(nbt)));

        if (AnyAttributes.DURABLE.hasAttribute(nbt) && AnyAttributes.DURABLE.rollChance()) {
            int repair = (int) AnyAttributes.DURABLE.getCalculatedValue(nbt);
            StackUtil.safeRepair(meta, repair);
        }
        //endregion

        double dmgAmount = event.getDamage();
        //region Weapon attributes
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
            event.setDamage(dmgAmount + bonus);
        }
        //endregion

        Material type = stack.getType();
        if (ItemUtil.isSword(type)) {
            if (SwordAttribute.CHAINED.hasAttribute(nbt) && SwordAttribute.CHAINED.rollChance()) {
                double radius = SwordAttribute.CHAINED.getCalculatedValue(nbt);
                World world = enemy.getWorld();
                Collection<Entity> entityCollection = world.getNearbyEntities(player.getLocation(), radius, radius, radius, LivingEntity.class::isInstance);

                for (Entity entity : entityCollection) {
                    LivingEntity living = (LivingEntity) entity;

                    if (!(entity instanceof Player) && living instanceof Monster) {
                        ((Monster) entity).damage(dmgAmount / 2, player);
                    }
                }
            }
        }

        // ARMOR
        if (ItemUtil.isArmor(type)) {
            double magicDmg = event.getDamage(EntityDamageEvent.DamageModifier.MAGIC);
            if (ArmorAttribute.MAGICAL.hasAttribute(nbt) && ((int) Math.floor(magicDmg)) == 0)
                event.setDamage(EntityDamageEvent.DamageModifier.MAGIC, (float) (magicDmg * ArmorAttribute.MAGICAL.getCalculatedValue(nbt) / 100.0)); // tiers: (20%, 30%, 45%)
        }
    }

    /**
     * Attempts to level up the current stack.
     * @param stack
     * @param player
     */
    private void attemptLevel(ItemStack stack, ItemMeta meta, Player player) {
        new Experience(stack, meta).new LevelUp(player).levelUp();
    }
}
