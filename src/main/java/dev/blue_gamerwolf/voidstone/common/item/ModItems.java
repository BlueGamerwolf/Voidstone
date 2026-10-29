package dev.blue_gamerwolf.voidstone.common.item;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;
import dev.blue_gamerwolf.voidstone.common.block.ModBlocks;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(net.minecraft.core.registries.Registries.ITEM, VoidstoneMod.MODID);
    public static final RegistryObject<Item> RAW_VOIDSTONE = ITEMS.register("raw_voidstone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VOIDSTONE_INGOT = ITEMS.register("voidstone_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VOIDSTONE_NUGGET = ITEMS.register("voidstone_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VOID_SENTINEL_CORE = ITEMS.register("void_sentinel_core", () -> new Item(new Item.Properties()));

    public static final Tier NULLSTONE_TIER = new Tier() {
        public int getUses() { return 180; }
        public float getSpeed() { return 5.0F; }
        public float getAttackDamageBonus() { return 1.0F; }
        public int getLevel() { return 1; }
        public int getEnchantmentValue() { return 14; }
        public Ingredient getRepairIngredient() { return Ingredient.of(ModBlocks.NULL_COBBLESTONE.get()); }
    };
    public static final RegistryObject<SwordItem> NULLSTONE_SWORD = ITEMS.register("nullstone_sword", () -> new SwordItem(NULLSTONE_TIER, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<PickaxeItem> NULLSTONE_PICKAXE = ITEMS.register("nullstone_pickaxe", () -> new PickaxeItem(NULLSTONE_TIER, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<AxeItem> NULLSTONE_AXE = ITEMS.register("nullstone_axe", () -> new AxeItem(NULLSTONE_TIER, 5.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<ShovelItem> NULLSTONE_SHOVEL = ITEMS.register("nullstone_shovel", () -> new ShovelItem(NULLSTONE_TIER, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<HoeItem> NULLSTONE_HOE = ITEMS.register("nullstone_hoe", () -> new HoeItem(NULLSTONE_TIER, -2, 0.0F, new Item.Properties()));

    public static final RegistryObject<ArmorItem> VOIDSTONE_HELMET = ITEMS.register("voidstone_helmet", () -> new ArmorItem(ModArmorMaterials.VOIDSTONE, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<ArmorItem> VOIDSTONE_CHESTPLATE = ITEMS.register("voidstone_chestplate", () -> new ArmorItem(ModArmorMaterials.VOIDSTONE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<ArmorItem> VOIDSTONE_LEGGINGS = ITEMS.register("voidstone_leggings", () -> new ArmorItem(ModArmorMaterials.VOIDSTONE, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<ArmorItem> VOIDSTONE_BOOTS = ITEMS.register("voidstone_boots", () -> new ArmorItem(ModArmorMaterials.VOIDSTONE, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<SwordItem> VOIDSTONE_SWORD = ITEMS.register("voidstone_sword", () -> new SwordItem(ModToolMaterials.VOIDSTONE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<PickaxeItem> VOIDSTONE_PICKAXE = ITEMS.register("voidstone_pickaxe", () -> new PickaxeItem(ModToolMaterials.VOIDSTONE, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<AxeItem> VOIDSTONE_AXE = ITEMS.register("voidstone_axe", () -> new AxeItem(ModToolMaterials.VOIDSTONE, 5.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<ShovelItem> VOIDSTONE_SHOVEL = ITEMS.register("voidstone_shovel", () -> new ShovelItem(ModToolMaterials.VOIDSTONE, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<HoeItem> VOIDSTONE_HOE = ITEMS.register("voidstone_hoe", () -> new HoeItem(ModToolMaterials.VOIDSTONE, -2, 0.0F, new Item.Properties()));
    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}
