package com.qbits.dora.init;

import com.qbits.dora.Dora;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> CARVABLE = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Dora.MODID, "carvable"));
}
