package dev.blue_gamerwolf.voidstone.common.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ModToolMaterials {

    public static final Tier VOIDSTONE = new Tier() {

        @Override
        public int getUses() {
            return 3200;
        }

        @Override
        public float getSpeed() {
            return 12.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 10.0F;
        }

        @Override
        public int getLevel() {
            return 5;
        }

        @Override
        public int getEnchantmentValue() {
            return 30;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ModItems.VOIDSTONE_INGOT.get());
        }
    };

}
