package dev.blue_gamerwolf.voidstone.common.item;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;
import dev.blue_gamerwolf.voidstone.common.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, VoidstoneMod.MODID);
    private static RegistryObject<BlockItem> block(String id, RegistryObject<? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    public static final RegistryObject<BlockItem> NULLSTONE = block("nullstone", ModBlocks.NULLSTONE);
    public static final RegistryObject<BlockItem> NULL_COBBLESTONE = block("null_cobblestone", ModBlocks.NULL_COBBLESTONE);
    public static final RegistryObject<BlockItem> VOIDWOOD_LOG = block("voidwood_log", ModBlocks.VOIDWOOD_LOG);
    public static final RegistryObject<BlockItem> VOIDWOOD_PLANKS = block("voidwood_planks", ModBlocks.VOIDWOOD_PLANKS);
    public static final RegistryObject<BlockItem> VOID_LEAVES = block("void_leaves", ModBlocks.VOID_LEAVES);
    public static final RegistryObject<BlockItem> VOID_RUIN_BRICKS = block("void_ruin_bricks", ModBlocks.VOID_RUIN_BRICKS);
    public static final RegistryObject<BlockItem> VOID_CRYSTAL = block("void_crystal", ModBlocks.VOID_CRYSTAL);
    public static final RegistryObject<BlockItem> VOIDSTONE_ORE = block("voidstone_ore", ModBlocks.VOIDSTONE_ORE);
    public static final RegistryObject<BlockItem> DEEPSLATE_VOIDSTONE_ORE = block("deepslate_voidstone_ore", ModBlocks.DEEPSLATE_VOIDSTONE_ORE);
    public static final RegistryObject<BlockItem> RAW_VOIDSTONE_BLOCK = block("raw_voidstone_block", ModBlocks.RAW_VOIDSTONE_BLOCK);
    public static final RegistryObject<BlockItem> VOIDSTONE_BLOCK = block("voidstone_block", ModBlocks.VOIDSTONE_BLOCK);
    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}
