package com.qbits.dora.init;

import com.qbits.dora.Dora;
import com.qbits.dora.block.CarvablePumpkinBlock;
import com.qbits.dora.block.FlammableLeavesBlock;
import com.qbits.dora.block.FlammableLogBlock;
import com.qbits.dora.block.LeafLitterBlock;
import com.qbits.dora.block.StrippedMapleLogBlock;
import com.qbits.dora.block.sign.DoraCeilingHangingSignBlock;
import com.qbits.dora.block.sign.DoraStandingSignBlock;
import com.qbits.dora.block.sign.DoraWallHangingSignBlock;
import com.qbits.dora.block.sign.DoraWallSignBlock;
import com.qbits.dora.worldgen.DoraTreeGrower;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Dora.MODID);

    public static final RegistryObject<Block> LUMINA = register("lumina", () -> new CarvablePumpkinBlock(BlockBehaviour.Properties.copy(Blocks.PUMPKIN)));
    public static final RegistryObject<Block> JARRAHDALE = register("jarrahdale", () -> new CarvablePumpkinBlock(BlockBehaviour.Properties.copy(Blocks.PUMPKIN)));
    public static final RegistryObject<Block> WARTY_GOBLIN = register("warty_goblin", () -> new CarvablePumpkinBlock(BlockBehaviour.Properties.copy(Blocks.PUMPKIN)));
    public static final RegistryObject<Block> CARVED_PUMPKIN = register("carved_pumpkin", () -> new CarvablePumpkinBlock(BlockBehaviour.Properties.copy(Blocks.PUMPKIN)));

    public static final RegistryObject<Block> STRIPPED_MAPLE_LOG = register("stripped_maple_log", () -> new StrippedMapleLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> DRAINED_MAPLE_LOG = register("drained_maple_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> DRAINED_MAPLE_WOOD = register("drained_maple_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> MAPLE_LOG = register("maple_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG), STRIPPED_MAPLE_LOG));
    public static final RegistryObject<Block> STRIPPED_MAPLE_WOOD = register("stripped_maple_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> MAPLE_WOOD = register("maple_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD), STRIPPED_MAPLE_WOOD));
    public static final RegistryObject<Block> RED_MAPLE_LEAVES = leaves("red_maple_leaves");
    public static final RegistryObject<Block> ORANGE_MAPLE_LEAVES = leaves("orange_maple_leaves");
    public static final RegistryObject<Block> YELLOW_MAPLE_LEAVES = leaves("yellow_maple_leaves");
    public static final RegistryObject<Block> SPECKLED_RED_MAPLE_LEAVES = leaves("speckled_red_maple_leaves");
    public static final RegistryObject<Block> SPECKLED_ORANGE_MAPLE_LEAVES = leaves("speckled_orange_maple_leaves");
    public static final RegistryObject<Block> SPECKLED_YELLOW_MAPLE_LEAVES = leaves("speckled_yellow_maple_leaves");
    public static final RegistryObject<Block> MAPLE_SAPLING = register("maple_sapling", () -> new SaplingBlock(new DoraTreeGrower(ModFeatures.SUGAR_MAPLE), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> MAPLE_PLANKS = register("maple_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MAPLE_STAIRS = register("maple_stairs", () -> new StairBlock(() -> MAPLE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> MAPLE_SLAB = register("maple_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> MAPLE_FENCE = register("maple_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> MAPLE_FENCE_GATE = register("maple_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), ModWoodTypes.MAPLE));
    public static final RegistryObject<Block> MAPLE_DOOR = register("maple_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), BlockSetType.OAK), block -> new DoubleHighBlockItem(block, new Item.Properties()));
    public static final RegistryObject<Block> MAPLE_TRAPDOOR = register("maple_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
    public static final RegistryObject<Block> MAPLE_BUTTON = register("maple_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BlockSetType.OAK, 30, true));
    public static final RegistryObject<Block> MAPLE_PRESSURE_PLATE = register("maple_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));
    public static final RegistryObject<Block> MAPLE_SIGN = BLOCKS.register("maple_sign", () -> new DoraStandingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), ModWoodTypes.MAPLE));
    public static final RegistryObject<Block> MAPLE_WALL_SIGN = BLOCKS.register("maple_wall_sign", () -> new DoraWallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN).dropsLike(MAPLE_SIGN.get()), ModWoodTypes.MAPLE));
    public static final RegistryObject<Block> MAPLE_HANGING_SIGN = BLOCKS.register("maple_hanging_sign", () -> new DoraCeilingHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN), ModWoodTypes.MAPLE));
    public static final RegistryObject<Block> MAPLE_WALL_HANGING_SIGN = BLOCKS.register("maple_wall_hanging_sign", () -> new DoraWallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN).dropsLike(MAPLE_HANGING_SIGN.get()), ModWoodTypes.MAPLE));
    public static final RegistryObject<Block> LEAF_LITTER = register("leaf_litter", () -> new LeafLitterBlock(BlockBehaviour.Properties.copy(Blocks.PINK_PETALS).replaceable()));

    public static final RegistryObject<Block> WILLOW_LOG = register("willow_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> WILLOW_LEAVES = register("willow_leaves", () -> new FlammableLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> WILLOW_SAPLING = register("willow_sapling", () -> new SaplingBlock(new DoraTreeGrower(ModFeatures.WEEPING_WILLOW), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> STRIPPED_ENGLISH_OAK_LOG = register("stripped_english_oak_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> ENGLISH_OAK_LOG = register("english_oak_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG), STRIPPED_ENGLISH_OAK_LOG));
    public static final RegistryObject<Block> STRIPPED_ENGLISH_OAK_WOOD = register("stripped_english_oak_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> ENGLISH_OAK_WOOD = register("english_oak_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD), STRIPPED_ENGLISH_OAK_WOOD));
    public static final RegistryObject<Block> RED_ENGLISH_OAK_LEAVES = leaves("red_english_oak_leaves");
    public static final RegistryObject<Block> YELLOW_ENGLISH_OAK_LEAVES = leaves("yellow_english_oak_leaves");
    public static final RegistryObject<Block> ENGLISH_OAK_SAPLING = register("english_oak_sapling", () -> new SaplingBlock(new DoraTreeGrower(ModFeatures.ENGLISH_OAK), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> ENGLISH_OAK_PLANKS = register("english_oak_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ENGLISH_OAK_STAIRS = register("english_oak_stairs", () -> new StairBlock(() -> ENGLISH_OAK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> ENGLISH_OAK_SLAB = register("english_oak_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> ENGLISH_OAK_FENCE = register("english_oak_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> ENGLISH_OAK_FENCE_GATE = register("english_oak_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), ModWoodTypes.ENGLISH_OAK));
    public static final RegistryObject<Block> ENGLISH_OAK_DOOR = register("english_oak_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), BlockSetType.OAK), block -> new DoubleHighBlockItem(block, new Item.Properties()));
    public static final RegistryObject<Block> ENGLISH_OAK_TRAPDOOR = register("english_oak_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
    public static final RegistryObject<Block> ENGLISH_OAK_BUTTON = register("english_oak_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BlockSetType.OAK, 30, true));
    public static final RegistryObject<Block> ENGLISH_OAK_PRESSURE_PLATE = register("english_oak_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));

    private static RegistryObject<Block> leaves(String name) {
        return register(name, () -> new FlammableLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        return register(name, block, registered -> new BlockItem(registered, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, Function<T, ? extends Item> item) {
        RegistryObject<T> registered = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> item.apply(registered.get()));
        return registered;
    }
}
