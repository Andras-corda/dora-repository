package com.qbits.dora.block;

import com.qbits.dora.init.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class WoodFlammability {
    public static final int ENCOURAGEMENT = 5;
    public static final int FLAMMABILITY = 20;
    public static final int DRAINED_ENCOURAGEMENT = 10;
    public static final int DRAINED_FLAMMABILITY = 30;

    public static void register() {
        Method setFlammable = ObfuscationReflectionHelper.findMethod(FireBlock.class, "m_53444_", Block.class, int.class, int.class);
        FireBlock fire = (FireBlock) Blocks.FIRE;
        List<RegistryObject<Block>> regular = List.of(
                ModBlocks.MAPLE_PLANKS, ModBlocks.MAPLE_STAIRS, ModBlocks.MAPLE_SLAB, ModBlocks.MAPLE_FENCE, ModBlocks.MAPLE_FENCE_GATE,
                ModBlocks.ENGLISH_OAK_PLANKS, ModBlocks.ENGLISH_OAK_STAIRS, ModBlocks.ENGLISH_OAK_SLAB, ModBlocks.ENGLISH_OAK_FENCE, ModBlocks.ENGLISH_OAK_FENCE_GATE,
                ModBlocks.WISTERIA_PLANKS, ModBlocks.WISTERIA_STAIRS, ModBlocks.WISTERIA_SLAB, ModBlocks.WISTERIA_FENCE, ModBlocks.WISTERIA_FENCE_GATE);
        List<RegistryObject<Block>> drained = List.of(
                ModBlocks.DRAINED_MAPLE_PLANKS, ModBlocks.DRAINED_MAPLE_STAIRS, ModBlocks.DRAINED_MAPLE_SLAB, ModBlocks.DRAINED_MAPLE_FENCE, ModBlocks.DRAINED_MAPLE_FENCE_GATE);
        try {
            for (RegistryObject<Block> block : regular) {
                setFlammable.invoke(fire, block.get(), ENCOURAGEMENT, FLAMMABILITY);
            }
            for (RegistryObject<Block> block : drained) {
                setFlammable.invoke(fire, block.get(), DRAINED_ENCOURAGEMENT, DRAINED_FLAMMABILITY);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
