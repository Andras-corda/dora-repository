package com.qbits.dora.init;

import com.qbits.dora.Dora;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Dora.MODID);
    public static final List<RegistryObject<SimpleParticleType>> FALLING_LEAVES = new ArrayList<>();

    public static final RegistryObject<SimpleParticleType> MAPLE_LEAF = fallingLeaf("maple_leaf");
    public static final RegistryObject<SimpleParticleType> ENGLISH_OAK_LEAF = fallingLeaf("english_oak_leaf");
    public static final RegistryObject<SimpleParticleType> AUTUMN_LEAF = fallingLeaf("autumn_leaf");

    // public static final RegistryObject<SimpleParticleType> WISTORIA_PETAL = fallingLeaf("wistoria_petal");


    private static RegistryObject<SimpleParticleType> fallingLeaf(String name) {
        RegistryObject<SimpleParticleType> type = PARTICLES.register(name, () -> new SimpleParticleType(false));
        FALLING_LEAVES.add(type);
        return type;
    }
}
