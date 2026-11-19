package com.qbits.dora.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class TemplateTreeFeature extends Feature<TemplateTreeConfiguration> {
    private static final int UPDATE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;
    private static final Map<StructureTemplate, Shape> SHAPES = Collections.synchronizedMap(new WeakHashMap<>());

    public TemplateTreeFeature(Codec<TemplateTreeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<TemplateTreeConfiguration> context) {
        List<ResourceLocation> templates = context.config().templates();
        if (templates.isEmpty()) {
            return false;
        }
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        ResourceLocation id = templates.get(random.nextInt(templates.size()));
        StructureTemplate template = level.getLevel().getStructureManager().get(id).orElse(null);
        if (template == null) {
            return false;
        }
        Shape shape = SHAPES.computeIfAbsent(template, key -> Shape.of(level, key));
        if (shape.base == null) {
            return false;
        }
        Rotation rotation = Rotation.getRandom(random);

        for (Entry entry : shape.logs) {
            if (!TreeFeature.validTreePos(level, entry.target(origin, shape.base, rotation))) {
                return false;
            }
        }
        Set<BlockPos> logs = new HashSet<>();
        for (Entry entry : shape.logs) {
            BlockPos target = entry.target(origin, shape.base, rotation);
            level.setBlock(target, entry.state.rotate(rotation), UPDATE_FLAGS);
            logs.add(target);
        }
        List<BlockPos> placed = new ArrayList<>();
        for (Entry entry : shape.leaves) {
            BlockPos target = entry.target(origin, shape.base, rotation);
            if (TreeFeature.validTreePos(level, target)) {
                level.setBlock(target, entry.state.rotate(rotation), UPDATE_FLAGS);
                placed.add(target);
            }
        }
        updateDistances(level, placed);
        if (!context.config().decorators().isEmpty()) {
            TreeDecorator.Context decoration = new TreeDecorator.Context(level, (pos, state) -> level.setBlock(pos, state, UPDATE_FLAGS), random, logs, new HashSet<>(placed), Set.of());
            context.config().decorators().forEach(decorator -> decorator.place(decoration));
        }
        return true;
    }

    private static void updateDistances(WorldGenLevel level, List<BlockPos> leaves) {
        Map<BlockPos, Integer> distances = new HashMap<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        for (BlockPos pos : leaves) {
            for (Direction direction : Direction.values()) {
                if (level.getBlockState(pos.relative(direction)).is(BlockTags.LOGS)) {
                    distances.put(pos, 1);
                    queue.add(pos);
                    break;
                }
            }
        }
        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            int next = distances.get(pos) + 1;
            if (next >= LeavesBlock.DECAY_DISTANCE) {
                continue;
            }
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = pos.relative(direction);
                if (!distances.containsKey(neighbor) && level.getBlockState(neighbor).hasProperty(LeavesBlock.DISTANCE)) {
                    distances.put(neighbor, next);
                    queue.add(neighbor);
                }
            }
        }
        distances.forEach((pos, distance) -> {
            BlockState state = level.getBlockState(pos);
            if (state.hasProperty(LeavesBlock.DISTANCE)) {
                level.setBlock(pos, state.setValue(LeavesBlock.DISTANCE, distance), UPDATE_FLAGS);
            }
        });
    }

    private record Entry(BlockPos pos, BlockState state) {
        BlockPos target(BlockPos origin, BlockPos base, Rotation rotation) {
            return origin.offset(pos.subtract(base).rotate(rotation));
        }
    }

    private record Shape(BlockPos base, List<Entry> logs, List<Entry> leaves) {
        static Shape of(WorldGenLevel level, StructureTemplate template) {
            CompoundTag tag = template.save(new CompoundTag());
            ListTag palette = tag.getList("palette", Tag.TAG_COMPOUND);
            List<BlockState> states = new ArrayList<>();
            for (int i = 0; i < palette.size(); i++) {
                states.add(NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), palette.getCompound(i)));
            }
            List<Entry> logs = new ArrayList<>();
            List<Entry> leaves = new ArrayList<>();
            BlockPos base = null;
            ListTag blocks = tag.getList("blocks", Tag.TAG_COMPOUND);
            for (int i = 0; i < blocks.size(); i++) {
                CompoundTag block = blocks.getCompound(i);
                ListTag position = block.getList("pos", Tag.TAG_INT);
                BlockPos pos = new BlockPos(position.getInt(0), position.getInt(1), position.getInt(2));
                BlockState state = states.get(block.getInt("state"));
                if (state.is(BlockTags.LOGS)) {
                    logs.add(new Entry(pos, state));
                    if (base == null || pos.getY() < base.getY()) {
                        base = pos;
                    }
                } else if (!state.isAir()) {
                    leaves.add(new Entry(pos, state));
                }
            }
            return new Shape(base, logs, leaves);
        }
    }
}
