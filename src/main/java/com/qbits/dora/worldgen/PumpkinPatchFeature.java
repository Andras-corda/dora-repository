package com.qbits.dora.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.qbits.dora.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PumpkinPatchFeature extends Feature<PumpkinPatchFeature.Config> {
    private static final int Y_SPREAD = 3;
    private static final float STEM_CHANCE = 0.25F;
    private static final float THREADS_CHANCE = 0.12F;
    private static final float COBWEB_BETWEEN_CHANCE = 0.35F;

    public PumpkinPatchFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        if (!(context.config().block() instanceof StemGrownBlock fruit)) {
            return false;
        }
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        int spread = context.config().spread();
        List<BlockPos> placed = new ArrayList<>();

        for (int i = 0; i < context.config().tries(); i++) {
            BlockPos pos = context.origin().offset(
                    random.nextInt(spread + 1) - random.nextInt(spread + 1),
                    random.nextInt(Y_SPREAD + 1) - random.nextInt(Y_SPREAD + 1),
                    random.nextInt(spread + 1) - random.nextInt(spread + 1));
            if (isFree(level, pos) && level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)) {
                level.setBlock(pos, fruit.defaultBlockState(), Block.UPDATE_CLIENTS);
                placed.add(pos);
            }
        }

        for (BlockPos pos : placed) {
            if (random.nextFloat() < STEM_CHANCE) {
                plantStem(level, random, pos, fruit);
            }
        }
        for (BlockPos pos : placed) {
            decorate(level, random, pos, fruit);
        }
        return !placed.isEmpty();
    }

    private static boolean isFree(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.canBeReplaced() && level.getFluidState(pos).isEmpty() && !state.is(ModBlocks.COBWEB_THREADS.get());
    }

    private static void plantStem(WorldGenLevel level, RandomSource random, BlockPos fruitPos, StemGrownBlock fruit) {
        Direction side = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos stem = fruitPos.relative(side);
        if (isFree(level, stem) && level.getBlockState(stem.below()).is(Blocks.GRASS_BLOCK)) {
            level.setBlock(stem.below(), Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, FarmBlock.MAX_MOISTURE), Block.UPDATE_CLIENTS);
            level.setBlock(stem, fruit.getAttachedStem().defaultBlockState().setValue(AttachedStemBlock.FACING, side.getOpposite()), Block.UPDATE_CLIENTS);
        }
    }

    private static void decorate(WorldGenLevel level, RandomSource random, BlockPos fruitPos, StemGrownBlock fruit) {
        for (Direction side : Direction.Plane.HORIZONTAL) {
            BlockPos pos = fruitPos.relative(side);
            if (!isFree(level, pos) || !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) {
                continue;
            }
            boolean between = level.getBlockState(pos.relative(side)).is(fruit);
            if (between && random.nextFloat() < COBWEB_BETWEEN_CHANCE) {
                level.setBlock(pos, Blocks.COBWEB.defaultBlockState(), Block.UPDATE_CLIENTS);
            } else if (random.nextFloat() < THREADS_CHANCE) {
                BlockState threads = ModBlocks.COBWEB_THREADS.get().stateFor(level, pos, side.getOpposite());
                if (threads != null) {
                    level.setBlock(pos, threads, Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    public record Config(Block block, int tries, int spread) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(Config::block),
                ExtraCodecs.POSITIVE_INT.fieldOf("tries").forGetter(Config::tries),
                ExtraCodecs.POSITIVE_INT.fieldOf("spread").forGetter(Config::spread)
        ).apply(instance, Config::new));
    }
}
