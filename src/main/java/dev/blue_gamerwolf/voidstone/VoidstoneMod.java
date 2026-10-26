package dev.blue_gamerwolf.voidstone;

import dev.blue_gamerwolf.voidstone.common.block.ModBlocks;
import dev.blue_gamerwolf.voidstone.common.item.ModArmorMaterials;
import dev.blue_gamerwolf.voidstone.common.item.ModBlockItems;
import dev.blue_gamerwolf.voidstone.common.item.ModItems;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VoidstoneMod.MODID)
public class VoidstoneMod {

    public static final String MODID = "voidstone";

    public VoidstoneMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockItems.register(modEventBus);
        ModArmorMaterials.register(modEventBus);

        System.out.println("[Voidstone] Voidstone Core loading...");
    }

}
