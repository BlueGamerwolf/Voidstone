package dev.blue_gamerwolf.voidstone.common.item;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;
import dev.blue_gamerwolf.voidstone.common.block.ModBlocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockItems {

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
                        net.minecraft.core.registries.Registries.ITEM,
                        VoidstoneMod.MODID);

        public static final RegistryObject<BlockItem> VOIDSTONE_ORE = ITEMS.register(
                        "voidstone_ore",
                        () -> new BlockItem(
                                        ModBlocks.VOIDSTONE_ORE.get(),
                                        new Item.Properties()));

        public static final RegistryObject<BlockItem> DEEPSLATE_VOIDSTONE_ORE = ITEMS.register(
                        "deepslate_voidstone_ore",
                        () -> new BlockItem(
                                        ModBlocks.DEEPSLATE_VOIDSTONE_ORE.get(),
                                        new Item.Properties()));

        public static final RegistryObject<BlockItem> RAW_VOIDSTONE_BLOCK = ITEMS.register(
                        "raw_voidstone_block",
                        () -> new BlockItem(
                                        ModBlocks.RAW_VOIDSTONE_BLOCK.get(),
                                        new Item.Properties()));

        public static final RegistryObject<BlockItem> VOIDSTONE_BLOCK = ITEMS.register(
                        "voidstone_block",
                        () -> new BlockItem(
                                        ModBlocks.VOIDSTONE_BLOCK.get(),
                                        new Item.Properties()));

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }

}
