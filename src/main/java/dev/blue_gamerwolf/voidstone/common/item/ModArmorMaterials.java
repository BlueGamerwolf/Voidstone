package dev.blue_gamerwolf.voidstone.common.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class ModArmorMaterials {

        public static final ArmorMaterial VOIDSTONE = new ArmorMaterial() {

                @Override
                public int getDurabilityForType(ArmorItem.Type type) {
                        return switch (type) {
                                case HELMET -> 11 * 13;
                                case CHESTPLATE -> 11 * 15;
                                case LEGGINGS -> 11 * 16;
                                case BOOTS -> 11 * 11;
                                default -> 0;
                        };
                }

                @Override
                public int getDefenseForType(ArmorItem.Type type) {
                        return switch (type) {
                                case BOOTS -> 5;
                                case LEGGINGS -> 8;
                                case CHESTPLATE -> 10;
                                case HELMET -> 5;
                                default -> 0;
                        };
                }

                @Override
                public int getEnchantmentValue() {
                        return 35;
                }

                @Override
                public SoundEvent getEquipSound() {
                        return SoundEvents.ARMOR_EQUIP_NETHERITE;
                }

                @Override
                public Ingredient getRepairIngredient() {
                        return Ingredient.of(ModItems.VOIDSTONE_INGOT.get());
                }

                @Override
                public String getName() {
                        return "voidstone:voidstone";
                }

                @Override
                public float getToughness() {
                        return 7.0F;
                }

                @Override
                public float getKnockbackResistance() {
                        return 0.5F;
                }
        };

        public static void register(
                        net.minecraftforge.eventbus.api.IEventBus eventBus) {
        }

}
