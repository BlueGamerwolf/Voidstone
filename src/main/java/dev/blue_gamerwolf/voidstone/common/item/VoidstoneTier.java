package dev.blue_gamerwolf.voidstone.common.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public enum VoidstoneTier implements Tier {

    VOIDSTONE(
            5,
            3200,
            12.0F,
            10.0F,
            30);

    private final int level;
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;

    VoidstoneTier(
            int level,
            int uses,
            float speed,
            float attackDamageBonus,
            int enchantmentValue) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.VOIDSTONE_INGOT.get());
    }

    public TagKey<Block> getIncorrectBlocksForDrops() {
        return BlockTags.NEEDS_DIAMOND_TOOL;
    }

}
