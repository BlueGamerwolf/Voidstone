package dev.blue_gamerwolf.voidstone.common.tab;

import dev.blue_gamerwolf.voidstone.VoidstoneMod;
import dev.blue_gamerwolf.voidstone.common.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

        public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(
                        Registries.CREATIVE_MODE_TAB,
                        VoidstoneMod.MODID);

        public static final RegistryObject<CreativeModeTab> VOIDSTONE_TAB = CREATIVE_TABS.register(
                        "voidstone",
                        () -> CreativeModeTab.builder()
                                        .title(Component.translatable("itemGroup.voidstone"))
                                        .icon(() -> new ItemStack(ModItems.VOIDSTONE_INGOT.get()))
                                        .displayItems((parameters, output) -> {
                                                output.accept(ModItems.RAW_VOIDSTONE.get());
                                                output.accept(ModItems.VOIDSTONE_INGOT.get());

                                                output.accept(ModItems.VOIDSTONE_HELMET.get());
                                                output.accept(ModItems.VOIDSTONE_CHESTPLATE.get());
                                                output.accept(ModItems.VOIDSTONE_LEGGINGS.get());
                                                output.accept(ModItems.VOIDSTONE_BOOTS.get());

                                                output.accept(ModItems.VOIDSTONE_SWORD.get());
                                                output.accept(ModItems.VOIDSTONE_PICKAXE.get());
                                                output.accept(ModItems.VOIDSTONE_AXE.get());
                                                output.accept(ModItems.VOIDSTONE_SHOVEL.get());
                                                output.accept(ModItems.VOIDSTONE_HOE.get());
                                        })
                                        .build());

        public static void register(
                        net.minecraftforge.eventbus.api.IEventBus eventBus) {
                CREATIVE_TABS.register(eventBus);
        }

}
