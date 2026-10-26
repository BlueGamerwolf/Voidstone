package dev.blue_gamerwolf.voidstone.common.enchantment;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Enchantment;
import net.minecraft.world.item.EnchantmentCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Enchantments provided by Voidstone.
 */
public final class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(Registries.ENCHANTMENT, VoidstoneMod.MODID);

    public static final RegistryObject<Enchantment> DIMENSIONAL = ENCHANTMENTS.register(
            "dimensional",
            () -> new Enchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR,
                    new EquipmentSlot[] {
                            EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                            EquipmentSlot.LEGS, EquipmentSlot.FEET
                    }) {
                @Override
                public boolean canEnchant(ItemStack stack) {
                    return stack.is(Items.NETHERITE_HELMET)
                            || stack.is(Items.NETHERITE_CHESTPLATE)
                            || stack.is(Items.NETHERITE_LEGGINGS)
                            || stack.is(Items.NETHERITE_BOOTS);
                }

                @Override
                public int getMaxLevel() {
                    return 1;
                }
            });

    private ModEnchantments() {
    }

    public static void register(IEventBus modEventBus) {
        ENCHANTMENTS.register(modEventBus);
    }
}
