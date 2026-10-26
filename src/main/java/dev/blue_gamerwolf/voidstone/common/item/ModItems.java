package dev.blue_gamerwolf.voidstone.common.item;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
                        net.minecraft.core.registries.Registries.ITEM,
                        VoidstoneMod.MODID);

        public static final RegistryObject<Item> RAW_VOIDSTONE = ITEMS.register("raw_voidstone",
                        () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> VOIDSTONE_INGOT = ITEMS.register("voidstone_ingot",
                        () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> VOID_SENTINEL_CORE = ITEMS.register("void_sentinel_core",
                        () -> new Item(new Item.Properties()));

        public static final RegistryObject<ArmorItem> VOIDSTONE_HELMET = ITEMS.register("voidstone_helmet",
                        () -> new ArmorItem(ModArmorMaterials.VOIDSTONE, ArmorItem.Type.HELMET, new Item.Properties()));

        public static final RegistryObject<ArmorItem> VOIDSTONE_CHESTPLATE = ITEMS.register("voidstone_chestplate",
                        () -> new ArmorItem(ModArmorMaterials.VOIDSTONE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

        public static final RegistryObject<ArmorItem> VOIDSTONE_LEGGINGS = ITEMS.register("voidstone_leggings",
                        () -> new ArmorItem(ModArmorMaterials.VOIDSTONE, ArmorItem.Type.LEGGINGS, new Item.Properties()));

        public static final RegistryObject<ArmorItem> VOIDSTONE_BOOTS = ITEMS.register("voidstone_boots",
                        () -> new ArmorItem(ModArmorMaterials.VOIDSTONE, ArmorItem.Type.BOOTS, new Item.Properties()));

        public static final RegistryObject<SwordItem> VOIDSTONE_SWORD = ITEMS.register("voidstone_sword",
                        () -> new SwordItem(ModToolMaterials.VOIDSTONE, 3, -2.4F, new Item.Properties()));

        public static final RegistryObject<PickaxeItem> VOIDSTONE_PICKAXE = ITEMS.register("voidstone_pickaxe",
                        () -> new PickaxeItem(ModToolMaterials.VOIDSTONE, 1, -2.8F, new Item.Properties()));

        public static final RegistryObject<AxeItem> VOIDSTONE_AXE = ITEMS.register("voidstone_axe",
                        () -> new AxeItem(ModToolMaterials.VOIDSTONE, 5.0F, -3.0F, new Item.Properties()));

        public static final RegistryObject<ShovelItem> VOIDSTONE_SHOVEL = ITEMS.register("voidstone_shovel",
                        () -> new ShovelItem(ModToolMaterials.VOIDSTONE, 1.5F, -3.0F, new Item.Properties()));

        public static final RegistryObject<HoeItem> VOIDSTONE_HOE = ITEMS.register("voidstone_hoe",
                        () -> new HoeItem(ModToolMaterials.VOIDSTONE, -2, 0.0F, new Item.Properties()));

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }

}
