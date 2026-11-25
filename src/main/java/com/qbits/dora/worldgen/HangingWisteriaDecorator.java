package com.qbits.dora.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.qbits.dora.block.WisteriaVinesBlock;
import com.qbits.dora.init.ModBlocks;
import com.qbits.dora.init.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class HangingWisteriaDecorator extends TreeDecorator {
    public static final Codec<HangingWisteriaDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.floatRange(0, 1).fieldOf("probability").forGetter(decorator -> decorator.probability),
            ExtraCodecs.POSITIVE_INT.fieldOf("max_length").forGetter(decorator -> decorator.maxLength)
    ).apply(instance, HangingWisteriaDecorator::new));

    private final float probability;
    private final int maxLength;

    public HangingWisteriaDecorator(float probability, int maxLength) {
        this.probability = probability;
        this.maxLength = maxLength;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModFeatures.HANGING_WISTERIA.get();
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        for (BlockPos leaf : context.leaves()) {
            if (random.nextFloat() >= probability || !context.isAir(leaf.below())) {
                continue;
            }
            int length = 1 + random.nextInt(maxLength);
            BlockPos pos = leaf.below();
            int placed = 0;
            while (placed < length && context.isAir(pos)) {
                placed++;
                pos = pos.below();
            }
            for (int i = 1; i <= placed; i++) {
                context.setBlock(leaf.below(i), ModBlocks.WISTERIA_VINES.get().defaultBlockState().setValue(WisteriaVinesBlock.TIP, i == placed));
            }
        }
    }
}
