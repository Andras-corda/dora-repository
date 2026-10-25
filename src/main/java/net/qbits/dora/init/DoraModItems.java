/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.qbits.dora.init;

import net.qbits.dora.DoraMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.ForgeSpawnEggItem;

import net.minecraft.world.item.Item;

public class DoraModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, DoraMod.MODID);
	public static final RegistryObject<Item> PALACECAT_SPAWN_EGG;
	static {
		PALACECAT_SPAWN_EGG = REGISTRY.register("palacecat_spawn_egg", () -> new ForgeSpawnEggItem(DoraModEntities.PALACECAT, -1, -1, new Item.Properties()));
	}
	// Start of user code block custom items
	// End of user code block custom items
}