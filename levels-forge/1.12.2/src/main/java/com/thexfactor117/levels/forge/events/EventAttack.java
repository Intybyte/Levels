package com.thexfactor117.levels.forge.events;

import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.Rarity;
import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.SwordAttribute;
import com.thexfactor117.levels.common.leveling.attributes.WeaponAttributes;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.forge.leveling.Experience;
import com.thexfactor117.levels.forge.nbt.NBTHelper;
import com.thexfactor117.levels.forge.util.ItemUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author TheXFactor117
 *
 */
public class EventAttack {
    @SubscribeEvent
    public void onAttack(LivingHurtEvent event) {
        Entity source = event.getSource().getImmediateSource();
        if (source instanceof EntityPlayer && !(source instanceof FakePlayer)) {
            EntityPlayer player = (EntityPlayer) source;
            EntityLivingBase enemy = event.getEntityLiving();
            ItemStack stack = player.inventory.getCurrentItem();
            NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

            if (stack != null && nbt != null && stack.getItem() instanceof ItemSword) {
                processHit(event, nbt, stack, enemy, player);
            }
        } else if (source instanceof EntityLivingBase && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            EntityLivingBase enemy = (EntityLivingBase) source;

            if (enemy != null && player != null) {
                for (ItemStack stack : player.inventory.armorInventory) {
                    NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

                    if (stack != null && nbt != null && stack.getItem() instanceof ItemArmor) {
                        processHit(event, nbt, stack, enemy, player);
                    }
                }

                if (player.inventory.offHandInventory.get(0).getItem() instanceof ItemShield) {
                    ItemStack stack = player.inventory.offHandInventory.get(0);
                    NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

                    if (stack != null && nbt != null && player.getActiveHand() == EnumHand.OFF_HAND) {
                        processHit(event, nbt, stack, enemy, player);
                    }
                }
            }
        }
        else if (source instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) source;

            if (arrow.shootingEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
                EntityLivingBase enemy = event.getEntityLiving();
                ItemStack stack = player.inventory.getCurrentItem();
                NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

                if (player != null && enemy != null && stack != null && nbt != null && stack.getItem() instanceof ItemBow) {
                    processHit(event, nbt, stack, enemy, player);
                }
            } else if (arrow.shootingEntity instanceof EntityLivingBase) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                EntityLivingBase enemy = (EntityLivingBase) arrow.shootingEntity;

                if (player != null && enemy != null) {
                    for (ItemStack stack : player.inventory.armorInventory) {
                        NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

                        if (stack != null && nbt != null && stack.getItem() instanceof ItemArmor) {
                            processHit(event, nbt, stack, enemy, player);
                        }
                    }

                    if (player.inventory.offHandInventory.get(0).getItem() instanceof ItemShield) {
                        ItemStack stack = player.inventory.offHandInventory.get(0);
                        NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

                        if (stack != null && nbt != null && player.getActiveHand() == EnumHand.OFF_HAND) {
                            processHit(event, nbt, stack, enemy, player);
                        }
                    }
                }
            }
        }
    }

    private void processHit(LivingHurtEvent event, NBTTagCompound nbt, ItemStack stack, EntityLivingBase enemy, EntityPlayer player) {
        addExperience(nbt, stack, enemy);
        useRarity(nbt, stack, false);
        useAttributes(nbt, event, stack, player, enemy);
        attemptLevel(nbt, stack, player);
    }

    /**
     * Called every time a living entity dies.
     * @param event
     */
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        Entity source = event.getSource().getTrueSource();
        if (source instanceof EntityPlayer && !(source instanceof FakePlayer)) {
            EntityPlayer player = (EntityPlayer) source;
            EntityLivingBase enemy = event.getEntityLiving();
            ItemStack stack = player.inventory.getCurrentItem();
            NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

            if (player != null && enemy != null && stack != null && nbt != null && stack.getItem() instanceof ItemSword) {
                addExperience(nbt, stack, enemy);
                useRarity(nbt, stack, true);
                attemptLevel(nbt, stack, player);
            }
        } else if (source instanceof EntityArrow) {
            EntityArrow arrow = (EntityArrow) source;

            if (arrow.shootingEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
                EntityLivingBase enemy = event.getEntityLiving();
                ItemStack stack = player.inventory.getCurrentItem();
                NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

                if (player != null && enemy != null && stack != null && nbt != null && stack.getItem() instanceof ItemBow) {
                    addExperience(nbt, stack, enemy);
                    useRarity(nbt, stack, true);
                    attemptLevel(nbt, stack, player);

                    INBT inbt = NBTHelper.toCommon(nbt);
                    if (BowAttribute.RECOVER.hasAttribute(inbt)) {
                        enemy.dropItem(Items.ARROW, (int) (Math.random() * 2));
                    }
                }
            }
        }
    }

    /**
     * Adds experience to the stack's NBT.
     * @param nbt
     * @param stack
     * @param enemy
     */
    private void addExperience(NBTTagCompound nbt, ItemStack stack, EntityLivingBase enemy) {
        Experience exp = new Experience(stack);
        if (exp.isMaxLevel()) {
            return;
        }

        // DEV
        boolean isDev = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        if (isDev) {
            exp.addExperience(200);
        }

        // WEAPONS AND BOW
        if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemBow) {
            int xp = (int) (enemy.getMaxHealth() * 0.2);
            exp.addExperience(xp);
        }

        // ARMOR AND SHIELD
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemShield) {
            int xp = 0;

            // Default to use Attack Damage if available; uses Health if Attack Damage doesn't exist.
            if (enemy.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null) {
                xp = (int) (enemy.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 0.5);
            } else {
                xp = (int) (enemy.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue() * 0.5);
            }

            exp.addExperience(xp);
        }
    }

    /**
     * Uses rarity bonuses, such as bonus experience or durability bonuses.
     * @param nbt
     * @param stack
     */
    private void useRarity(NBTTagCompound nbt, ItemStack stack, boolean death) {
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
            stack.setItemDamage(
                    Math.max(stack.getItemDamage() - repairDurability, 0)
            );
        }
    }

    /**
     * Uses any attributes the stack currently has.
     * @param baseNbt
     * @param event
     * @param stack
     * @param player
     * @param enemy
     */
    private void useAttributes(NBTTagCompound baseNbt, LivingHurtEvent event, ItemStack stack, EntityPlayer player, EntityLivingBase enemy) {
        // WEAPONS
        //TODO: invert void parameters for correct display and extract common attributes

        INBT nbt = NBTHelper.toCommon(baseNbt);

        //region Any attributes
        if (AnyAttributes.FIRE.hasAttribute(nbt) && AnyAttributes.FIRE.rollChance())
            enemy.setFire((int) AnyAttributes.FIRE.getCalculatedValue(nbt));

        if (AnyAttributes.FROST.hasAttribute(nbt) && AnyAttributes.FROST.rollChance())
            enemy.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, (int) AnyAttributes.FROST.getCalculatedValue(nbt), 10));

        if (AnyAttributes.POISON.hasAttribute(nbt) && AnyAttributes.POISON.rollChance())
            enemy.addPotionEffect(new PotionEffect(MobEffects.POISON, (int) AnyAttributes.POISON.getCalculatedValue(nbt), AnyAttributes.POISON.getAttributeTier(nbt)));

        if (AnyAttributes.DURABLE.hasAttribute(nbt) && AnyAttributes.DURABLE.rollChance())
            stack.setItemDamage(stack.getItemDamage() - (int) AnyAttributes.DURABLE.getCalculatedValue(nbt));
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

        if (stack.getItem() instanceof ItemSword) {
            if (SwordAttribute.CHAINED.hasAttribute(nbt) && SwordAttribute.CHAINED.rollChance()) {
                double radius = SwordAttribute.CHAINED.getCalculatedValue(nbt);
                World world = enemy.getEntityWorld();
                List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.posX - radius, player.posY - radius, player.posZ - radius, player.posX + radius, player.posY + radius, player.posZ + radius));
                Iterator<EntityLivingBase> iterator = entityList.iterator();

                while (iterator.hasNext()) {
                    Entity entity = iterator.next();

                    if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer) && !(entity instanceof EntityAnimal)) {
                        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), dmgAmount / 2);
                    }
                }
            }
        }

        // ARMOR
        if (stack.getItem() instanceof ItemArmor) {
            if (ArmorAttribute.MAGICAL.hasAttribute(nbt) && event.getSource().isMagicDamage())
                event.setAmount((float) (dmgAmount * ArmorAttribute.MAGICAL.getCalculatedValue(nbt) / 100.0)); // tiers: (20%, 30%, 45%)
        }
    }

    /**
     * Attempts to level up the current stack.
     * @param stack
     * @param player
     */
    private void attemptLevel(NBTTagCompound nbt, ItemStack stack, EntityPlayer player) {
        new Experience(stack).new LevelUp(player).levelUp();
        NBTHelper.saveStackNBT(stack, nbt);
    }
}
