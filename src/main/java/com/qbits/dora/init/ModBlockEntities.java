package com.qbits.dora.init;

import com.qbits.dora.Dora;
import com.qbits.dora.block.sign.DoraHangingSignBlockEntity;
import com.qbits.dora.block.sign.DoraSignBlockEntity;
import com.qbits.dora.carving.CarvingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Dora.MODID);

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<CarvingBlockEntity>> CARVING = BLOCK_ENTITIES.register("carving", () ->
            BlockEntityType.Builder.of(CarvingBlockEntity::new,
                    ModBlocks.CARVED_PUMPKIN.get(),
                    ModBlocks.LUMINA.get(),
                    ModBlocks.JARRAHDALE.get(),
                    ModBlocks.WARTY_GOBLIN.get()
            ).build(null));

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<DoraSignBlockEntity>> SIGN = BLOCK_ENTITIES.register("sign", () ->
            BlockEntityType.Builder.of(DoraSignBlockEntity::new, ModBlocks.MAPLE_SIGN.get(), ModBlocks.MAPLE_WALL_SIGN.get(), ModBlocks.ENGLISH_OAK_SIGN.get(), ModBlocks.ENGLISH_OAK_WALL_SIGN.get(), ModBlocks.DRAINED_MAPLE_SIGN.get(), ModBlocks.DRAINED_MAPLE_WALL_SIGN.get()).build(null));

    @SuppressWarnings("DataFlowIssue")
    public static final RegistryObject<BlockEntityType<DoraHangingSignBlockEntity>> HANGING_SIGN = BLOCK_ENTITIES.register("hanging_sign", () ->
            BlockEntityType.Builder.of(DoraHangingSignBlockEntity::new, ModBlocks.MAPLE_HANGING_SIGN.get(), ModBlocks.MAPLE_WALL_HANGING_SIGN.get(), ModBlocks.ENGLISH_OAK_HANGING_SIGN.get(), ModBlocks.ENGLISH_OAK_WALL_HANGING_SIGN.get(), ModBlocks.DRAINED_MAPLE_HANGING_SIGN.get(), ModBlocks.DRAINED_MAPLE_WALL_HANGING_SIGN.get()).build(null));
}
