package dev.blue_gamerwolf.voidstone.common.block;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, VoidstoneMod.MODID);

    public static final RegistryObject<Block> NULLSTONE = BLOCKS.register("nullstone",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.END_STONE)
                    .strength(2.5F, 9.0F).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final RegistryObject<Block> NULL_COBBLESTONE = BLOCKS.register("null_cobblestone",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE)
                    .strength(2.5F, 9.0F).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final RegistryObject<Block> VOIDWOOD_LOG = BLOCKS.register("voidwood_log",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LOG).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> VOIDWOOD_PLANKS = BLOCKS.register("voidwood_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> VOID_LEAVES = BLOCKS.register("void_leaves",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.AZALEA_LEAVES).strength(0.2F).sound(SoundType.GRASS).noOcclusion()));
    public static final RegistryObject<Block> VOID_RUIN_BRICKS = BLOCKS.register("void_ruin_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).strength(3.0F, 9.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VOID_CRYSTAL = BLOCKS.register("void_crystal",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).strength(1.5F).sound(SoundType.AMETHYST).lightLevel(state -> 7)));

    public static final RegistryObject<Block> VOIDSTONE_ORE = BLOCKS.register("voidstone_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE).strength(5.0F, 6.0F).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final RegistryObject<Block> DEEPSLATE_VOIDSTONE_ORE = BLOCKS.register("deepslate_voidstone_ore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_DIAMOND_ORE).strength(6.0F, 7.0F).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));
    public static final RegistryObject<Block> RAW_VOIDSTONE_BLOCK = BLOCKS.register("raw_voidstone_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK).strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VOIDSTONE_BLOCK = BLOCKS.register("voidstone_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(8.0F, 50.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<LostGatewayBlock> LOST_GATEWAY = BLOCKS.register("lost_gateway",
            () -> new LostGatewayBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).strength(10.0F, 1200.0F).sound(SoundType.AMETHYST)));

    public static void register(IEventBus eventBus) { BLOCKS.register(eventBus); }
}
