package dev.blue_gamerwolf.voidstone.worldgen;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModWorldgen {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, VoidstoneMod.MODID);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> VOIDWOOD_TREE =
            FEATURES.register("voidwood_tree", VoidwoodTreeFeature::new);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> VOID_RUINS =
            FEATURES.register("void_ruins", VoidRuinFeature::new);
    private ModWorldgen() {}
    public static void register(IEventBus bus) { FEATURES.register(bus); }
}
