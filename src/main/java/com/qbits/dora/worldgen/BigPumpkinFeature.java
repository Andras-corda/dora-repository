package com.qbits.dora.worldgen;

import com.mojang.serialization.Codec;
import com.qbits.dora.block.BigPumpkinBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BigPumpkinFeature extends Feature<BigPumpkinFeature.Config> {
    public BigPumpkinFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        if (!(context.config().block() instanceof BigPumpkinBlock big)) {
            return false;
        }
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                if (!level.getBlockState(origin.offset(x, -1, z)).is(BlockTags.DIRT)) {
                    return false;
                }
            }
        }
        if (!big.canFit(level, origin, pos -> level.getBlockState(pos).canBeReplaced()) || !big.hasGround(level, origin)) {
            return false;
        }
        big.place(level, origin, Block.UPDATE_CLIENTS);
        return true;
    }

    public record Config(Block block) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").xmap(Config::new, Config::block).codec();
    }
}
